import org.antlr.v4.Tool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateParsers {

  private static final String baseGenPath = "parser/src/main/java/org/sudu/experiments/parser/";
  private static final String basePackageName = "org.sudu.experiments.parser.";
  private static final Path grammarPath = Path.of("parser-generator/src/main/resources/grammar/");

  private static final boolean generateActivity = false;

  public static void main(String[] args) {
    walk(grammarPath);
  }

  public static void walk(Path currentPath) {
    try (var stream = Files.newDirectoryStream(currentPath)) {
      List<String> grammars = new ArrayList<>();
      for (var subPath: stream) {
        if (Files.isDirectory(subPath)) walk(subPath);
        else grammars.add(subPath.toString());
      }
      if (!grammars.isEmpty()) {
        var path = grammarPath.relativize(currentPath) + "/";
        path = path.replaceFirst("[/\\\\]", "/gen/");
        var packageName = basePackageName + path.replaceAll("[/\\\\]", ".");
        if (packageName.endsWith(".")) packageName = packageName.substring(0, packageName.length() - 1);
        generate(
            baseGenPath + path,
            packageName,
            grammars.toArray(String[]::new)
        );
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void generate(String outputDir, String packageName, String[] grammarPaths) {
    if (!generateActivity && Arrays.stream(grammarPaths).anyMatch(path ->
        Path.of(path).getFileName().toString().startsWith("Activity")
    )) return;
    String[] args = new String[5 + grammarPaths.length];
    args[0] = "-o";
    args[1] = outputDir;
    args[2] = "-package";
    args[3] = packageName;
    args[4] = "-Xexact-output-dir";
    System.arraycopy(grammarPaths, 0, args, 5, grammarPaths.length);
    new Tool(args).processGrammarsOnCommandLine();
    System.out.println("Processed " + Arrays.toString(grammarPaths));
  }
}
