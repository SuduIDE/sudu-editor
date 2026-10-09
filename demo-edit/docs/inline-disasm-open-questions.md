# Inline Disassembly - Open Questions

Open (unanswered) questions for integrating inline disassembly (and composing it with unified diff and per-line numbering) into the pipeline described in [text-tokenization-pipeline.md](text-tokenization-pipeline.md).

Status: **open**. Each item lists context, the options, and why it matters. Decisions, once made, should move to the relevant decisions doc (e.g. [line-numbers-refactoring-QA.md](line-numbers-refactoring-QA.md)).

---

## A. Model and ownership

### 1. Where do disassembly lines live?
**Context:** Earlier decision says disassembly lines are `CodeLine`s in the same `Document` (so view order is document order, and compact mapping includes them).
**Options:**
- (a) Same `Document.lines`, interleaved with source lines.
- (b) A distinct "attached lines" layer owned by the view/editor, rendered interleaved but **not** part of the parser document.
**Why it matters:** This single choice drives every question below. Option (a) keeps one coordinate space but poisons `getChars()` and line indexing for the parser; option (b) keeps the parser document clean but requires a view/interleave layer and a mapping to positions.

### 2. Who produces disassembly lines, and when?
**Context:** Nothing in the current model generates disassembly. It would come from an external/debug source (async).
**Open:** What is the producer (worker job? debugger service?), is it per-file/per-line, is it lazy (on scroll/expand), and how are stale/invalidated results handled? How does it interact with `document.currentVersion`?

### 3. Lifetime and anchoring of disassembly lines
**Context:** If a source line is edited/deleted, or lines are inserted/removed, what happens to its disassembly lines?
**Options:** (a) anchored to a source line (move/delete with it); (b) independent, pinned at a position; (c) invalidated/recomputed on any edit.
**Why it matters:** Affects edits, incremental reparse intervals, and mapping.

---

## B. Parser input and coordinate mapping

### 4. How is source extracted for the parser?
**Context:** `Model` sends `document.getChars()` to the worker. With disassembly lines present, this must be source-only.
**Options:**
- (a) Maintain a parallel **source-only** buffer (mirrors source lines; disassembly excluded).
- (b) Keep one array and build a filtered char[] on demand.
- (c) Pass a source-line mask to the worker (worker skips disassembly lines).
**Why it matters:** Determines the worker contract and how much the parser must know about disassembly (ideally nothing).

### 5. Line index mapping (parser <-> document)
**Context:** The parser returns `N` **source** lines; `ParserUtils.updateDocument` replaces `document.lines[0..N)`.
**Open:** Where does the bidirectional map `sourceLine <-> documentLine` live, and how is it maintained on edit/insert/delete? Is it a simple index map, or is document indexing redefined so disassembly lines are "attached" to a source line index rather than occupying a top-level index?

### 6. Shared coordinate space for Pos/tree/scope/usages
**Context:** `usageToDef`/`defToUsages`, `IntervalTree`, `scopeGraph`, and viewport/reparse intervals all use document `(line, char)` coordinates.
**Options:**
- (a) Keep these in **source space**, translate to document space only when rendering/hit-testing.
- (b) Make the document index **source-only**, and attach disassembly lines to source indices (so the parser coordinate space is the real index space).
- (c) Store disassembly lines with offset/fractional indices.
**Why it matters:** This is the most invasive question. Option (a) is least intrusive to the parser but adds translation in many render/hit-test paths; option (b) aligns with the earlier clarification that "compaction models work with real indices".

### 7. Applying parse results without clobbering disassembly
**Context:** `ParserUtils.updateDocument` / `updateDocumentInterval` / `setLine(i, ..., success)` assume line `i` is a source line.
**Open:** How are these adapted so disassembly lines are skipped/merged? Does `saveOldLines` semantics change? Are the `left`/`right` splices in `updateDocumentInterval` still valid when disassembly lines sit on the boundary?

### 8. Viewport parsing with disassembly present
**Context:** `Model.parseViewport` computes `document.getLineStartInd(firstLine)` / `getVpEnd(lastLine)` / `getIntervals()` from visible document lines.
**Open:** How do we translate a visible range that includes disassembly lines into a source-only viewport? Do we widen/narrow the range, and how do we avoid re-parsing unchanged source ranges?

### 9. Incremental reparse vs disassembly insertion
**Context:** `Model.iterativeParsing` uses `document.tree.getReparseNode()` and sends the interval plus `document.getChars()`.
**Open:** How are reparse intervals expressed (source or document space)? Do inserting/removing disassembly lines invalidate the tree, and if so, can we avoid a reparse for a purely view-level change?

---

## C. Versioning, diff, and editing

### 10. Versioning: which changes invalidate the parse?
**Context:** `parseRes.version` must equal `document.currentVersion` to be applied.
**Open:** Should adding/removing/folding disassembly lines (view-level) bump `document.currentVersion`? Expected answer: no - view-level changes should not force a reparse. Need to confirm and implement.

### 11. Diff, sync, and copy must exclude disassembly
**Context:** `updateModelOnDiff`, `CpxDiff`, `syncEditing`, `Document.getChars()`/`getFullLength`, and copy operate over "the document".
**Open:** How do we guarantee disassembly lines are excluded from source diff/sync/copy while still being visible and selectable? What is the "copy" contract - copy source only, or include disassembly? (Selection across all lines is allowed; what is copied is the question.)

### 12. Undo/redo
**Context:** `UndoBuffer` records edits over the document.
**Open:** Do disassembly insertions participate in undo? Expected: no - they are derived/view data, not user edits. Confirm.

---

## D. Rendering and numbering

### 13. Styling of disassembly `CodeElement`s
**Open:** Which colors/styles do disassembly elements use (a dedicated scheme, not `ParserConstants.TokenTypes`)? Does `setSemanticTokens` ever touch them? How do they look in diff mode?

### 14. Line numbers for disassembly
**Decided:** disassembly lines have `hasLineNumber == false`; non-integer labels (e.g. addresses) go into `CodeElement` content, not the gutter (see [line-numbers-refactoring-QA.md](line-numbers-refactoring-QA.md) Q5/Q12/Q13).
**Open:** Is an address column actually desired? If so, is it `CodeElement` content only, or a second gutter lane? (Current decision says content.)

### 15. Numbering source lines when disassembly is present
**Context:** Display numbers are arbitrary integers from `CodeLine.lineNumber` (per-line, not position-derived).
**Open:** When disassembly lines are interleaved, source lines must keep their **source** numbers (e.g. `{1,-,-,2,3,-,4}`). Who assigns `lineNumber` - the parse pipeline, the view builder, or the disassembly producer? How is it kept consistent across edits/folding?

### 16. Interaction with compact view / folding
**Context:** `CompactCodeMapping` uses real document indices.
**Open:** Should folding a source line fold its attached disassembly lines too? How do the compact mappings and number provider compose when disassembly is present?

---

## E. Unified diff composition

### 17. Unified diff + inline disassembly
**Context:** Unified diff interleaves lines from two documents; inline disassembly attaches to lines within one document.
**Open:** How do these compose - can a diff line also have inline disassembly? Which document's disassembly is shown? How is numbering resolved when both sides contribute numbers?

### 18. Two-pane diff vs unified view
**Context:** Today two-pane diff uses `lineNumbers1`/`lineNumbers2` and separate `LineNumbersComponent`s; unified (single-pane) is a later step.
**Open:** Is unified diff a new view over two `Document`s, and does it reuse the parser pipeline at all, or is it purely a view-level interleave of already-tokenized documents?

---

## F. Summary of the critical path

Most other questions depend on **Q1 (where disassembly lives)** and **Q6 (coordinate space)**:
- Choose Q1 first.
- If disassembly is in the same `Document`, Q4-Q9 (source extraction, mapping, coordinate space, result application, viewport/reparse) must all be solved.
- If disassembly is a separate layer, the parser pipeline stays mostly untouched and the work concentrates in the view/interleave and numbering layers.
