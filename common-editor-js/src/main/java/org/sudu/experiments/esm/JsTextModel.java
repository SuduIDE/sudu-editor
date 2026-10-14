package org.sudu.experiments.esm;

import org.sudu.experiments.SplitInfo;
import org.sudu.experiments.editor.CodeLine;
import org.sudu.experiments.editor.Document;
import org.sudu.experiments.editor.Model;
import org.sudu.experiments.editor.SemanticTokenInfo;
import org.sudu.experiments.esm.semantic.JsSemanticToken;
import org.sudu.experiments.esm.semantic.JsSemanticTokenLegendItem;
import org.sudu.experiments.js.*;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.text.SplitJsText;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

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

      @JSBody(params = {"f"}, script = "newTextModelWithInline = f;")
      static native void setInline(InlineApi f);
    }

    static void install() {
      Setter.set(JsTextModel::new);
      Setter.setInline(JsTextModel::newInline);
    }
  }

  // Builds a mapped (inline-disassembly) document from an array of line descriptors.
  // See demo-edit/docs/inline-disasm-open-questions.md (Q2).
  @JSFunctor
  public interface InlineApi extends JSObject {
    JsITextModel create(
        JsWorkerPool workerPool,
        JsArray<JsInlineDisasmLine> lines,
        JSString language, JsUri uri);
  }

  public static JsTextModel newInline(
      JsWorkerPool workerPool,
      JsArray<JsInlineDisasmLine> jsLines,
      JSString language, JsUri uri
  ) {
    int n = jsLines == null ? 0 : jsLines.getLength();
    int sourceCount = 0;
    CodeLine[] data = new CodeLine[Math.max(1, n)];
    for (int i = 0; i < n; i++) {
      JsInlineDisasmLine d = jsLines.get(i);
      String text = d != null && d.hasText() ? d.getText().stringValue() : "";
      CodeLine line = new CodeLine(text);
      if (d != null && d.hasLineNumber()) line.lineNumber = d.getLineNumber();
      if (line.lineNumber > 0) sourceCount++;
      data[i] = line;
    }
    if (n == 0) data[0] = new CodeLine("");

    Document document = Document.mapped(data, sourceCount);
    String lang = JsHelper.toString(language, null);
    Model model = new Model(document, lang, uri != null ? uri.toJava() : null);
    model.setExecutor(JsWorkerPool.pool(workerPool));
    return new JsTextModel(model);
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
