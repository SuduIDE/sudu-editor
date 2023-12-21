// Generated from parser-generator\src\main\resources\grammar\cpp\help\CPP14Directive.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.cpp.gen.help;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CPP14DirectiveParser}.
 */
public interface CPP14DirectiveListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CPP14DirectiveParser#directive}.
	 * @param ctx the parse tree
	 */
	void enterDirective(CPP14DirectiveParser.DirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14DirectiveParser#directive}.
	 * @param ctx the parse tree
	 */
	void exitDirective(CPP14DirectiveParser.DirectiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link CPP14DirectiveParser#include}.
	 * @param ctx the parse tree
	 */
	void enterInclude(CPP14DirectiveParser.IncludeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14DirectiveParser#include}.
	 * @param ctx the parse tree
	 */
	void exitInclude(CPP14DirectiveParser.IncludeContext ctx);

	/**
	 * Enter a parse tree produced by {@link CPP14DirectiveParser#error}.
	 * @param ctx the parse tree
	 */
	void enterError(CPP14DirectiveParser.ErrorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14DirectiveParser#error}.
	 * @param ctx the parse tree
	 */
	void exitError(CPP14DirectiveParser.ErrorContext ctx);

	/**
	 * Enter a parse tree produced by {@link CPP14DirectiveParser#dir}.
	 * @param ctx the parse tree
	 */
	void enterDir(CPP14DirectiveParser.DirContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14DirectiveParser#dir}.
	 * @param ctx the parse tree
	 */
	void exitDir(CPP14DirectiveParser.DirContext ctx);

	/**
	 * Enter a parse tree produced by {@link CPP14DirectiveParser#other}.
	 * @param ctx the parse tree
	 */
	void enterOther(CPP14DirectiveParser.OtherContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14DirectiveParser#other}.
	 * @param ctx the parse tree
	 */
	void exitOther(CPP14DirectiveParser.OtherContext ctx);
}