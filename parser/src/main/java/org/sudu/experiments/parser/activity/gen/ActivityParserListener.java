// Generated from parser-generator/src/main/resources/grammar/activity/ActivityParser.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.activity.gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ActivityParser}.
 */
public interface ActivityParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ActivityParser#activity}.
	 * @param ctx the parse tree
	 */
	void enterActivity(ActivityParser.ActivityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#activity}.
	 * @param ctx the parse tree
	 */
	void exitActivity(ActivityParser.ActivityContext ctx);

	/**
	 * Enter a parse tree produced by {@link ActivityParser#blocksemi}.
	 * @param ctx the parse tree
	 */
	void enterBlocksemi(ActivityParser.BlocksemiContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#blocksemi}.
	 * @param ctx the parse tree
	 */
	void exitBlocksemi(ActivityParser.BlocksemiContext ctx);

	/**
	 * Enter a parse tree produced by {@link ActivityParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(ActivityParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(ActivityParser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link ActivityParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(ActivityParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(ActivityParser.StatContext ctx);

	/**
	 * Enter a parse tree produced by {@link ActivityParser}.
	 * @param ctx the parse tree
	 */
	void enterExpr(ActivityParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser}.
	 * @param ctx the parse tree
	 */
	void exitExpr(ActivityParser.ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link ActivityParser#exprcomma}.
	 * @param ctx the parse tree
	 */
	void enterExprcomma(ActivityParser.ExprcommaContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#exprcomma}.
	 * @param ctx the parse tree
	 */
	void exitExprcomma(ActivityParser.ExprcommaContext ctx);

	/**
	 * Enter a parse tree produced by {@link ActivityParser#exprcons}.
	 * @param ctx the parse tree
	 */
	void enterExprcons(ActivityParser.ExprconsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#exprcons}.
	 * @param ctx the parse tree
	 */
	void exitExprcons(ActivityParser.ExprconsContext ctx);
}