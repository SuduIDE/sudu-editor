package org.sudu.experiments.parser.java.walker;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.JavaParserBaseListener;
import org.sudu.experiments.parser.java.model.JavaClass;
import static org.sudu.experiments.parser.java.ParserConstants.*;
import static org.sudu.experiments.parser.java.ParserConstants.TokenTypes.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaWalker extends JavaParserBaseListener {
  private final int[] tokenTypes;
  private final int[] tokenStyles;
  private JavaClass javaClass;

  private Set<String> curMethodArgs;

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
      tokenTypes[id.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[id.getTokenIndex()] = ClassWalker.isStatic(modifiers) ? TokenStyles.ITALIC : TokenStyles.NORMAL;
    }
  }

  @Override
  public void exitConstDeclaration(JavaParser.ConstDeclarationContext ctx) {
    super.exitConstDeclaration(ctx);

    var constantDeclarators = ctx.constantDeclarator();
    for (var constantDeclarator: constantDeclarators) {
      var id = getIdentifier(constantDeclarator.identifier()).getSymbol();
      tokenTypes[id.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[id.getTokenIndex()] = TokenStyles.ITALIC;
    }
  }

  @Override
  public void exitEnumConstant(JavaParser.EnumConstantContext ctx) {
    super.exitEnumConstant(ctx);
    var node = getIdentifier(ctx.identifier()).getSymbol();
    tokenTypes[node.getTokenIndex()] = TokenTypes.FIELD;
    tokenStyles[node.getTokenIndex()] = TokenStyles.ITALIC;
  }

  @Override
  public void exitAnnotationConstantRest(JavaParser.AnnotationConstantRestContext ctx) {
    super.exitAnnotationConstantRest(ctx);
    var variableDeclarators = ctx.variableDeclarators().variableDeclarator();
    for (var variableDeclarator: variableDeclarators) {
      var id = getIdentifier(variableDeclarator.variableDeclaratorId().identifier()).getSymbol();
      var modifiers = ClassWalker.getModifiers(ctx);
      tokenTypes[id.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[id.getTokenIndex()] = ClassWalker.isStatic(modifiers) ? TokenStyles.ITALIC : TokenStyles.NORMAL;
    }
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
  public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.enterEnumDeclaration(ctx);
    int ind = javaClass.nestPos;
    javaClass = javaClass.nestedClasses.get(ind);
  }

  @Override
  public void enterRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.enterRecordDeclaration(ctx);
    int ind = javaClass.nestPos;
    javaClass = javaClass.nestedClasses.get(ind);
  }

  @Override
  public void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.enterAnnotationTypeDeclaration(ctx);
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
  public void exitEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.exitEnumDeclaration(ctx);
    javaClass = javaClass.innerClass;
    javaClass.nestPos++;
  }

  @Override
  public void exitRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.exitRecordDeclaration(ctx);
    javaClass = javaClass.innerClass;
    javaClass.nestPos++;
  }

  @Override
  public void exitAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.exitAnnotationTypeDeclaration(ctx);
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
    var id = getIdentifier(ctx.identifier()).getSymbol();
    var isStatic = javaClass.getMethod(id.getText()).isStatic;
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
    tokenStyles[id.getTokenIndex()] = isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;

    curMethodArgs = Collections.emptySet();
  }

  @Override
  public void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
    super.exitInterfaceMethodDeclaration(ctx);
    var id = getIdentifier(ctx.interfaceCommonBodyDeclaration().identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
    tokenStyles[id.getTokenIndex()] = TokenStyles.NORMAL;
  }

  @Override
  public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.exitConstructorDeclaration(ctx);
    curMethodArgs = Collections.emptySet();

    var id = getIdentifier(ctx.identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
  }

  @Override
  public void exitAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx) {
    super.exitAnnotationMethodRest(ctx);
    var id = getIdentifier(ctx.identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
  }

  @Override
  public void exitQualifiedName(JavaParser.QualifiedNameContext ctx) {
    super.exitQualifiedName(ctx);
    var first = ctx.identifier(0);
    var ind = first.getRuleIndex();
    if (isField(first)) {
      var field = javaClass.getField(first.getText());
      tokenTypes[ind] = TokenTypes.FIELD;
      tokenStyles[ind] = field.isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;
    }
  }

  @Override
  public void exitPrimary(JavaParser.PrimaryContext ctx) {
    super.exitPrimary(ctx);
    if (ctx.identifier() == null) return;
    var token = getIdentifier(ctx.identifier()).getSymbol();
    if (isField(ctx.identifier())) {
      var field = javaClass.getField(ctx.identifier().getText());
      tokenTypes[token.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[token.getTokenIndex()] = field.isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;
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
        tokenTypes[token.getTokenIndex()] = TokenTypes.FIELD;
        tokenStyles[token.getTokenIndex()] = field.isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;
      }
      else
        tokenTypes[token.getTokenIndex()] = TokenTypes.ERROR;
    }
    var method = javaClass.getMethod(name);
    if (method != null && method.isStatic)
      tokenStyles[getIdentifier(ctx).getSymbol().getTokenIndex()] = TokenStyles.ITALIC;
  }

  @Override
  public void exitAnnotation(JavaParser.AnnotationContext ctx) {
    super.exitAnnotation(ctx);
    if (ctx.qualifiedName() != null) {
      var ids = ctx.qualifiedName().identifier();
      var node = getIdentifier(ids.get(0)).getSymbol();
      tokenTypes[node.getTokenIndex()] = TokenTypes.ANNOTATION;
    }
    if (ctx.altAnnotationQualifiedName() != null) {
      var ids = ctx.altAnnotationQualifiedName().identifier();
      var node = getIdentifier(ids.get(ids.size() - 1)).getSymbol();
      tokenTypes[node.getTokenIndex()] = TokenTypes.ANNOTATION;
    }
  }

  @Override
  public void exitMethodCall(JavaParser.MethodCallContext ctx) {
    super.exitMethodCall(ctx);
    if (ctx.identifier() == null) return;
    var id = getIdentifier(ctx.identifier()).getSymbol();
    var method = javaClass.getMethod(id.getText());
    if (method != null) {
      tokenTypes[id.getTokenIndex()] = DEFAULT;
      tokenStyles[id.getTokenIndex()] = method.isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;
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
}