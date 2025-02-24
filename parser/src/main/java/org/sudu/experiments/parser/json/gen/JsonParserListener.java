// Generated from parser-generator/src/main/resources/grammar/json/JsonParser.g4 by ANTLR 4.13.1
package org.sudu.experiments.parser.json.gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JsonParser}.
 */
public interface JsonParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JsonParser#json}.
	 * @param ctx the parse tree
	 */
	void enterJson(JsonParser.JsonContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsonParser#json}.
	 * @param ctx the parse tree
	 */
	void exitJson(JsonParser.JsonContext ctx);

	/**
	 * Enter a parse tree produced by {@link JsonParser#obj}.
	 * @param ctx the parse tree
	 */
	void enterObj(JsonParser.ObjContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsonParser#obj}.
	 * @param ctx the parse tree
	 */
	void exitObj(JsonParser.ObjContext ctx);

	/**
	 * Enter a parse tree produced by {@link JsonParser#pair}.
	 * @param ctx the parse tree
	 */
	void enterPair(JsonParser.PairContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsonParser#pair}.
	 * @param ctx the parse tree
	 */
	void exitPair(JsonParser.PairContext ctx);

	/**
	 * Enter a parse tree produced by {@link JsonParser#arr}.
	 * @param ctx the parse tree
	 */
	void enterArr(JsonParser.ArrContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsonParser#arr}.
	 * @param ctx the parse tree
	 */
	void exitArr(JsonParser.ArrContext ctx);

	/**
	 * Enter a parse tree produced by {@link JsonParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(JsonParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link JsonParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(JsonParser.ValueContext ctx);
}