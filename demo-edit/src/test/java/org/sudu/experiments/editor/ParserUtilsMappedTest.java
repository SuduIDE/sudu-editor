package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.editor.worker.parser.ParseStatus;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.parser.common.Pos;

import static org.junit.jupiter.api.Assertions.*;

public class ParserUtilsMappedTest {

  @Test void fullApplyPreservesLineNumber() {
    Document d = Document.fromDescriptors(
        new String[]{"ab", "xx", "cd"},
        new int[]{1, 0, 2});
    // N=2, K=0, L=0; two source lines "ab" / "cd"
    int[] ints = {
        2, 0, 0,
        1, 0, 2, 3, 0,
        1, 3, 5, 4, 0
    };
    char[] chars = "ab\ncd".toCharArray();
    ParserUtils.updateDocument(d, ints, chars, null, null, false);

    assertEquals(1, d.lines[0].lineNumber);
    assertEquals(0, d.lines[1].lineNumber);
    assertEquals(2, d.lines[2].lineNumber);
    assertEquals("xx", d.lines[1].makeString());
    assertEquals(3, d.lines[0].get(0).color);
    assertEquals(4, d.lines[2].get(0).color);
  }

  @Test void intervalApplyAbortsOnLineCountMismatch() {
    Document d = Document.fromDescriptors(
        new String[]{"ab", "xx", "cd"},
        new int[]{1, 0, 2});
    String before = d.makeString();
    int[] ints = {
        0, 5, 1,
        1, 0,
        1, 0, 2, 0, 0
    };
    char[] chars = "ab\ncd".toCharArray();
    ParserUtils.updateDocumentInterval(d, ints, chars, null, null);
    assertEquals(before, d.makeString());
    assertEquals(1, d.lines[0].lineNumber);
    assertEquals(2, d.lines[2].lineNumber);
  }

  @Test void semanticTokensMapToSourceLinesOnly() {
    Document d = Document.fromDescriptors(
        new String[]{"ab", "xx", "cd"},
        new int[]{1, 0, 2});
    Model model = new Model(d, "java", null);
    model.fullFileLexed = ParseStatus.PARSED;
    model.setSemanticTokens(new SemanticTokenInfo[]{
        new SemanticTokenInfo(1, 0, 7, 0, null, null, "cd"),
        new SemanticTokenInfo(5, 0, 8, 0, null, null, "nope")
    });
    assertEquals(7, d.lines[2].get(0).color);
    assertEquals(0, d.lines[1].get(0).color);
  }

  @Test void resolveDoesNotColorAttachedLine() {
    Document d = Document.fromDescriptors(
        new String[]{"ab", "xx", "cd"},
        new int[]{1, 0, 2});
    Model model = new Model(d, "java", null);
    d.onResolve(new int[]{1, 3, 1, 0, 9, 0}, false);
    assertEquals(9, d.lines[2].get(0).color);
    assertEquals(0, d.lines[1].get(0).color);
    assertNotNull(model);
    assertEquals(new Pos(0, 0), d.usageToDef.get(new Pos(1, 0)));
  }
}
