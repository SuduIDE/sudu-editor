package org.sudu.experiments.parser.cpp.walker;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.gen.CPP14ParserBaseListener;
import org.sudu.experiments.parser.cpp.model.CppClass;
import org.sudu.experiments.parser.cpp.model.CppMethod;

import java.util.List;

import static org.sudu.experiments.parser.cpp.walker.CppWalker.*;
import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Cpp.*;

public class CppClassWalker extends CPP14ParserBaseListener {

  public CppClass dummy;
  public CppClass current;

  public int curDepth = 0;
  public int maxDepth = 0;
  public double depthSum = 0;
  public int amount = 0;

  public IntervalNode node;

  public int intervalStart = 0;
  private int lastIntervalEnd = 0;

  public CppClassWalker(IntervalNode node) {
    dummy = new CppClass(null, null, null);
    current = dummy;
    this.node = node;
  }

  @Override
  public void enterDeclaration(CPP14Parser.DeclarationContext ctx) {
    super.enterDeclaration(ctx);
    addChild(ctx, DECLARATION);
    enterChild();
  }

  @Override
  public void exitDeclaration(CPP14Parser.DeclarationContext ctx) {
    super.exitDeclaration(ctx);
    exitChild();
  }

  @Override
  public void enterMemberSpecification(CPP14Parser.MemberSpecificationContext ctx) {
    super.enterMemberSpecification(ctx);
    var classHead = (CPP14Parser.ClassSpecifierContext) ctx.parent;
    var className = classHead.classHead().classHeadName().className();
    var node = getIdentifier(className);

    CppClass struct = new CppClass(className.getText(), Pos.fromNode(node), current);
    current.nestedClasses.add(struct);
    current = struct;
  }

  @Override
  public void exitMemberSpecification(CPP14Parser.MemberSpecificationContext ctx) {
    super.exitMemberSpecification(ctx);
    current = current.innerClass;
  }

  @Override
  public void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx) {
    super.enterFunctionDefinition(ctx);
    if (!(ctx.parent instanceof CPP14Parser.MemberdeclarationContext)) return;

    var node = getIdentifier(ctx.declarator());
    if (node == null) return;
    var method = new CppMethod(node.getText(), Pos.fromNode(node), List.of());
    current.methods.add(method);
  }

  @Override
  public void enterMemberDeclarator(CPP14Parser.MemberDeclaratorContext ctx) {
    super.enterMemberDeclarator(ctx);
    var node = getIdentifier(ctx.declarator());
    if (node == null) return;
    var field = Decl.fromNode(node);
    current.fields.add(field);
  }

  private void addChild(ParserRuleContext ctx, int type) {
    int end = ctx.stop.getStopIndex() + 1;
    node.addChild(new Interval(intervalStart + lastIntervalEnd, intervalStart + end, type));
    lastIntervalEnd = end;
  }

  private void enterChild() {
    node = node.lastChild();
  }

  private void exitChild() {
    node = node.parent;
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    super.enterEveryRule(ctx);
    curDepth++;
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    super.exitEveryRule(ctx);
    curDepth--;
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    super.visitTerminal(node);
    maxDepth = Math.max(maxDepth, curDepth);
    depthSum += curDepth;
    amount++;
  }

}
