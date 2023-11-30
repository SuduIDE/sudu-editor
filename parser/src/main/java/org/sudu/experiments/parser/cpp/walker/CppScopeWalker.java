package org.sudu.experiments.parser.cpp.walker;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.graph.node.InferenceNode;
import org.sudu.experiments.parser.common.graph.node.decl.DeclNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.ExprRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.MethodCallNode;
import org.sudu.experiments.parser.common.graph.node.ref.QualifiedRefNode;
import org.sudu.experiments.parser.common.graph.node.ref.RefNode;
import org.sudu.experiments.parser.cpp.gen.CPP14Parser;
import org.sudu.experiments.parser.cpp.gen.CPP14ParserBaseListener;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Cpp.*;
import static org.sudu.experiments.parser.ParserConstants.*;

public class CppScopeWalker extends CPP14ParserBaseListener {

  public ScopeWalker scopeWalker;
  public int offset = 0;

  private final int[] tokenTypes, tokenStyles;

  public CppScopeWalker(ScopeWalker scopeWalker, int offset, int[] tokenTypes, int[] tokenStyles) {
    scopeWalker.offset = offset;
    this.scopeWalker = scopeWalker;
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
  }

  @Override
  public void enterDeclaration(CPP14Parser.DeclarationContext ctx) {
    super.enterDeclaration(ctx);
    if (ctx.functionDefinition() != null) handleFunction(ctx.functionDefinition(), false);
    else {
      scopeWalker.enterScope();
      scopeWalker.addInterval(ctx, DECLARATION);
      scopeWalker.enterInterval();
    }
  }

  @Override
  public void exitDeclaration(CPP14Parser.DeclarationContext ctx) {
    super.exitDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitInterval();
    if (ctx.functionDefinition() != null) scopeWalker.exitScope();
    scopeWalker.newIntervalStart = ctx.stop.getStopIndex() + 1;
  }

  @Override
  public void enterClassSpecifier(CPP14Parser.ClassSpecifierContext ctx) {
    super.enterClassSpecifier(ctx);
    var className = ctx.classHead().classHeadName().className();
    var typeStr = getIdentifier(className).getText();
    scopeWalker.newIntervalStart = scopeWalker.currentNode.getStart();

    scopeWalker.enterScope();
    scopeWalker.associateType(typeStr, scopeWalker.currentScope);
    scopeWalker.addInterval(ctx, TYPE);
    scopeWalker.enterInterval();

    scopeWalker.newIntervalStart = ctx.LeftBrace().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitClassSpecifier(CPP14Parser.ClassSpecifierContext ctx) {
    super.exitClassSpecifier(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitInterval();

    scopeWalker.newIntervalStart = ctx.RightBrace().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterMemberSpecification(CPP14Parser.MemberSpecificationContext ctx) {
    super.enterMemberSpecification(ctx);
    if (ctx.accessSpecifier() != null && ctx.Colon() != null) {
      scopeWalker.enterScope();
      scopeWalker.addInterval(ctx, UNKNOWN);
      scopeWalker.enterInterval();
    } else {
      var member = ctx.memberdeclaration();
      if (member.declSpecifierSeq() != null &&
          member.memberDeclaratorList() != null
      ) handleFields(ctx, member);
      else if (member.functionDefinition() != null) handleFunction(member.functionDefinition(), true);
      else {
        scopeWalker.enterScope();
        scopeWalker.addInterval(ctx, UNKNOWN);
        scopeWalker.enterInterval();
      }
    }
  }

  private void handleFields(CPP14Parser.MemberSpecificationContext ctx, CPP14Parser.MemberdeclarationContext member) {
    var type = getType(member.declSpecifierSeq());
    var fields = new ArrayList<DeclNode>();
    var forMark = new ArrayList<TerminalNode>();
    for (var decl: member.memberDeclaratorList().memberDeclarator()) {
      var node = getIdentifier(decl.declarator());
      if (node == null) continue;
      fields.add(new DeclNode(Name.fromNode(node, offset), type, DeclNode.FIELD));
      forMark.add(node);
    }

    scopeWalker.getType(type);
    scopeWalker.enterMember(fields);
    scopeWalker.addInterval(ctx, MEMBER);
    scopeWalker.enterInterval();

    mark(forMark, TokenTypes.FIELD, TokenStyles.NORMAL);
  }

  private void handleFunction(CPP14Parser.FunctionDefinitionContext func, boolean isMember) {
    boolean isConstructorDef = func.declSpecifierSeq() == null;
    var args = getMethodArguments(func.declarator());
    var argsTypes = new ArrayList<String>();
    for (var arg: args) argsTypes.add(scopeWalker.getType(arg.type));
    var node = getIdentifier(func.declarator());

    String name = node.getText();
    String type = isConstructorDef ? name : getType(func.declSpecifierSeq());
    int methodType = isMember
        ? isConstructorDef ? MethodNode.CREATOR : MethodNode.METHOD
        : MethodNode.LOCAL_VAR;

    scopeWalker.getType(type);
    var method = new MethodNode(Name.fromNode(node, offset), type, methodType, argsTypes);
    if (isMember) scopeWalker.enterMember(method);
    else {
      scopeWalker.enterScope();
      scopeWalker.addDecl(method);
    }
    scopeWalker.addInterval(func, MEMBER);
    scopeWalker.enterInterval();
    scopeWalker.enterScope();
    scopeWalker.addDecls(args);

    mark(node, TokenTypes.METHOD, TokenStyles.NORMAL);
  }

  @Override
  public void exitMemberSpecification(CPP14Parser.MemberSpecificationContext ctx) {
    super.exitMemberSpecification(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitInterval();
    if (ctx.memberdeclaration() != null &&
        ctx.memberdeclaration().functionDefinition() != null
    ) scopeWalker.exitScope();
    scopeWalker.newIntervalStart = ctx.stop.getStopIndex() + 1;
  }

  @Override
  public void exitSimpleDeclaration(CPP14Parser.SimpleDeclarationContext ctx) {
    super.exitSimpleDeclaration(ctx);
    if (ctx.initDeclaratorList() == null) return;
    boolean isDeclaration = ctx.declSpecifierSeq() != null;
    String type = isDeclaration ? getType(ctx.declSpecifierSeq()) : null;
    scopeWalker.getType(type);
    boolean isInference = isDeclaration && type != null && type.equals("auto");

    for (var initDecl: ctx.initDeclaratorList().initDeclarator()) {
      var node = getIdentifier(initDecl.declarator());
      var name = Name.fromNode(node, offset);
      if (isDeclaration && !isInference) scopeWalker.addDecl(new DeclNode(name, type, DeclNode.LOCAL_VAR));
      else scopeWalker.addRef(new RefNode(name));

      if (initDecl.initializer() == null) continue;
      var initRef = handleInitializer(initDecl.initializer());
      if (isInference) {
        var decl = new DeclNode(name, initRef.type, DeclNode.LOCAL_VAR);
        scopeWalker.addInference(new InferenceNode(decl, initRef));
      } else scopeWalker.addRef(initRef);
    }
  }

  @Override
  public void exitJumpStatement(CPP14Parser.JumpStatementContext ctx) {
    super.exitJumpStatement(ctx);
    if (ctx.expression() != null) {
      var ref = handleExpression(ctx.expression());
      scopeWalker.addRef(ref);
    } else if (ctx.bracedInitList() != null) {
      var exprs = handleBracedInitList(ctx.bracedInitList());
      if (exprs.size() == 1) scopeWalker.addRef(exprs.get(0));
      else if (!exprs.isEmpty()) scopeWalker.addRef(new ExprRefNode(exprs));
    }
  }

  @Override
  public void enterIterationStatement(CPP14Parser.IterationStatementContext ctx) {
    super.enterIterationStatement(ctx);
    scopeWalker.enterScope();
    if (ctx.condition() != null && ctx.condition().expression() != null) {
      var cond = handleExpression(ctx.condition().expression());
      scopeWalker.addRef(cond);
    }
    if (allNotNull(ctx.expression())) {
      var expr = handleExpression(ctx.expression());
      scopeWalker.addRef(expr);
    }
  }

  @Override
  public void exitIterationStatement(CPP14Parser.IterationStatementContext ctx) {
    super.exitIterationStatement(ctx);
    scopeWalker.exitScope();
  }

  @Override
  public void exitExpressionStatement(CPP14Parser.ExpressionStatementContext ctx) {
    super.exitExpressionStatement(ctx);
    if (ctx.expression() == null) return;
    var ref = handleExpression(ctx.expression());
    if (ref != null) scopeWalker.addRef(ref);
  }

  RefNode handleInitializer(CPP14Parser.InitializerContext initializer) {
    if (initializer.braceOrEqualInitializer() != null)
      return handleBraceOrEqualInitializer(initializer.braceOrEqualInitializer());
    else return new ExprRefNode(handleExpressionList(initializer.expressionList()));
  }

  private RefNode handleBraceOrEqualInitializer(CPP14Parser.BraceOrEqualInitializerContext braceOrEqualInitializer) {
    if (braceOrEqualInitializer.initializerClause() != null)
      return handleInitClause(braceOrEqualInitializer.initializerClause());
    else return new ExprRefNode(handleBracedInitList(braceOrEqualInitializer.bracedInitList()));
  }

  RefNode handleExpression(CPP14Parser.ExpressionContext expr) {
    return handleSingleOrList(expr.assignmentExpression(), this::handleAssExpression, refs -> {
      RefNode firstNotNull = null;
      boolean allNotNull = true;
      for (var ref: refs) {
        if (firstNotNull == null && ref != null) firstNotNull = ref;
        if (ref == null) allNotNull = false;
      }
      if (!allNotNull) return firstNotNull;
      return new QualifiedRefNode(refs);
    });
  }

  RefNode handleAssExpression(CPP14Parser.AssignmentExpressionContext assExpr) {
    if (assExpr.conditionalExpression() != null) return handleCondExpr(assExpr.conditionalExpression());
    else if (assExpr.throwExpression() != null) return handleThrowExpr(assExpr.throwExpression());
    else {
      // todo
      return handleOrExpr(assExpr.logicalOrExpression());
    }
  }

  RefNode handleCondExpr(CPP14Parser.ConditionalExpressionContext condExpr) {
    if (allNotNull(condExpr.Question(), condExpr.expression(), condExpr.Colon(), condExpr.assignmentExpression())) {
      var cond = handleOrExpr(condExpr.logicalOrExpression());
      var expr1 = handleExpression(condExpr.expression());
      var expr2 = handleAssExpression(condExpr.assignmentExpression());
      return new ExprRefNode(List.of(cond, expr1, expr2), expr1.type);
    } else return handleOrExpr(condExpr.logicalOrExpression());
  }

  RefNode handleThrowExpr(CPP14Parser.ThrowExpressionContext throwExpression) {
    return handleAssExpression(throwExpression.assignmentExpression());
  }

  RefNode handleOrExpr(CPP14Parser.LogicalOrExpressionContext logicalOrExpression) {
    return handleSingleOrList(logicalOrExpression.logicalAndExpression(), this::handleAndExpr, ExprRefNode::new);
  }

  RefNode handleAndExpr(CPP14Parser.LogicalAndExpressionContext logicalAndExpression) {
    return handleSingleOrList(logicalAndExpression.inclusiveOrExpression(), this::handleInclOrExpr, ExprRefNode::new);
  }

  RefNode handleInclOrExpr(CPP14Parser.InclusiveOrExpressionContext inclusiveOrExpression) {
    return handleSingleOrList(inclusiveOrExpression.exclusiveOrExpression(), this::handleExclOrExpr, ExprRefNode::new);
  }

  RefNode handleExclOrExpr(CPP14Parser.ExclusiveOrExpressionContext exclusiveOrExpression) {
    return handleSingleOrList(exclusiveOrExpression.andExpression(), this::handleAndExpr, ExprRefNode::new);
  }

  RefNode handleAndExpr(CPP14Parser.AndExpressionContext andExpression) {
    return handleSingleOrList(andExpression.equalityExpression(), this::handleEqualityExpr, ExprRefNode::new);
  }

  RefNode handleEqualityExpr(CPP14Parser.EqualityExpressionContext equalityExpression) {
    return handleSingleOrList(equalityExpression.relationalExpression(), this::handleRelExpr, ExprRefNode::new);
  }

  RefNode handleRelExpr(CPP14Parser.RelationalExpressionContext relationalExpression) {
    return handleSingleOrList(relationalExpression.shiftExpression(), this::handleShiftExpr, ExprRefNode::new);
  }

  RefNode handleShiftExpr(CPP14Parser.ShiftExpressionContext shiftExpression) {
    return handleSingleOrList(shiftExpression.additiveExpression(), this::handleAdditiveExpr, ExprRefNode::new);
  }

  RefNode handleAdditiveExpr(CPP14Parser.AdditiveExpressionContext additiveExpression) {
    return handleSingleOrList(additiveExpression.multiplicativeExpression(), this::handleMultExpr, ExprRefNode::new);
  }

  RefNode handleMultExpr(CPP14Parser.MultiplicativeExpressionContext multiplicativeExpression) {
    return handleSingleOrList(multiplicativeExpression.pointerMemberExpression(), this::handlePointerExpr, ExprRefNode::new);
  }

  RefNode handlePointerExpr(CPP14Parser.PointerMemberExpressionContext pointerMemberExpression) {
    return handleSingleOrList(pointerMemberExpression.castExpression(), this::handleCastExpr, ExprRefNode::new);
  }

  RefNode handleCastExpr(CPP14Parser.CastExpressionContext castExpression) {
    if (castExpression.unaryExpression() != null) return handleUnaryExpression(castExpression.unaryExpression());
    else {
      var type = scopeWalker.getType(castExpression.theTypeId().toString());
      var expr = handleCastExpr(castExpression.castExpression());
      expr.type = type;
      return expr;
    }
  }

  private RefNode handleUnaryExpression(CPP14Parser.UnaryExpressionContext unaryExpression) {
    if (unaryExpression.postfixExpression() != null)
      return fromList(handlePostExpr(unaryExpression.postfixExpression()));
    else if (unaryExpression.unaryExpression() != null)
      return handleUnaryExpression(unaryExpression.unaryExpression());
    else if (unaryExpression.Sizeof() != null)
      return handleSizeOf(unaryExpression.theTypeId());
    else if (unaryExpression.Alignof() != null)
      return handleAlignOf(unaryExpression.theTypeId());
    else if (unaryExpression.noExceptExpression() != null)
      return handleNoExceptExpr(unaryExpression.noExceptExpression());
    else if (unaryExpression.newExpression() != null)
      return handleNewExpression(unaryExpression.newExpression());
    else return handleDeleteExpression(unaryExpression.deleteExpression());
  }

  private List<RefNode> handlePostExpr(CPP14Parser.PostfixExpressionContext postfixExpression) {
    if (postfixExpression.primaryExpression() != null)
      return List.of(handlePrimaryExpr(postfixExpression.primaryExpression()));
    else if (allNotNull(postfixExpression.postfixExpression(), postfixExpression.LeftParen(), postfixExpression.RightParen())) {
      List<RefNode> callArgs = postfixExpression.expressionList() != null
          ? handleExpressionList(postfixExpression.expressionList())
          : List.of();
      var refNodes = new ArrayList<>(handlePostExpr(postfixExpression.postfixExpression()));
      var last = refNodes.get(refNodes.size() - 1);
      refNodes.set(refNodes.size() - 1, new MethodCallNode(last.ref, callArgs));
      return refNodes;
    } else if (oneNotNull(postfixExpression.typeNameSpecifier(), postfixExpression.simpleTypeSpecifier())
        && oneNotNull(postfixExpression.expressionList(), postfixExpression.bracedInitList())) {
      String type = postfixExpression.simpleTypeSpecifier() != null
          ? getType(postfixExpression.simpleTypeSpecifier())
          : getType(postfixExpression.typeNameSpecifier());

      var refs = postfixExpression.bracedInitList() != null
          ? handleBracedInitList(postfixExpression.bracedInitList())
          : handleExpressionList(postfixExpression.expressionList());

      var expr = new ExprRefNode(refs);
      expr.type = type;
      return List.of(expr);
    } else if (allNotNull(postfixExpression.postfixExpression(), postfixExpression.idExpression())) {
      var result = new ArrayList<>(handlePostExpr(postfixExpression.postfixExpression()));
      result.add(handleIdExpression(postfixExpression.idExpression()));
      return result;
    } else if (allNotNull(postfixExpression.theTypeId(), postfixExpression.expression())) {
      var type = scopeWalker.getType(postfixExpression.theTypeId().getText());
      RefNode refNode = handleExpression(postfixExpression.expression());
      return List.of(new ExprRefNode(List.of(refNode), type));
    } else if (allNotNull(postfixExpression.theTypeId(), postfixExpression.expression())) {
      var type = getType(postfixExpression.theTypeId());
      var ref = handleExpression(postfixExpression.expression());
      ref.type = type;
      return List.of(ref);
    } else if (postfixExpression.typeIdOfTheTypeId() != null) {
      var ref = postfixExpression.theTypeId() != null
          ? new RefNode(Name.fromRule(postfixExpression.theTypeId(), offset))
          : handleExpression(postfixExpression.expression());
      return List.of(new ExprRefNode(List.of(ref)));
    } else if (postfixExpression.postfixExpression() != null)
      return handlePostExpr(postfixExpression.postfixExpression());
    else throw new IllegalStateException(postfixExpression.getText());
  }

  private RefNode handlePrimaryExpr(CPP14Parser.PrimaryExpressionContext primaryExpression) {
    if (primaryExpression.literal() != null && !primaryExpression.literal().isEmpty())
      return handleLiteral(primaryExpression.literal(0));
    else if (primaryExpression.expression() != null)
      return handleExpression(primaryExpression.expression());
    else if (primaryExpression.idExpression() != null)
      return handleIdExpression(primaryExpression.idExpression());
    else if (primaryExpression.This() != null)
      return new RefNode(Name.fromNode(primaryExpression.This(), offset), null, RefNode.THIS);
    else return handleLambdaExpression(primaryExpression.lambdaExpression());
  }

  private RefNode handleLambdaExpression(CPP14Parser.LambdaExpressionContext lambdaExpressionContext) {
    throw new UnsupportedOperationException("");
  }

  private RefNode handleIdExpression(CPP14Parser.IdExpressionContext idExpression) {
    return new RefNode(Name.fromRule(idExpression, offset));
  }

  private RefNode handleLiteral(CPP14Parser.LiteralContext literal) {
    String type = null;
    var node = (TerminalNode) literal.getChild(0);
    if (literal.IntegerLiteral() != null) type = "int";
    else if (literal.CharacterLiteral() != null) type = "char";
    else if (literal.FloatingLiteral() != null) type = "float";
    else if (literal.StringLiteral() != null) type = "char*";
    else if (literal.BooleanLiteral() != null) type = "boolean";
    if (type != null) scopeWalker.getType(type);
    return new RefNode(Name.fromNode(node, offset), type, RefNode.TYPE);
  }

  private RefNode handleNewExpression(CPP14Parser.NewExpressionContext newExpression) {
    var name = Name.fromRule(
        newExpression.newTypeId() == null
            ? newExpression.newTypeId()
            : newExpression.theTypeId(),
        offset);
    List<RefNode> callArgs = newExpression.newInitializer() != null
        ? handleNewInitializer(newExpression.newInitializer())
        : List.of();
    return new MethodCallNode(name, name.name, MethodNode.CREATOR, callArgs);
  }

  List<RefNode> handleNewInitializer(CPP14Parser.NewInitializerContext newInitializer) {
    if (newInitializer.bracedInitList() != null) return handleBracedInitList(newInitializer.bracedInitList());
    else if (newInitializer.expressionList() != null) return handleExpressionList(newInitializer.expressionList());
    else return List.of();
  }

  private List<RefNode> handleExpressionList(CPP14Parser.ExpressionListContext expressionList) {
    if (expressionList.initializerList() != null)
      return handleInitializerList(expressionList.initializerList());
    else return List.of();
  }

  List<RefNode> handleBracedInitList(CPP14Parser.BracedInitListContext bracedInitList) {
    if (bracedInitList.initializerList() != null) return handleInitializerList(bracedInitList.initializerList());
    else return List.of();
  }

  private List<RefNode> handleInitializerList(CPP14Parser.InitializerListContext initializerList) {
    var exprs = new ArrayList<RefNode>();
    for (var initClause: initializerList.initializerClause()) exprs.add(handleInitClause(initClause));
    return exprs;
  }

  private RefNode handleInitClause(CPP14Parser.InitializerClauseContext initClause) {
    if (initClause.bracedInitList() != null) {
      var bracedInitClause = handleBracedInitList(initClause.bracedInitList());
      if (bracedInitClause.size() == 1) return bracedInitClause.get(0);
      else return new ExprRefNode(bracedInitClause);
    } else return handleAssExpression(initClause.assignmentExpression());
  }

  private RefNode handleDeleteExpression(CPP14Parser.DeleteExpressionContext deleteExpression) {
    return handleCastExpr(deleteExpression.castExpression());
  }

  private RefNode handleNoExceptExpr(CPP14Parser.NoExceptExpressionContext noExceptExpression) {
    return handleExpression(noExceptExpression.expression());
  }

  private RefNode handleSizeOf(CPP14Parser.TheTypeIdContext typeId) {
    return new RefNode(Name.fromRule(typeId, offset), "int", RefNode.TYPE);
  }

  private RefNode handleAlignOf(CPP14Parser.TheTypeIdContext typeId) {
    return new RefNode(Name.fromRule(typeId, offset), "boolean", RefNode.TYPE);
  }

  private <P extends ParserRuleContext> RefNode handleSingleOrList(
      List<P> rules,
      Function<P, RefNode> mapper,
      Function<List<RefNode>, RefNode> collector
  ) {
    if (rules.size() == 1) return mapper.apply(rules.get(0));
    else return collector.apply(handleExprList(rules, mapper));
  }

  List<DeclNode> getMethodArguments(CPP14Parser.DeclaratorContext declaratorContext) {
    CPP14Parser.ParametersAndQualifiersContext params;
    if (declaratorContext.parametersAndQualifiers() != null) params = declaratorContext.parametersAndQualifiers();
    else if (declaratorContext.pointerDeclarator() != null &&
        declaratorContext.pointerDeclarator().noPointerDeclarator() != null &&
        declaratorContext.pointerDeclarator().noPointerDeclarator().parametersAndQualifiers() != null) {
      params = declaratorContext.pointerDeclarator().noPointerDeclarator().parametersAndQualifiers();
    } else return Collections.emptyList();
    if (params.parameterDeclarationClause() == null) return Collections.emptyList();

    return getArgList(params.parameterDeclarationClause().parameterDeclarationList());
  }

  List<DeclNode> getArgList(CPP14Parser.ParameterDeclarationListContext ctx) {
    List<DeclNode> result = new ArrayList<>();
    for (var paramDecl: ctx.parameterDeclaration()) {
      if (paramDecl.declarator() != null) {
        var node = getIdentifier(paramDecl.declarator());
        if (node == null) continue;
        String type = getType(paramDecl.declSpecifierSeq());
        result.add(new DeclNode(Name.fromNode(node, offset), type, DeclNode.ARGUMENT));
      }
    }
    return result;
  }

  void mark(TerminalNode node, int type, int style) {
    if (tokenTypes == null || tokenStyles == null) return;
    int ind = node.getSymbol().getTokenIndex();
    tokenTypes[ind] = type;
    tokenStyles[ind] = style;
  }

  void mark(List<TerminalNode> nodes, int type, int style) {
    nodes.forEach(node -> mark(node, type, style));
  }

  <P extends ParserRuleContext> List<RefNode> handleExprList(Collection<P> rules, Function<P, RefNode> handle) {
    List<RefNode> exprs = new ArrayList<>();
    for (var rule: rules) exprs.add(handle.apply(rule));
    return exprs;
  }

  boolean allNotNull(ParseTree... nodes) {
    for (var node: nodes) if (node == null) return false;
    return true;
  }

  boolean oneNotNull(ParseTree... nodes) {
    for (var node: nodes) if (node != null) return true;
    return false;
  }

  static TerminalNode getIdentifier(CPP14Parser.DeclaratorContext ctx) {
    var noPointerDecl = ctx.pointerDeclarator() != null
        ? ctx.pointerDeclarator().noPointerDeclarator()
        : ctx.noPointerDeclarator();
    var idExpression = getDeclaratorId(noPointerDecl).idExpression();
    return getIdentifier(idExpression);
  }

  static TerminalNode getIdentifier(CPP14Parser.IdExpressionContext idExpression) {
    if (idExpression.unqualifiedId() != null) return getIdentifier(idExpression.unqualifiedId());
    else return getIdentifier(idExpression.qualifiedId().unqualifiedId());
  }

  static TerminalNode getIdentifier(CPP14Parser.UnqualifiedIdContext unqualifiedId) {
    if (unqualifiedId.Identifier() != null) return unqualifiedId.Identifier();
    else return null; // todo
  }

  static TerminalNode getIdentifier(CPP14Parser.ClassNameContext ctx) {
    if (ctx.Identifier() != null) return ctx.Identifier();
    else return ctx.simpleTemplateId().templateName().Identifier();
  }

  static String getType(CPP14Parser.DeclSpecifierSeqContext ctx) {
    for (var decl: ctx.declSpecifier()) {
      if (decl.typeSpecifier() != null)
        return getType(decl.typeSpecifier());
    }
    return null;
  }

  static String getType(CPP14Parser.TypeSpecifierContext ctx) {
    return ctx.getText(); // todo
  }

  static String getType(CPP14Parser.TheTypeIdContext ctx) {
    return ctx.getText(); // todo
  }

  static String getType(CPP14Parser.SimpleTypeSpecifierContext ctx) {
    return ctx.getText(); // todo
  }

  static String getType(CPP14Parser.TypeNameSpecifierContext ctx) {
    return ctx.getText(); // todo
  }

  RefNode fromList(List<RefNode> refs) {
    return refs.size() == 1 ? refs.get(0) : new QualifiedRefNode(refs);
  }

  static CPP14Parser.DeclaratoridContext getDeclaratorId(CPP14Parser.NoPointerDeclaratorContext ctx) {
    if (ctx.declaratorid() != null) return ctx.declaratorid();
    if (ctx.noPointerDeclarator() != null) return getDeclaratorId(ctx.noPointerDeclarator());
    else return getDeclaratorId(ctx.pointerDeclarator().noPointerDeclarator());
  }

  static boolean isNotDeclaration(CPP14Parser.SimpleDeclarationContext ctx) {
    return ctx.declSpecifierSeq() == null || ctx.initDeclaratorList() == null;
  }

}
