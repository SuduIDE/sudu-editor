package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.gen.st.JavaStructureLexer;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParser;
import org.sudu.experiments.parser.java.walker.StructureWalker;

import java.util.*;

import static org.sudu.experiments.parser.java.ParserConstants.*;

public class JavaFullStructureParser extends BaseJavaFullParser {

  public int[] parse(String source) {
    long parsingStartTime = System.currentTimeMillis();

    initLexer(source);
    JavaStructureParser parser = new JavaStructureParser(tokenStream);

    var compUnit = parser.compilationUnit();
    var stWalker = new StructureWalker();
    stWalker.intervals.add(new Interval(0, source.length(), IntervalTypes.COMP_UNIT));

    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(stWalker, compUnit);

    List<Interval> intervals = stWalker.intervals;
    for (var token : allTokens) {
      if (token.getType() == JavaStructureLexer.COMMENT) {
        var commentInterval = new Interval(token.getStartIndex(), token.getStopIndex() + 1, IntervalTypes.COMMENT);
        intervals.add(commentInterval);
      }
    }

    int[] result = getInts(stWalker.intervals);
    System.out.println("Parsing java structure time: " + (System.currentTimeMillis() - parsingStartTime) + "ms");
    return result;
  }

  @Override
  protected boolean isComment(int type) {
    return type == JavaStructureLexer.COMMENT
        || type == JavaStructureLexer.LINE_COMMENT;
  }

  @Override
  protected boolean isMultilineToken(int tokenType) {
    return true;
  }

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaStructureLexer(stream);
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaStructureLexer.NEW_LINE
        && type != JavaStructureLexer.EOF;
  }

}
