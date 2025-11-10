package org.sudu.experiments.esm;

import org.sudu.experiments.SplitInfo;
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

  @JSFunctor
  public interface Api extends JSObject {
    JsITextModel create(JSString value, JSString language, JsUri uri);

    class Setter {
      @JSBody(params = {"f"}, script = "newTextModel = f;")
      static native void set(Api f);
    }

    static void install() {
      Setter.set(JsTextModel::new);
    }
  }

  public final Model javaModel;
  public final JsUri jsUri;
  public final JSString jsLanguage;
  private boolean fireEvent = true;
  public static final boolean PRINT_DEBUG = true;

  public JsTextModel(JSString text, JSString language, JsUri uri) {
    SplitInfo split = SplitJsText.split(text);
    String lang = JsHelper.toString(language, null);
    javaModel = new Model(split, lang, uri.toJava());
    javaModel.platformObject = this;
    jsLanguage = language;
    jsUri = uri;
  }

  private JsTextModel(Model model) {
    javaModel = model;
    javaModel.platformObject = this;
    jsUri = null;
    jsLanguage = null;
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
      var tokenType = getSemanticType(legendItem.getTokenType().stringValue());
      int tokenStyle;
      if (color == null) tokenStyle = getDefaultTokenStyle(legendItem);
      else {
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
  public void setEditListener(JsFunctions.Consumer<JsITextModel> listener) {
    javaModel.setOnDiffMadeListener(() -> {
      if (fireEvent) listener.f(this);
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
    return jsLanguage != null ? jsLanguage : JSString.valueOf(javaModel.docLanguage());
  }

  public static JsTextModel fromJava(Model model) {
    if (model.platformObject == null) {
      return new JsTextModel(model);
    }
    return (JsTextModel) model.platformObject;
  }
}
