package org.sudu.experiments.parser.java.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.base.BaseFullParser;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.java.JavaSplitRules;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.model.JavaClass;
import org.sudu.experiments.parser.java.walker.JavaClassWalker;
import org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting;
import org.sudu.experiments.parser.java.walker.JavaWalker;

/**
 * Deprecated
 * Use JavaFullScopeParser instead
 */
public class JavaFullParser extends BaseFullParser<JavaParser> {

  protected JavaClass javaClass;

  @Override
  protected Lexer initLexer(CharStream stream) {
    return new JavaLexer(stream);
  }

  @Override
  protected JavaParser initParser() {
    return new JavaParser(tokenStream);
  }

  @Override
  protected SplitRules initSplitRules() {
    return new JavaSplitRules();
  }

  @Override
  protected boolean tokenFilter(Token token) {
    int type = token.getType();
    return type != JavaLexer.NEW_LINE
        && type != JavaLexer.EOF;
  }

  @Override
  protected ParserRuleContext getStartRule(JavaParser parser) {
    return parser.compilationUnit();
  }

  @Override
  protected IntervalNode walk(ParserRuleContext startRule) {
    ParseTreeWalker walker = new ParseTreeWalker();
    var defaultInterval = defaultIntervalNode(ParserConstants.IntervalTypes.Java.COMP_UNIT);
    var classWalker = new JavaClassWalker(defaultInterval);
    walker.walk(classWalker, startRule);

    javaClass = classWalker.dummy;
    var types = classWalker.types;
    var javaWalker = new JavaWalker(tokenTypes, tokenStyles, javaClass, types, usageToDefinition);
    walker.walk(javaWalker, startRule);
    return classWalker.node;
  }

  @Override
  protected void highlightTokens() {
    JavaLexerHighlighting.highlightCommentTokens(allTokens, tokenTypes, tokenStyles);
  }

  public JavaClass getJavaClass() {
    return javaClass;
  }
}
