// Generated from parser/src/main/resources/grammar/java/JavaStructureParser.g4 by ANTLR 4.12.0
package org.sudu.experiments.parser.java.gen.st;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JavaStructureParser}.
 */
public interface JavaStructureParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(JavaStructureParser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(JavaStructureParser.CompilationUnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(JavaStructureParser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(JavaStructureParser.PackageDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(JavaStructureParser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(JavaStructureParser.ImportDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(JavaStructureParser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(JavaStructureParser.TypeDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(JavaStructureParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(JavaStructureParser.ModifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(JavaStructureParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(JavaStructureParser.ClassDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDeclaration(JavaStructureParser.InterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDeclaration(JavaStructureParser.InterfaceDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(JavaStructureParser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(JavaStructureParser.EnumDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterRecordDeclaration(JavaStructureParser.RecordDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#recordDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitRecordDeclaration(JavaStructureParser.RecordDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeDeclaration(JavaStructureParser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeDeclaration(JavaStructureParser.AnnotationTypeDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(JavaStructureParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(JavaStructureParser.ClassBodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(JavaStructureParser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(JavaStructureParser.ClassBodyDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(JavaStructureParser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(JavaStructureParser.MemberDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(JavaStructureParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(JavaStructureParser.MethodDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#recordBody}.
	 * @param ctx the parse tree
	 */
	void enterRecordBody(JavaStructureParser.RecordBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#recordBody}.
	 * @param ctx the parse tree
	 */
	void exitRecordBody(JavaStructureParser.RecordBodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#compactConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterCompactConstructorDeclaration(JavaStructureParser.CompactConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#compactConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitCompactConstructorDeclaration(JavaStructureParser.CompactConstructorDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(JavaStructureParser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(JavaStructureParser.FieldDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(JavaStructureParser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(JavaStructureParser.MethodBodyContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#anyBlock}.
	 * @param ctx the parse tree
	 */
	void enterAnyBlock(JavaStructureParser.AnyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#anyBlock}.
	 * @param ctx the parse tree
	 */
	void exitAnyBlock(JavaStructureParser.AnyBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(JavaStructureParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(JavaStructureParser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link JavaStructureParser#anyToken}.
	 * @param ctx the parse tree
	 */
	void enterAnyToken(JavaStructureParser.AnyTokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaStructureParser#anyToken}.
	 * @param ctx the parse tree
	 */
	void exitAnyToken(JavaStructureParser.AnyTokenContext ctx);
}