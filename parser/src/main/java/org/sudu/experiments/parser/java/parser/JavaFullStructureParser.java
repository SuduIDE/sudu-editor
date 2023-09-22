package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.BaseFullParser;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.JavaStructureSplitRules;
import org.sudu.experiments.parser.java.gen.st.JavaStructureLexer;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParser;
import org.sudu.experiments.parser.java.walker.StructureWalker;

import static org.sudu.experiments.parser.ParserConstants.*;

public class JavaFullStructureParser extends BaseFullParser {

  public int[] parse(String source) {
    long parsingStartTime = System.currentTimeMillis();

    initLexer(source);
    JavaStructureParser parser = new JavaStructureParser(tokenStream);

    var compUnit = parser.compilationUnit();
    Interval compUnitInterval = new Interval(0, source.length(), IntervalTypes.Java.COMP_UNIT);
    var stWalker = new StructureWalker(new IntervalNode(compUnitInterval));

    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(stWalker, compUnit);

    int[] result = getInts(stWalker.node);
    System.out.println("Parsing java structure time: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  protected boolean isComment(int type) {
    return type == JavaStructureLexer.COMMENT
        || type == JavaStructureLexer.LINE_COMMENT;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaStructureLexer(stream);
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
  protected void highlightTokens() {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      if (isComment(token.getType())) tokenTypes[ind] = TokenTypes.COMMENT;
      if (isErrorToken(token.getType())) tokenTypes[ind] = ParserConstants.TokenTypes.ERROR;
    }
  }

}
