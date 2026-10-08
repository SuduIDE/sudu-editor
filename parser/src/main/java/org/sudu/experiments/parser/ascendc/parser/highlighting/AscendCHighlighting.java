package org.sudu.experiments.parser.ascendc.parser.highlighting;

import org.sudu.experiments.parser.ascendc.gen.AscendCLexer;
import org.sudu.experiments.parser.common.Highlighting;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class AscendCHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new AscendCHighlighting();

  private AscendCHighlighting() {
    addRules(
        // Language Extensions
        AscendCLexer.GLOBAL, AscendCLexer.GM_ADDR, OPERATOR,
        // Kernel Launch
        AscendCLexer.KERNEL_LAUNCH_START, AscendCLexer.KERNEL_LAUNCH_END, OPERATOR,
        // Data Type
        AscendCLexer.HALF, AscendCLexer.BFLOAT16, KEYWORD,
        // Data Structures
        AscendCLexer.LOCAL_TENSOR, AscendCLexer.MATMUL_CONFIG, TYPE,
        // TPosition / QuePosition
        AscendCLexer.POS_VECIN, AscendCLexer.CUBE_ND_ALIGN, TYPE,
        // Macros
        AscendCLexer.REGISTER_TILING_DEFAULT, AscendCLexer.ASCENDC_CPU_DEBUG, OPERATOR,
        // Pipeline
        AscendCLexer.INIT_BUFFER, AscendCLexer.DEVICE_PRINTF, TYPE,
        // Keyword
        AscendCLexer.ALIGNAS, AscendCLexer.CONCEPT, KEYWORD,
        // Int Type
        AscendCLexer.INT8_T, AscendCLexer.PTRDIFF_T, TYPE,
        // Numeric Literal
        AscendCLexer.INTEGER_LITERAL, AscendCLexer.FLOAT_LITERAL, NUMERIC,
        // Char Literal
        AscendCLexer.CHAR_LITERAL, AscendCLexer.CHAR_LITERAL, STRING,
        // String Literal
        AscendCLexer.STRING_LITERAL, AscendCLexer.RAW_STRING_LITERAL, STRING,
        // Comment
        AscendCLexer.LINE_COMMENT, AscendCLexer.LINE_COMMENT, COMMENT,
        AscendCLexer.BLOCK_COMMENT, AscendCLexer.BLOCK_COMMENT, COMMENT,
        AscendCLexer.DOCUMENTATION, AscendCLexer.DOCUMENTATION, DOCUMENTATION,
        // Directive
        AscendCLexer.DIRECTIVE, AscendCLexer.MULTILINE_MACRO, OPERATOR,
        // Operator
        AscendCLexer.SCOPE, AscendCLexer.COMMA, OPERATOR
    );
    addErrorRule(AscendCLexer.UNKNOWN);
  }
}
