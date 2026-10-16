package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.ui.colors.EditorColorScheme;
import org.sudu.experiments.editor.worker.parser.ParseStatus;
import org.sudu.experiments.editor.worker.parser.ParserUtils;
import org.sudu.experiments.math.Color;
import org.sudu.experiments.parser.ParserConstants;
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

  @Test void documentTokensColorAttachedLine() {
    Document d = Document.fromDescriptors(
        new String[]{"mov", "src", "ret"},
        new int[]{0, 1, 0});
    Model model = new Model(d, "cpp", null);
    CodeLineColorScheme scheme = EditorColorScheme.darculaIdeaColorScheme().editorCodeLineScheme();
    model.setEditor(new EditorStub(scheme), null);
    model.setDocumentTokens(new SemanticTokenInfo[]{
        new SemanticTokenInfo(0, 0, ParserConstants.TokenTypes.DEFAULT, 0, "#ff0000", null, "mov"),
        new SemanticTokenInfo(2, 0, ParserConstants.TokenTypes.DEFAULT, 0, "#00ff00", null, "ret")
    });
    int c0 = d.lines[0].get(0).color;
    int c2 = d.lines[2].get(0).color;
    assertTrue(ParserConstants.TokenTypes.isSemanticToken(c0));
    assertTrue(ParserConstants.TokenTypes.isSemanticToken(c2));
    // the document dictionary colors are resolved through the editor color scheme
    assertEquals(new Color("#ff0000"), scheme.elementColor(c0).colorF);
    assertEquals(new Color("#00ff00"), scheme.elementColor(c2).colorF);
    // the source line in between is untouched
    assertEquals(ParserConstants.TokenTypes.DEFAULT, d.lines[1].get(0).color);
  }

  @Test void documentTokensApplyWhenEditorAttachedLater() {
    Document d = Document.fromDescriptors(new String[]{"mov"}, new int[]{0});
    Model model = new Model(d, "cpp", null);
    model.setDocumentTokens(new SemanticTokenInfo[]{
        new SemanticTokenInfo(0, 0, ParserConstants.TokenTypes.DEFAULT, 0, "#ff0000", null, "mov")
    });
    // no editor yet: colors are deferred
    assertEquals(ParserConstants.TokenTypes.DEFAULT, d.lines[0].get(0).color);
    CodeLineColorScheme scheme = EditorColorScheme.darculaIdeaColorScheme().editorCodeLineScheme();
    model.setEditor(new EditorStub(scheme), null);
    int c0 = d.lines[0].get(0).color;
    assertTrue(ParserConstants.TokenTypes.isSemanticToken(c0));
    assertEquals(new Color("#ff0000"), scheme.elementColor(c0).colorF);
  }

  @Test void reapplyDocumentTokensResolvesAgainstCurrentScheme() {
    Document d = Document.fromDescriptors(new String[]{"mov"}, new int[]{0});
    Model model = new Model(d, "cpp", null);
    CodeLineColorScheme first = EditorColorScheme.darculaIdeaColorScheme().editorCodeLineScheme();
    EditorStub stub = new EditorStub(first);
    model.setEditor(stub, null);
    model.setDocumentTokens(new SemanticTokenInfo[]{
        new SemanticTokenInfo(0, 0, ParserConstants.TokenTypes.DEFAULT, 0, "#ff0000", null, "mov")
    });
    assertEquals(new Color("#ff0000"), first.elementColor(d.lines[0].get(0).color).colorF);

    // a theme change swaps the scheme; re-applying must resolve against the new one
    CodeLineColorScheme second = EditorColorScheme.darculaIdeaColorScheme().editorCodeLineScheme();
    stub.scheme = second;
    model.reapplyDocumentTokens();
    assertEquals(new Color("#ff0000"), second.elementColor(d.lines[0].get(0).color).colorF);
  }

  static final class EditorStub implements Model.EditorToModel {
    CodeLineColorScheme scheme;

    EditorStub(CodeLineColorScheme scheme) {
      this.scheme = scheme;
    }

    public void useDocumentHighlightProvider(int line, int column) {}
    public void fireFileLexed() {}
    public void fireFileIterativeParsed(int start, int stop) {}
    public void updateModelOnDiff(Diff diff, boolean isUndo) {}
    public void onDiffMade() {}
    public boolean isDisableParser() { return true; }
    public CodeLineColorScheme getColorScheme() { return scheme; }
  }
}
