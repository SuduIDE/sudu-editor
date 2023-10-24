// Generated from parser-generator/src/main/resources/grammar/activity/ActivityParser.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.activity.gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ActivityParser}.
 */
public interface ActivityParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ActivityParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ActivityParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ActivityParser.ProgramContext ctx);

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
	 * Enter a parse tree produced by {@link ActivityParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(ActivityParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(ActivityParser.ExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link ActivityParser#exprlist}.
	 * @param ctx the parse tree
	 */
	void enterExprlist(ActivityParser.ExprlistContext ctx);
	/**
	 * Exit a parse tree produced by {@link ActivityParser#exprlist}.
	 * @param ctx the parse tree
	 */
	void exitExprlist(ActivityParser.ExprlistContext ctx);

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