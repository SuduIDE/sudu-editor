# Line Numbers Refactoring Plan

## Goals and Intentions

1. **Unified diff view**: Support rendering where lines from two documents can interlace (unified diff style), rather than side-by-side split.
2. **Inline rendering**: Support showing related lines inline (e.g., disassembly generated for a code line) attached below/associated with code lines without creating separate editor panes.
3. **Move line numbers out of separate gutter component**: Render line numbers as part of the code line rendering. Remove dependence on a separate LineNumbersComponent for drawing.
4. **Make line numbers optional**: Some lines (inline disassembly/attached lines) should not show line numbers.
5. **Preserve performance**: Use the same rendering approach/resources where sensible (same font). Draw numbers efficiently.
6. **Preserve selection/copy behavior**: Allow selecting across all lines for copy; constrain editing appropriately.

## Current State Summary

- **CodeLine**: Represents a single document line split into CodeElements. No line number/flags. Pure model.
- **CodeLineRenderer**: Renders a CodeLine to texture tiles, handles selection/highlights/diffs, horizontal scrolling. No line numbers.
- **LineNumbersComponent + LineNumbersTexture**: Separate gutter renderer (tiled textures in batches of 20), does hit testing, cursor changes, diff backgrounds, caret highlighting. Drawn in separate pass by EditorComponent.
- **EditorComponent**: Uses docToView (CodeLineMapping) to map view lines to document lines. Renderer pool recycles by content identity. Layout has separate gutter width.

## Decisions


1) Line numbers rendering: separate draw call in gutter area (not baked into text textures, not treated as CodeElement). Preserves existing horizontal scroll texture logic and avoids complicating editing/selection logic.

2) CodeLine model: add
- public boolean hasLineNumber = true (default) - disasm/attached lines set false
- public boolean editable = true (default) - disasm lines set false
- public int lineNumber - display number when hasLineNumber is true

3) Rendering: CodeLineRenderer draws number in left gutter via separate draw call using same font. No margin baked into horizontal texture width; gutter fixed. Pass leftMarginWidth.

4) Selection/editing: allow selection across all rendered lines (for copy). Block editing if caret on non-editable or selection spans non-editable.

5) Inline disasm: as other CodeLines in same Document with hasLineNumber=false, editable=false. View order/compact mapping can include them.

### Java<->JS API
- On JS side, represent 'no line number' as absence/undefined. On Java side use boolean flags## Answers to Remaining Questions (from discussion)

8. Disasm ordering: "just separate code lines that exist in the document" - Confirmed. Disasm lines added as CodeLines in the same Document; view order is document order as presented through docToView/compact mapping. They have hasLineNumber=false, editable=false.

9. Caret in gutter: Preserve current behavior - draw caret indicator in gutter for caret line (when it has line number). CodeLineRenderer needs to draw caret highlight in gutter area.

10. Sync points: Sync point UI lives in gutter. With numbers moving into renderer, better to have renderer draw sync point markers in gutter area when applicable (pass sync point state). Avoids separate component.

11. Max width: leftMarginWidth = max lineNumber (for lines with hasLineNumber true) across relevant scope. Recompute when document length/numbers change (affects textBaseX); integrate with checkLineNumbersLayout.

12. View vs model numbers: Display numbers depend on view (folding/compact). Storing in model couples to view. Better to pass display number per view line to renderer, or compute from view context. Alternatively, set lineNumber appropriately when building view sequence. Need to clarify - but key is hasLineNumber controls visibility.
