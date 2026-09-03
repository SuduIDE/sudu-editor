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

-----

The purpose is to make isolated thin events (less than 1px) visible at exactly 1 device pixel.

I want to extend the event left and right if the distance to prev/next event is "large"
lets introduce a constant (in the VS code) that will mean the margin, in device pixels
and of the BOTH distances (to left and to right) more then he margin then we extend the event in both directions: to left and to right simultaneously
and extend rage is computed like this: (1.0 - (event.x1 - event.x0)) / 2, so the resulting event size will be equal to 1 device pixel, we will extend it in both directions uniformly 

Events at the start/end of the line also needs to be extended if opposite distance large enought, for that reason we can assume that the left most event (and rightmost) have quite large fake distance to left (for leftmost evevnt) or to the right (for right most event)

eventSize means event.x1-event.x0 - the length of the event

lets margin be equal to 3 device pixels

we extend the eveny only if gapPrevPx AND gapNextPx greater or equal to the margin 

the quad vertices snap to pixel boundaries via floor/ceil operations must be applied after extension 

now there is two major issue to address :
1. "if (gapPrevPx >= MARGIN && gapNextPx >= MARGIN && eventSize < 1.0) {"

any if in the shader code making JUMPS, it was not frame before but the star appears on the next frame
so 1st goal is to remove if, we do the following :

a) we split gapPrexPx and gapNextPs processing into individual parts
b) we make two margins instead of one (MARGIN), MARGIN1 = 2 and MARGIN2 = 4
when the the gap is between MARGIN1 and MARGIN2, the amount of extend is "animated" between 0 and maxExtend
where maxExtend=(1-eventSize)*0.5
the extend factor is linear:  min(1, max(0, (MARGIN1 - gap)/(MARGIN1 - MARGIN2)))
the extend factor is individual for left gap and for right   
c) we still have eventSize < 1.0 condition to not make negative extends

2. TBD later after 1 is implemented

make the plan and ask all the unclear questions
also not forget to update the documentation      