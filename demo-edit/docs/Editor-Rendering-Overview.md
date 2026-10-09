# Editor Rendering Overview

High-level map of how the editor renders a document, and where to find the details. This page is an **index** - it intentionally avoids duplicating the focused documents below.

## Components at a glance

- **`EditorComponent`** - view/layout, vertical & horizontal scroll, view/document mapping, renderer pool, mouse hit testing, gutter layout.
- **`Document` / `CodeLine`** - model: a document is a list of `CodeLine`s; each `CodeLine` is a list of styled `CodeElement`s.
- **`docToView` (`CodeLineMapping`)** - maps view lines to document lines (identity today; folding/compact and interleaving use the same mechanism).
- **`CodeLineRenderer`** - renders one line's `CodeElement`s into horizontal texture strips; handles selection, highlights, usages, diff backgrounds.
- **`LineNumbersComponent` + `LineNumbersTexture`** - the gutter: line numbers cached as texture tiles of 20 numbers, drawn per visible range with diff backgrounds and caret highlighting.
- **Text rendering & shaders** - app-wide rasterization of text into a colored-per-draw coverage mask.

## Docs index

- [CodeLine-and-Renderer.md](CodeLine-and-Renderer.md) - `CodeLine` model and `CodeLineRenderer` responsibilities.
- [LineNumbersComponent.md](LineNumbersComponent.md) - gutter component: tiling, range drawing, caret line, sizing.
- [LineNumbersTexture.md](LineNumbersTexture.md) - the 20-number texture tile: rasterization, range slicing, tiling/recycling.
- [text-rendering-and-shaders.md](text-rendering-and-shaders.md) - how all text is rasterized (coverage mask) and colored at draw time (app-wide).
- [line-numbers-refactoring-plan.md](line-numbers-refactoring-plan.md) - goals and high-level plan for the line-number refactoring.
- [line-numbers-refactoring-QA.md](line-numbers-refactoring-QA.md) - detailed Q&A decisions, staged transition, and `LineNumbersTexture` mechanics.
