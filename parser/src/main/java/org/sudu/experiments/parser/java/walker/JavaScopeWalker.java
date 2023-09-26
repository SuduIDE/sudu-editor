package org.sudu.experiments.parser.java.walker;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.common.Name;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.graph.ScopeWalker;
import org.sudu.experiments.parser.common.graph.node.decl.ArgNode;
import org.sudu.experiments.parser.common.graph.node.decl.CreatorNode;
import org.sudu.experiments.parser.common.graph.node.decl.FieldNode;
import org.sudu.experiments.parser.common.graph.node.decl.MethodNode;
import org.sudu.experiments.parser.common.graph.node.ref.*;
import org.sudu.experiments.parser.common.graph.type.Type;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.gen.JavaParserBaseListener;

import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.Java.*;
import static org.sudu.experiments.parser.ParserConstants.IntervalTypes.UNKNOWN;

import java.util.ArrayList;
import java.util.List;

public class JavaScopeWalker extends JavaParserBaseListener {

  public ScopeWalker scopeWalker;

  public JavaScopeWalker(IntervalNode node) {
    scopeWalker = new ScopeWalker(node);
  }

  @Override
  public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
    super.enterPackageDeclaration(ctx);
    scopeWalker.enterFakeScope();
    scopeWalker.addInterval(ctx, PACKAGE);
  }

  @Override
  public void exitPackageDeclaration(JavaParser.PackageDeclarationContext ctx) {
    super.exitPackageDeclaration(ctx);
    scopeWalker.exitFakeScope();
  }

  @Override
  public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
    super.enterImportDeclaration(ctx);
    scopeWalker.enterFakeScope();
    scopeWalker.addInterval(ctx, IMPORT);
  }

  @Override
  public void exitImportDeclaration(JavaParser.ImportDeclarationContext ctx) {
    super.exitImportDeclaration(ctx);
    scopeWalker.exitFakeScope();
  }

  @Override
  public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
    super.enterTypeDeclaration(ctx);
    scopeWalker.enterScope();
    scopeWalker.addInterval(ctx, TYPE_DECL);
    scopeWalker.enterInterval();
  }

  @Override
  public void exitTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
    super.exitTypeDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitInterval();
  }

  @Override
  public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.enterClassDeclaration(ctx);
    scopeWalker.enterScope();

    var node = getNode(ctx.identifier());
    String typeString = node.getText();
    var type = scopeWalker.addType(typeString, scopeWalker.currentScope);
    scopeWalker.enterType(type);

    if (ctx.typeType() != null) {
      String supertypeString = getType(ctx.typeType());
      var supertype = scopeWalker.getType(supertypeString);
      type.supertypes.add(supertype);
      scopeWalker.currentScope.importTypes.add(supertype);
    }

    addSupertypes(type, ctx.typeList());

    scopeWalker.newIntervalStart =
        ctx.classBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
    super.exitClassDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitType();

    scopeWalker.newIntervalStart =
        ctx.classBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
    super.enterClassBodyDeclaration(ctx);
    if (ctx.parent instanceof JavaParser.EnumBodyDeclarationsContext) return;
    if (isMember(ctx)) addMember(ctx.memberDeclaration());
    else scopeWalker.enterScope();

    scopeWalker.addInterval(ctx, CLASS_BODY);
    scopeWalker.enterInterval();
  }

  @Override
  public void exitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx) {
    super.exitClassBodyDeclaration(ctx);
    if (ctx.parent instanceof JavaParser.EnumBodyDeclarationsContext) return;
    if (isMember(ctx)) scopeWalker.exitMember();
    else scopeWalker.exitScope();

    scopeWalker.exitInterval();
  }

  @Override
  public void enterInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.enterInterfaceDeclaration(ctx);
    scopeWalker.enterScope();

    var node = getNode(ctx.identifier());
    String typeString = node.getText();
    var type = scopeWalker.addType(typeString, scopeWalker.currentScope);
    scopeWalker.enterType(type);

    addSupertypes(type, ctx.typeList());

    scopeWalker.newIntervalStart =
        ctx.interfaceBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx) {
    super.exitInterfaceDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitType();

    scopeWalker.newIntervalStart =
        ctx.interfaceBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {
    super.enterInterfaceBodyDeclaration(ctx);
    if (!isMember(ctx)) scopeWalker.enterScope();
    else addMember(ctx.interfaceMemberDeclaration());
    scopeWalker.addInterval(ctx, CLASS_BODY);
    scopeWalker.enterInterval();
  }

  @Override
  public void exitInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx) {
    super.exitInterfaceBodyDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitInterval();
  }

  @Override
  public void enterEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.enterEnumDeclaration(ctx);
    scopeWalker.enterScope();
    scopeWalker.newIntervalStart = ctx.LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitEnumDeclaration(JavaParser.EnumDeclarationContext ctx) {
    super.exitEnumDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.newIntervalStart = ctx.RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.enterRecordDeclaration(ctx);
    scopeWalker.enterScope();
    scopeWalker.newIntervalStart = ctx.recordBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitRecordDeclaration(JavaParser.RecordDeclarationContext ctx) {
    super.exitRecordDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.newIntervalStart = ctx.recordBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.enterAnnotationTypeDeclaration(ctx);
    scopeWalker.enterScope();
    scopeWalker.newIntervalStart = ctx.annotationTypeBody().LBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void exitAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx) {
    super.exitAnnotationTypeDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.newIntervalStart = ctx.annotationTypeBody().RBRACE().getSymbol().getStartIndex() + 1;
  }

  @Override
  public void enterAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {
    super.enterAnnotationTypeElementDeclaration(ctx);
    scopeWalker.enterScope();
    scopeWalker.addInterval(ctx, UNKNOWN);
    scopeWalker.enterInterval();
  }

  @Override
  public void exitAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx) {
    super.exitAnnotationTypeElementDeclaration(ctx);
    scopeWalker.exitScope();
    scopeWalker.exitInterval();
  }

  @Override
  public void enterLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
    super.enterLocalVariableDeclaration(ctx);
  }

  @Override
  public void exitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx) {
    super.exitLocalVariableDeclaration(ctx);
    if (ctx.typeType() != null && ctx.variableDeclarators() != null) {
      var typeString = getType(ctx.typeType());
      var type = scopeWalker.getType(typeString);
      for (var variableDeclarator: ctx.variableDeclarators().variableDeclarator()) {
        var node = getNode(variableDeclarator);
        scopeWalker.addVarDecl(Name.fromNode(node), type);
        if (variableDeclarator.variableInitializer() != null &&
            variableDeclarator.variableInitializer().expression() != null
        ) addRef(handleExpression(variableDeclarator.variableInitializer().expression()));
      }
    }
    if (ctx.identifier() != null && ctx.expression() != null) {
      var ref = handleExpression(ctx.expression());
      var type = ref != null ? ref.type : null;
      addRef(ref);
      scopeWalker.addVarDecl(Name.fromNode(getNode(ctx.identifier())), type);
    }
  }

  @Override
  public void exitStatement(JavaParser.StatementContext ctx) {
    super.exitStatement(ctx);
    if (ctx.expression() != null && !ctx.expression().isEmpty()) {
      addRef(handleExpression(ctx.expression(0)));
    }
  }

  @Override
  public void enterBlock(JavaParser.BlockContext ctx) {
    super.enterBlock(ctx);
    scopeWalker.enterScope();
  }

  @Override
  public void exitBlock(JavaParser.BlockContext ctx) {
    super.exitBlock(ctx);
    scopeWalker.exitScope();
  }

  public void handleField(JavaParser.FieldDeclarationContext ctx) {
    String typeString = getType(ctx.typeType());
    Type type = scopeWalker.getType(typeString);
    List<TerminalNode> declarators = getVarDeclarators(ctx.variableDeclarators());

    List<FieldNode> fields = declarators.stream()
        .map(decl -> new FieldNode(Name.fromNode(decl), type))
        .toList();

    scopeWalker.enterMember(fields);
  }

  public void handleMethod(JavaParser.MethodDeclarationContext ctx) {
    addMethodDecl(ctx.identifier(), ctx.typeTypeOrVoid(), ctx.formalParameters());
  }

  public void handleMethod(JavaParser.InterfaceMethodDeclarationContext ctx) {
    if (ctx.interfaceCommonBodyDeclaration() == null) return;
    var cbm = ctx.interfaceCommonBodyDeclaration();
    addMethodDecl(cbm.identifier(), cbm.typeTypeOrVoid(), cbm.formalParameters());
  }

  public void handleMethod(JavaParser.GenericInterfaceMethodDeclarationContext ctx) {
    if (ctx.interfaceCommonBodyDeclaration() == null) return;
    var cbm = ctx.interfaceCommonBodyDeclaration();
    addMethodDecl(cbm.identifier(), cbm.typeTypeOrVoid(), cbm.formalParameters());
  }

  public void handleConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx) {
    super.enterConstructorDeclaration(ctx);
    TerminalNode node = getNode(ctx.identifier());
    Name decl = Name.fromNode(node);
    String typeString = node.getText();
    Type type = scopeWalker.getType(typeString);
    List<ArgNode> args = getArgsTypes(ctx.formalParameters());
    var creator = new CreatorNode(decl, type, args);
    scopeWalker.enterMember(creator);
    scopeWalker.addToRoot(creator);
  }

  private void addMethodDecl(
      JavaParser.IdentifierContext identifier,
      JavaParser.TypeTypeOrVoidContext typeTypeOrVoid,
      JavaParser.FormalParametersContext formalParameters
  ) {
    TerminalNode node = getNode(identifier);
    Name decl = Name.fromNode(node);
    String typeString = getType(typeTypeOrVoid);
    Type type = scopeWalker.getType(typeString);
    List<ArgNode> args = getArgsTypes(formalParameters);
    scopeWalker.enterMember(new MethodNode(decl, type, args));
  }

  private boolean isMember(JavaParser.ClassBodyDeclarationContext ctx) {
    return ctx.memberDeclaration() != null
        && (ctx.memberDeclaration().fieldDeclaration() != null
        || ctx.memberDeclaration().methodDeclaration() != null
        || ctx.memberDeclaration().genericMethodDeclaration() != null
        || ctx.memberDeclaration().constructorDeclaration() != null);
  }

  private boolean isMember(JavaParser.InterfaceBodyDeclarationContext ctx) {
    return ctx.interfaceMemberDeclaration() != null
        && (ctx.interfaceMemberDeclaration().interfaceMethodDeclaration() != null
        || ctx.interfaceMemberDeclaration().genericInterfaceMethodDeclaration() != null
//        || ctx.interfaceMemberDeclaration().constDeclaration() != null
    );
  }

  private void addMember(JavaParser.MemberDeclarationContext ctx) {
    if (ctx.fieldDeclaration() != null) handleField(ctx.fieldDeclaration());
    else if (ctx.methodDeclaration() != null) handleMethod(ctx.methodDeclaration());
    else if (ctx.genericMethodDeclaration() != null) handleMethod(ctx.genericMethodDeclaration().methodDeclaration());
    else if (ctx.constructorDeclaration() != null) handleConstructorDeclaration(ctx.constructorDeclaration());
    else throw new IllegalArgumentException();
  }

  private void addMember(JavaParser.InterfaceMemberDeclarationContext ctx) {
    if (ctx.interfaceMethodDeclaration() != null) handleMethod(ctx.interfaceMethodDeclaration());
    else if (ctx.genericInterfaceMethodDeclaration() != null) handleMethod(ctx.genericInterfaceMethodDeclaration());
    else throw new IllegalArgumentException();
  }

  private void addSupertypes(
      Type type,
      List<JavaParser.TypeListContext> typeListContexts
  ) {
    if (typeListContexts != null && !typeListContexts.isEmpty()) {
      var typeTypeList = typeListContexts.stream().flatMap(typeList -> typeList.typeType().stream()).toList();
      for (var typeType: typeTypeList) {
        String supertypeString = getType(typeType);

        var supertype = scopeWalker.getType(supertypeString);
        type.supertypes.add(supertype);
        scopeWalker.currentScope.importTypes.add(supertype);
      }
    }
  }

  private RefNode handleExpression(JavaParser.ExpressionContext ctx) {
    if (isBooleanBinOperation(ctx)) return handleBooleanExpression(ctx);
    if (hasThis(ctx)) return handleThis(ctx);
    if (isTernary(ctx)) return handleTernary(ctx);
    if (isQualified(ctx)) return handleQualified(ctx);
    if (ctx.primary() != null) return handlePrimary(ctx.primary());
    if (ctx.creator() != null) return handleCreator(ctx.creator());
    if (ctx.getChildCount() == 1 && ctx.methodCall() != null)
      return handleMethodCall(ctx.methodCall());
    if (ctx.expression() != null && !ctx.expression().isEmpty()) {
      var result = handleExpression(ctx.expression(0));
      for (int i = 1; i < ctx.expression().size(); i++) {
        addRef(handleExpression(ctx.expression(i)));
      }
      return result;
    }
    return null;
  }

  private RefNode addRef(RefNode ref) {
    if (ref != null && !(ref instanceof TypeNode)) scopeWalker.addRef(ref);
    return ref;
  }

  private List<RefNode> handleExpressionList(JavaParser.ExpressionListContext ctx) {
    var result = ctx.expression().stream()
        .map(this::handleExpression)
        .toList();
    result.forEach(this::addRef);
    return result;
  }

  private MethodCallNode handleMethodCall(JavaParser.MethodCallContext ctx) {
    List<RefNode> args = ctx.expressionList() != null
        ? handleExpressionList(ctx.expressionList())
        : new ArrayList<>();

    if (ctx.identifier() != null) {
      var node = getNode(ctx.identifier());
      return new MethodCallNode(Name.fromNode(node), args);
    } else if (ctx.THIS() != null) {

    } else if (ctx.SUPER() != null) {

    }
    return null;
  }

  private RefNode handleThis(JavaParser.ExpressionContext ctx) {
    if (ctx.identifier() != null) {
      var node = getNode(ctx.identifier());
      return addRef(new FieldRefNode(Name.fromNode(node)));
    } else if (ctx.methodCall() != null) {
      return handleMethodCall(ctx.methodCall());
    } else return null;
  }

  private QualifiedRefNode handleQualified(JavaParser.ExpressionContext ctx) {
    List<RefNode> refs = new ArrayList<>();
    handleQualifiedRec(ctx, refs);

    for (var ref: refs) {
      if (ref == null) {
        addRef(refs.get(0));
        return null;
      }
    }
    var preLast = refs.get(refs.size() - 2);
    var last = refs.get(refs.size() - 1);

    var cur = new QualifiedRefNode(preLast, last);
    for (int i = refs.size() - 3; i >= 0; i--) {
      cur = new QualifiedRefNode(refs.get(i), cur);
    }
    return cur;
  }

  private CreatorCallNode handleCreator(JavaParser.CreatorContext ctx) {
    var node = getNode(ctx.createdName());
    var decl = Name.fromNode(node);
    var typeString = node.getText();
    var type = scopeWalker.getType(typeString);

    List<RefNode> args;
    if (ctx.classCreatorRest() != null &&
        ctx.classCreatorRest().arguments() != null &&
        ctx.classCreatorRest().arguments().expressionList() != null
    ) args = handleExpressionList(ctx.classCreatorRest().arguments().expressionList());
    else args = new ArrayList<>();

    var creatorCall = new CreatorCallNode(decl, type, args);
    addRef(creatorCall);
    return creatorCall;
  }

  private void handleQualifiedRec(JavaParser.ExpressionContext ctx, List<RefNode> refs) {
    if (ctx.expression() != null &&
        ctx.expression().size() == 1 &&
        ctx.dot != null
    ) handleQualifiedRec(ctx.expression(0), refs);

    if (ctx.identifier() != null) refs.add(new RefNode(Name.fromNode(getNode(ctx.identifier()))));
    else if (ctx.primary() != null) refs.add(handlePrimary(ctx.primary()));
    else if (ctx.methodCall() != null) refs.add(handleMethodCall(ctx.methodCall()));
    else refs.add(null);
  }

  private RefNode handleQualifiedBegin(JavaParser.ExpressionContext ctx) {
    if (ctx.methodCall() != null) {
      return handleMethodCall(ctx.methodCall());
    } else if (ctx.identifier() != null) {
      return new RefNode(Name.fromNode(getNode(ctx.identifier())));
    } else return null;
  }

  private RefNode handlePrimary(JavaParser.PrimaryContext ctx) {
    if (ctx.expression() != null) return handleExpression(ctx.expression());
    if (ctx.THIS() != null) return new ThisNode(Name.fromNode(ctx.THIS()), scopeWalker.currentType());
    if (ctx.SUPER() != null) return new SuperNode(Name.fromNode(ctx.THIS()), scopeWalker.currentType());
    if (ctx.literal() != null) return new TypeNode(handleLiteral(ctx.literal()));
    if (ctx.identifier() != null) return new RefNode(Name.fromNode(getNode(ctx.identifier())));
    return null;
  }

  private TypeNode handleBooleanExpression(JavaParser.ExpressionContext ctx) {
    addRef(handleExpression(ctx.expression(0)));
    addRef(handleExpression(ctx.expression(1)));
    return new TypeNode(scopeWalker.getType("boolean"));
  }

  private Type handleLiteral(JavaParser.LiteralContext ctx) {
    if (ctx.integerLiteral() != null) return scopeWalker.getType("int");
    if (ctx.floatLiteral() != null) return scopeWalker.getType("float");
    if (ctx.CHAR_LITERAL() != null) return scopeWalker.getType("char");
    if (ctx.BOOL_LITERAL() != null) return scopeWalker.getType("boolean");
    if (ctx.STRING_LITERAL() != null ||
        ctx.TEXT_BLOCK() != null) return scopeWalker.getType("String");
    return null;
  }

  private RefNode handleTernary(JavaParser.ExpressionContext ctx) {
    var result = handleExpression(ctx.expression(0));
    addRef(handleExpression(ctx.expression(1)));
    addRef(handleExpression(ctx.expression(2)));
    return result;
  }

  public String toString(JavaParser.QualifiedNameContext ctx) {
    return ctx.toString();
  }

  private List<TerminalNode> getVarDeclarators(JavaParser.VariableDeclaratorsContext ctx) {
    List<TerminalNode> result = new ArrayList<>();
    for (var declarator: ctx.variableDeclarator()) {
      var id = declarator.variableDeclaratorId().identifier();
      result.add(getNode(id));
    }
    return result;
  }

  private List<ArgNode> getArgsTypes(JavaParser.FormalParametersContext ctx) {
    List<ArgNode> result = new ArrayList<>();
    if (ctx.formalParameterList() != null) {
      var list = ctx.formalParameterList();
      for (var formalParam: list.formalParameter()) {
        String typeString = getType(formalParam.typeType());
        Type type = scopeWalker.getType(typeString);

        TerminalNode node = getNode(formalParam.variableDeclaratorId().identifier());
        Name decl = Name.fromNode(node);
        result.add(new ArgNode(decl, type));
      }
      if (list.lastFormalParameter() != null) {
        String typeString = getType(list.lastFormalParameter().typeType());
        Type type = scopeWalker.getType(typeString);

        TerminalNode node = getNode(list.lastFormalParameter().variableDeclaratorId().identifier());
        Name decl = Name.fromNode(node);
        result.add(new ArgNode(decl, type));
      }
    }
    return result;
  }

  private boolean isStatic(JavaParser.FieldDeclarationContext ctx) {
    if (ctx.parent instanceof JavaParser.MemberDeclarationContext memberDeclaration) {
      return isStatic(memberDeclaration);
    }
    return false;
  }

  private boolean isStatic(JavaParser.MemberDeclarationContext ctx) {
    if (ctx.parent instanceof JavaParser.ClassBodyDeclarationContext classBodyDeclaration) {
      return classBodyDeclaration.STATIC() != null;
    }
    return false;
  }

  private boolean hasThis(JavaParser.ExpressionContext ctx) {
    if (ctx.getChildCount() < 3
        || ctx.expression() == null
        || ctx.expression().isEmpty()
    ) return false;
    var expression = ctx.expression(0);
    return expression.primary() != null
        && expression.primary().THIS() != null
        && ctx.dot != null;
  }

  private boolean isQualified(JavaParser.ExpressionContext ctx) {
    return ctx.getChildCount() >= 3
        && ctx.expression() != null
        && !ctx.expression().isEmpty()
        && ctx.dot != null
        && (ctx.identifier() != null || ctx.methodCall() != null);
  }

  private boolean isBooleanBinOperation(JavaParser.ExpressionContext ctx) {
    return ctx.getChildCount() == 3
        && (isNonNullAndEmpty(ctx.LT())
        || isNonNullAndEmpty(ctx.GT())
        || ctx.LE() != null
        || ctx.GE() != null
        || ctx.EQUAL() != null
        || ctx.NOTEQUAL() != null);
  }

  static boolean isTernary(JavaParser.ExpressionContext ctx) {
    return ctx.getChildCount() == 5
        && ctx.QUESTION() != null
        && ctx.COLON() != null;
  }

  private <T extends ParseTree> boolean isNonNullAndEmpty(List<T> nodes) {
    return nodes != null && !nodes.isEmpty();
  }

  private TerminalNode getNode(JavaParser.VariableDeclaratorContext ctx) {
    return getNode(ctx.variableDeclaratorId());
  }

  private TerminalNode getNode(JavaParser.VariableDeclaratorIdContext ctx) {
    return getNode(ctx.identifier());
  }

  private TerminalNode getNode(JavaParser.IdentifierContext ctx) {
    return (TerminalNode) ctx.getChild(0);
  }

  private TerminalNode getNode(JavaParser.CreatedNameContext ctx) {
    return ctx.identifier() != null && !ctx.identifier().isEmpty()
        ? getNode(ctx.identifier(ctx.identifier().size() - 1))
        : (TerminalNode) ctx.primitiveType().getChild(0);
  }

  private String getType(JavaParser.TypeTypeOrVoidContext ctx) {
    return ctx.typeType() != null
        ? getType(ctx.typeType())
        : ctx.VOID().getText();
  }

  private String getType(JavaParser.TypeTypeContext ctx) {
    return ctx.classOrInterfaceType() != null
        ? getType(ctx.classOrInterfaceType())
        : ctx.primitiveType().getText();
  }

  private String getType(JavaParser.ClassOrInterfaceTypeContext ctx) {
    return ctx.getText();
  }

}
