package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.IntervalTree;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.node.FakeNode;
import org.sudu.experiments.parser.common.graph.node.ScopeNode;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.walker.JavaScopeWalker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScopeTest {

  @Test
  public void testFieldResolve() {
    String source = readFile("java/TestFieldResolve.java");
    var tokenStream = CharStreams.fromString(source);
    JavaLexer lexer = new JavaLexer(tokenStream);
    JavaParser parser = new JavaParser(new CommonTokenStream(lexer));
    var compUnit = parser.compilationUnit();

    var scopeWalker = new JavaScopeWalker(new IntervalNode(new Interval(0, source.length(), 0)));

    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(scopeWalker, compUnit);

    ScopeGraph graph = scopeWalker.scopeWalker.graph;
    IntervalTree tree = new IntervalTree(scopeWalker.scopeWalker.currentNode);

    graph.resolveAll((ref, decl) -> System.out.println(ref + " |-> " + decl));

    graph.root.print(0);
    tree.printIntervals(source);
    printRec(scopeWalker.scopeWalker.currentScope, source);

    String toDelete = "    int a = field;";
    int ind = source.indexOf(toDelete);
    tree.makeDeleteDiff(ind, toDelete.length());
    graph.makeDeleteDiff(ind, toDelete.length());
    source = source.replace(toDelete, "");
    System.out.println("____".repeat(20));
    printRec(scopeWalker.scopeWalker.currentScope, source);
  }

  private void printRec(ScopeNode scope, String source) {
    if (!scope.declarations.isEmpty()) System.out.println("Decls: ");
    for(var decl: scope.declarations) printRef(source, decl.decl.position);
    if (!scope.references.isEmpty()) System.out.println("Refs: ");
    for(var decl: scope.references) printRef(source, decl.ref.position);
    if (!scope.declarations.isEmpty() || !scope.references.isEmpty()) System.out.println("__".repeat(20));
    for (var child: scope.children) printRec(child, source);
  }

  private void printRef(String source, int pos) {
    int to = Math.min(pos + 20, source.length());
    System.out.println(source.substring(pos, to).replace("\n", "\\n"));
  }

  void printDeclarations(ScopeNode scope) {
    if (scope instanceof FakeNode) return;
    for (var decl: scope.declarations) System.out.println(decl);
    if (!scope.declarations.isEmpty()) System.out.println();
    for (var subScope: scope.children) printDeclarations(subScope);
  }

  private String readFile(String filename) {
    try {
      return Files.readString(Path.of("src", "test", "resources", filename))
          .replace("\r", "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
