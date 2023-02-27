import org.antlr.v4.Tool;

public class GenerateParsers {

  private static final String baseGenPath = "parser/src/main/java/org/sudu/experiments/parser/";
  private static final String basePackageName = "org.sudu.experiments.parser";
  private static final String baseGrammarPath = "parser/src/main/resources/grammar/";

  private static final String javaGenPath = baseGenPath + "java/gen/";
  private static final String javaPackagePath = basePackageName + ".java.gen";
  private static final String javaGrammarPath = baseGrammarPath + "java/";

  public static void main(String[] args) {
    new Tool(new String[]{
        "-o",
        javaGenPath,
        "-package",
        javaPackagePath,
        javaGrammarPath + "JavaLexer.g4",
        javaGrammarPath + "JavaParser.g4"
    }).processGrammarsOnCommandLine();
  }
}
