# LineNumbersTexture: Texture Tiling for Line Numbers

## Overview
LineNumbersTexture renders a batch of line numbers into a single GL texture and draws subranges of that batch with per-line background colors (for diff states). It is managed by LineNumbersComponent as a pool of tiles covering the document in chunks of numberOfLines=20.


## Key Concepts
- Batch size: 20 lines per texture (static final int numberOfLines = 20).
- init(): draws numbers 1-based for each position; right-aligned with padding from LineNumbersComponent.rightPad.
- draw(): renders a subrange [fromLine, toLine) by slicing the texture; merges consecutive lines with identical background color (from EditorColorScheme.getDiffColor via colors array) into larger rects to reduce draw calls.
- drawCaretLine(): highlights a single line in the batch with caret text color and its diff background.
- Caching: textures are recycled by LineNumbersComponent using frameId/lastFrame; dispose() frees the GL texture.

## Implementation Notes
- When drawing a range, draw() tracks consecutive lines with same background color to batch: it stores prevColor from first line, accumulates height while color unchanged, and flushes a rect+text draw when color changes (or at end). The draw helper draws a single chunk at (yPos + startLine*lineHeight) using rectRegion/rectSize covering that chunk.
- rectRegion/rectSize are reused V4f/V2i instances (per texture).
- cleartype flag is stored from textureCanvas and passed through on draw.
- If the requested range falls entirely outside the tile, draw() returns early.

