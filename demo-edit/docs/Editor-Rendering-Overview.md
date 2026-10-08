# CodeLine and CodeLineRenderer: Architecture Overview

## CodeLine
Represents a single line of text broken into styled fragments (CodeElement[]). 


# LineNumbersComponent: Architecture Overview

## Overview
LineNumbersComponent (demo-edit/src/main/java/org/sudu/experiments/editor/LineNumbersComponent.java) renders line numbers in the gutter. It caches numbers in texture tiles (LineNumbersTexture) and draws only the visible range per frame, tracking frame IDs to recycle stale textures.

