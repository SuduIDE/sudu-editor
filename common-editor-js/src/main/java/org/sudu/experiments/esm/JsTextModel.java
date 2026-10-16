package org.sudu.experiments.esm;

import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.editor.CodeElement;
import org.sudu.experiments.editor.CodeLine;
import org.sudu.experiments.editor.Document;
import org.sudu.experiments.editor.Model;
import org.sudu.experiments.editor.SemanticTokenInfo;
import org.sudu.experiments.esm.semantic.JsSemanticToken;
import org.sudu.experiments.esm.semantic.JsSemanticTokenColorSettings;
import org.sudu.experiments.esm.semantic.JsSemanticTokenLegendItem;
import org.sudu.experiments.js.*;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.text.SplitJsText;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.getSemanticType;

public class JsTextModel implements JsITextModel {
  public static final boolean PRINT_DEBUG = false;

  @JSFunctor
  public interface Api extends JSObject {
    JsITextModel create(
        JsWorkerPool workerPool,
        JSString value, JSString language, JsUri uri);

    class Setter {
      @JSBody(params = {"f"}, script = "newTextModel = f;")
      static native void set(Api f);
    }

    static void install() {
      Setter.set(JsTextModel::new);
      FromLines.Setter.set(JsTextModel::fromLines);
    }
  }

  @JSFunctor
  public interface FromLines extends JSObject {
    JsITextModel create(
        JsWorkerPool workerPool,
        JsArray<JsLineDescriptor> lines,
        JSString language, JsUri uri,
        JSObject tokenDictionary);

    class Setter {
      @JSBody(params = {"f"}, script = "newTextModelFromLines = f;")
      static native void set(FromLines f);
    }
  }

  public final Model javaModel;
  public final JsUri jsUri;
  private boolean fireEvent = true;

  public JsTextModel(JsWorkerPool workerPool,
                     JSString text, JSString language, JsUri uri
  ) {
    SplitInfo split = SplitJsText.split(text);
    String lang = JsHelper.toString(language, null);
    javaModel = new Model(split, lang, uri.toJava());
    javaModel.setExecutor(JsWorkerPool.pool(workerPool));
    javaModel.platformObject = this;
    jsUri = uri;
  }

  public static JsTextModel fromLines(
      JsWorkerPool workerPool,
      JsArray<JsLineDescriptor> lines,
      JSString language, JsUri uri,
      JSObject tokenDictionary
  ) {
    int n = lines.getLength();
    CodeLine[] codeLines = new CodeLine[n];
    int[] numbers = new int[n];
    List<SemanticTokenInfo> documentTokens = new ArrayList<>();
    JSObject dictionary = isDictionary(tokenDictionary) ? tokenDictionary : null;
    for (int i = 0; i < n; i++) {
      JsLineDescriptor d = lines.get(i);
      numbers[i] = d.hasLineNumber() ? d.getLineNumber() : 0;
      codeLines[i] = buildLine(
          d.hasTokens() ? d.getTokens() : null, i, dictionary, documentTokens);
    }
    Model model = new Model(
        Document.fromLines(codeLines, numbers),
        JsHelper.toString(language, null),
        uri.toJava());
    model.setExecutor(JsWorkerPool.pool(workerPool));
    JsTextModel js = new JsTextModel(model);
    if (!documentTokens.isEmpty())
      js.javaModel.setDocumentTokens(documentTokens.toArray(new SemanticTokenInfo[0]));
    return js;
  }

  private static boolean isDictionary(JSObject dictionary) {
    return dictionary != null && !JSObjects.isUndefined(dictionary);
  }

  /**
   * Builds a tokenized line from its run segments. A run with a token type known
   * to the document dictionary is left for {@link Model#setDocumentTokens}
   * (its color needs the editor color scheme); a run with a built-in semantic
   * type is colored right away; a plain run stays default.
   */
  private static CodeLine buildLine(
      JsArray<JsLineToken> runs, int lineIndex,
      JSObject dictionary, List<SemanticTokenInfo> documentTokens
  ) {
    if (runs == null || runs.getLength() == 0) return new CodeLine("");
    CodeElement[] elements = new CodeElement[runs.getLength()];
    int pos = 0;
    for (int i = 0; i < runs.getLength(); i++) {
      JsLineToken run = runs.get(i);
      String text = run != null && run.hasText() ? JsHelper.toString(run.getText(), "") : "";
      String type = run != null && run.hasTokenType()
          ? JsHelper.toString(run.getTokenType(), null) : null;
      int color = ParserConstants.TokenTypes.DEFAULT;
      if (type != null && dictionary != null && JsHelper.hasProperty(dictionary, type)) {
        JsSemanticTokenColorSettings settings = JsHelper.getProperty(dictionary, type);
        documentTokens.add(colorToken(lineIndex, pos, settings, text));
      } else if (type != null) {
        color = getSemanticType(type);
      }
      elements[i] = new CodeElement(text, color);
      pos += text.length();
    }
    return new CodeLine(elements);
  }

  private static SemanticTokenInfo colorToken(
      int line, int startChar, JsSemanticTokenColorSettings color, String text
  ) {
    String fg = color.hasForeground() ? color.getForeground().stringValue() : null;
    String bg = color.hasBackground() ? color.getBackground().stringValue() : null;
    int style = ParserConstants.TokenStyles.NORMAL;
    if (color.hasItalic() && color.getItalic()) style |= ParserConstants.TokenStyles.ITALIC;
    if (color.hasBold() && color.getBold()) style |= ParserConstants.TokenStyles.BOLD;
    return new SemanticTokenInfo(
        line, startChar, ParserConstants.TokenTypes.DEFAULT, style, fg, bg, text);
  }

  private JsTextModel(Model model) {
    javaModel = model;
    javaModel.platformObject = this;
    jsUri = null;
  }

  @Override
  public void setText(JSString newText, boolean fireEvent) {
    String[] lines = SplitJsText.split(newText).lines;
    this.fireEvent = fireEvent;
    javaModel.document.replaceText(lines);
    this.fireEvent = true;
  }

  @Override
  public void setSemanticTokens(
      JsArray<JsSemanticTokenLegendItem> legend,
      JsArray<JsSemanticToken> semanticTokens
  ) {
    if (PRINT_DEBUG) {
      System.out.println("JsTextModel.setSemanticTokens: ");
      System.out.println("\tlegend.length = " + legend.getLength());
      System.out.println("\tsemanticTokens.length = " + semanticTokens.getLength());
      for (int i = 0; i < legend.getLength(); i++) System.out.println(i + ": " + legend.get(i).print());
    }
    var tokens = new SemanticTokenInfo[semanticTokens.getLength()];
    for (int i = 0; i < semanticTokens.getLength(); i++) {
      var token = semanticTokens.get(i);
      var legendItem = legend.get(token.getLegendIdx());
      if (PRINT_DEBUG) System.out.println(token.print() + ", legend = " + legendItem.print());
      if (!validateSemanticToken(token)) continue;
      var color = legendItem.hasColor() ? legendItem.getColor() : null;
      var colorF = color != null && color.hasForeground() ? color.getForeground().stringValue() : null;
      var colorB = color != null && color.hasBackground() ? color.getBackground().stringValue() : null;
      var text = token.getText().stringValue();
      int tokenType, tokenStyle;
      if (color == null) {
        tokenType = getSemanticType(legendItem.getTokenType().stringValue());
        tokenStyle = getDefaultTokenStyle(legendItem);
      } else {
        tokenType = ParserConstants.TokenTypes.DEFAULT;
        tokenStyle = ParserConstants.TokenStyles.NORMAL;
        if (color.hasItalic() && color.getItalic()) tokenStyle |= ParserConstants.TokenStyles.ITALIC;
        if (color.hasBold() && color.getBold()) tokenStyle |= ParserConstants.TokenStyles.BOLD;
      }
      tokens[i] = new SemanticTokenInfo(
          token.getLine(), token.getStartChar(),
          tokenType, tokenStyle,
          colorF, colorB,
          text
      );
    }
    javaModel.setSemanticTokens(tokens);
  }

  private static int getDefaultTokenStyle(JsSemanticTokenLegendItem legendItem) {
    int style = ParserConstants.TokenStyles.NORMAL;
    if (!legendItem.hasModifiers()) return style;
    var modifiers = legendItem.getModifiers();
    for (int i = 0; i < modifiers.getLength(); i++) {
      var modifier = modifiers.get(i);
      style |= ParserConstants.TokenStyles.getSemanticStyle(modifier.stringValue());
    }
    return style;
  }

  private static boolean validateSemanticToken(JsSemanticToken token) {
    return token.hasLine() && token.hasStartChar() && token.hasLegendIdx() && token.hasText();
  }

  @Override
  public void dispose() {
    javaModel.document.clear();
  }

  @Override
  public void setEditListener(JsFunctions.BiConsumer<JsITextModel, JsEditInfo> listener) {
    javaModel.setOnDiffMadeListener((range) -> {
      if (fireEvent) listener.f(this, JsEditInfo.create(range[0], range[1]));
    });
  }

  @Override
  public int getOffsetAt(JsPosition position) {
    return javaModel.document.getOffsetAt(
        position.getLineNumber() - 1, position.getColumn() - 1);
  }

  @Override
  public JsPosition getPositionAt(int offset) {
    return JsPosition.fromJava(javaModel.document.getPositionAt(offset));
  }

  public JSString getText() {
    char[] chars = javaModel.document.getChars();
    return TextDecoder.decodeUTF16(chars);
  }

  @Override
  public JsUri getUri() {
    return jsUri != null ? jsUri : JsUri.fromJava(javaModel.uri);
  }

  @Override
  public JSString getLanguage() {
    String language = javaModel.language();
    return JSString.valueOf(language);
  }

  public static JsTextModel fromJava(Model model) {
    if (model.platformObject == null) {
      return new JsTextModel(model);
    }
    return (JsTextModel) model.platformObject;
  }
}
