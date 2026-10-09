# Text Rendering and Shaders

How text is rasterized and drawn across the whole application. This is **not specific to line numbers** - the same mechanism is used by every text path, including `CodeLineRenderer` (code lines) and `LineNumbersTexture` (gutter numbers).

Source of truth for the code: `graphics/src/main/java/org/sudu/experiments/Shaders.java`, `graphics/src/main/java/org/sudu/experiments/WglGraphics.java`.

## Pipeline

1. Text is rasterized on a `Canvas` (software rasterization) using the current `FontDesk`, producing a `GL.Texture`.
2. The texture stores a **coverage mask**, not colors.
3. At draw time, `g.drawText(...)` selects a shader and supplies foreground/background colors and a gamma; the shader combines the coverage mask with those colors.

## Coverage mask (not real colors)

The rasterized texture stores glyph **coverage**, not a foreground/background color pair:

- **Grayscale shader** (`psCodeText`): reads the texture's **alpha**
  ```
  float t = texture(sDiffuse, uv).a;
  outColor = mix(uBgColor, uColor, pow(t, uTextPow.x));
  ```
- **ClearType shader** (`psCodeTextClearType`): reads per-subpixel-channel **RGB** coverage
  ```
  vec3 textRGB = texture(sDiffuse, uv).rgb;
  vec3 textRGBp = pow(textRGB, uTextPow.x);
  outColor = vec4(mix(uBgColor.rgb, uColor.rgb, textRGBp), 1.0);
  ```

Because ClearType coverage is per-subpixel (R/G/B), the stored texture can itself look colorful - but that RGB is **subpixel coverage, not the displayed text/background color**.

## Colors are supplied at draw time

The real colors are uniforms:

- `uColor` - foreground (text) color
- `uBgColor` - background color
- `uTextPow` - gamma applied to coverage (sharpen/soften)

`g.drawText(x, y, size, textureRect, texture, color, bgColor, cleartype)` chooses `shTextGray` (cleartype=false) or `shTextCT` (cleartype=true) and sets these uniforms. `ClrContext.drawText` is a thin wrapper over this.

**Consequence:** the same rasterized texture can be tinted/recolored per draw (caret highlight, selection background, diff background, syntax color) **without re-rasterization**. Re-rasterization is required only when the glyph coverage itself changes:

- font / pixel size
- line height / baseline (`lineHeight`)
- ClearType vs grayscale mode

## Relationship to textures and tiling

Tiling / atlasing is **orthogonal** to the coverage-mask + shader mechanism:

- Tiling is about caching rasterized coverage for many glyphs/lines so we don't re-rasterize every frame.
- `CodeLineRenderer` rasterizes a line's `CodeElement`s into horizontal texture strips (one strip per `TEXTURE_WIDTH`).
- `LineNumbersTexture` rasterizes a bank of 20 numbers into one texture (see [LineNumbersTexture.md](LineNumbersTexture.md)).
- Because color is per-draw, tiles are **color-independent**: their content depends only on font/lineHeight/cleartype. This is why one line-number tile can serve diff backgrounds, caret coloring, and selection states.

## Where it is used

- `CodeLineRenderer` - code text: strips drawn with per-element colors, selection, and diff backgrounds.
- `LineNumbersTexture` - gutter numbers: bank drawn with number text color + diff background.
- Any other component that calls `g.drawText` / `ClrContext.drawText`.
