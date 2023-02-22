package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.JavaParserBaseListener;
import org.sudu.experiments.parser.java.model.JavaClass;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaWalker extends JavaParserBaseListener {
  private final int[] tokenTypes;
  private final int[] tokenStyles;
  private JavaClass javaClass;

  private Set<String> curMethodArgs;

  private static final int KEYWORD = 1;
  private static final int NUMERIC = 7;
  private static final int BOOLEAN = 1;
  private static final int STRING = 3;
  private static final int NULL = 1;
  private static final int SEMI = 1;
  private static final int ERROR = 5;
  private static final int FIELD = 2;
  private static final int METHOD = 8;
  private static final int ANNOTATION = 12;

  private static final int NORMAL = 0;
  private static final int ITALIC = 1;
  private static final int BOLD = 2;
  private static final int ITALIC_BOLD = 3;

  public JavaWalker(int[] tokenTypes, int[] tokenStyles, JavaClass javaClass) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.javaClass = javaClass;

    curMethodArgs = Collections.emptySet();
  }

  @Override
  public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
    super.exitFieldDeclaration(ctx);

    var variableDeclarators = ctx.variableDeclarators().variableDeclarator();
    for (var variableDeclarator: variableDeclarators) {
      var id = getIdentifier(variableDeclarator.variableDeclaratorId().identifier()).getSymbol();
      var modifiers = ClassWalker.getModifiers(ctx);
      tokenTypes[id.getTokenIndex()] = FIELD;
      tokenStyles[id.getTokenIndex()] = ClassWalker.isStatic(modifiers) ? ITALIC : NORMAL;
    }
  }

  @Override
  public void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
    super.exitConstDeclaration(ctx);

    var constantDeclarators = ctx.constantDeclarator();
    for (var constantDeclarator: constantDeclarators) {
      var id = getIdentifier(constantDeclarator.identifier()).getSymbol();
      tokenTypes[id.getTokenIndex()] = FIELD;
      tokenStyles[id.getTokenIndex()] = ITALIC;
    }
  }

  @Override
  public void exitEnumConstant(JavaParser.EnumConstantContext ctx) {
    super.exitEnumConstant(ctx);
    var node = getIdentifier(ctx.identifier()).getSymbol();
    tokenTypes[node.getTokenIndex()] = FIELD;
    tokenStyles[node.getTokenIndex()] = ITALIC;
  }

  @Override
  public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.enterClassDeclaration(ctx);
    int ind = javaClass.nestPos;
    javaClass = javaClass.nestedClasses.get(ind);
  }

  @Override
  public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.enterInterfaceDeclaration(ctx);
    int ind = javaClass.nestPos;
    javaClass = javaClass.nestedClasses.get(ind);
  }

  @Override
  public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.exitClassDeclaration(ctx);
    javaClass = javaClass.innerClass;
    javaClass.nestPos++;
  }

  @Override
  public void exitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.exitInterfaceDeclaration(ctx);
    javaClass = javaClass.innerClass;
    javaClass.nestPos++;
  }

  @Override
  public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
    super.enterMethodDeclaration(ctx);
    curMethodArgs = getMethodArguments(ctx.formalParameters());
  }

  @Override
  public void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.enterConstructorDeclaration(ctx);
    curMethodArgs = getMethodArguments(ctx.formalParameters());
  }

  @Override
  public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
    super.exitMethodDeclaration(ctx);
    curMethodArgs = Collections.emptySet();

    var id = getIdentifier(ctx.identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = METHOD;
    tokenStyles[id.getTokenIndex()] = NORMAL;
  }

  @Override
  public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.exitConstructorDeclaration(ctx);
    curMethodArgs = Collections.emptySet();

    var id = getIdentifier(ctx.identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = METHOD;
  }

  @Override
  public void enterQualifiedName(JavaParser.QualifiedNameContext ctx) {
    super.enterQualifiedName(ctx);
    var first = ctx.identifier(0);
    var ind = first.getRuleIndex();
    if (isField(first)) {
      var field = javaClass.getField(first.getText());
      tokenTypes[ind] = FIELD;
      tokenStyles[ind] = field.isStatic ? ITALIC : NORMAL;
    };
  }

  @Override
  public void enterPrimary(JavaParser.PrimaryContext ctx) {
    super.enterPrimary(ctx);
    if (ctx.identifier() == null) return;
    var token = getIdentifier(ctx.identifier()).getSymbol();
    if (isField(ctx.identifier())) {
      var field = javaClass.getField(ctx.identifier().getText());
      tokenTypes[token.getTokenIndex()] = FIELD;
      tokenStyles[token.getTokenIndex()] = field.isStatic ? ITALIC : NORMAL;
    }
  }

  @Override
  public void exitIdentifier(JavaParser.IdentifierContext ctx) {
    super.exitIdentifier(ctx);
    var name = ctx.getText();
    if (ctx.parent instanceof JavaParser.ExpressionContext parent && parent.getText().startsWith("this.")) {
      var token = getIdentifier(parent.identifier()).getSymbol();
      var field = javaClass.getField(name);
      if (field != null) {
        tokenTypes[token.getTokenIndex()] = FIELD;
        tokenStyles[token.getTokenIndex()] = field.isStatic ? ITALIC : NORMAL;
      }
      else
        tokenTypes[token.getTokenIndex()] = ERROR;
    }
    var method = javaClass.getMethod(name);
    if (method != null && method.isStatic)
      tokenStyles[getIdentifier(ctx).getSymbol().getTokenIndex()] = ITALIC;
  }

  @Override
  public void exitAnnotation(JavaParser.AnnotationContext ctx) {
    super.exitAnnotation(ctx);
    var ids = ctx.qualifiedName().identifier();
    for (var id: ids) {
      var node = getIdentifier(id).getSymbol();
      tokenTypes[node.getTokenIndex()] = ANNOTATION;
    }
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    super.visitTerminal(node);
    var token = node.getSymbol();
    int ind = token.getTokenIndex();
    int type = token.getType();
    if (isKeyword(type) && !isIdentifier(node)) tokenTypes[ind] = KEYWORD;
    else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
    else if (isBooleanLiteral(type)) tokenTypes[ind] = BOOLEAN;
    else if (isStringOrChar(type)) tokenTypes[ind] = STRING;
    else if (isNull(type)) tokenTypes[ind] = NULL;
    else if (isSemi(type)) tokenTypes[ind] = SEMI;
    else if (isAT(type)) tokenTypes[ind] = ANNOTATION;
  }

  private static Set<String> getMethodArguments(JavaParser.FormalParametersContext formalParameters) {
    var parameterList = formalParameters.formalParameterList();
    if (parameterList == null) return Collections.emptySet();
    var parameters = parameterList.formalParameter();
    return parameters.stream().map(param -> param.variableDeclaratorId().getText()).collect(Collectors.toSet());
  }

  private static boolean isKeyword(int type) {
    return type >= JavaLexer.ABSTRACT && type <= JavaLexer.NON_SEALED;
  }

  private static boolean isNumeric(int type) {
    return type >= JavaLexer.DECIMAL_LITERAL && type <= JavaLexer.HEX_FLOAT_LITERAL;
  }

  private static boolean isBooleanLiteral(int type) {
    return type == JavaLexer.BOOL_LITERAL;
  }

  private static boolean isStringOrChar(int type) {
    return type >= JavaLexer.CHAR_LITERAL && type <= JavaLexer.TEXT_BLOCK;
  }

  private static boolean isNull(int type) {
    return type == JavaLexer.NULL_LITERAL;
  }

  private static boolean isSemi(int type) {
    return type == JavaLexer.SEMI || type == JavaLexer.COMMA;
  }

  private static boolean isAT(int type) {
    return type == JavaLexer.AT;
  }

  private static boolean isIdentifier(TerminalNode node) {
    if (node.getParent() == null) return false;
    return node.getParent() instanceof JavaParser.IdentifierContext;
  }

  private boolean isField(JavaParser.IdentifierContext ctx) {
    String id = ctx.getText();
    return javaClass.getField(id) != null && !curMethodArgs.contains(id);
  }

  private static TerminalNode getIdentifier(JavaParser.IdentifierContext ctx) {
    return (TerminalNode) ctx.children.get(0);
  }
}