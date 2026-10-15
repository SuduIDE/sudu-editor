# LineNumbersComponent: Architecture Overview

## Overview
LineNumbersComponent (demo-edit/src/main/java/org/sudu/experiments/editor/LineNumbersComponent.java) renders line numbers in the gutter. It caches numbers in texture tiles (LineNumbersTexture) and draws only the visible range per frame, tracking frame IDs to recycle stale textures.


## LineNumbersTexture Integration
- numberOfLines constant shared (LineNumbersTexture.numberOfLines).
- For each visible segment, LineNumbersComponent computes startLine = (line/20)*20, gets texture via texture(g,startLine) (creates/reuses), then calls texture.draw with dY offset for that segment.
- drawRange loops over tiles: for each tile index ind = i/numberOfLines, gets texture for startLine=ind*numberOfLines, draws range [begin,end) intersected with tile, adds (end-begin)*lineHeight to dY.
- drawCaretLine draws a single caret line by looking up its tile and calling texture.drawCaretLine.
- Text alignment is RIGHT on the canvas (textureCanvas.setTextAlign(Canvas.TextAlign.RIGHT)) with right padding. Text rasterization & colors are covered by [text-rendering-and-shaders.md](text-rendering-and-shaders.md).
- width() is the gutter width (size.x). measureDigits() estimates width for N digits using '0' chars plus padding.

