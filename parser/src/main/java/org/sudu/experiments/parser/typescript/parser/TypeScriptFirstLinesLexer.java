package org.sudu.experiments.parser.typescript.parser;

import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.typescript.TsSplitRules;
import org.sudu.experiments.parser.typescript.gen.LightTypeScriptLexer;
import org.sudu.experiments.parser.typescript.parser.highlighting.LightTypeScriptHighlighting;

public class TypeScriptFirstLinesLexer extends BaseFirstLinesLexer<NullParser> {

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new LightTypeScriptLexer(stream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new TsSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != LightTypeScriptLexer.LineTerminator
        && type != LightTypeScriptLexer.EOF;
  }

  @Override
  protected void highlightTokens() {
    LightTypeScriptHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
