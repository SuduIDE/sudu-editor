package org.sudu.experiments.parser.java.walker;

import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParser;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParserBaseListener;

import java.util.ArrayList;
import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Java.*;

public class StructureWalker extends JavaStructureParserBaseListener {

  public final List<Interval> intervals;

  private int lastClassBodyEnd = -1;

  public StructureWalker() {
    intervals = new ArrayList<>();
  }

  @Override
  public void enterCompilationUnit(JavaStructureParser.CompilationUnitContext ctx) {
    super.enterCompilationUnit(ctx);
  }

  @Override
  public void enterPackageDeclaration(JavaStructureParser.PackageDeclarationContext ctx) {
    super.enterPackageDeclaration(ctx);
    intervals.add(new Interval(ctx, PACKAGE));
  }

  @Override
  public void enterImportDeclaration(JavaStructureParser.ImportDeclarationContext ctx) {
    super.enterImportDeclaration(ctx);
    intervals.add(new Interval(ctx, IMPORT));
  }

  @Override
  public void enterTypeDeclaration(JavaStructureParser.TypeDeclarationContext ctx) {
    super.enterTypeDeclaration(ctx);
    intervals.add(new Interval(ctx, TYPE_DECL));
  }

  @Override
  public void enterClassBodyDeclaration(JavaStructureParser.ClassBodyDeclarationContext ctx) {
    super.enterClassBodyDeclaration(ctx);
    int end = ctx.stop.getStopIndex() + 1;
    intervals.add(new Interval(lastClassBodyEnd, end, CLASS_BODY));
    lastClassBodyEnd = end + 1;
  }

  @Override
  public void enterClassBody(JavaStructureParser.ClassBodyContext ctx) {
    super.enterClassBody(ctx);
    lastClassBodyEnd = ctx.LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitClassBody(JavaStructureParser.ClassBodyContext ctx) {
    super.exitClassBody(ctx);
    lastClassBodyEnd = ctx.RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterEnumDeclaration(JavaStructureParser.EnumDeclarationContext ctx) {
    super.enterEnumDeclaration(ctx);
    lastClassBodyEnd = ctx.anyBlock().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitEnumDeclaration(JavaStructureParser.EnumDeclarationContext ctx) {
    super.exitEnumDeclaration(ctx);
    lastClassBodyEnd = ctx.anyBlock().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterRecordDeclaration(JavaStructureParser.RecordDeclarationContext ctx) {
    super.enterRecordDeclaration(ctx);
    if (ctx.parent instanceof JavaStructureParser.AnyBlockContext) return;
    lastClassBodyEnd = ctx.recordBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitRecordDeclaration(JavaStructureParser.RecordDeclarationContext ctx) {
    super.exitRecordDeclaration(ctx);
    if (ctx.parent instanceof JavaStructureParser.AnyBlockContext) return;
    lastClassBodyEnd = ctx.recordBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterAnnotationTypeDeclaration(JavaStructureParser.AnnotationTypeDeclarationContext ctx) {
    super.enterAnnotationTypeDeclaration(ctx);
    lastClassBodyEnd = ctx.anyBlock().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitAnnotationTypeDeclaration(JavaStructureParser.AnnotationTypeDeclarationContext ctx) {
    super.exitAnnotationTypeDeclaration(ctx);
    lastClassBodyEnd = ctx.anyBlock().RBRACE().getSymbol().getStartIndex() + 1;
  }

}
