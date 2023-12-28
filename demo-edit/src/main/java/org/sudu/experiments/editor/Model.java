package org.sudu.experiments.editor;

import org.sudu.experiments.Debug;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.editor.worker.proxy.FileProxy;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.worker.ArrayView;

import java.util.*;
import java.util.function.BiConsumer;

public class Model {

  public final Uri uri;
  public final Document document;

  public Object platformObject;
  private String docLanguage;
  private String languageFromFile;
  final Selection selection = new Selection();
  LineDiff[] diffModel;
  final NavigationStack navStack = new NavigationStack();
  int caretLine, caretCharPos, caretPos;
  CodeElement definition = null;
  final List<CodeElement> usages = new ArrayList<>();
  boolean fullFileParsed, fileStructureParsed, firstLinesParsed;
  final Map<String, String> properties = new HashMap<>();
  long parsingTimeStart, viewportParseStart;
  long resolveTimeStart;
  boolean highlightResolveError, printResolveTime;

  public Model(String text, String language, Uri uri) {
    this(SplitText.split(text), language, uri);
  }

  public Model(String[] text, Uri uri) {
    this(text, null, uri);
  }

  public Model(String[] text, String language, Uri uri) {
    this.uri = uri;
    docLanguage = language;
    languageFromFile = uri != null ? Languages.languageFromFilename(uri.path) : null;
    document = new Document(text);
  }

  public Model() {
    this.document = new Document();
    this.docLanguage = Languages.TEXT;
    this.uri = null;
  }

  public void setLanguage(String language) {
    docLanguage = language;
  }

  public String language() {
    return docLanguage != null ? docLanguage : languageFromFile;
  }

  public String docLanguage() { return docLanguage; }

  public String uriScheme() {
    return uri != null ? uri.scheme : null;
  }

  void onFileParsed(Object[] result, BiConsumer<int[], char[]> resolveAll) {
    Debug.consoleInfo("onFileParsed");
    fileStructureParsed = true;
    firstLinesParsed = true;
    fullFileParsed = true;

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    int type = ((ArrayView) result[2]).ints()[0];

    if (type == FileProxy.ACTIVITY_FILE) { //TODO hack to transfer special data, need to be refactored

      String dag1 = new String(((ArrayView) result[3]).chars());
      properties.put("mermaid", dag1);
//      String dag2 = new String(((ArrayView) result[4]).chars());
//      properties.put("mermaid2", dag2);

//      System.out.println("dag1 = " + dag1);
//      System.out.println("dag2 = " + dag2);

      ParserUtils.updateDocument(document, ints, chars);
    } else if (result.length >= 5) {
      int[] graphInts = ((ArrayView) result[3]).ints();
      char[] graphChars = ((ArrayView) result[4]).chars();
      ParserUtils.updateDocument(document, ints, chars, graphInts, graphChars, false);
      resolveAll.accept(
          Arrays.copyOf(graphInts, graphInts.length),
          Arrays.copyOf(graphChars, graphChars.length)
      );
    } else {
      ParserUtils.updateDocument(document, ints, chars);
    }

    changeModelLanguage(Languages.getLanguage(type));
    Debug.consoleInfo("Full file parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
//    if (fullFileParseListener != null) {
//      fullFileParseListener.accept(this);
//    }
  }

  void onResolved(Object[] result, BiConsumer<Integer, Integer> useDocumentHighlightProvider) {
    int[] ints = ((ArrayView) result[0]).ints();
    int version = ((ArrayView) result[1]).ints()[0];
    if (document.needReparse() || document.currentVersion != version) return;
    document.onResolve(ints, highlightResolveError);
    computeUsages(useDocumentHighlightProvider);
    if (printResolveTime) {
      long resolveTime = System.currentTimeMillis() - resolveTimeStart;
      if (resolveTime >= EditorConst.BIG_RESOLVE_TIME_MS) {
        System.out.println("Resolved in " + resolveTime + "ms");
      }
    }
  }

  void onFileStructureParsed(Object[] result, BiConsumer<int[], char[]> resolveAll) {
    if (fullFileParsed) return;
    fileStructureParsed = true;
    int type = ((ArrayView) result[2]).ints()[0];
    if (type != FileProxy.JAVA_FILE) {
      onFileParsed(result, resolveAll);
      return;
    }

    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    ParserUtils.updateDocument(document, ints, chars, firstLinesParsed);
    changeModelLanguage(Languages.getLanguage(type));

    Debug.consoleInfo("File structure parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  void onVpParsed(Object[] result) {
//    Debug.consoleInfo("onVpParsed");
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();

    ParserUtils.updateDocumentInterval(document, ints, chars);

    Debug.consoleInfo("Viewport parsed in " + (System.currentTimeMillis() - viewportParseStart) + "ms");
  }

  void onFirstLinesParsed(Object[] result) {
    if (fileStructureParsed || fullFileParsed) return;
    int[] ints = ((ArrayView) result[0]).ints();
    char[] chars = ((ArrayView) result[1]).chars();
    ParserUtils.updateDocument(document, ints, chars);
    firstLinesParsed = true;
    Debug.consoleInfo("First lines parsed in " + (System.currentTimeMillis() - parsingTimeStart) + "ms");
  }

  void changeModelLanguage(String languageFromParser) {
    String language = language();
    if (!Objects.equals(language, languageFromParser)) {
      Debug.consoleInfo("change model language: from = " + language + " to = " + languageFromParser);
      setLanguage(languageFromParser);
    }
  }

  void computeUsages(BiConsumer<Integer, Integer> useDocumentHighlightProvider) {
    Pos caretPos = new Pos(caretLine, caretCharPos);
    Pos elementPos = document.getElementStart(caretLine, caretCharPos);
    computeUsages(caretPos, elementPos, useDocumentHighlightProvider);

    if ((definition == null || usages.isEmpty()) && caretCharPos > 0) {
      Pos prevCaretPos = new Pos(caretLine, caretCharPos - 1);
      Pos prevElementPos = document.getElementStart(caretLine, caretCharPos - 1);
      computeUsages(prevCaretPos, prevElementPos, useDocumentHighlightProvider);
    }
  }

  private void computeUsages(Pos caretPos, Pos elementPos, BiConsumer<Integer, Integer> useDocumentHighlightProvider) {
    clearUsages();

    Pos def = document.getDefinition(elementPos);

    if (def == null) def = elementPos;

    List<Pos> usageList = document.getUsagesList(def);
    if (usageList != null) {
      definition = document.getCodeElement(def);
      for (var usage : usageList) {
        usages.add(document.getCodeElement(usage));
      }
    }

    useDocumentHighlightProvider.accept(caretPos.line, caretPos.pos);
  }

  void clearUsages() {
    definition = null;
    usages.clear();
  }
}
