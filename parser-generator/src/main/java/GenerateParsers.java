import org.antlr.v4.Tool;

public class GenerateParsers {

  private static final String baseGenPath = "parser/src/main/java/org/sudu/experiments/parser/";
  private static final String basePackageName = "org.sudu.experiments.parser";
  private static final String baseGrammarPath = "parser-generator/src/main/resources/grammar/";

  private static final String javaGenPath = baseGenPath + "java/gen/";
  private static final String javaPackagePath = basePackageName + ".java.gen";
  private static final String javaGrammarPath = baseGrammarPath + "java/";

  private static final String javaStGenPath = baseGenPath + "java/gen/st/";
  private static final String javaStPackagePath = basePackageName + ".java.gen.st";

  private static final String javaHelpGenPath = baseGenPath + "java/gen/help/";
  private static final String javaHelpPackagePath = basePackageName + ".java.gen.help";

  private static final String cppGenPath = baseGenPath + "cpp/gen/";
  private static final String cppPackagePath = basePackageName + ".cpp.gen";
  private static final String cppGrammarPath = baseGrammarPath + "cpp/";

  private static final String cppHelpGenPath = baseGenPath + "cpp/gen/help/";
  private static final String cppHelpPackagePath = basePackageName + ".cpp.gen.help";

  private static final String jsGenPath = baseGenPath + "javascript/gen/";
  private static final String jsPackagePath = basePackageName + ".javascript.gen";
  private static final String jsGrammarPath = baseGrammarPath + "javascript/";

  private static final String activityPath = baseGenPath + "activity/gen/";
  private static final String activityPackagePath = basePackageName + ".activity.gen";
  private static final String activityGrammarPath = baseGrammarPath + "activity/";


  private static final String lightJsGenPath = baseGenPath + "javascript/gen/light/";
  private static final String lightJsPackagePath = basePackageName + ".javascript.gen.light";



  public static void main(String[] args) {
    new Tool(new String[]{
        "-o",
        javaGenPath,
        "-package",
        javaPackagePath,
        javaGrammarPath + "JavaLexer.g4",
        javaGrammarPath + "JavaParser.g4"
    }).processGrammarsOnCommandLine();

    new Tool(new String[]{
        "-o",
        javaStGenPath,
        "-package",
        javaStPackagePath,
        javaGrammarPath + "JavaStructureLexer.g4",
        javaGrammarPath + "JavaStructureParser.g4"
    }).processGrammarsOnCommandLine();

    new Tool(new String[]{
        "-o",
        javaHelpGenPath,
        "-package",
        javaHelpPackagePath,
        javaGrammarPath + "JavaStringSplitter.g4"
    }).processGrammarsOnCommandLine();

    new Tool(new String[]{
        "-o",
        cppGenPath,
        "-package",
        cppPackagePath,
        cppGrammarPath + "CPP14Lexer.g4",
        cppGrammarPath + "CPP14Parser.g4"
    }).processGrammarsOnCommandLine();

    new Tool(new String[]{
        "-o",
        cppHelpGenPath,
        "-package",
        cppHelpPackagePath,
        cppGrammarPath + "CPP14Directive.g4"
    }).processGrammarsOnCommandLine();

    new Tool(new String[]{
        "-o",
        jsGenPath,
        "-package",
        jsPackagePath,
        jsGrammarPath + "JavaScriptLexer.g4",
        jsGrammarPath + "JavaScriptParser.g4"
    }).processGrammarsOnCommandLine();


    new Tool(new String[]{
            "-o",
            activityPath,
            "-package",
            activityPackagePath,
            activityGrammarPath + "ActivityLexer.g4",
            activityGrammarPath + "ActivityParser.g4"
    }).processGrammarsOnCommandLine();
    
    
    new Tool(new String[]{
        "-o",
        lightJsGenPath,
        "-package",
        lightJsPackagePath,
        jsGrammarPath + "LightJavaScriptLexer.g4",
    }).processGrammarsOnCommandLine();

  }

}
