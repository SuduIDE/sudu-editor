package org.sudu.experiments.parser.java;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;
import org.sudu.experiments.parser.Interval;
import org.sudu.experiments.parser.common.IntervalNode;
import org.sudu.experiments.parser.common.graph.ScopeGraph;
import org.sudu.experiments.parser.common.graph.reader.ScopeGraphReader;
import org.sudu.experiments.parser.common.graph.writer.ScopeGraphWriter;
import org.sudu.experiments.parser.java.gen.JavaLexer;
import org.sudu.experiments.parser.java.gen.JavaParser;
import org.sudu.experiments.parser.java.walker.JavaScopeWalker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScopeGraphSerializationTest {

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
    scopeWalker.scopeWalker.updateTypes();

    ScopeGraph fromGraph = scopeWalker.scopeWalker.graph;
    ScopeGraphWriter scopeGraphWriter = new ScopeGraphWriter(fromGraph, scopeWalker.scopeWalker.currentNode);
    scopeGraphWriter.toInts();
    int[] ints = scopeGraphWriter.ints;
    char[] chars = scopeGraphWriter.chars;

    ScopeGraphReader reader = new ScopeGraphReader(ints, chars);
    reader.readFromInts();
    ScopeGraph toGraph = new ScopeGraph();
    toGraph.root = reader.scopeRoot;

    toGraph.resolveAll((ref, decl) -> System.out.println(ref + " |-> " + decl));
  }

  private String readFile(String filename) {
    try {
      var url = getClass().getClassLoader().getResource(filename);
      if (url == null) throw new IllegalArgumentException("Illegal resource name: " + filename);
      return Files.readString(Path.of(url.toURI()));
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

}
