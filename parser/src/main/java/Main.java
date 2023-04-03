import org.antlr.v4.runtime.*;
import org.sudu.experiments.parser.java.gen.st.JavaStructureLexer;
import org.sudu.experiments.parser.java.gen.st.JavaStructureParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

  public static String code;

  static {
    try {
      code = Files.readString(Path.of("parser/src/main/resources/grammar/java/test.java"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    CharStream stream = CharStreams.fromString(code);
    long parseTimeStart = System.currentTimeMillis();

    Lexer lexer = new JavaStructureLexer(stream);
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();
    var list = tokenStream.getTokens();

    JavaStructureParser parser = new JavaStructureParser(tokenStream);
    var ctx = parser.compilationUnit();

    System.out.println("Parsed in " + (System.currentTimeMillis() - parseTimeStart) + "ms");
  }

  static String makeText(ParserRuleContext ctx) {
    int start = ctx.start.getStartIndex();
    int stop = ctx.stop.getStopIndex();
    return code.substring(start, stop + 1);
  }

}
