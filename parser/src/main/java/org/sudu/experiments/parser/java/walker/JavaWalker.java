package org.sudu.experiments.parser.java.walker;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.common.Decl;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.JavaParserBaseListener;
import org.sudu.experiments.parser.java.model.JavaBlock;
import org.sudu.experiments.parser.java.model.JavaClass;
import org.sudu.experiments.parser.common.TypedDecl;

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

  private boolean isInstanceOfPatternAvailable = false;

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
      var id = getNode(constantDeclarator.identifier()).getSymbol();
      tokenTypes[id.getTokenIndex()] = TokenTypes.FIELD;
      tokenStyles[id.getTokenIndex()] = TokenStyles.ITALIC;
    }
  }

  @Override
  public void exitEnumConstant(JavaParser.EnumConstantContext ctx) {
    super.exitEnumConstant(ctx);
    var node = getNode(ctx.identifier()).getSymbol();
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
    var id = getNode(ctx.identifier()).getSymbol();
    var isStatic = javaClass.getMethod(id.getText(), getArgsTypes(ctx.formalParameters())).isStatic;
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
    tokenStyles[id.getTokenIndex()] = isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;

    exitBlock();
  }

  @Override
  public void exitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx) {
    super.exitInterfaceMethodDeclaration(ctx);
    var id = getNode(ctx.interfaceCommonBodyDeclaration().identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
    tokenStyles[id.getTokenIndex()] = TokenStyles.NORMAL;

    exitBlock();
  }

  @Override
  public void exitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.exitConstructorDeclaration(ctx);
    exitBlock();

    var id = getNode(ctx.identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
  }

  @Override
  public void exitAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx) {
    super.exitAnnotationMethodRest(ctx);
    var id = getNode(ctx.identifier()).getSymbol();
    tokenTypes[id.getTokenIndex()] = TokenTypes.METHOD;
  }

  @Override
  public void exitAnnotation(JavaParser.AnnotationContext ctx) {
    super.exitAnnotation(ctx);
    if (ctx.qualifiedName() != null) {
      var ids = ctx.qualifiedName().identifier();
      var node = getNode(ids.get(0)).getSymbol();
      tokenTypes[node.getTokenIndex()] = TokenTypes.ANNOTATION;
    }
    if (ctx.altAnnotationQualifiedName() != null) {
      var ids = ctx.altAnnotationQualifiedName().identifier();
      var node = getNode(ids.get(ids.size() - 1)).getSymbol();
      tokenTypes[node.getTokenIndex()] = TokenTypes.ANNOTATION;
    }
  }

  @Override
  public void exitExpression(JavaParser.ExpressionContext ctx) {
    super.exitExpression(ctx);
    if (ctx.parent instanceof JavaParser.ExpressionListContext) return;
    handleExpression(ctx);
  }

  @Override
  public void enterTypeIdentifier(JavaParser.TypeIdentifierContext ctx) {
    super.enterTypeIdentifier(ctx);
    var node = (TerminalNode) ctx.getChild(0);
    markType(node);
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
      var id = getNode(catchClause.identifier());
      var catchTypes = catchClause.catchType().qualifiedName();
      String type = catchTypes.size() == 1
          ? catchTypes.get(0).getText() : "Exception";
      currentBlock.localVars.add(TypedDecl.fromNode(id, type));
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
    List<TypedDecl> params = getLambdaParameters(ctx.lambdaParameters());
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
      isInstanceOfPatternAvailable = true;
      if (ctx.forControl().enhancedForControl() != null) {
        var forControl = ctx.forControl().enhancedForControl();
        var id = forControl.variableDeclaratorId().identifier();
        String type = forControl.VAR() != null ? handleExpression(forControl.expression()) : getType(forControl.typeType());
        currentBlock.localVars.add(TypedDecl.fromNode(getNode(id), type));
      }
    }
    if (ctx.parExpression() != null) {
      enterBlock();
      if (ctx.IF() != null ||
          ctx.WHILE() != null
      ) isInstanceOfPatternAvailable = true;
    }
  }

  @Override
  public void exitStatement(JavaParser.StatementContext ctx) {
    super.exitStatement(ctx);
    if (ctx.FOR() != null) {
      isInstanceOfPatternAvailable = false;
      exitBlock();
    }
    if (ctx.parExpression() != null) {
      if (ctx.IF() != null ||
          ctx.WHILE() != null
      ) isInstanceOfPatternAvailable = false;
      exitBlock();
    }
  }

  @Override
  public void exitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
    super.exitLocalVariableDeclaration(ctx);
    if (ctx.identifier() != null) {
      var node = getNode(ctx.identifier());
      currentBlock.localVars.add(TypedDecl.fromNode(node, handleExpression(ctx.expression())));
    } else {
      String type = getType(ctx.typeType());
      var nodes = getVarDeclarators(ctx.variableDeclarators())
          .stream().map(node -> TypedDecl.fromNode(node, type)).toList();
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
    if (ind == -1) return;
    tokenTypes[ind] = ERROR;
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

  private String handleExpression(JavaParser.ExpressionContext ctx) {
    if (hasThis(ctx)) return handleThis(ctx);
    if (ctx.primary() != null) return handlePrimary(ctx.primary());
    if (isTernary(ctx)) return handleTernary(ctx);
    if (isBooleanExpression(ctx)) return handleBooleanExpression(ctx);
    if (isCast(ctx)) return handleCast(ctx);
    if (ctx.INSTANCEOF() != null) return handleInstanceOf(ctx);
    if (ctx.methodCall() != null && ctx.getChildCount() == 1) {
      var methodCall = handleMethodCall(ctx.methodCall(), false);
      return methodCall != null ? methodCall.type : null;
    }
    if (ctx.creator() != null) {
      var creator = handleCreator(ctx.creator());
      return creator != null ? creator.type : getCreatorType(ctx.creator());
    }
    if (ctx.expression() != null && !ctx.expression().isEmpty()) {
      String result = handleExpression(ctx.expression(0));
      for (int i = 1; i < ctx.expression().size(); i++)
        handleExpression(ctx.expression(i));
      return result;
    }
    return null;
  }

  private String handleThis(JavaParser.ExpressionContext ctx) {
    if (ctx.identifier() != null) {
      var field = markField(ctx.identifier(), true);
      return field != null ? field.type : null;
    } else if (ctx.methodCall() != null) {
      var methodCall = handleMethodCall(ctx.methodCall(), true);
      return methodCall != null ? methodCall.type : null;
    } else return null;
  }

  private String handlePrimary(JavaParser.PrimaryContext ctx) {
    if (ctx.expression() != null) return handleExpression(ctx.expression());
    if (ctx.THIS() != null) return javaClass.name;
    if (ctx.literal() != null) return handleLiteral(ctx.literal());
    if (ctx.identifier() != null) {
      var decl = markLocalVarOrField(ctx.identifier());
      return decl != null ? decl.type : null;
    }
    return null;
  }

  private String handleTernary(JavaParser.ExpressionContext ctx) {
    handleExpression(ctx.expression(0));
    var result = handleExpression(ctx.expression(1));
    handleExpression(ctx.expression(2));
    return result;
  }

  private String handleBooleanExpression(JavaParser.ExpressionContext ctx) {
    handleExpression(ctx.expression(0));
    handleExpression(ctx.expression(1));
    return "boolean";
  }

  private String handleCast(JavaParser.ExpressionContext ctx) {
    handleExpression(ctx.expression(0));
    return getType(ctx.typeType(0));
  }


  private String handleLiteral(JavaParser.LiteralContext ctx) {
    if (ctx.integerLiteral() != null) return "int";
    if (ctx.floatLiteral() != null) return "float";
    if (ctx.CHAR_LITERAL() != null) return "char";
    if (ctx.STRING_LITERAL() != null || ctx.TEXT_BLOCK() != null) return "String";
    if (ctx.BOOL_LITERAL() != null) return "boolean";
    return null;
  }

  private String handleInstanceOf(JavaParser.ExpressionContext ctx) {
    handleExpression(ctx.expression(0));
    if (isInstanceOfPatternAvailable && ctx.pattern() != null) {
      var pattern = ctx.pattern();
      var node = getNode(pattern.identifier());
      String name = node.getText();
      Pos pos = Pos.fromNode(node);
      String type = getType(pattern.typeType());
      currentBlock.localVars.add(new TypedDecl(name, pos, type));
    }
    return "boolean";
  }

  private List<String> handleExpressionList(JavaParser.ExpressionListContext ctx) {
    List<String> result = new ArrayList<>();
    for (var expr: ctx.expression())
      result.add(handleExpression(expr));
    return result;
  }

  private TypedDecl handleMethodCall(JavaParser.MethodCallContext methodCall, boolean hasThis) {
    List<String> argsTypes;
    if (methodCall.expressionList() != null)
      argsTypes = handleExpressionList(methodCall.expressionList());
    else argsTypes = new ArrayList<>();

    if (methodCall.identifier() != null) return markMethod(methodCall.identifier(), argsTypes, hasThis);
    else if (methodCall.THIS() != null) return markConstructor(methodCall.THIS(), argsTypes, true);
    return null;
  }


  private TypedDecl handleCreator(JavaParser.CreatorContext ctx) {
    List<String> argsTypes;
    if (ctx.classCreatorRest() == null) return null;
    var arguments = ctx.classCreatorRest().arguments();
    if (arguments.expressionList() != null) argsTypes = handleExpressionList(arguments.expressionList());
    else argsTypes = new ArrayList<>();

    var node = getNode(ctx.createdName());
    return markConstructor(node, argsTypes, false);
  }

  private String getCreatorType(JavaParser.CreatorContext ctx) {
    return getNode(ctx.createdName()).getText();
  }

  private TypedDecl markLocalVarOrField(JavaParser.IdentifierContext identifier) {
    var localVar = markLocalVar(identifier);
    return localVar != null ? localVar : markField(identifier, false);
  }

  private TypedDecl markLocalVar(JavaParser.IdentifierContext identifier) {
    var id = getNode(identifier);
    var pos = Pos.fromNode(id);

    String name = id.getText();
    TypedDecl decl;

    decl = currentBlock.getLocalDecl(name);
    if (decl != null) usagesToDefs.put(pos, decl.position);
    return decl;
  }

  private TypedDecl markField(JavaParser.IdentifierContext identifier, boolean hasThis) {
    var id = getNode(identifier);
    var pos = Pos.fromNode(id);
    int ind = id.getSymbol().getTokenIndex();
    String name = id.getText();

    var decl = javaClass.getField(name);

    if (decl != null) {
      tokenTypes[ind] = FIELD;
      tokenStyles[ind] = decl.isStatic ? TokenStyles.ITALIC : TokenStyles.NORMAL;
      usagesToDefs.put(pos, decl.position);
    } else if (hasThis) tokenTypes[ind] = ERROR;

    return decl;
  }

  private TypedDecl markMethod(JavaParser.IdentifierContext identifier, List<String> argsTypes, boolean hasThis) {
    var id = getNode(identifier);
    var pos = Pos.fromNode(id);
    int ind = id.getSymbol().getTokenIndex();
    String name = id.getText();

    var decl = javaClass.getMethod(name, argsTypes);
    if (decl != null) {
      int style = TokenStyles.BOLD;
      if (decl.isStatic) style |= TokenStyles.ITALIC;
      tokenStyles[ind] = style;
      usagesToDefs.put(pos, decl.position);
    } else if (hasThis) tokenTypes[ind] = ERROR;

    return decl;
  }

  private TypedDecl markConstructor(TerminalNode node, List<String> argsTypes, boolean isThis) {
    var pos = Pos.fromNode(node);
    int ind = node.getSymbol().getTokenIndex();
    String name = node.getText();

    var def = isThis
        ? javaClass.getThisConstructor(argsTypes)
        : javaClass.getConstructor(name, argsTypes);

    if (def != null) {
      if (!isThis) {
        int style = TokenStyles.BOLD;
        if (def.isStatic) style |= TokenStyles.ITALIC;
        tokenStyles[ind] = style;
      }
      usagesToDefs.put(pos, def.position);
    }

    return def;
  }

  private Decl markType(TerminalNode node) {
    var token = node.getSymbol();
    var name = token.getText();
    for (var type: types) {
      if (type.name.equals(name)) {
        usagesToDefs.put(Pos.fromNode(node), type.position);
        return type;
      }
    }
    return null;
  }

  static List<String> getArgsTypes(JavaParser.FormalParametersContext ctx) {
    List<String> result = new ArrayList<>();
    if (ctx.receiverParameter() != null) {
      result.add(getType(ctx.receiverParameter().typeType()));
    }
    if (ctx.formalParameterList() != null) {
      var list = ctx.formalParameterList();
      for (var formalParam: list.formalParameter()) {
        result.add(getType(formalParam.typeType()));
      }
      if (list.lastFormalParameter() != null) {
        result.add(getType(list.lastFormalParameter().typeType()));
      }
    }
    return result;
  }

  static TerminalNode getNode(JavaParser.IdentifierContext ctx) {
    return (TerminalNode) ctx.getChild(0);
  }

  static TerminalNode getNode(JavaParser.CreatedNameContext ctx) {
    if (isNonNullAndEmpty(ctx.identifier())) return getNode(ctx.identifier(0));
    else return (TerminalNode) ctx.primitiveType().getChild(0);
  }

  static TerminalNode getLastNode(JavaParser.QualifiedNameContext ctx) {
    var size = ctx.identifier().size();
    return getNode(ctx.identifier(size - 1));
  }

  static String getType(JavaParser.TypeTypeOrVoidContext ctx) {
    return ctx.typeType() != null
        ? getType(ctx.typeType())
        : ctx.VOID().getText();
  }

  static String getType(JavaParser.TypeTypeContext ctx) {
    return ctx.classOrInterfaceType() != null
        ? getType(ctx.classOrInterfaceType())
        : ctx.primitiveType().getText();
  }

  static String getType(JavaParser.ClassOrInterfaceTypeContext ctx) {
    return ctx.getText();
  }

  static List<TypedDecl> getMethodArguments(JavaParser.FormalParametersContext formalParameters) {
    var parameterList = formalParameters.formalParameterList();
    if (parameterList == null) return new ArrayList<>();
    else return getMethodArguments(parameterList);
  }

  static List<TypedDecl> getMethodArguments(JavaParser.FormalParameterListContext ctx) {
    var parameters = ctx.formalParameter();
    List<TypedDecl> result = new ArrayList<>();
    for (var param: parameters) {
      var id = param.variableDeclaratorId().identifier();
      String type = getType(param.typeType());
      result.add(TypedDecl.fromNode(getNode(id), type));
    }
    if (ctx.lastFormalParameter() != null) {
      var last = ctx.lastFormalParameter();
      var lastId = last.variableDeclaratorId().identifier();
      String type = getType(last.typeType());
      result.add(TypedDecl.fromNode(getNode(lastId), type));
    }
    return result;
  }

  static List<TypedDecl> getLambdaParameters(JavaParser.LambdaParametersContext ctx) {
    List<TypedDecl> result = new ArrayList<>();
    if (ctx.identifier() != null && !ctx.identifier().isEmpty()) {
      for (var id: ctx.identifier())
        result.add(TypedDecl.fromNode(getNode(id), null));
    } else if (ctx.lambdaLVTIList() != null) {
      for (var param: ctx.lambdaLVTIList().lambdaLVTIParameter())
        result.add(TypedDecl.fromNode(getNode(param.identifier()), null));
    } else if (ctx.formalParameterList() != null) {
      result.addAll(getMethodArguments(ctx.formalParameterList()));
    }
    return result;
  }

  List<TypedDecl> getResources(JavaParser.ResourceSpecificationContext ctx) {
    List<TypedDecl> result = new ArrayList<>();
    var resources = ctx.resources().resource();
    for (var resource: resources) {
      var typedDecl = getTypedDecl(resource);
      if (typedDecl != null) result.add(typedDecl);
    }
    return result;
  }

  TypedDecl getTypedDecl(JavaParser.ResourceContext ctx) {
    if (ctx.identifier() != null && ctx.VAR() != null) {
      return TypedDecl.fromNode(getNode(ctx.identifier()), handleExpression(ctx.expression()));
    } else if (ctx.classOrInterfaceType() != null) {
      var id = getNode(ctx.variableDeclaratorId().identifier());
      var type = getType(ctx.classOrInterfaceType());
      return TypedDecl.fromNode(id, type);
    }
    return null;
  }

  static List<TerminalNode> getVarDeclarators(JavaParser.VariableDeclaratorsContext ctx) {
    List<TerminalNode> result = new ArrayList<>();
    for (var declarator: ctx.variableDeclarator()) {
      var id = declarator.variableDeclaratorId().identifier();
      result.add(getNode(id));
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

  static boolean hasThis(JavaParser.ExpressionContext ctx) {
    if (ctx.getChildCount() < 3
        || ctx.expression() == null
        || ctx.expression().isEmpty()
    ) return false;
    var expression = ctx.expression(0);
    return expression.primary() != null && expression.primary().THIS() != null
        && ctx.dot != null;
  }

  static boolean isTernary(JavaParser.ExpressionContext ctx) {
    return ctx.getChildCount() == 5
        && ctx.QUESTION() != null
        && ctx.COLON() != null;
  }

  static boolean isBooleanExpression(JavaParser.ExpressionContext ctx) {
    return ctx.getChildCount() == 3
        && (isNonNullAndEmpty(ctx.LT())
        || isNonNullAndEmpty(ctx.GT())
        || ctx.LE() != null
        || ctx.GE() != null
        || ctx.EQUAL() != null
        || ctx.NOTEQUAL() != null);
  }

  static boolean isCast(JavaParser.ExpressionContext ctx) {
    return ctx.getChildCount() >= 4
        && ctx.LPAREN() != null
        && ctx.typeType() != null
        && ctx.RPAREN() != null
        && isNonNullAndEmpty(ctx.expression());
  }

  static boolean isIdentifier(TerminalNode node) {
    if (node.getParent() == null) return false;
    return node.getParent() instanceof JavaParser.IdentifierContext
        || node.getParent() instanceof JavaParser.AnySeqContext;
  }

  static <T extends ParseTree> boolean isNonNullAndEmpty(List<T> nodes) {
    return nodes != null && !nodes.isEmpty();
  }

}