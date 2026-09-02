Essential Project information:

SwimlaneTest and SwimlaneShader renders events timeline diagram

It uses vertex shader to compute precise horizontal event coverage of each event in each pixel
Then it accumulate all event using blending add function.
An intermediate texture is used to accumulate data then in rendered to framebuffer using data from texture making the image have decided colors. 
Vertex has the following type: vec2 pos, vec2 uv
To each vertex we pass event.begin and event.end to vertex shader in the following form:
  [ x1,-1, x0,1, /**/ x1,1, x0,1, /**/ x0,-1, x1,0, /**/ x0,1, x1,0 ]

So each vertex has event { begin, end } information. 
The opposite x coordinate (x1 for left and x0 for right) is passed in uv.x, and uv.y has the following meaning: 
  1 for right vertices, and 0 for left.
Using this information the vertex shader extends the gl-coordinates of the vertex to left pixel boundary (if the vertex is left) or to the right
Also vertex shader pass the original event boundaries in device pixel coordinates to pixel shader varying to let pixel shader compute precise pixel coverage 
of the event and emit this value to accumulate it later from all events  using blending add operation.

QA:
1. Are lines 173-174 and 175-176 leftover from experimentation?
yes
2. Should the dead code be cleaned up?
no, we will left it as is for noe, it will not affect our next task, but be used later
3. Is the current intent to simply do mix(uColorB, uColorF, gray) without any uMinValue remapping?
for current task yes, we want to see acculuated coverage for now
4. The RT height = lines (20 pixels) — is for other experiment

Next goal: 

Now we need to add one more vertex format: { vec2 pos, vec2 uv, vec2 data }.
To the new data fiend we need to put distance from each event the previous event end (.x) and to next event start (.y).
In other words we need to have distance between events on the vertex shader to perform some computation using this distance 

1. Should createSwRectangle also be updated to the new layout, or left as-is since it's a single-event helper?

we need to create a new method next to old one, event if the old one left unused

2. For vertex data packing order — do you want [pos.x, pos.y, uv.x, uv.y, data.x, data.y] per vertex (interleaved)?

similar to previous one, it was hold in float32 array [pos.x, pos.y, uv.x, uv.y], new one will add a two new floats 

- Vertex shader gets the distance in new fields, then it need to convert it to device pixels, 
 like it does for current event begin and end, outputs leftRightDistance varying 
 (in device pixels, same coordinate system used for other data) for future use in pixel shader.
- 


