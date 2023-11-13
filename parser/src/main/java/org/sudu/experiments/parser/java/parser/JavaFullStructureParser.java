package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.JavaStructureSplitRules;
import org.sudu.experiments.parser.java.gen.st.JavaStructureLexer;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParser;
import org.sudu.experiments.parser.java.walker.StructureWalker;


public class JavaFullStructureParser extends BaseFullParser<JavaStructureParser> {

  protected boolean isComment(int type) {
    return type == JavaStructureLexer.COMMENT
        || type == JavaStructureLexer.LINE_COMMENT;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaStructureLexer(stream);
  }

  @Override
  protected JavaStructureParser initParser() {
    return new JavaStructureParser(tokenStream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JavaStructureSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaStructureLexer.NEW_LINE
        && type != JavaStructureLexer.EOF;
  }

  @Override
  protected ParserRuleContext getStartRule(JavaStructureParser parser) {
    return parser.compilationUnit();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    var defaultInterval = defaultIntervalNode();
    defaultInterval.interval.intervalType = ParserConstants.IntervalTypes.Java.COMP_UNIT;
    var stWalker = new StructureWalker(defaultInterval);

    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(stWalker, startRule);

    return stWalker.node;
  }

  @Override
  protected void highlightTokens() {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (isComment(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.COMMENT;
    }
  }
}
