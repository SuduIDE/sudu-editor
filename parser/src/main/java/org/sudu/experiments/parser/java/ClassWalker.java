package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.JavaParserBaseListener;
import org.sudu.experiments.parser.java.model.JavaClass;
import org.sudu.experiments.parser.java.model.JavaMethodField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Walker for getting JavaClass
class ClassWalker extends JavaParserBaseListener {
  JavaClass dummy;
  JavaClass current;

  int curDepth = 0;
  int maxDepth = 0;
  double depthSum = 0;
  int amount = 0;

  private static final int COMP_UNIT = 0;
  private static final int PACKAGE = 1;
  private static final int IMPORT = 2;
  private static final int TYPE_DECL = 3;
  private static final int CLASS_BODY_DECL = 4;
  private static final int INTERFACE_BODY_DECL = 5;
  private static final int ANNO_TYPE_ELEM_DECL = 6;
  private static final int ENUM_CONSTS = 7;

  List<Interval> intervals;

  public ClassWalker() {
    dummy = new JavaClass(null);
    current = dummy;
    intervals = new ArrayList<>();
  }

  @Override
  public void enterCompilationUnit(JavaParser.CompilationUnitContext ctx) {
    super.enterCompilationUnit(ctx);
    intervals.add(new Interval(ctx, COMP_UNIT));
  }

  @Override
  public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
    super.enterPackageDeclaration(ctx);
    intervals.add(new Interval(ctx, PACKAGE));
  }

  @Override
  public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
    super.enterImportDeclaration(ctx);
    intervals.add(new Interval(ctx, IMPORT));
  }

  @Override
  public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
    super.enterTypeDeclaration(ctx);
    intervals.add(new Interval(ctx, TYPE_DECL));
  }

  @Override
  public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
    super.enterClassBodyDeclaration(ctx);
    intervals.add(new Interval(ctx, CLASS_BODY_DECL));
  }

  @Override
  public void enterInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {
    super.enterInterfaceBodyDeclaration(ctx);
    intervals.add(new Interval(ctx, INTERFACE_BODY_DECL));
  }

  @Override
  public void enterAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {
    super.enterAnnotationTypeElementDeclaration(ctx);
    intervals.add(new Interval(ctx, ANNO_TYPE_ELEM_DECL));
  }

  @Override
  public void enterEnumConstants(JavaParser.EnumConstantsContext ctx) {
    super.enterEnumConstants(ctx);
    intervals.add(new Interval(ctx, ENUM_CONSTS));
  }

  @Override
  public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.enterClassDeclaration(ctx);
    String className = getIdentifier(ctx.identifier()).getText();
    JavaClass clazz = new JavaClass(className, current);
    current.nestedClasses.add(clazz);
    current = clazz;
  }

  @Override
  public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.enterInterfaceDeclaration(ctx);
    String className = getIdentifier(ctx.identifier()).getText();
    JavaClass clazz = new JavaClass(className, current);
    current.nestedClasses.add(clazz);
    current = clazz;
  }

  @Override
  public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.enterEnumDeclaration(ctx);
    String className = getIdentifier(ctx.identifier()).getText();
    JavaClass clazz = new JavaClass(className, current);
    current.nestedClasses.add(clazz);
    current = clazz;
  }

  @Override
  public void enterRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.enterRecordDeclaration(ctx);
    String className = getIdentifier(ctx.identifier()).getText();
    JavaClass clazz = new JavaClass(className, current);
    current.nestedClasses.add(clazz);
    current = clazz;
  }

  @Override
  public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.exitClassDeclaration(ctx);
    current = current.innerClass;
  }

  @Override
  public void exitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.exitInterfaceDeclaration(ctx);
    current = current.innerClass;
  }

  @Override
  public void exitEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.exitEnumDeclaration(ctx);
    current = current.innerClass;
  }

  @Override
  public void exitRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.exitRecordDeclaration(ctx);
    current = current.innerClass;
  }

  @Override
  public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
    super.exitFieldDeclaration(ctx);
    var variableDeclarators = ctx.variableDeclarators().variableDeclarator();
    for (var variableDeclarator : variableDeclarators) {
      var variableDeclaratorId = variableDeclarator.variableDeclaratorId();
      var node = getIdentifier(variableDeclaratorId.identifier());
      boolean isStatic = isStatic(getModifiers(ctx));
      current.fields.add(new JavaMethodField(node.getText(), isStatic));
    }
  }

  @Override
  public void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
    super.exitConstDeclaration(ctx);
    var constantDeclarators = ctx.constantDeclarator();
    for (var constantDeclarator : constantDeclarators) {
      var node = getIdentifier(constantDeclarator.identifier());
      current.fields.add(new JavaMethodField(node.getText(), true));
    }
  }

  @Override
  public void exitEnumConstant(JavaParser.EnumConstantContext ctx) {
    super.exitEnumConstant(ctx);
    var node = getIdentifier(ctx.identifier());
    current.fields.add(new JavaMethodField(node.getText(), true));
  }

  @Override
  public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
    super.exitMethodDeclaration(ctx);
    var node = getIdentifier(ctx.identifier());
    var isStatic = isStatic(getModifiers(ctx));
    current.methods.add(new JavaMethodField(node.getText(), isStatic));
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

  public static boolean isStatic(List<String> list) {
    return list.contains("static");
  }

  public static TerminalNode getIdentifier(JavaParser.IdentifierContext ctx) {
    return (TerminalNode) ctx.children.get(0);
  }

  public static List<String> getModifiers(RuleContext ctx) {
    var parent = ctx.parent.parent;
    JavaParser.ClassBodyDeclarationContext classBodyDeclaration = null;

    if (parent instanceof JavaParser.ClassBodyDeclarationContext) {
      classBodyDeclaration = (JavaParser.ClassBodyDeclarationContext) parent;
    } else if (parent instanceof JavaParser.MemberDeclarationContext memberDeclarationContext) {
      if (memberDeclarationContext.parent instanceof JavaParser.ClassBodyDeclarationContext) {
        classBodyDeclaration = (JavaParser.ClassBodyDeclarationContext) memberDeclarationContext.parent;
      }
    }
    if (classBodyDeclaration == null) return Collections.emptyList();
    ArrayList<String> modifiers = new ArrayList<>();
    for (var mod : classBodyDeclaration.modifier())
      modifiers.add(mod.children.get(0).getText());
    return modifiers;
  }
}