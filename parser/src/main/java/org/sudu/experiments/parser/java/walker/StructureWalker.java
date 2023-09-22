package org.sudu.experiments.parser.java.walker;

import org.antlr.v4.runtime.ParserRuleContext;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParser;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParserBaseListener;

import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Java.*;

public class StructureWalker extends JavaStructureParserBaseListener {

  public IntervalNode node;

  private int lastIntervalEnd = -1;

  public StructureWalker(IntervalNode node) {
    this.node = node;
  }

  @Override
  public void enterCompilationUnit(JavaStructureParser.CompilationUnitContext ctx) {
    super.enterCompilationUnit(ctx);
  }

  @Override
  public void enterPackageDeclaration(JavaStructureParser.PackageDeclarationContext ctx) {
    super.enterPackageDeclaration(ctx);
    addChild(ctx, PACKAGE);
  }

  @Override
  public void enterImportDeclaration(JavaStructureParser.ImportDeclarationContext ctx) {
    super.enterImportDeclaration(ctx);
    addChild(ctx, IMPORT);
  }

  @Override
  public void enterTypeDeclaration(JavaStructureParser.TypeDeclarationContext ctx) {
    super.enterTypeDeclaration(ctx);
    addChild(ctx, TYPE_DECL);
    enterChild();
  }

  @Override
  public void exitTypeDeclaration(JavaStructureParser.TypeDeclarationContext ctx) {
    super.exitTypeDeclaration(ctx);
    exitChild();
  }

  @Override
  public void enterClassBodyDeclaration(JavaStructureParser.ClassBodyDeclarationContext ctx) {
    super.enterClassBodyDeclaration(ctx);
    addChild(ctx, CLASS_BODY);
    enterChild();
  }

  @Override
  public void exitClassBodyDeclaration(JavaStructureParser.ClassBodyDeclarationContext ctx) {
    super.exitClassBodyDeclaration(ctx);
    exitChild();
  }

  @Override
  public void enterClassBody(JavaStructureParser.ClassBodyContext ctx) {
    super.enterClassBody(ctx);
    lastIntervalEnd = ctx.LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitClassBody(JavaStructureParser.ClassBodyContext ctx) {
    super.exitClassBody(ctx);
    lastIntervalEnd = ctx.RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterEnumDeclaration(JavaStructureParser.EnumDeclarationContext ctx) {
    super.enterEnumDeclaration(ctx);
    lastIntervalEnd = ctx.anyBlock().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitEnumDeclaration(JavaStructureParser.EnumDeclarationContext ctx) {
    super.exitEnumDeclaration(ctx);
    lastIntervalEnd = ctx.anyBlock().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterRecordDeclaration(JavaStructureParser.RecordDeclarationContext ctx) {
    super.enterRecordDeclaration(ctx);
    if (ctx.parent instanceof JavaStructureParser.AnyBlockContext) return;
    lastIntervalEnd = ctx.recordBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitRecordDeclaration(JavaStructureParser.RecordDeclarationContext ctx) {
    super.exitRecordDeclaration(ctx);
    if (ctx.parent instanceof JavaStructureParser.AnyBlockContext) return;
    lastIntervalEnd = ctx.recordBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterAnnotationTypeDeclaration(JavaStructureParser.AnnotationTypeDeclarationContext ctx) {
    super.enterAnnotationTypeDeclaration(ctx);
    lastIntervalEnd = ctx.anyBlock().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitAnnotationTypeDeclaration(JavaStructureParser.AnnotationTypeDeclarationContext ctx) {
    super.exitAnnotationTypeDeclaration(ctx);
    lastIntervalEnd = ctx.anyBlock().RBRACE().getSymbol().getStartIndex() + 1;
  }

  private void addChild(ParserRuleContext ctx, int type) {
    int end = ctx.stop.getStopIndex() + 1;
    node.addChild(new Interval(lastIntervalEnd, end, type));
    lastIntervalEnd = end;
  }

  private void enterChild() {
    node = node.lastChild();
  }

  private void exitChild() {
    node = node.parent;
  }

}
