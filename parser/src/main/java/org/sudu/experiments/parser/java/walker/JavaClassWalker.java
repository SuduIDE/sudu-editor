package org.sudu.experiments.parser.java.walker;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.tree.IntervalNode;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.JavaParserBaseListener;
import org.sudu.experiments.parser.java.model.JavaConstructor;
import org.sudu.experiments.parser.java.model.JavaField;
import org.sudu.experiments.parser.java.model.JavaClass;
import org.sudu.experiments.parser.java.model.JavaMethod;

import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Java.*;
import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.UNKNOWN;
import static org.sudu.experiments.parser.java.walker.JavaWalker.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Walker for getting JavaClass
public class JavaClassWalker extends JavaParserBaseListener {
  public JavaClass dummy;
  public JavaClass current;
  public List<Decl> types;

  public int curDepth = 0;
  public int maxDepth = 0;
  public double depthSum = 0;
  public int amount = 0;

  public IntervalNode node;
  public int intervalStart = 0;

  private int lastIntervalEnd = 0;

  public JavaClassWalker(IntervalNode node) {
    dummy = new JavaClass(null, null, null);
    current = dummy;
    types = new ArrayList<>();
    this.node = node;
  }

  @Override
  public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
    super.enterPackageDeclaration(ctx);
    addChild(ctx, PACKAGE);
  }

  @Override
  public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
    super.enterImportDeclaration(ctx);
    addChild(ctx, IMPORT);

    var node = getLastNode(ctx.qualifiedName());
    types.add(Decl.fromNode(node));
  }

  @Override
  public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
    super.enterTypeDeclaration(ctx);
    addChild(ctx, TYPE_DECL);
    enterChild();
  }

  @Override
  public void exitTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
    super.exitTypeDeclaration(ctx);
    exitChild();
  }

  @Override
  public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
    super.enterClassBodyDeclaration(ctx);
    if (ctx.parent instanceof JavaParser.EnumBodyDeclarationsContext) return;
    addChild(ctx, CLASS_BODY);
    enterChild();
  }

  @Override
  public void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
    super.exitClassBodyDeclaration(ctx);
    exitChild();
  }

  @Override
  public void enterInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {
    super.enterInterfaceBodyDeclaration(ctx);
    addChild(ctx, CLASS_BODY);
    enterChild();
  }

  @Override
  public void exitInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {
    super.exitInterfaceBodyDeclaration(ctx);
    exitChild();
  }

  @Override
  public void enterAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {
    super.enterAnnotationTypeElementDeclaration(ctx);
    addChild(ctx, UNKNOWN);
    enterChild();
  }

  @Override
  public void exitAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {
    super.exitAnnotationTypeElementDeclaration(ctx);
    exitChild();
  }

  @Override
  public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.enterClassDeclaration(ctx);
    var id = getNode(ctx.identifier());
    JavaClass clazz = new JavaClass(id.getText(), Pos.fromNode(id), current);
    current.nestedClasses.add(clazz);
    current = clazz;

    types.add(Decl.fromNode(id));

    lastIntervalEnd = ctx.classBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.enterInterfaceDeclaration(ctx);
    var id = getNode(ctx.identifier());
    JavaClass clazz = new JavaClass(id.getText(), Pos.fromNode(id), current);
    current.nestedClasses.add(clazz);
    current = clazz;

    types.add(Decl.fromNode(id));

    lastIntervalEnd = ctx.interfaceBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.enterEnumDeclaration(ctx);
    var id = getNode(ctx.identifier());
    JavaClass clazz = new JavaClass(id.getText(), Pos.fromNode(id), current);
    current.nestedClasses.add(clazz);
    current = clazz;

    types.add(Decl.fromNode(id));

    lastIntervalEnd = ctx.LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.enterRecordDeclaration(ctx);
    var id = getNode(ctx.identifier());
    JavaClass clazz = new JavaClass(id.getText(), Pos.fromNode(id), current);
    current.nestedClasses.add(clazz);
    current = clazz;

    types.add(Decl.fromNode(id));

    lastIntervalEnd = ctx.recordBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.enterAnnotationTypeDeclaration(ctx);
    var id = getNode(ctx.identifier());
    JavaClass clazz = new JavaClass(id.getText(), Pos.fromNode(id), current);
    current.nestedClasses.add(clazz);
    current = clazz;

    types.add(Decl.fromNode(id));

    lastIntervalEnd = ctx.annotationTypeBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.exitClassDeclaration(ctx);
    current = current.innerClass;

    lastIntervalEnd = ctx.classBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.exitInterfaceDeclaration(ctx);
    current = current.innerClass;

    lastIntervalEnd = ctx.interfaceBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.exitEnumDeclaration(ctx);
    current = current.innerClass;

    lastIntervalEnd = ctx.RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.exitRecordDeclaration(ctx);
    current = current.innerClass;

    lastIntervalEnd = ctx.recordBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.exitAnnotationTypeDeclaration(ctx);
    current = current.innerClass;

    lastIntervalEnd = ctx.annotationTypeBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
    super.exitFieldDeclaration(ctx);
    var variableDeclarators = getVarDeclarators(ctx.variableDeclarators());
    String type = getType(ctx.typeType());
    boolean isStatic = isStatic(getModifiers(ctx));
    for (var variableDeclarator : variableDeclarators) {
      addField(variableDeclarator, type, isStatic);
    }
  }

  @Override
  public void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
    super.exitConstDeclaration(ctx);
    var constantDeclarators = ctx.constantDeclarator();
    var type = getType(ctx.typeType());
    for (var constantDeclarator : constantDeclarators) {
      var node = getNode(constantDeclarator.identifier());
      addField(node, type, true);
    }
  }

  @Override
  public void exitAnnotationConstantRest(JavaParser.AnnotationConstantRestContext ctx) {
    super.exitAnnotationConstantRest(ctx);
    var variableDeclarators = ctx.variableDeclarators().variableDeclarator();
    boolean isStatic = isStatic(getAnnotationMethodOrConstantRestModifiers((JavaParser.AnnotationMethodOrConstantRestContext) ctx.parent));
    String type = getType(((JavaParser.AnnotationTypeElementRestContext) ctx.parent.parent).typeType());
    for (var variableDeclarator: variableDeclarators) {
      var variableDeclaratorId = variableDeclarator.variableDeclaratorId();
      var node = getNode(variableDeclaratorId.identifier());
      addField(node, type, isStatic);
    }
  }

  @Override
  public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
    super.exitMethodDeclaration(ctx);
    var node = getNode(ctx.identifier());
    var isStatic = isStatic(getModifiers(ctx));
    addMethod(node, getType(ctx.typeTypeOrVoid()), isStatic, getArgsTypes(ctx.formalParameters()));
  }

  @Override
  public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.exitConstructorDeclaration(ctx);
    var node = getNode(ctx.identifier());
    addConstructor(node, getArgsTypes(ctx.formalParameters()));
  }

  @Override
  public void exitCompactConstructorDeclaration(JavaParser.CompactConstructorDeclarationContext ctx) {
    super.exitCompactConstructorDeclaration(ctx);
    var node = getNode(ctx.identifier());
    addConstructor(node, List.of());
  }

  @Override
  public void exitAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx) {
    super.exitAnnotationMethodRest(ctx);
    var node = getNode(ctx.identifier());
    String type = getType(((JavaParser.AnnotationTypeElementRestContext) ctx.parent.parent).typeType());
    var isStatic = isStatic(getAnnotationMethodOrConstantRestModifiers((JavaParser.AnnotationMethodOrConstantRestContext) ctx.parent));
    addMethod(node, type, isStatic, List.of());
  }

  @Override
  public void exitInterfaceCommonBodyDeclaration(JavaParser.InterfaceCommonBodyDeclarationContext ctx) {
    super.exitInterfaceCommonBodyDeclaration(ctx);
    var node = getNode(ctx.identifier());
    addMethod(node, getType(ctx.typeTypeOrVoid()), false, getArgsTypes(ctx.formalParameters()));
  }

  private void addField(TerminalNode node, String type, boolean isStatic) {
    var token = node.getSymbol();
    var pos = new Pos(token.getLine(), token.getCharPositionInLine());
    var text = token.getText();
    current.fields.add(new JavaField(text, pos, type, isStatic));
  }

  private void addMethod(TerminalNode node, String type, boolean isStatic, List<String> argsTypes) {
    var token = node.getSymbol();
    var pos = new Pos(token.getLine(), token.getCharPositionInLine());
    var text = token.getText();
    current.methods.add(new JavaMethod(text, pos, type, isStatic, argsTypes));
  }

  private void addConstructor(TerminalNode node, List<String> argsTypes) {
    var token = node.getSymbol();
    var pos = new Pos(token.getLine(), token.getCharPositionInLine());
    var text = token.getText();
    current.constructors.add(new JavaConstructor(text, pos, argsTypes));
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

  private void addChild(ParserRuleContext ctx, int type) {
    int end = ctx.stop.getStopIndex() + 1;
    node.addChild(new Interval(lastIntervalEnd + intervalStart, end + intervalStart, type));
    lastIntervalEnd = end;
  }

  private void enterChild() {
    node = node.lastChild();
  }

  private void exitChild() {
    node = node.parent;
  }

  public static boolean isStatic(List<String> list) {
    return list.contains("static");
  }

  public static List<String> getAnnotationMethodOrConstantRestModifiers(JavaParser.AnnotationMethodOrConstantRestContext ctx) {
    ArrayList<String> modifiers = new ArrayList<>();
    if (ctx.parent.parent instanceof JavaParser.AnnotationTypeElementDeclarationContext declarationContext) {
      for (var mod : declarationContext.modifier())
        modifiers.add(mod.getChild(0).getText());
    }
    return modifiers;
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
      modifiers.add(mod.getChild(0).getText());
    return modifiers;
  }
}