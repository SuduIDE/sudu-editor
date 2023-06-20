package org.sudu.experiments.parser.java.walker;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.JavaParserBaseListener;
import org.sudu.experiments.parser.java.model.JavaBlock;
import org.sudu.experiments.parser.java.model.JavaField;
import org.sudu.experiments.parser.java.model.JavaClass;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;
import static org.sudu.experiments.parser.java.parser.highlighting.JavaLexerHighlighting.*;

import java.util.*;

public class JavaWalker extends JavaParserBaseListener {
  private final int[] tokenTypes;
  private final int[] tokenStyles;
  private JavaClass javaClass;
  private final Map<Pos, Pos> usagesToDefs;

  private List<Decl> curMethodArgs;
  private JavaBlock currentBlock;

  public JavaWalker(int[] tokenTypes, int[] tokenStyles, JavaClass javaClass, Map<Pos, Pos> usagesToDefs) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.javaClass = javaClass;
    this.usagesToDefs = usagesToDefs;

    currentBlock = new JavaBlock(null);
    curMethodArgs = new ArrayList<>();
  }

  @Override
  public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
    super.exitFieldDeclaration(ctx);

    var variableDeclarators = ctx.variableDeclarators().variableDeclarator();
    for (var variableDeclarator: variableDeclarators) {
      var id = getIdentifier(variableDeclarator.variableDeclaratorId().identifier()).getSymbol();
      var modifiers = JavaClassWalker.getModifiers(ctx);
      tokenTypes[id.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[id.getTokenIndex()] = JavaClassWalker.isStatic(modifiers) ? TokenStyles.ITALIC : TokenStyles.NORMAL;
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
      var modifiers = JavaClassWalker.getModifiers(ctx);
      tokenTypes[id.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[id.getTokenIndex()] = JavaClassWalker.isStatic(modifiers) ? TokenStyles.ITALIC : TokenStyles.NORMAL;
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
  public void enterInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
    super.enterInterfaceMethodDeclaration(ctx);
    curMethodArgs = getMethodArguments(ctx.interfaceCommonBodyDeclaration().formalParameters());
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

    curMethodArgs.clear();
  }

  @Override
  public void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
    super.exitInterfaceMethodDeclaration(ctx);
    var id = getIdentifier(ctx.interfaceCommonBodyDeclaration().identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
    tokenStyles[id.getTokenIndex()] = TokenStyles.NORMAL;

    curMethodArgs.clear();
  }

  @Override
  public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.exitConstructorDeclaration(ctx);
    curMethodArgs.clear();

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
    markFieldUsage(getIdentifier(first), false);
  }

  @Override
  public void exitIdentifier(JavaParser.IdentifierContext ctx) {
    super.exitIdentifier(ctx);
    if (!(ctx.parent instanceof JavaParser.MethodCallContext
        || ctx.parent instanceof JavaParser.PrimaryContext
        || ctx.parent instanceof JavaParser.ExpressionContext)
    ) return;

    boolean isMethodCall = false;
    if (ctx.parent instanceof JavaParser.MethodCallContext methodCall) {
      if (!isSoleMethodCall(methodCall)) return;
      isMethodCall = true;
    }

    boolean hasThis = ctx.parent instanceof JavaParser.ExpressionContext exprParent && hasThis(exprParent);
    if (ctx.parent instanceof JavaParser.ExpressionContext && !hasThis) return;

    if (isMethodCall) markMethodUsage(getIdentifier(ctx), hasThis);
    else markFieldUsage(getIdentifier(ctx), hasThis);
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
  public void enterBlock(JavaParser.BlockContext ctx) {
    super.enterBlock(ctx);
    JavaBlock block = new JavaBlock(currentBlock);
    currentBlock.subBlock = block;
    currentBlock = block;
  }

  @Override
  public void exitBlock(JavaParser.BlockContext ctx) {
    super.exitBlock(ctx);
    currentBlock = currentBlock.innerBlock;
    currentBlock.subBlock = null;
  }

  @Override
  public void exitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
    super.exitLocalVariableDeclaration(ctx);
    if (!(ctx.parent instanceof JavaParser.BlockStatementContext)) return;
    if (ctx.identifier() != null) {
      var node = getIdentifier(ctx.identifier());
      Decl decl = new Decl(node.getText(), Pos.fromNode(node));
      currentBlock.localVars.add(decl);
      return;
    }
    currentBlock.localVars.addAll(getVarDeclarators(ctx.variableDeclarators()));
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    super.visitTerminal(node);
    var token = node.getSymbol();
    int ind = token.getTokenIndex();
    int type = token.getType();
    if (isKeyword(type) || (isKeywordIdentifier(type) && !isIdentifier(node))) tokenTypes[ind] = KEYWORD;
    else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
    else if (isBooleanLiteral(type)) tokenTypes[ind] = BOOLEAN;
    else if (isStringOrChar(type)) tokenTypes[ind] = STRING;
    else if (isNull(type)) tokenTypes[ind] = NULL;
    else if (isSemi(type)) tokenTypes[ind] = SEMI;
    else if (isAT(type)) tokenTypes[ind] = ANNOTATION;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    super.visitErrorNode(node);
    var token = node.getSymbol();
    int ind = token.getTokenIndex();
    if (ind == -1) {
      return;
    }
    tokenTypes[ind] = ERROR;
  }

  private void markFieldUsage(TerminalNode node, boolean hasThis) {
    var token = node.getSymbol();
    String name = token.getText();
    int ind = token.getTokenIndex();

    Decl decl;
    if (hasThis) {
      decl = javaClass.getField(name);
      if (decl == null) {
        tokenTypes[ind] = ERROR;
        return;
      }
      tokenTypes[ind] = FIELD;
      tokenStyles[ind] = ((JavaField) decl).isStatic ? ERROR : TokenStyles.NORMAL;
      usagesToDefs.put(Pos.fromNode(node), decl.position);
      return;
    }

    decl = getMethodArgument(name);
    if (decl != null) {
      usagesToDefs.put(Pos.fromNode(node), decl.position);
      return;
    }
    decl = currentBlock.getLocalDecl(name);
    if (decl != null) {
      usagesToDefs.put(Pos.fromNode(node), decl.position);
      return;
    }

    decl = javaClass.getField(name);
    if (decl == null) return;

    JavaField declField = (JavaField) decl;
    tokenTypes[ind] = FIELD;
    tokenStyles[ind] = declField.isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;
    usagesToDefs.put(Pos.fromNode(node), declField.position);
  }

  private void markMethodUsage(TerminalNode node, boolean hasThis) {
    var token = node.getSymbol();
    String name = token.getText();
    int ind = token.getTokenIndex();
    var def = javaClass.getMethod(name);
    if (def == null) {
      tokenTypes[ind] = hasThis ? ERROR : DEFAULT;
      return;
    }

    int style = TokenStyles.BOLD;
    if (def.isStatic) style |= TokenStyles.ITALIC;
    tokenStyles[ind] = style;
    usagesToDefs.put(Pos.fromNode(node), def.position);
  }

  private static List<Decl> getMethodArguments(JavaParser.FormalParametersContext formalParameters) {
    var parameterList = formalParameters.formalParameterList();
    if (parameterList == null) return new ArrayList<>();
    var parameters = parameterList.formalParameter();
    List<Decl> result = new ArrayList<>();
    for (var param: parameters) {
      var id = getIdentifier(param.variableDeclaratorId().identifier());
      result.add(new Decl(id.getText(), Pos.fromNode(id)));
    }
    return result;
  }

  private Decl getMethodArgument(String declName) {
    for (var arg: curMethodArgs) {
      if (arg.name.equals(declName)) return arg;
    }
    return null;
  }

  private static List<Decl> getVarDeclarators(JavaParser.VariableDeclaratorsContext ctx) {
    List<Decl> result = new ArrayList<>();
    for (var declarator: ctx.variableDeclarator()) {
      var node = getIdentifier(declarator.variableDeclaratorId().identifier());
      Decl decl = new Decl(node.getText(), Pos.fromNode(node));
      result.add(decl);
    }
    return result;
  }

  private static boolean isSoleMethodCall(JavaParser.MethodCallContext ctx) {
    JavaParser.ExpressionContext parent = (JavaParser.ExpressionContext) ctx.parent;
    return parent.getChildCount() == 1 && parent.getChild(0) instanceof JavaParser.MethodCallContext;
  }

  private static boolean hasThis(JavaParser.ExpressionContext ctx) {
    if (ctx.getChildCount() < 3) return false;
    var expression = ctx.expression(0);
    return expression.primary() != null && expression.primary().THIS() != null
        && ctx.bop != null;
  }

  private static boolean isIdentifier(TerminalNode node) {
    if (node.getParent() == null) return false;
    return node.getParent() instanceof JavaParser.IdentifierContext
        || node.getParent() instanceof JavaParser.AnySeqContext;
  }

  private static TerminalNode getIdentifier(JavaParser.IdentifierContext ctx) {
    return (TerminalNode) ctx.getChild(0);
  }

}