# CodeLine and CodeLineRenderer: Architecture Overview

This page describes the two central pieces of the text layer: the **`CodeLine`** model and the **`CodeLineRenderer`** that rasterizes one line on screen. For the gutter (numbers) and shared text rasterization, see the links at the bottom.

## CodeLine (`demo-edit/.../editor/CodeLine.java`)

A single line of text broken into styled fragments (`CodeElement[]`).

**Fields (model):**
- `CodeElement[] elements` - the line content, split into styled runs (each `CodeElement` carries `s`, `color`, `style`).
- `int totalStrLength` - cached sum of element text lengths.
- `int lineNumber = 0` - **added by the line-numbers refactoring** ([line-numbers-refactoring-QA.md](line-numbers-refactoring-QA.md) Q9): the gutter display number; `0` = **AUTO** in a classic document, or an **attached (non-source) line** in a mapped document. See [inline-disasm-open-questions.md](inline-disasm-open-questions.md) Q5 for the ordinal invariant.
- Measurement caches: `fMeasure`/`iMeasure` (pixel widths), `lengthCache` (char offsets), `glyphMeasureCache` (per-glyph widths), and the dirty flags `measureDirty` / `contentDirty` / `lengthDirty`.

**Construction:** `new CodeLine(String)` (one default element) and `new CodeLine(CodeElement...)`; `makeLines(String[])` builds a `CodeLine[]` for a document. Constructors stay unchanged; `lineNumber` defaults to `0`.

**Content edits:** `split(pos)` / `getElementsToLeft` / `getElementsToRight`, `delete(...)`, `deleteAt`, `insertAt`, `insertToEnd` / `insertToBegin`. These mutate `elements` and invalidate caches; they are the primitives used by the edit path.

**Measurement / hit testing:** `measure` (fills width tables via `Canvas.measureText`), `computeCharPos` (pixel -> char), `computePixelLocation` (char -> pixel), `lineMeasure()` (total pixel width), `getElementIndex` / `getElementStart`. `invalidateMeasure` / `invalidateCache` mark the line dirty.

**Text extraction:** `makeString`, `append`, `toCharArray(dst, pos)`, `getChar`.

## CodeLineRenderer (`demo-edit/.../editor/CodeLineRenderer.java`)

Renders **one** `CodeLine` into horizontal texture strips, and draws those strips with selection/highlight/diff coloring.

**Textures / strips:** a `CodeLineRenderer` owns `GL.Texture[] textures`. Texture width is `TEXTURE_WIDTH`; the count is `countNumOfTextures(editorWidth, lineMeasure)` so a long line is split into several strips. A texture is (re)rasterized in `updateTexture(...)` when the bound line changes (`line != content`) or `contentDirty`, then `updateTextureOnScroll(...)` re-slices for horizontal scroll. Strips are positioned via `setXOffset` / `xOffset`.

**Draw:** `draw(...)` emits the visible strip ranges, applying selection (`drawSelected`), usage/highlight underlines (`drawUnderline`), and the row's diff background. `drawTail(...)` draws the remainder beyond the last strip. `contentDirty` is cleared after rasterization.

**Pool / recycling:** `EditorComponent` keeps a pool of `CodeLineRenderer` instances and recycles them by **content identity** - a renderer is reused for a visible line when its bound `CodeLine` matches, so rendering a frame does not allocate per line. `allocRenderLines`, `makeContentDirty`, and `disposeLines` manage the pool; `dispose()` releases the textures.

**Scope:** the renderer draws the **text area only**. The gutter (line numbers, gutter diff background, caret highlight) is drawn by `LineNumbersComponent` today; moving it into the renderer is the deferred "Option B" ([line-numbers-refactoring-QA.md](line-numbers-refactoring-QA.md) Q1/Q6/Q11).

## See also
- [text-rendering-and-shaders.md](text-rendering-and-shaders.md) - how line text is rasterized (coverage mask) and colored at draw time.
- [LineNumbersComponent.md](LineNumbersComponent.md) - the gutter component that owns line-number drawing today.
- [LineNumbersTexture.md](LineNumbersTexture.md) - the analogous texture tiling for gutter numbers.
- [Editor-Rendering-Overview.md](Editor-Rendering-Overview.md) - the component map and docs index.
