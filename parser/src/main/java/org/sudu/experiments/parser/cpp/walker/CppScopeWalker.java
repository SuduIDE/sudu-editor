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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.sudu.experiments.parser.ParserConstants.*;
import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.*;
import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Cpp.*;
import static org.sudu.experiments.parser.common.graph.node.NodeTypes.*;

public class CppScopeWalker extends CPP14ParserBaseListener {

  public ScopeWalker scopeWalker;
  public int offset = 0;

  private final int[] tokenTypes, tokenStyles;

  private boolean isInsideMethodParams = false;

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
      scopeWalker.enterMember(List.of());
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
    var typeName = Name.fromRule(className, offset);
    scopeWalker.newIntervalStart = scopeWalker.currentNode.getStart();

//    scopeWalker.enterMember(List.of());
    scopeWalker.associateType(typeName, scopeWalker.currentScope);
    scopeWalker.addInterval(ctx, TYPE);
    scopeWalker.enterInterval();

    scopeWalker.newIntervalStart = ctx.LeftBrace().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitClassSpecifier(CPP14Parser.ClassSpecifierContext ctx) {
    super.exitClassSpecifier(ctx);
//    scopeWalker.exitScope();
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
      var name = handleDeclarator(decl.declarator());
      fields.add(new DeclNode(name, type, DeclTypes.FIELD));
      forMark.add(getIdentifier(decl.declarator()));
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
    isInsideMethodParams = true;
    var name = handleDeclarator(func.declarator());
    isInsideMethodParams = false;

    String type = isConstructorDef ? name.name : getType(func.declSpecifierSeq());

    int methodType = isConstructorDef ? MethodTypes.CREATOR : MethodTypes.METHOD;
    scopeWalker.getType(type);
    var method = new MethodNode(name, type, methodType, argsTypes);
    if (!isMember) method.declType = DeclTypes.LOCAL_VAR;

    scopeWalker.enterMember(method);
    scopeWalker.addInterval(func, MEMBER);
    scopeWalker.enterInterval();
    scopeWalker.enterScope();
    scopeWalker.addDecls(args);

    mark(getIdentifier(func.declarator()), TokenTypes.METHOD, TokenStyles.NORMAL);
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
      var name = handleDeclarator(initDecl.declarator());
      if (isDeclaration && !isInference) scopeWalker.addDecl(new DeclNode(name, type, DeclTypes.LOCAL_VAR));
      else if (!isInference) scopeWalker.addRef(new RefNode(name));

      if (initDecl.initializer() == null) continue;
      var initRef = handleInitializer(initDecl.initializer());
      if (isInference) {
        var decl = new DeclNode(name, initRef.type, DeclTypes.LOCAL_VAR);
        scopeWalker.addInference(new InferenceNode(decl, initRef));
      } else scopeWalker.addRef(initRef);
    }
  }

  @Override
  public void exitJumpStatement(CPP14Parser.JumpStatementContext ctx) {
    super.exitJumpStatement(ctx);
    if (ctx.expression() != null) {
      var ref = handleExpression(ctx.expression());
      scopeWalker.addRefs(ref);
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
      scopeWalker.addRefs(cond);
    }
    if (ctx.expression() != null) {
      var expr = handleExpression(ctx.expression());
      scopeWalker.addRefs(expr);
    }
    if (allNotNull(ctx.forRangeDeclaration(), ctx.forRangeInitializer())) {
      var forRangeDecl = ctx.forRangeDeclaration();
      var forRangeInit = ctx.forRangeInitializer();
      var type = getType(forRangeDecl.declSpecifierSeq());
      scopeWalker.getType(type);
      var name = handleDeclarator(forRangeDecl.declarator());
      var refs = forRangeInit.expression() != null
          ? handleExpression(forRangeInit.expression())
          : handleBracedInitList(forRangeInit.bracedInitList());
      var decl = new DeclNode(name, type, DeclTypes.LOCAL_VAR);
      if (type != null && type.equals("auto")) {
        var ref = refs.get(0);
        decl.type = ref.type;
        scopeWalker.addInference(new InferenceNode(decl, ref, InferenceNode.FOR_EACH));
      } else {
        scopeWalker.addDecl(decl);
        scopeWalker.addRefs(refs);
      }
    }
  }

  @Override
  public void exitIterationStatement(CPP14Parser.IterationStatementContext ctx) {
    super.exitIterationStatement(ctx);
    scopeWalker.exitScope();
  }

  @Override
  public void enterStatement(CPP14Parser.StatementContext ctx) {
    super.enterStatement(ctx);
    if (ctx.parent instanceof CPP14Parser.SelectionStatementContext) scopeWalker.enterScope();
  }

  @Override
  public void exitStatement(CPP14Parser.StatementContext ctx) {
    super.exitStatement(ctx);
    if (ctx.parent instanceof CPP14Parser.SelectionStatementContext) scopeWalker.exitScope();
  }

  @Override
  public void enterSelectionStatement(CPP14Parser.SelectionStatementContext ctx) {
    super.enterSelectionStatement(ctx);
    scopeWalker.enterScope();
    if (ctx.condition().expression() != null) {
      var cond = handleExpression(ctx.condition().expression());
      scopeWalker.addRefs(cond);
    }
  }

  @Override
  public void exitSelectionStatement(CPP14Parser.SelectionStatementContext ctx) {
    super.exitSelectionStatement(ctx);
    scopeWalker.exitScope();
  }

  @Override
  public void exitExpressionStatement(CPP14Parser.ExpressionStatementContext ctx) {
    super.exitExpressionStatement(ctx);
    if (ctx.expression() == null) return;
    var refs = handleExpression(ctx.expression());
    if (refs != null) scopeWalker.addRefs(refs);
  }

  RefNode handleInitializer(CPP14Parser.InitializerContext initializer) {
    if (initializer.braceOrEqualInitializer() != null)
      return handleBraceOrEqualInitializer(initializer.braceOrEqualInitializer());
    else if (initializer.expressionList() != null)
      return new ExprRefNode(handleExpressionList(initializer.expressionList()));
    return null;
  }

  private RefNode handleBraceOrEqualInitializer(CPP14Parser.BraceOrEqualInitializerContext braceOrEqualInitializer) {
    if (braceOrEqualInitializer.initializerClause() != null)
      return handleInitClause(braceOrEqualInitializer.initializerClause());
    else return new ExprRefNode(handleBracedInitList(braceOrEqualInitializer.bracedInitList()));
  }

  List<RefNode> handleExpression(CPP14Parser.ExpressionContext expr) {
    return handleExprList(expr.assignmentExpression(), this::handleAssExpression);
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
      List<RefNode> exprs = new ArrayList<>();
      exprs.add(handleOrExpr(condExpr.logicalOrExpression()));
      exprs.addAll(handleExpression(condExpr.expression()));
      var expr = handleAssExpression(condExpr.assignmentExpression());
      exprs.add(expr);
      return new ExprRefNode(exprs, expr.type);
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
      return handlePostExpr(unaryExpression.postfixExpression());
    else if (unaryExpression.unaryExpression() != null)
      return handleUnaryExpression(unaryExpression.unaryExpression());
    else if (unaryExpression.Sizeof() != null)
      return handleSizeOf(unaryExpression.theTypeId());
    else if (unaryExpression.Alignof() != null)
      return handleAlignOf(unaryExpression.theTypeId());
    else if (unaryExpression.noExceptExpression() != null)
      return handleNoExceptExpr(unaryExpression.noExceptExpression());
    else if (unaryExpression.newExpression_() != null)
      return handleNewExpression(unaryExpression.newExpression_());
    else return handleDeleteExpression(unaryExpression.deleteExpression());
  }

  private RefNode handlePostExpr(CPP14Parser.PostfixExpressionContext postfixExpression) {
    if (postfixExpression.primaryExpression() != null)
      return handlePrimaryExpr(postfixExpression.primaryExpression());

    if (allNotNull(postfixExpression.postfixExpression(), postfixExpression.LeftBracket(), postfixExpression.RightBracket())) {
      var refs = new ArrayList<>(List.of(handlePostExpr(postfixExpression.postfixExpression())));

      if (postfixExpression.expression() != null)
        refs.addAll(handleExpression(postfixExpression.expression()));
      else
        refs.addAll(handleBracedInitList(postfixExpression.bracedInitList()));

      return new ExprRefNode(refs, RefTypes.ARRAY_INDEX);
    }

    if (allNotNull(postfixExpression.postfixExpression(), postfixExpression.idExpression())) {
      var prev = handlePostExpr(postfixExpression.postfixExpression());
      var name = Name.fromRule(postfixExpression.idExpression(), offset);
      RefNode curRef;

      if (allNotNull(postfixExpression.LeftParen(), postfixExpression.LeftParen())) {
        List<RefNode> callArgs = postfixExpression.expressionList() != null
            ? handleExpressionList(postfixExpression.expressionList())
            : List.of();
        curRef = new MethodCallNode(name, callArgs);
      } else curRef = new RefNode(name);

      if (prev instanceof QualifiedRefNode qualifiedRef) {
        var flat = qualifiedRef.flatten();
        flat.add(curRef);
        return new QualifiedRefNode(flat);
      } else return new QualifiedRefNode(List.of(prev, curRef));
    }

    if (allNotNull(postfixExpression.postfixExpression(), postfixExpression.LeftParen(), postfixExpression.RightParen())) {
      List<RefNode> callArgs = postfixExpression.expressionList() != null
          ? handleExpressionList(postfixExpression.expressionList())
          : List.of();
      var ref = handlePostExpr(postfixExpression.postfixExpression());
      if (ref == null || ref.ref == null) return null;
      return new MethodCallNode(ref.ref, callArgs);
    }

    if (oneNotNull(postfixExpression.typeNameSpecifier(), postfixExpression.simpleTypeSpecifier())
        && oneNotNull(postfixExpression.expressionList(), postfixExpression.bracedInitList())) {
      String type = postfixExpression.simpleTypeSpecifier() != null
          ? getType(postfixExpression.simpleTypeSpecifier())
          : getType(postfixExpression.typeNameSpecifier());

      var refs = postfixExpression.bracedInitList() != null
          ? handleBracedInitList(postfixExpression.bracedInitList())
          : handleExpressionList(postfixExpression.expressionList());

      var expr = new ExprRefNode(refs);
      expr.type = type;
      return expr;
    }

    if (allNotNull(postfixExpression.theTypeId(), postfixExpression.expression())) {
      var type = scopeWalker.getType(postfixExpression.theTypeId().getText());
      List<RefNode> refNodes = handleExpression(postfixExpression.expression());
      return new ExprRefNode(refNodes, type);
    }

    if (allNotNull(postfixExpression.theTypeId(), postfixExpression.expression())) {
      var type = getType(postfixExpression.theTypeId());
      var refs = handleExpression(postfixExpression.expression());
      return new ExprRefNode(refs, type);
    }

    if (postfixExpression.typeIdOfTheTypeId() != null) {
      if (postfixExpression.theTypeId() != null)
        return new RefNode(Name.fromRule(postfixExpression.theTypeId(), offset));
      else return new ExprRefNode(handleExpression(postfixExpression.expression()));
    }

    if (postfixExpression.postfixExpression() != null)
      return handlePostExpr(postfixExpression.postfixExpression());

    throw new IllegalStateException(postfixExpression.getText());
  }

  private RefNode handlePrimaryExpr(CPP14Parser.PrimaryExpressionContext primaryExpression) {
    if (primaryExpression.literal() != null && !primaryExpression.literal().isEmpty())
      return handleLiteral(primaryExpression.literal(0));
    else if (primaryExpression.expression() != null)
      return new ExprRefNode(handleExpression(primaryExpression.expression()));
    else if (primaryExpression.idExpression() != null)
      return handleIdExpression(primaryExpression.idExpression());
    else if (primaryExpression.This() != null)
      return new RefNode(Name.fromNode(primaryExpression.This(), offset), null, RefTypes.THIS);
    else return handleLambdaExpression(primaryExpression.lambdaExpression());
  }

  private RefNode handleLambdaExpression(CPP14Parser.LambdaExpressionContext lambdaExpressionContext) {
    return null;
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
    return new RefNode(Name.fromNode(node, offset), type, RefTypes.LITERAL);
  }

  private RefNode handleNewExpression(CPP14Parser.NewExpression_Context newExpression) {
    var name = Name.fromRule(
        newExpression.newTypeId() != null
            ? newExpression.newTypeId()
            : newExpression.theTypeId(),
        offset);
    List<RefNode> callArgs = newExpression.newInitializer_() != null
        ? handleNewInitializer(newExpression.newInitializer_())
        : List.of();
    return new MethodCallNode(name, name.name, MethodTypes.CREATOR, callArgs);
  }

  List<RefNode> handleNewInitializer(CPP14Parser.NewInitializer_Context newInitializer) {
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
    return new ExprRefNode(handleExpression(noExceptExpression.expression()));
  }

  private RefNode handleSizeOf(CPP14Parser.TheTypeIdContext typeId) {
    return new RefNode(Name.fromRule(typeId, offset), "int", RefTypes.LITERAL);
  }

  private RefNode handleAlignOf(CPP14Parser.TheTypeIdContext typeId) {
    return new RefNode(Name.fromRule(typeId, offset), "boolean", RefTypes.LITERAL);
  }

  private <P extends ParserRuleContext> RefNode handleSingleOrList(
      List<P> rules,
      Function<P, RefNode> mapper,
      Function<List<RefNode>, RefNode> collector
  ) {
    if (rules.size() == 1) return mapper.apply(rules.get(0));
    else return collector.apply(handleExprList(rules, mapper));
  }


  Name handleDeclarator(CPP14Parser.DeclaratorContext declarator) {
    if (declarator.pointerDeclarator() != null) return handlePointerDeclarator(declarator.pointerDeclarator());
    else return handleNoPointerDeclarator(declarator.noPointerDeclarator());
  }

  Name handlePointerDeclarator(CPP14Parser.PointerDeclaratorContext pointerDeclarator) {
    return handleNoPointerDeclarator(pointerDeclarator.noPointerDeclarator());
  }

  Name handleNoPointerDeclarator(CPP14Parser.NoPointerDeclaratorContext noPointerDeclarator) {
    if (!isInsideMethodParams && noPointerDeclarator.initializer() != null) {
      var ref = handleInitializer(noPointerDeclarator.initializer());
      scopeWalker.addRef(ref);
    }
    if (noPointerDeclarator.declaratorid() != null) return Name.fromRule(noPointerDeclarator.declaratorid(), offset);
    else if (noPointerDeclarator.noPointerDeclarator() != null) {
      if (noPointerDeclarator.constantExpression() != null) {
        var ref = handleCondExpr(noPointerDeclarator.constantExpression().conditionalExpression());
        scopeWalker.addRef(ref);
      }
      return handleNoPointerDeclarator(noPointerDeclarator.noPointerDeclarator());
    } else return handlePointerDeclarator(noPointerDeclarator.pointerDeclarator());
  }

  List<DeclNode> getMethodArguments(CPP14Parser.DeclaratorContext declaratorContext) {
    CPP14Parser.ParametersAndQualifiersContext params;
    if (declaratorContext.parametersAndQualifiers() != null)
      return getArgList(declaratorContext.parametersAndQualifiers().parameterDeclarationClause().parameterDeclarationList());
    else if (declaratorContext.pointerDeclarator() != null &&
        declaratorContext.pointerDeclarator().noPointerDeclarator() != null) {
      var noPointer = declaratorContext.pointerDeclarator().noPointerDeclarator();
      if (noPointer.initializer() != null && noPointer.initializer().expressionList() != null) {
        return getArgList(noPointer.initializer().expressionList());
      } else if (noPointer.parametersAndQualifiers() != null &&
          noPointer.parametersAndQualifiers().parameterDeclarationClause() != null) {
        return getArgList(noPointer.parametersAndQualifiers().parameterDeclarationClause().parameterDeclarationList());
      }
    }
    return Collections.emptyList();
  }

  List<DeclNode> getArgList(CPP14Parser.ParameterDeclarationListContext ctx) {
    List<DeclNode> result = new ArrayList<>();
    for (var paramDecl: ctx.parameterDeclaration()) {
      if (paramDecl.declarator() != null) {
        var name = handleDeclarator(paramDecl.declarator());
        String type = getType(paramDecl.declSpecifierSeq());
        result.add(new DeclNode(name, type, DeclTypes.ARGUMENT));
      }
    }
    return result;
  }

  List<DeclNode> getArgList(CPP14Parser.ExpressionListContext ctx) {
    var exprs = handleExpressionList(ctx);
    List<DeclNode> result = new ArrayList<>();
    for (var expr: exprs) {
      if (expr instanceof ExprRefNode exprRef && exprRef.refNodes.size() >= 2) {
        var type = exprRef.refNodes.get(0).ref.name;
        var name = exprRef.refNodes.get(1).ref;
        result.add(new DeclNode(name, type, DeclTypes.ARGUMENT));
      }
    }
    return result;
  }

  void mark(TerminalNode node, int type, int style) {
    if (tokenTypes == null || tokenStyles == null || node == null) return;
    int ind = node.getSymbol().getTokenIndex();
    if (tokenTypes[ind] != TokenTypes.DEFAULT || tokenStyles[ind] != TokenStyles.NORMAL) return;
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
