import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.sudu.experiments.parser.java.gen.st.JavaStructureLexer;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestParsing {

  public static void main(String[] args) throws IOException {
    testFile("parser/src/main/resources/grammar/java/examples/EUC_TW_OLD.java");
    testFile("parser/src/main/resources/grammar/java/examples/test.java");
    testFile("parser/src/main/resources/grammar/java/examples/JavaParser.java");
    testFile("parser/src/main/resources/grammar/java/examples/AllInOne7.java");
    testFile("parser/src/main/resources/grammar/java/examples/AllInOne8.java");
    testFile("parser/src/main/resources/grammar/java/examples/AllInOne11.java");
    testFile("parser/src/main/resources/grammar/java/examples/AllInOne17.java");
    testFile("parser/src/main/resources/grammar/java/examples/Escapes.java");
    testFile("parser/src/main/resources/grammar/java/examples/GenericConstructor.java");
    testFile("parser/src/main/resources/grammar/java/examples/LocalVariableDeclaration.java");
    testFile("parser/src/main/resources/grammar/java/examples/ManyStringsConcat.java");
    testFile("parser/src/main/resources/grammar/java/examples/RecordsTesting.java");
    testFile("parser/src/main/resources/grammar/java/examples/SwitchExpression.java");
  }

  static void testFile(String name) throws IOException {
    System.out.println("TESTING " + name);
    String code = Files.readString(Path.of(name));
    CharStream stream = CharStreams.fromString(code);
    long parseTimeStart = System.currentTimeMillis();

    Lexer lexer = new JavaStructureLexer(stream);
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();
    var list = tokenStream.getTokens();

    JavaStructureParser parser = new JavaStructureParser(tokenStream);
    var ctx = parser.compilationUnit();

    System.out.println("Parsed in " + (System.currentTimeMillis() - parseTimeStart) + "ms");
    System.out.println("___________________________");
  }

}
