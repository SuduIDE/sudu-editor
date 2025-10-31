package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CodeLineColorScheme {
  public final Color currentLineBg;
  public final Color definitionBg;
  public final Color usageBg;
  public final Color selectionBg;
  public final Color defaultBg;
  public final CodeElementColor[] codeElement;                  // TokenTypes
  public final CodeElementColor[] defaultSemanticColors;        // Semantic TokenTypes
  public final ArrayList<CodeElementColor> semanticColors;      // Semantic Tokens, that have color property
  public final Map<Pair<String, String>, Integer> colorToIndex; // (foreground, background) -> index
  public final DiffColors diff;

  public CodeLineColorScheme(
      Color currentLineBg, Color definitionBg, Color usageBg,
      Color selectionBg, Color defaultBg,
      CodeElementColor[] codeElement, DiffColors diff
  ) {
    this.currentLineBg = currentLineBg;
    this.definitionBg = definitionBg;
    this.usageBg = usageBg;
    this.selectionBg = selectionBg;
    this.defaultBg = defaultBg;
    this.codeElement = codeElement;
    this.diff = diff;
    this.defaultSemanticColors = new CodeElementColor[] {
        codeElement[TYPE],       // Namespace — For identifiers that declare or reference a namespace, module, or package.
        codeElement[TYPE],       // Class — For identifiers that declare or reference a class type.
        codeElement[TYPE],       // Enum — For identifiers that declare or reference an enumeration type.
        codeElement[TYPE],       // Interface — For identifiers that declare or reference an interface type.
        codeElement[TYPE],       // Struct — For identifiers that declare or reference a struct type.
        codeElement[TYPE],       // TypeParameter — For identifiers that declare or reference a type parameter.
        codeElement[TYPE],       // Type — For identifiers that declare or reference a type that is not covered above.
        codeElement[DEFAULT],    // Parameter — For identifiers that declare or reference a function or method parameters.
        codeElement[DEFAULT],    // Variable — For identifiers that declare or reference a local or global variable.
        codeElement[FIELD],      // Property — For identifiers that declare or reference a member property, member field, or member variable.
        codeElement[FIELD],      // EnumMember — For identifiers that declare or reference an enumeration property, constant, or member.
        codeElement[ANNOTATION], // Decorator — For identifiers that declare or reference decorators and annotations.
        codeElement[ANNOTATION], // Event — For identifiers that declare an event property.
        codeElement[METHOD],     // Function — For identifiers that declare a function.
        codeElement[METHOD],     // Method — For identifiers that declare a member function or method.
        codeElement[ANNOTATION], // Macro — For identifiers that declare a macro.
        codeElement[ANNOTATION], // Label — For identifiers that declare a label.
        codeElement[COMMENT],    // Comment — For tokens that represent a comment.
        codeElement[STRING],     // String — For tokens that represent a string literal.
        codeElement[KEYWORD],    // Keyword — For tokens that represent a language keyword.
        codeElement[NUMERIC],    // Number — For tokens that represent a number literal.
        codeElement[STRING],     // Regexp — For tokens that represent a regular expression literal.
        codeElement[OPERATOR],   // Operator — For tokens that represent an operator.

        codeElement[ANNOTATION], // Modifier — ???
        codeElement[OPERATOR],   // Bracket — ???

        codeElement[KEYWORD],    // Builtin Constant — ???
        codeElement[DEFAULT],    // Default — ???

        codeElement[DEFAULT],    // Unknown — ???
    };
    semanticColors = new ArrayList<>();
    colorToIndex = new HashMap<>();
  }

  public int getSemanticIndex(String foreground, String background) {
    var key = Pair.of(
        Objects.requireNonNullElse(foreground, ""),
        Objects.requireNonNullElse(background, "")
    );
    var ind = colorToIndex.get(key);
    if (ind == null) {
      Color colorF = foreground != null ? new Color(foreground) : codeElement[DEFAULT].colorF;
      Color colorB = background != null ? new Color(background) : codeElement[DEFAULT].colorB;
      var elemColor = new CodeElementColor(colorF, colorB);
      ind = semanticColors.size();
      semanticColors.add(elemColor);
      colorToIndex.put(key, ind);
    }
    return TYPES_LENGTH + SEMANTIC_LENGTH + ind;
  }

  public CodeElementColor elementColor(int ind) {
    if (ind < TYPES_LENGTH) return codeElement[ind];
    ind -= TYPES_LENGTH;
    if (ind < SEMANTIC_LENGTH) return defaultSemanticColors[ind];
    ind -= SEMANTIC_LENGTH;
    if (semanticColors.isEmpty()) return codeElement[DEFAULT];
    return semanticColors.get(ind);
  }

  public Color collapseWaveColor() {
    return codeElement[ParserConstants.TokenTypes.COMMENT].colorF;
  }
}
