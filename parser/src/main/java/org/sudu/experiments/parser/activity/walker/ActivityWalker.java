package org.sudu.experiments.parser.activity.walker;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.sudu.experiments.parser.ParserConstants;
import org.sudu.experiments.parser.activity.gen.ActivityLexer;
import org.sudu.experiments.parser.activity.gen.ActivityParser;
import org.sudu.experiments.parser.activity.gen.ActivityParserBaseListener;
import org.sudu.experiments.parser.activity.graph.stat.Activity;
import org.sudu.experiments.parser.activity.graph.expr.BinaryExpr;
import org.sudu.experiments.parser.activity.graph.expr.CommaExpr;
import org.sudu.experiments.parser.activity.graph.stat.ComplexStat;
import org.sudu.experiments.parser.activity.graph.expr.ConsExpr;
import org.sudu.experiments.parser.activity.graph.IExpr;
import org.sudu.experiments.parser.activity.graph.expr.ExprKind;
import org.sudu.experiments.parser.activity.graph.stat.Id;
import org.sudu.experiments.parser.activity.graph.stat.If;
import org.sudu.experiments.parser.activity.graph.expr.NotExpr;
import org.sudu.experiments.parser.activity.graph.stat.Random;
import org.sudu.experiments.parser.activity.graph.stat.Repeat;
import org.sudu.experiments.parser.activity.graph.stat.Schedule;
import org.sudu.experiments.parser.activity.graph.stat.Select;
import org.sudu.experiments.parser.activity.graph.IStat;
import org.sudu.experiments.parser.activity.graph.stat.Sequence;
import org.sudu.experiments.parser.activity.graph.stat.UnknownStat;
import org.sudu.experiments.parser.common.Pos;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivityWalker extends ActivityParserBaseListener {
  private final int[] tokenTypes;
  private final int[] tokenStyles;
  private Map<Pos, Pos> usageToDef;
  private Map<String, Pos> def = new HashMap<>();

  public Activity getActivity() {
    return activity;
  }

  private Activity activity;
  private LinkedList<IStat> statStack = new LinkedList<>();
  private LinkedList<List<IStat>> containerOfStatStack = new LinkedList<>();

  private LinkedList<IExpr> exprStack = new LinkedList<>();

  public ActivityWalker(int[] tokenTypes, int[] tokenStyles, Map<Pos, Pos> usageToDef) {
    this.tokenTypes = tokenTypes;
    this.tokenStyles = tokenStyles;
    this.usageToDef = usageToDef;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    int index = node.getSymbol().getTokenIndex();
    if (index < 0) return;

    tokenTypes[index] = ParserConstants.TokenTypes.ERROR;
  }


  @Override
  public void enterActivity(ActivityParser.ActivityContext ctx) {
    activity = new Activity();

    statStack.add(activity);
    containerOfStatStack.add(activity.block());
  }

  @Override
  public void exitActivity(ActivityParser.ActivityContext ctx) {
    statStack.removeLast();
    containerOfStatStack.removeLast();

    if (!statStack.isEmpty()) {
      System.err.println("Stat Stack must be empty but still have "+statStack.size()+" elements");
    }
  }

  @Override
  public void enterExpr(ActivityParser.ExprContext ctx) {
    IExpr expr;
    if (ctx.ID() != null) {
      expr = new Id(ctx.ID().getText());
    }

    else if (ctx.LPAREN() != null) {
      //special case of ( expr )
      return;

    } else if (ctx.AND() != null) {
      expr = new BinaryExpr(ExprKind.And);

    } else if (ctx.XOR() != null) {
      expr = new BinaryExpr(ExprKind.Xor);

    } else if (ctx.OR() != null) {
      expr = new BinaryExpr(ExprKind.Or);

    } else if (ctx.NOT() != null) {
      expr = new NotExpr();

    } else if (ctx.exprcomma() != null) {
      CommaExpr comma = new CommaExpr();
      expr = comma;
      for (var consExpr : ctx.exprcomma().exprcons()) {
        var cons = new ConsExpr();
        for (var id: consExpr.ID()) {
          cons.exprs.add(new Id(id.getText()));
        }
        comma.exprs.add(cons);
      }
    } else {
      expr = new BinaryExpr(ExprKind.Unknown); //error
      System.err.println("Unknown node");
    }

    exprStack.add(expr);
  }

  @Override
  public void exitExpr(ActivityParser.ExprContext ctx) {
    if (ctx.LPAREN() != null) {
      //special case of ( expr )
      return;
    }

    var expr = exprStack.removeLast();

    if (exprStack.isEmpty()) {
      if (statStack.getLast() instanceof If ifStat) {
        ifStat.cond = expr;
      } else if (statStack.getLast() instanceof Select select) {
        select.conditions.set(select.conditions.size() - 1, expr);
      } else {
        System.out.println("Parse error for condition: "+statStack.getLast());
      }

    } else if (exprStack.getLast() instanceof NotExpr parent) {
      parent.innerExpr = expr;

    } else if (exprStack.getLast() instanceof BinaryExpr parent) {
      //try to flatten first
      if (expr instanceof BinaryExpr binExpr && parent.kind == binExpr.kind) {
        parent.list().addAll(binExpr.list());
      } else {
        parent.list().add(expr);
      }

    } else {
      System.out.println("Illegal expr on top of expression stack: " + expr);
    }

  }

  @Override
  public void enterExprstat(ActivityParser.ExprstatContext ctx) {
    var select = (Select)statStack.getLast();
    select.conditions.add(null);
  }

  @Override
  public void enterStat(ActivityParser.StatContext ctx) {
    super.enterStat(ctx);
    if (ctx.ID() != null) { //terminal
      var stat = new Id(ctx.ID().getText());
      containerOfStatStack.getLast().add(stat);

    } else if (ctx.IF() != null) {
      var stat = new If();
      containerOfStatStack.getLast().add(stat);

      statStack.add(stat);
      containerOfStatStack.add(stat.ifBlock);

    } else { //Complex blocks
      ComplexStat stat;
      if (ctx.REPEAT() != null) {
        var count = Integer.parseInt(ctx.INT().getText());
        stat = new Repeat(count);

      } else if (ctx.SCHEDULE() != null) {
        stat = new Schedule();

      } else if (ctx.SEQUENCE() != null) {
        stat = new Sequence();

      }
      else if (ctx.SELECT() != null) {
        stat = new Select();

      } else if (ctx.RANDOM() != null) {
        var count = ctx.INT() != null ? Integer.parseInt(ctx.INT().getText()) : 1;
        stat = new Random(count);
      }

      else {
        stat = new UnknownStat();
      }

      containerOfStatStack.getLast().add(stat);

      statStack.add(stat);
      containerOfStatStack.add(stat.block());
    }

  }



  @Override
  public void exitStat(ActivityParser.StatContext ctx) {
    super.exitStat(ctx);
    if (ctx.ID() == null) { //when not terminal
      statStack.removeLast();
      containerOfStatStack.removeLast();
    }
  }



  @Override
  public void visitTerminal(TerminalNode node) {
    int type = node.getSymbol().getType();
    int index = node.getSymbol().getTokenIndex();

    if (type >= ActivityLexer.ACTIVITY && type <= ActivityLexer.ELSE) {
      tokenTypes[index] = ParserConstants.TokenTypes.KEYWORD;

      if (type == ActivityLexer.DEFAULT) {
        var select = (Select)statStack.getLast();

        var or = new BinaryExpr(ExprKind.Or);
        var defaultExpr = new NotExpr("default");
        defaultExpr.innerExpr = or;
        for (var cond: select.conditions) {
          if (cond != null)
            or.list().add(cond);
        }

        //default is possible only when previous branches are with conditions
        if (or.list().size() > 0 && or.list().size() == select.conditions.size()) {
          select.conditions.add(defaultExpr);
        } else {
          select.conditions.add(null);
          tokenTypes[index] = ParserConstants.TokenTypes.ERROR;
        }
      }

    } else if (type == ActivityLexer.INT) {
      tokenTypes[index] = ParserConstants.TokenTypes.NUMERIC;

    } else if (type == ActivityLexer.SEMI) {
      tokenTypes[index] = ParserConstants.TokenTypes.SEMI;

    } else if (type == ActivityLexer.ID) {
      var id = node.getText();
      var pos = Pos.fromNode(node);
      if (def.containsKey(id)) {
        usageToDef.put(pos, def.get(id));
        tokenTypes[index] = ParserConstants.TokenTypes.FIELD;
      } else if (exprStack.isEmpty()) {
        tokenTypes[index] = ParserConstants.TokenTypes.FIELD;
        def.put(id, pos);
      } else {
        tokenTypes[index] = ParserConstants.TokenTypes.ERROR;
      }
    }

    if (!exprStack.isEmpty())
      tokenStyles[index] = ParserConstants.TokenStyles.ITALIC;

    //AST building logic
    if (type == ActivityLexer.ELSE) {
      var lastIf = (If) statStack.getLast();
      containerOfStatStack.removeLast();
      containerOfStatStack.add(lastIf.elseBlock);
    }
  }
}