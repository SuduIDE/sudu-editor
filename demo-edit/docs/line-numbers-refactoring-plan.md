# Line Numbers Refactoring Plan

> **Note:** This document holds only the goals, intentions, and current-state summary. All decisions live in [line-numbers-refactoring-QA.md](line-numbers-refactoring-QA.md), which is the single source of truth. See the topic index under [Decisions](#decisions).

> **Status (v1):** the immediate scope — goals **3-6** (per-line numbers + inline rendering) — is **implemented and working**. **Goal 1 (unified diff) is not yet implemented**; it remains a later, separate view-level step. See the implementation notes in [inline-disasm-open-questions.md](inline-disasm-open-questions.md).

## Goals and Intentions

1. **Unified diff view**: Support rendering where lines from two documents can interlace (unified diff style), rather than side-by-side split.
2. **Inline rendering**: Support showing related lines inline (e.g., disassembly generated for a code line) attached below/associated with code lines without creating separate editor panes.
3. **Put line numbers in the model (model-first scope)**: Store per-line number state on `CodeLine` (`lineNumber`) and drive the existing gutter with a number-addressed lookup. Moving the gutter drawing pass out of `LineNumbersComponent` into `CodeLineRenderer` is a later, deferred step.
4. **Make line numbers optional**: Some lines (inline disassembly/attached lines) should not show line numbers.
5. **Preserve performance**: Use the same rendering approach/resources where sensible (same font). Draw numbers efficiently.
6. **Preserve selection/copy behavior**: Allow selecting across all lines for copy; constrain editing appropriately.

> **Sequencing (read this first):** These goals are **sequenced, not simultaneous deliverables** for this refactoring. The immediate scope is goals **3-6** (per-line numbers + inline rendering) — **now implemented** (see Status above). **Unified diff (goal 1) is a later, separate view-level step** and — per [inline-disasm-open-questions.md](inline-disasm-open-questions.md) Q17-Q18 — **does not compose with inline disassembly in v1**. Goal 2 (inline rendering) and goal 1 (unified diff) are orthogonal line structures, not one combined feature.

## Current State Summary

Status: the immediate scope (goals **3-6**: per-line numbers + inline rendering) is **implemented**; **goal 1 (unified diff) is not yet implemented**.

- **CodeLine**: Represents a single document line split into CodeElements. Now also carries the per-line display number `lineNumber` (`0` = **AUTO** in a classic document, or attached / unnumbered in a mapped one). *(implemented)*
- **CodeLineRenderer**: Renders a CodeLine to texture tiles, handles selection/highlights/diffs, horizontal scrolling. Still renders only the text area — no line numbers; the gutter stays a separate pass.
- **LineNumbersComponent + LineNumbersTexture**: Separate gutter renderer (tiled textures, integer banks of `numberOfLines == 20`), now driven by a **number-addressed lookup** (`LineNumbersTexture.drawNumberRun`); does hit testing, cursor changes, diff backgrounds, caret highlighting. Drawn in a separate pass by EditorComponent.
- **EditorComponent**: Uses docToView (CodeLineMapping) to map view lines to document lines. Renderer pool recycles by content identity. Layout has a separate gutter width, sized from the document-global max line number. Gates editing via `editsBlocked()` — a mapped document (`sourceToDoc != null`) is read-only.

## Decisions

All detailed decisions and their rationale live in
[line-numbers-refactoring-QA.md](line-numbers-refactoring-QA.md), which is the **single
source of truth** for this refactoring. Do not duplicate decision text here - it has
drifted before and produced stale guidance. This section is only a topic index into that
doc:

- **Q1-Q2** - gutter/diff background **stays in the current path** for now (moving it to `CodeLineRenderer` is deferred); sync points are **out of scope**.
- **Q3** - caret gutter highlight: drawn for the caret view line regardless of whether the line shows a number (stays in the current path).
- **Q4** - number-column width (`leftMarginWidth`) from the document-global max line number; view-independent.
- **Q5** - display numbers stored on `CodeLine`, decoupled from compact/folding indices.
- **Q6** - keep `LineNumbersComponent`/`LineNumbersTexture`; moving the drawing pass to the renderer is deferred.
- **Q7** - text positioning/padding unchanged (`leftMarginWidth` = **number-column width**; `textBaseX = leftMarginWidth + mergeWidth + vLineW + vLineTextOffset` non-mirrored, mirrored variant per `internalLayout`).
- **Q8** - editing guards: **any mapped document (`sourceToDoc != null`) is never editable** (hard mapping gate, disasm Q9); in classic documents edits are blocked on attached (non-numbered) lines or when `readonly`; copy always allowed; editability derived as `!readonly && (sourceToDoc == null) && numbered`.
- **Q9** - backward-compatible defaults (`lineNumber == 0` = **AUTO**: derive the natural ordinal as **document line index + 1**, never the view row); JS<->Java mapping.
- **Q10** - number-column bounds `[0, leftMarginWidth)`; merge buttons / vertical rule outside.
- **Q11** - texture storage: existing tile stays in `LineNumbersComponent`; shared-cache Option B is deferred.
- **Q12** - staged Step 1: number-addressed tile lookup (integer banks).
- **Q13** - `LineNumbersTexture` mechanics (coverage mask; bank/row math).
- **Q14** - background color comes from the row's `LineDiff`, not from `N` (separate inputs in number-addressed drawing).
