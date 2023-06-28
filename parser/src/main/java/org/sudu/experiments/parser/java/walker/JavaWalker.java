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
  private final List<Decl> types;
  private final Map<Pos, Pos> usagesToDefs;

  private JavaBlock currentBlock;

  public JavaWalker(int[] tokenTypes, int[] tokenStyles, JavaClass javaClass, List<Decl> types, Map<Pos, Pos> usagesToDefs) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.javaClass = javaClass;
    this.usagesToDefs = usagesToDefs;
    this.types = types;

    currentBlock = new JavaBlock(null);
  }

  @Override
  public void exitFieldDeclaration(JavaParser.FieldDeclarationContext ctx) {
    super.exitFieldDeclaration(ctx);
    boolean isStatic = JavaClassWalker.isStatic(JavaClassWalker.getModifiers(ctx));
    markFields(ctx.variableDeclarators(), isStatic);
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
    boolean isStatic = JavaClassWalker.isStatic(JavaClassWalker.getModifiers(ctx));
    markFields(ctx.variableDeclarators(), isStatic);
  }

  @Override
  public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.enterClassDeclaration(ctx);
    enterClass();
  }

  @Override
  public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.enterInterfaceDeclaration(ctx);
    enterClass();
  }

  @Override
  public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.enterEnumDeclaration(ctx);
    enterClass();
  }

  @Override
  public void enterRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.enterRecordDeclaration(ctx);
    enterClass();
  }

  @Override
  public void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.enterAnnotationTypeDeclaration(ctx);
    enterClass();
  }

  @Override
  public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.exitClassDeclaration(ctx);
    exitClass();
  }

  @Override
  public void exitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.exitInterfaceDeclaration(ctx);
    exitClass();
  }

  @Override
  public void exitEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.exitEnumDeclaration(ctx);
    exitClass();
  }

  @Override
  public void exitRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.exitRecordDeclaration(ctx);
    exitClass();
  }

  @Override
  public void exitAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.exitAnnotationTypeDeclaration(ctx);
    exitClass();
  }

  @Override
  public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
    super.enterMethodDeclaration(ctx);
    enterBlock();
    currentBlock.localVars = getMethodArguments(ctx.formalParameters());
  }

  @Override
  public void enterInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
    super.enterInterfaceMethodDeclaration(ctx);
    enterBlock();
    currentBlock.localVars = getMethodArguments(ctx.interfaceCommonBodyDeclaration().formalParameters());
  }

  @Override
  public void enterConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.enterConstructorDeclaration(ctx);
    enterBlock();
    currentBlock.localVars = getMethodArguments(ctx.formalParameters());
  }

  @Override
  public void exitMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
    super.exitMethodDeclaration(ctx);
    var id = getIdentifier(ctx.identifier()).getSymbol();
    var isStatic = javaClass.getMethod(id.getText(), countNumberOfArgs(ctx.formalParameters())).isStatic;
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
    tokenStyles[id.getTokenIndex()] = isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;

    exitBlock();
  }

  @Override
  public void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
    super.exitInterfaceMethodDeclaration(ctx);
    var id = getIdentifier(ctx.interfaceCommonBodyDeclaration().identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
    tokenStyles[id.getTokenIndex()] = TokenStyles.NORMAL;

    exitBlock();
  }

  @Override
  public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.exitConstructorDeclaration(ctx);
    exitBlock();

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
    int numOfArgs = 0;

    if (ctx.parent instanceof JavaParser.MethodCallContext methodCall) {
      if (!isSoleMethodCall(methodCall)) return;
      if (methodCall.identifier() != null) isMethodCall = true;
      numOfArgs = methodCall.expressionList() == null ? 0
          : methodCall.expressionList().expression().size();
    }

    boolean hasThis = ctx.parent instanceof JavaParser.ExpressionContext exprParent && hasThis(exprParent);
    if (ctx.parent instanceof JavaParser.ExpressionContext && !hasThis) return;

    if (isMethodCall) markMethodUsage(getIdentifier(ctx), numOfArgs, hasThis);
    else markFieldUsage(getIdentifier(ctx), hasThis);
  }

  @Override
  public void exitMethodCall(JavaParser.MethodCallContext ctx) {
    super.exitMethodCall(ctx);
    if (ctx.identifier() != null) return;   // already handled in exitIdentifier
    int numOfArgs = ctx.expressionList() == null ? 0
        : ctx.expressionList().expression().size();
    var node = Objects.requireNonNullElse(ctx.THIS(), ctx.SUPER());
    markConstructorUsage(node, numOfArgs, true);
  }

  @Override
  public void exitCreator(JavaParser.CreatorContext ctx) {
    super.exitCreator(ctx);
    if (ctx.classCreatorRest() == null) return; // todo
    var node = ctx.createdName().primitiveType() != null
        ? (TerminalNode) ctx.createdName().primitiveType().getChild(0)
        : getIdentifier(ctx.createdName().identifier(0));
    int numOfArgs = countNumberOfArgs(ctx.classCreatorRest().arguments());
    markConstructorUsage(node, numOfArgs, false);
  }

  @Override
  public void enterTypeIdentifier(JavaParser.TypeIdentifierContext ctx) {
    super.enterTypeIdentifier(ctx);
    var node = (TerminalNode) ctx.getChild(0);
    var token = node.getSymbol();
    var name = token.getText();
    for (var type: types) {
      if (type.name.equals(name)) {
        usagesToDefs.put(Pos.fromNode(node), type.position);
        return;
      }
    }
  }

  @Override
  public void enterBlock(JavaParser.BlockContext ctx) {
    super.enterBlock(ctx);
    enterBlock();
    if (isInsideTryWithResources(ctx)) {
      var statement = (JavaParser.StatementContext) ctx.parent;
      currentBlock.localVars.addAll(getResources(statement.resourceSpecification()));
    } else if (isInsideCatchClause(ctx)) {
      var catchClause = (JavaParser.CatchClauseContext) ctx.parent;
      currentBlock.localVars.add(Decl.fromNode(getIdentifier(catchClause.identifier())));
    }
  }

  @Override
  public void exitBlock(JavaParser.BlockContext ctx) {
    super.exitBlock(ctx);
    exitBlock();
  }

  @Override
  public void enterLambdaExpression(JavaParser.LambdaExpressionContext ctx) {
    super.enterLambdaExpression(ctx);
    enterBlock();
    List<Decl> params = getLambdaParameters(ctx.lambdaParameters());
    currentBlock.localVars.addAll(params);
  }

  @Override
  public void exitLambdaExpression(JavaParser.LambdaExpressionContext ctx) {
    super.exitLambdaExpression(ctx);
    exitBlock();
  }

  @Override
  public void enterStatement(JavaParser.StatementContext ctx) {
    super.enterStatement(ctx);
    if (ctx.FOR() != null) {
      enterBlock();
      if (ctx.forControl().enhancedForControl() != null) {
        var forControl = ctx.forControl().enhancedForControl();
        var id = forControl.variableDeclaratorId().identifier();
        currentBlock.localVars.add(Decl.fromNode(getIdentifier(id)));
      }
    }
  }

  @Override
  public void exitStatement(JavaParser.StatementContext ctx) {
    super.exitStatement(ctx);
    if (ctx.FOR() != null) exitBlock();
  }

  @Override
  public void exitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
    super.exitLocalVariableDeclaration(ctx);
    if (ctx.identifier() != null) {
      var node = getIdentifier(ctx.identifier());
      currentBlock.localVars.add(Decl.fromNode(node));
    } else {
      var nodes = getVarDeclarators(ctx.variableDeclarators())
          .stream().map(Decl::fromNode).toList();
      currentBlock.localVars.addAll(nodes);
    }
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

  private void markMethodUsage(TerminalNode node, int numOfArgs, boolean hasThis) {
    var token = node.getSymbol();
    String name = token.getText();
    int ind = token.getTokenIndex();
    var def = javaClass.getMethod(name, numOfArgs);
    if (def == null) {
      tokenTypes[ind] = hasThis ? ERROR : DEFAULT;
      return;
    }

    int style = TokenStyles.BOLD;
    if (def.isStatic) style |= TokenStyles.ITALIC;
    tokenStyles[ind] = style;
    usagesToDefs.put(Pos.fromNode(node), def.position);
  }

  private void markConstructorUsage(TerminalNode node, int numOfArgs, boolean isThis) {
    var token = node.getSymbol();
    String name = token.getText();
    int ind = token.getTokenIndex();
    var def = isThis
        ? javaClass.getThisConstructor(numOfArgs)
        : javaClass.getConstructor(name, numOfArgs);
    if (def == null) return;

    if (!isThis) {
      int style = TokenStyles.BOLD;
      if (def.isStatic) style |= TokenStyles.ITALIC;
      tokenStyles[ind] = style;
    }

    usagesToDefs.put(Pos.fromNode(node), def.position);
  }

  void markFields(JavaParser.VariableDeclaratorsContext ctx, boolean isStatic) {
    List<TerminalNode> declarators = getVarDeclarators(ctx);
    for (var decl: declarators) {
      var id = decl.getSymbol();
      tokenTypes[id.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[id.getTokenIndex()] = isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;
    }
  }

  private void enterBlock() {
    JavaBlock block = new JavaBlock(currentBlock);
    currentBlock.subBlock = block;
    currentBlock = block;
  }

  private void exitBlock() {
    currentBlock = currentBlock.innerBlock;
    currentBlock.subBlock = null;
  }

  private void enterClass() {
    int ind = javaClass.nestPos;
    javaClass = javaClass.nestedClasses.get(ind);
  }
  
  private void exitClass() {
    javaClass = javaClass.innerClass;
    javaClass.nestPos++;
  }

  static int countNumberOfArgs(JavaParser.FormalParametersContext ctx) {
    int cnt = 0;
    if (ctx.receiverParameter() != null) cnt++;
    if (ctx.formalParameterList() != null) {
      cnt += ctx.formalParameterList().formalParameter().size();
      if (ctx.formalParameterList().lastFormalParameter() != null) cnt++;
    }
    return cnt;
  }

  static int countNumberOfArgs(JavaParser.ArgumentsContext ctx) {
    if (ctx.expressionList() == null) return 0;
    else return ctx.expressionList().expression().size();
  }

  static TerminalNode getIdentifier(JavaParser.IdentifierContext ctx) {
    return (TerminalNode) ctx.getChild(0);
  }

  static List<Decl> getMethodArguments(JavaParser.FormalParametersContext formalParameters) {
    var parameterList = formalParameters.formalParameterList();
    if (parameterList == null) return new ArrayList<>();
    else return getMethodArguments(parameterList);
  }

  static List<Decl> getMethodArguments(JavaParser.FormalParameterListContext ctx) {
    var parameters = ctx.formalParameter();
    List<Decl> result = new ArrayList<>();
    for (var param: parameters) {
      var id = param.variableDeclaratorId().identifier();
      result.add(Decl.fromNode(getIdentifier(id)));
    }
    if (ctx.lastFormalParameter() != null) {
      var lastId = ctx.lastFormalParameter().variableDeclaratorId().identifier();
      result.add(Decl.fromNode(getIdentifier(lastId)));
    }
    return result;
  }

  static List<Decl> getLambdaParameters(JavaParser.LambdaParametersContext ctx) {
    List<Decl> result = new ArrayList<>();
    if (ctx.identifier() != null && !ctx.identifier().isEmpty()) {
      for (var id: ctx.identifier())
        result.add(Decl.fromNode(getIdentifier(id)));
    } else if (ctx.lambdaLVTIList() != null) {
      for (var param: ctx.lambdaLVTIList().lambdaLVTIParameter())
        result.add(Decl.fromNode(getIdentifier(param.identifier())));
    } else if (ctx.formalParameterList() != null) {
      result.addAll(getMethodArguments(ctx.formalParameterList()));
    }
    return result;
  }

  static List<Decl> getResources(JavaParser.ResourceSpecificationContext ctx) {
    List<Decl> result = new ArrayList<>();
    var resources = ctx.resources().resource();
    for (var resource: resources) {
      var id = Objects.requireNonNullElse(resource.identifier(), resource.variableDeclaratorId().identifier());
      result.add(Decl.fromNode(getIdentifier(id)));
    }
    return result;
  }

  static List<TerminalNode> getVarDeclarators(JavaParser.VariableDeclaratorsContext ctx) {
    List<TerminalNode> result = new ArrayList<>();
    for (var declarator: ctx.variableDeclarator()) {
      var id = declarator.variableDeclaratorId().identifier();
      result.add(getIdentifier(id));
    }
    return result;
  }

  static boolean isInsideTryWithResources(JavaParser.BlockContext ctx) {
    return ctx.parent instanceof JavaParser.StatementContext statement
        && statement.resourceSpecification() != null;
  }

  static boolean isInsideCatchClause(JavaParser.BlockContext ctx) {
    return ctx.parent instanceof JavaParser.CatchClauseContext;
  }

  static boolean isSoleMethodCall(JavaParser.MethodCallContext ctx) {
    JavaParser.ExpressionContext parent = (JavaParser.ExpressionContext) ctx.parent;
    return parent.getChildCount() == 1 && parent.getChild(0) instanceof JavaParser.MethodCallContext;
  }

  static boolean hasThis(JavaParser.ExpressionContext ctx) {
    if (ctx.getChildCount() < 3) return false;
    var expression = ctx.expression(0);
    return expression.primary() != null && expression.primary().THIS() != null
        && ctx.bop != null;
  }

  static boolean isIdentifier(TerminalNode node) {
    if (node.getParent() == null) return false;
    return node.getParent() instanceof JavaParser.IdentifierContext
        || node.getParent() instanceof JavaParser.AnySeqContext;
  }

}