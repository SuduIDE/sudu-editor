package org.sudu.experiments.parser.typescript.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.NullParser;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.common.base.BaseFirstLinesLexer;
import org.sudu.experiments.parser.help.Helper;
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
  protected String language() {
    return Helper.TS_LIGHT;
  }

  @Override
  protected void highlightTokens() {
    LightTypeScriptHighlighting.highlightTokens(allTokens, tokenTypes);
  }
}
