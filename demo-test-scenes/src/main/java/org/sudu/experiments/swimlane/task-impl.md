Swimlane Shader: Inter-Event Distance Data and Thin Event Extension

## Background

See task.md for the original rendering approach: vertex shader computes precise
horizontal event coverage per pixel, accumulated via blending add into an
intermediate texture, then mapped to colors by SwimlaneFromTextureShader.

Original vertex format: { vec2 pos, vec2 uv } — 4 floats per vertex.
Each vertex carries event.begin and event.end via the pos/uv encoding.

## Goal 1: Add inter-event distance data

Extend the vertex format to include distances between neighboring events,
so the vertex shader can reason about gaps.

New vertex format: { vec2 pos, vec2 uv, vec2 data } — 6 floats per vertex.
  data.x = distance from this event's start to the previous event's end
  data.y = distance from this event's end to the next event's start

For the first event, data.x = eventRange (distance from first event start to last event end).
For the last event (only when all events fit in the mesh), data.y = eventRange.

If we don't have gap data (edge events), the gap is assumed to be eventRange
(tsBE[last] - tsBE[0]). If the mesh is a subset and gap data can be retrieved
from tsBE, then it should be the correct value from tsBE.

### Implementation

GL.java:
  - Added VertexAttribute DATA2("vData", FLOAT, 2, index 2)
  - Added VertexLayout POS2_UV2_DATA2

SwimlaneShader.java:
  - New setVbSquareWithGaps(): writes 24 floats per quad (6 per vertex)
    interleaved as [pos.x, pos.y, uv.x, uv.y, gapPrev, gapNext]
  - New createSwRectangleWithGaps(): single-event mesh helper
  - createSwimlaneMesh() rewritten: computes gaps from event array,
    uses POS2_UV2_DATA2 layout, VB size = numSquares * 4 * 6
  - Old methods (setVbSquare, createSwRectangle) preserved untouched
  - Constructor uses POS2_UV2_DATA2 layout

### Gap distance conversion (event coords → device pixels)

vData contains distances in event coordinate space (same units as event begin/end).
Converting to device pixels:

  gapPx = vData * uSizePos.x * 0.5 * uResolution.x

Where:
  uSizePos.x = 3.0 * scale / timeRange  (set on Java side)
  uResolution.x = screen width in device pixels

This is NOT translateScaleX (which adds translation — wrong for distances)
and NOT glToPixelX (which assumes GL clip space — also wrong for distances).

The conversion chain is:
  1. Event coords → GL clip space: distance * uSizePos.x  (scale only, no translate)
  2. GL clip space → device pixels: * 0.5 * uResolution.x


## Goal 2: Extend isolated thin events

Very thin events (less than 1 device pixel wide) are invisible when zoomed out.
If an event is isolated (large gaps on both sides), extend it to exactly 1
device pixel so it becomes visible.

### Conditions

  const float MARGIN = 2.0;  // device pixels

  if (gapPrevPx >= MARGIN && gapNextPx >= MARGIN && eventSize < 1.0) {
    // both gaps are large AND event is thin — extend the event
  }

Edge events (first/last) use eventRange (total span from first event start to last event end)
as a fake gap on the missing side, so the condition triggers when the one real neighboring
gap is large enough.

### Extension formula

  eventSize = rPx - lPx                     // current event width in pixels
  extend = (1.0 - eventSize) * 0.5          // per side
  extendedL = lPx - extend
  extendedR = rPx + extend

Result: final event width = 1.0 device pixel (extend * 2 + eventSize = 1.0).

If eventSize >= 1.0, extend <= 0, no extension needed.

### Quad vertex snapping

The floor/ceil snapping of quad vertices MUST be applied AFTER extension,
not before. The quad must cover all pixels the extended event writes to:

  screenX = mix(floor(extendedL), ceil(extendedR), vTex.y);

  Left vertices (vTex.y = 0): snap to floor(extendedL)
  Right vertices (vTex.y = 1): snap to ceil(extendedR)

This ensures the quad covers the full pixel range of the extended event.

### lrScreen varying

lrScreen outputs the EXTENDED event bounds (extendedL, extendedR) to the
pixel shader. The pixel shader computes coverage unchanged:

  lPx = max(lrScreen.x, screenPos.x - 0.5);
  rPx = min(lrScreen.y, screenPos.x + 0.5);
  inside = rPx - lPx;

### Shader flow summary

Vertex shader:
  1. Compute original event bounds (lPx, rPx) from pos/uv encoding
  2. Convert gap distances to device pixels: vData * uSizePos.x * 0.5 * uResolution.x
  3. If both gaps >= MARGIN and eventSize < 1.0: extend event to 1px width
  4. Snap quad vertices to pixel boundaries of the extended event
  5. Output extended bounds in lrScreen varying

Pixel shader:
  1. Clamp extended bounds to current pixel [center-0.5, center+0.5]
  2. Compute fractional coverage
  3. Output grayscale value for blending accumulation

### Files changed

  GL.java                    — VertexAttribute.DATA2, VertexLayout.POS2_UV2_DATA2
  SwimlaneShader.java        — new mesh methods, new vertex format, VS extension logic

### What was NOT changed

  SwimlaneTest.java          — no changes needed
  SwimlaneFromTextureShader  — untouched (dead code left as-is per earlier decision)
  Old setVbSquare / createSwRectangle — preserved, unused
