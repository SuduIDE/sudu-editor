package org.sudu.experiments.parser.cpp.walker;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.gen.CPP14ParserBaseListener;
import org.sudu.experiments.parser.cpp.model.CppClass;
import org.sudu.experiments.parser.cpp.model.CppMethod;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.cpp.walker.CppWalker.*;

public class CppClassWalker extends CPP14ParserBaseListener {

  public CppClass dummy;
  public CppClass current;

  public int curDepth = 0;
  public int maxDepth = 0;
  public double depthSum = 0;
  public int amount = 0;

  public final List<Interval> intervals;

  int lastDeclarationInd = 0;

  public CppClassWalker() {
    dummy = new CppClass(null, null, null);
    current = dummy;
    this.intervals = new ArrayList<>();
  }

  @Override
  public void enterDeclaration(CPP14Parser.DeclarationContext ctx) {
    super.enterDeclaration(ctx);

    int stop = ctx.stop.getStopIndex() + 1;
    intervals.add(new Interval(lastDeclarationInd, stop, ParserConstants.IntervalTypes.Cpp.DECLARATION));
    lastDeclarationInd = stop;
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
