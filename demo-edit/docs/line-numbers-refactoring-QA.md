# Line Numbers Refactoring - Q&A and Decisions

This document captures all decisions made during the discussion of the line numbers refactoring plan. It is intended to be the source of truth for implementation.

## 1) Diff backgrounds in gutter

**Question:** LineNumbersComponent draws diff backgrounds in the gutter area. If numbers move into CodeLineRenderer, should renderer also draw gutter-area diff backgrounds?

**Decision:** Yes.

- **Move responsibility to CodeLineRenderer.** All gutter rendering for a row (gutter background + line number) moves from LineNumbersComponent to CodeLineRenderer. This includes drawing the gutter diff background for that line.
- **Leverage existing parameter.** Since `CodeLineRenderer.draw()` already takes `LineDiff diff`, the renderer will draw the gutter background rectangle in `[x, x+leftMarginWidth)` with the diff-appropriate colors, and draw the line number in that same area.
- **Caret highlight included.** The gutter caret highlight for the caret line also belongs to CodeLineRenderer (see Q3).
- **Sync points exception.** Sync points drawing remains **out of scope** for CodeLineRenderer in this refactoring. Sync points rendering and its visuals stay in the current path (LineNumbersComponent/EditorComponent) for now and will be TBD later.
- **Draw order considerations.** Sync points are drawn in a separate pass. We must ensure they render on top of the gutter area without clobbering, preserving the same z-order as current behavior.
- **State required.** CodeLineRenderer needs `leftMarginWidth`, `diff`, and whether the line is the caret line. No sync point state is passed to CodeLineRenderer in this phase.

## 2) Sync points drawing and hit testing

**Question:** You agreed renderer should draw sync point markers in gutter when applicable. Define API for state; hit testing moves to EditorComponent - how to map gutter clicks to sync point actions?

**Decision:** Sync points drawing is **out of scope** for CodeLineRenderer in this refactoring. It will remain as-is for now and may be addressed later (TBD).

- **Drawing:** Sync points drawing stays as-is (LineNumbersComponent/EditorComponent path). Do not move to CodeLineRenderer in this refactoring.
- **Hit testing:** In EditorComponent, when a mouse click occurs with `x < leftMarginWidth`, treat it as a gutter click for that view line (map y to view line). If that view line has a sync point, trigger the sync point action (preserving current behavior). Otherwise, treat as a gutter click (e.g. move caret to that line) - preserve current behavior.
- **No API changes to renderer:** No sync point state needs to be passed to CodeLineRenderer in this phase.

## 3) Caret in gutter

**Question:** Draw caret highlight in gutter for caret line (when it has line number). Just background highlight or also separate caret indicator?

**Decision:** Preserve exact current visuals. The caret gutter highlight is independent of whether a line number is rendered.

- **Unified highlight.** The gutter caret highlight is a state of the line itself, not tied to whether `hasLineNumber` is true. It must be drawn for the current caret line regardless of whether the line number is shown.
- **Condition:** Draw the gutter caret background whenever the view line is the caret line: `if (isCaretLine)`. Do **not** gate this on `line.hasLineNumber`.
- **Draw order:** For each row, if `isCaretLine` is true, draw the caret highlight background in `[x, x+leftMarginWidth)` first. Then, if `line.hasLineNumber == true`, draw the line number text on top. If `line.hasLineNumber == false`, draw nothing else in that gutter area - only the caret highlight remains.
- **No visual changes.** This preserves the exact current behavior. For lines without line numbers, the gutter area of that caret row still shows the caret highlight; the text-area caret remains the primary indicator.
- **Styling:** Use the same colors, same rectangle dimensions, and same conditions as LineNumbersComponent does today.

## 4) Max width calculation & recomputation

**Question:** leftMarginWidth from max lineNumber with hasLineNumber true. Scope (whole doc vs visible)? When to recompute? Also ensure stability across compact/folding.

**Decision:** Use a view-independent, document-global max width that remains stable across view modes and scrolling.

- **Scope (full document):** Compute `leftMarginWidth` from the maximum line number among lines with `hasLineNumber == true` across the **entire document model** (full view), not just the visible range. Do not depend on compact view or folding state.
- **Stability:** This prevents horizontal text jumps when toggling compact mode, folding/unfolding, or when scrolling. Gutter width remains consistent.
- **Store in Document:** Add fields to `Document`, e.g. `int maxLineNumber` and/or `float maxLineNumberWidth` (pixel width derived from `maxLineNumber` using the same font metrics used for rendering numbers).
- **Maintain on document changes only:** Update when the document's numbered lines change. Do **not** recompute on view mode changes - reuse the cached value.
- **Update rules:**
  - If a new numbered line has `lineNumber > maxLineNumber`, incrementally bump to the new max (efficient).
  - If the line(s) that held the previous maximum are removed (or max is no longer present), recompute by scanning the entire document (fallback).
  - Account for digit count increases (e.g. 9 → 10, 99 → 100) - recomputing from `maxLineNumber` gives correct width.
- **Layout updates:** If `maxLineNumberWidth`/`leftMarginWidth` changes due to document edits, update `leftMarginWidth`, recalculate `textBaseX`, and invalidate layout using the same path as `checkLineNumbersLayout`.
- **No horizontal scroll impact:** This preserves identical behavior to the current implementation. No shifts introduced during scrolling.

## 5) Where to store display numbers

**Question:** How to model display line numbers, and how do they relate to folding/compact views?

**Decision:** Store display numbers on `CodeLine`, decoupled from the compact view model.

- **Field meaning:** `lineNumber` on `CodeLine` is the **display number to show** in the gutter. It is **not** required to equal the line's position, and it is **not** the compact view model's index.
- **Clarification on compact view (important):** The statement that "numbering is the document model index" applies **only to the compact view model's internal bookkeeping** - the compaction/folding model works with **real document indices** (not visual line numbers) for its structural mapping. This is *not* a statement about what the gutter displays. Do **not** conflate compact model indices with the gutter display number.
- **Set during view construction:** When building/refreshing the view sequence (via `docToView` or the compact view model), assign `line.lineNumber = <display number>` and `line.hasLineNumber = <true/false>` for each line, based on that line's source of truth (e.g. its logical source line number). The compact view model's indices are used only for its own mapping; the display number is independent data stored on the line.
- **Attached lines:** For attached disasm lines (separate `CodeLine`s in the same `Document`), set `line.hasLineNumber = false` (and typically `line.editable = false`). They do not show numbers.
- **Flags remain independent:** `hasLineNumber` controls visibility; `editable` controls editing. The value of `lineNumber` is only meaningful when `hasLineNumber == true`.
- **Rationale:** Decoupling the display number from position and from compact/folding indices is what allows **per-line numbers that need not match the view slot** (see Q12) - e.g. imported/renumbered source lines. (Non-integer labels such as hex addresses are not gutter numbers; they belong in `CodeElement` content.)

## 6) Migration/cleanup

**Question:** Keep or remove LineNumbersComponent/LineNumbersTexture after moving to renderer?

**Decision:** Use a phased migration approach.

- **Phase 1 (Model + Renderer):** Add flags to `CodeLine` (`hasLineNumber`, `editable`, `lineNumber`) with sensible defaults. Extend `CodeLineRenderer` to draw gutter background, line number, and caret highlight in the gutter area (driven by new parameters: `leftMarginWidth`, `diff`, `isCaretLine`). Sync points drawing is not added to the renderer (remains separate, TBD).
- **Phase 2 (Editor integration):** Update `EditorComponent` to compute `leftMarginWidth` from the document's global max line number (with `hasLineNumber == true`), pass required state to renderers per view line, and stop calling `LineNumbersComponent.draw*` for the row's number/background/caret rendering that has moved. Keep hit testing updated for gutter clicks (including sync points as today). Sync points rendering remains unchanged for now.
- **Phase 3 (Verification & cleanup):** Verify visual parity and behavior against the current implementation. Once stable and correct, deprecate/remove `LineNumbersComponent` and all its unused references as appropriate. **Note:** `LineNumbersTexture` is **kept** (reused as the tile cache for CodeLineRenderer - see Q11); only the component that owned the separate drawing pass goes away. Do not remove until fully verified. Sync points handling may be revisited later (TBD).

**Rationale:** Low risk, allows easy comparison and rollback during transition.

## 7) Text positioning & padding

**Question:** Confirm leftMarginWidth is gutter width; textBaseX unchanged; hit testing same.

**Decision:** Yes - preserve existing layout math exactly.

- **Gutter width:** `leftMarginWidth` is the fixed width (pixels) of the left gutter area.
- **Text start:** `textBaseX = leftMarginWidth + horizontalPadding` (unchanged).
- **Horizontal texture slicing:** No changes to horizontal texture slicing or horizontal scroll logic.
- **Hit testing:** If `x < leftMarginWidth`, treat as gutter area/click. Otherwise map to text positions by subtracting `leftMarginWidth` using the same math as today. No change to coordinate mapping for text.
- **No overlap:** Gutter drawing is restricted to horizontal range `[0, leftMarginWidth)`. Text drawing starts at `leftMarginWidth` and extends to the right edge. No overlap between gutter and text areas.
- **Clipping:** Apply same vertical row clipping as current implementation.

## 8) Editing guard details

**Question:** Block if caret on non-editable OR selection spans any non-editable. Clarify edge cases.

**Decision:** Apply conservative guards as specified.

- **Empty selection (caret):** Block all modifying operations (insert, delete, backspace, typing, cut/paste that modifies text) if the caret is on a line with `editable == false`.
- **Non-empty selection:** Block modifying operations if any view line that is fully or partially inside the selection range has `editable == false`. In other words, if the selection spans any non-editable line, edits are blocked.
- **Coverage of attached lines:** Attached disasm lines have `editable == false` (and `hasLineNumber == false`), so both cases correctly prevent editing them.
- **Copy allowed:** Selection/copy (non-modifying operations) must remain allowed across **all** lines, including non-editable lines - per the requirements.
- **Partial crossings:** Any selection that includes or crosses into a non-editable line counts as spanning a non-editable line → edits are blocked. This is the intended conservative behavior.

## 9) Backward compatibility

**Question:** Adding fields with defaults; constructors; JS->Java API mapping.

**Decision:** Ensure full backward compatibility.

- **Defaults on CodeLine:** Add fields with safe defaults:
  - `public boolean hasLineNumber = true`
  - `public boolean editable = true`
  - `public int lineNumber = 0`
- **Constructors:** Existing CodeLine construction sites continue to work without changes. Update constructors where convenient, but not required due to defaults.
- **Behavior unchanged:** Any existing code paths that do not explicitly set these flags behave identically to before the refactoring.
- **JS → Java API mapping:**
  - If the line number is absent/`undefined` in the JS payload, set `hasLineNumber = false` (and leave `lineNumber` as-is or 0).
  - If the line number is present in the JS payload, set `hasLineNumber = true` and `lineNumber = <value>`.
  - `editable` defaults to `true` on the Java side unless explicitly set by the caller. For disasm/attached lines created on the Java side, explicitly set `editable = false` (and `hasLineNumber = false`).
- **Java → JS mapping (reverse):** On JS side, represent "no line number" as absence/`undefined`. This matches the above.

## 10) Gutter area bounds

**Question:** Renderer draws in [0, leftMarginWidth) horizontally; ensure no text overlap.

**Decision:** Enforce strict separation.

- **Horizontal bounds:** Gutter drawing (background, caret highlight, line number) is restricted to `[0, leftMarginWidth)`. Text drawing starts at `leftMarginWidth`.
- **No overlap:** There is no overlap between gutter content and text content.
- **Coordinate system:** Use the same coordinate system as EditorComponent.
- **Clipping:** Apply vertical clipping per row (same as current implementation). This ensures clean rendering and no bleed between rows.

## 11) Line number texture storage (Option B - agreed)

**Question:** LineNumbersComponent/LineNumbersTexture currently own rasterized line-number textures. If line number rendering moves to CodeLineRenderer, which textures hold the rendered line-number glyph shapes, and how are they updated per frame?

**Context - options considered:**
- **Option A:** Per-renderer gutter texture (one extra `GL.Texture` per CodeLineRenderer, rasterized in `updateTexture()` when the line changes). Self-contained, but duplicates logic across renderers.
- **Option B (chosen):** Keep the existing 20-number tile texture (`LineNumbersTexture`), relocate the pool out of LineNumbersComponent into a shared cache; CodeLineRenderer draws from it.
- **Option C:** Digit glyph atlas (0-9), compose numbers per draw. Zero re-rasterization but many draw calls per frame and right-align logic written from scratch - not worth it.

**Decision:** Option B - keep `LineNumbersTexture` tile (20 numbers per tile), move ownership out of the component drawing pass.

- **Tile concept unchanged:** `LineNumbersTexture` rasterizes 20 consecutive numbers (`numberOfLines = 20`) into one texture sized `(gutterWidth, 20 * lineHeight)`, right-aligned, one row per number. Reuses tested rasterization: right-align, padding, cleartype handling.
- **Pool becomes a shared cache:** Move the tile pool (keyed by `startLine = (line / 20) * 20`, frame-based eviction, on-demand rasterization) from `LineNumbersComponent` into a shared cache - owned by `EditorComponent` or `ClrContext` (e.g. a small `LineNumberTiles` helper). It is a texture cache only, not a rendering component.
- **CodeLineRenderer draws the gutter itself:** `CodeLineRenderer.draw()` performs the whole gutter draw per row: diff background rect (`[x, x+leftMarginWidth)`, using existing `LineDiff diff` parameter) → caret highlight (if `isCaretLine`, per Q3) → line number from the tile cache (if `line.hasLineNumber`). Then the horizontal text strips offset by `leftMarginWidth`, unchanged.
- **Cache is keyed by number, not view position:** Works with non-contiguous numbers caused by interleaved disasm lines and with compact/folding views - view lines with numbers 37 and 100 simply fetch tiles `[20,40)` and `[100,120)`; lines with `hasLineNumber == false` fetch nothing.
- **Per-frame update mechanism (unchanged from today):**
  - Glyph coverage is rasterized only when a tile is missing (evicted) or font/lineHeight/cleartype changes.
  - Colors (caret highlight, diff background, selection) are applied at draw time via `g.drawText(...)`: the shader combines the stored coverage mask with the supplied `textColor`/`bgColor`, so color changes need no re-rasterization. See [text-rendering-and-shaders.md](text-rendering-and-shaders.md).
  - Stale tiles are evicted by `frameId` as today (`lastFrame` bookkeeping).
- **No separate LineNumbersComponent pass for numbers/BG/caret:** After migration, the renderer's draw pass covers those; sync points remain in the existing separate pass (out of scope, TBD - see Q1/Q2).
- **LineNumbersTexture is retained** as the rasterization/cache implementation; `LineNumbersComponent` (the pass owner) is what gets removed in Phase 3 (see Q6).

## 12) Staged transition - Step 1 (per-line numbers via number-addressed tiles)

**Goal:** Actually display per-line numbers in the first step, where each view line shows its own `CodeLine.lineNumber` (or none when `hasLineNumber == false`), while keeping the change minimal and the majority of the gutter code unchanged (`LineNumbersComponent` + `LineNumbersTexture` stay).

**Key realization (from discussion):** Display numbers are **not random strings** - they are the natural source sequence with gaps for attached/inline lines, e.g. `{1,-,-,-,2,3,-,4,-,-,5,...}`, i.e. **plain integers**. See Q13 for the exact tile mechanics. Therefore:

- The existing tile rasterization is already universal: a tile is just a **bank of 20 consecutive integers** (`bank+1 .. bank+20`), keyed by bank. It does not need to know anything about the lines.
- **No content-driven rasterization is needed** for integer numbers.
- **No renumber-invalidation is needed**: tile content remains a pure function of the bank (as today). Only font/lineHeight/cleartype changes dispose tiles (as today).
- The only change is the **rendering driver**: from "view row `i` displays number = `start + i`" to "resolve this view line's number `N`, then fetch the tile for `N` and draw its row".

**Core idea:** Keep the tile cache **keyed by number bank** (`bank = ((N-1)/20)*20`), view-independent and scroll-stable, but resolve each view line's number `N` from a provider (the `CodeLine`), and draw the tile's row `(N-1) % 20` at that view row. Lines with `hasLineNumber == false` draw nothing (background only).

**Touch points (small, coherent):**

1. **`CodeLine`** - add `hasLineNumber = true`, `editable = true`, `lineNumber = 0` (defaults per Q9). Model-only.
2. **`LineNumbersTexture`** - **unchanged** (still rasterizes a bank of 20 consecutive integers, right-aligned; color modulation at draw time). The lookup for number `N` is tile `bank(N)`, region row `(N-1) % 20` (see Q13).
3. **`LineNumbersComponent`** - expose a **number-addressed** lookup/draw, e.g. `drawNumber(yPos, N, scheme, colors, g)` (or return `(texture, regionY)` for `N`), in addition to / instead of the range-based `drawRange`. Cache/pool/eviction logic unchanged. Width derived from `Document.maxLineNumber` (Q4) via `measureDigits`.
4. **`EditorComponent`** - provide each visible view line's `N` (from `CodeLine.lineNumber`/`hasLineNumber`); drive drawing per view line, batching adjacent view rows whose numbers are consecutive and whose diff background matches into a single rect (as today's batched runs). Non-numbered rows draw background only. Compute gutter width from `maxLineNumber`.

**Batching / performance note:** Within a run of adjacent view rows with consecutive numbers and the same diff color, one `g.drawText` region can cover multiple rows (as current `drawRange` does). A non-numbered row simply breaks the run (it cannot be part of a number region), and is filled with background. No per-scroll re-rasterization occurs: tiles are number-addressed and view-independent.

**Assumptions this step relies on (state explicitly):**

- Display numbers are **plain integers** (natural sequence with gaps). Non-integer / hex / arbitrary labels are **not** a gutter concern in this design: if inline disasm needs them, they go into `CodeElement` content and render as part of the line's text - so the integer-bank model for the gutter remains sufficient.
- Attached disasm lines are `CodeLine`s **in the same `Document`**, with `hasLineNumber == false` (they occupy document indices; nothing is drawn for them).
- One document per gutter (two-pane diff still uses `lineNumbers1`/`lineNumbers2`). Single-pane unified diff is a later step.
- A given `CodeLine`'s `lineNumber` is stable for its identity (not different per view). If two views ever need different numbers for the same line, this keying must be revisited.
- **Compact/folding view model indices are internal bookkeeping only and are not the displayed numbers** (see Q5). The display number is independent data stored on the line.

**Explicitly NOT Option B:** Gutter drawing (background + caret + number) still happens in `LineNumbersComponent`; `CodeLineRenderer` is untouched in this step. Option B (renderer draws the gutter, component retired) remains the later target; this step's fields and data model carry over unchanged.

**Note on the Fork A/Fork B distinction:** Originally two options were framed - Fork A (number equals position) vs Fork B (arbitrary per-line value, assumed to need content-driven rasterization). The realization above collapses the difference: tiles already cover arbitrary **integer** values via banks, so the implementation is close to the existing design, and the substantive change is only the number-addressed rendering driver plus the `CodeLine` fields.

## 13) LineNumbersTexture mechanics (and the number-addressed lookup)

**Purpose:** Document exactly how `LineNumbersTexture` works, to confirm that per-line numbers need no change to rasterization - only a different lookup driver.

**What a tile is:**

- One `GL.Texture` holding a **bank of 20 consecutive integers**. Texture size = `(gutterWidth, 20 * lineHeight)`.
- `init(width, lineHeight, startLine, textureCanvas, fontDesk, devicePR)`: clears the canvas and draws `String.valueOf(startLine + i + 1)` at row `i` (`lineHeight * i + baseline`), right-aligned with `rightPad`; then `lineTexture.setContent(textureCanvas)`; records `cleartype`.
- Content is a **coverage mask**, not real font colors; the actual foreground/background are supplied at draw time. That is why no per-line or per-color rasterization is needed - details in [text-rendering-and-shaders.md](text-rendering-and-shaders.md).

**Bank / row math for a number `N` (1-based):**

```
bank(N)   = ((N - 1) / 20) * 20        // startLine of the tile
row(N)    = (N - 1) % 20               // row within the tile
textureY  = row(N) * lineHeight
```

**Drawing (`draw`, `drawCaretLine`):**

- `draw(dXdY, yPos, fromLine, toLine, colorScheme, colors, g)`: `fromLine`/`toLine` are `(N-1)` bounds. It walks rows, groups adjacent rows with the same diff background (`colorScheme.getDiffColor(colors, n)`) into one `g.drawText` call over a region `(0, d + offset*lineHeight, width, h)`, using `lineNumberScheme.textColor` as foreground. Returns early if the tile doesn't cover `[fromLine, toLine)`.
- `drawCaretLine(dXdY, yPos, caretLine, colorScheme, colors, g)`: draws a single row with `colorScheme.lineNumber.caretTextColor` foreground and the line's diff background.
- `g.drawText(...)` combines the stored coverage mask with the given foreground/background colors, so caret/diff/selection states are pure draw-time state (see [text-rendering-and-shaders.md](text-rendering-and-shaders.md)).

**Pool / eviction (`LineNumbersComponent`):**

- Holds `List<LineNumbersTexture> textures` and a `Deque old` pool, plus `frameId`.
- `texture(g, line)` finds a tile with the matching `startLine`, else reuses from `old` or creates one; `init` rasterizes on creation.
- `beginDraw` marks tiles used this frame; tiles not used for ≥ 2 frames move to `old`; `endDraw` disables scissor.
- `setFont(...)` disposes the canvas and moves all tiles to `old` (forces re-rasterization with the new font/metrics).
- `measureDigits(numDigits, ...)` measures the width of `numDigits` zeros (plus padding) for gutter sizing.

**Consequence for per-line numbers (the key point):**

Because a tile is just a bank of integers and lookup is by number, showing number `N` on **any** view row requires only `bank(N)` + `row(N)`. The provider supplies `N` per view line (or "none"). **No change to rasterization, no per-line texture, no renumber invalidation** - only the driver changes (Q12).

**Boundary:** This integer-bank model covers plain integer display values (including gaps). Non-integer / hex-string / arbitrary labels are handled **outside** the number gutter - they are placed in `CodeElement` content and render as regular line text. So the gutter only ever needs integer bank lookups, and no content-driven rasterization is required for line numbers.