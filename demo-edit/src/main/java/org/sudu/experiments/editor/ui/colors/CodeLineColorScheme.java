package org.sudu.experiments.editor.ui.colors;

import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.ParserConstants;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class CodeLineColorScheme {
  public final Color currentLineBg;
  public final Color definitionBg;
  public final Color usageBg;
  public final Color selectionBg;
  public final Color defaultBg;
  public final CodeElementColor[] codeElement;    // TokenTypes
  public final CodeElementColor[] semanticColors; // Semantic TokenTypes
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
    this.semanticColors = new CodeElementColor[] {
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
    };
  }

  public CodeElementColor elementColor(int ind) {
    if (ind < ParserConstants.TokenTypes.TYPES_LENGTH) return codeElement[ind];
    ind -= ParserConstants.TokenTypes.TYPES_LENGTH;
    return semanticColors[ind];
  }

  public Color collapseWaveColor() {
    return codeElement[ParserConstants.TokenTypes.COMMENT].colorF;
  }
}
