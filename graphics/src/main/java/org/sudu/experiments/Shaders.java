package org.sudu.experiments;

import org.sudu.experiments.math.V2i;
import org.sudu.experiments.math.V4f;

public interface Shaders {
  String shaderHeader = "#version 300 es\n";
  String psShaderPrecision = "precision highp float;\n";

  String psCodeConstColor = shaderHeader + psShaderPrecision +
      """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColor;
          void main() {
            outColor = uColor;
          }""";

  String psCodeShowUV = shaderHeader + psShaderPrecision +
      """
          layout(location = 0) out vec4 outColor;
          in vec2 textureUV;
          void main() {
            outColor = vec4(textureUV.x, 0, textureUV.y, 1.0);
          }""";

  String psCodeTexture = shaderHeader + psShaderPrecision +
      """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColor;
          uniform sampler2D sDiffuse;
          in vec2 textureUV;
          void main() {
            vec4 t = texture(sDiffuse, textureUV);
            outColor = vec4(t.xyz, 1.0);
          }""";

  // contrast: f(0) = 0, f(1) = 1, f`(0) = 0, f`(1) = 0
  // textContrast: f(0) = 0, f(1) = 1, f`(0) = 1, f`(1) = 0
  String contrastCode =
      """
          float contrast(float x) {
            return x * x * (3.0 - x * 2.0);
          }
          float contrast2(float x) {
            return contrast(contrast(x));
          }
          float textContrast(float x) {
            return x + x * (x - x * x);
          }
          float textContrastBold(float x) {
            return sqrt(x);
          }
          """;
  String psCodeTextureShowAlpha = shaderHeader + psShaderPrecision + contrastCode +
      """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColor;
          uniform vec2 uContrast;
          uniform sampler2D sDiffuse;
          in vec2 textureUV;
          
          float contrastN(float value, float factor) {
            float c = contrast(value);
            return mix(value, textContrast(value), factor);
          }
          
          void main() {
            vec4 t = texture(sDiffuse, textureUV);
            float v = contrastN(t.a, uContrast.x);
            outColor = vec4(v, v, v, 1.0);
          }""";


  String psCodeText = shaderHeader + psShaderPrecision + contrastCode +
      """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColor;
          uniform vec4 uBgColor;
          uniform vec2 uContrast;
          uniform sampler2D sDiffuse;
          in vec2 textureUV;
          void main() {
            float t = texture(sDiffuse, textureUV).a;
//            if (t == 0.0) { discard; }
            float text = mix(t, textContrast(t), uContrast.x);
             
            outColor = mix(uBgColor, uColor, text);
          }""";

  String psCodeGrayIcon = shaderHeader + psShaderPrecision + contrastCode +
      """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColorB;
          uniform vec4 uColorF;
          uniform vec2 uContrast;
          uniform sampler2D sDiffuse;
          in vec2 textureUV;
          void main() {
            vec3 t = texture(sDiffuse, textureUV).rgb;
            float gray = (t.r + t.b + t.g) / 3.0;
            float value = mix(gray, contrast(gray), uContrast.x);
            outColor = vec4(mix(uColorB.rgb, uColorF.rgb, value), 1.0);
          }""";

  String screenPixelPos = "vec2 pixelPos(vec2 pos, vec2 resolution) {" +
      "  return vec2((pos.x + 1.0) * 0.5 * resolution.x, (1.0 - pos.y) * 0.5 * resolution.y); }\n";

  String vsCode2d = shaderHeader + psShaderPrecision + screenPixelPos +
      """
          uniform vec4 uSizePos;
          uniform vec2 uResolution;
          in vec2 vPos, vTex;
          out vec2 outScreenPos;
          out vec2 textureUV;
          void main() {
            vec2 pos = vec2(vPos.x * uSizePos.x + uSizePos.z, vPos.y * uSizePos.y + uSizePos.w);
            outScreenPos = pixelPos(pos, uResolution.xy);
            textureUV = vTex;
            gl_Position = vec4(pos, 0.0, 1.0);
          }""";

  String vsCode2dTexTransform = shaderHeader + psShaderPrecision + screenPixelPos +
      """
          uniform vec4 uSizePos;
          uniform vec2 uResolution;
          uniform vec4 uTexTransform;
          in vec2 vPos, vTex;
          out vec2 outScreenPos;
          out vec2 textureUV;
          void main() {
            vec2 pos = vec2(vPos.x * uSizePos.x + uSizePos.z, vPos.y * uSizePos.y + uSizePos.w);
            outScreenPos = pixelPos(pos, uResolution.xy);
            textureUV = uTexTransform.xy + vTex * uTexTransform.zw;
            gl_Position = vec4(pos, 0.0, 1.0);
          }""";

  class Shader2d extends GL.Program {
    final GLApi.UniformLocation uResolution;
    final GLApi.UniformLocation uSizePos;

    final V2i shaderValue = new V2i();

    Shader2d(GLApi.Context gl, String vsCode, String psCode, GL.VertexLayout layout) {
      super(gl, vsCode, psCode, layout);
      uResolution = gl.getUniformLocation(program, "uResolution");
      uSizePos = gl.getUniformLocation(program, "uSizePos");
    }


    void setScreenSize(GLApi.Context gl, V2i screen) {
      if (!shaderValue.equals(screen)) {
        shaderValue.set(screen);
        gl.uniform2f(uResolution, screen.x, screen.y);
      }
    }

    void setPosition(GLApi.Context gl, float x, float y, V2i size, V2i screen) {
      float sx = (float) size.x / screen.x;
      float sy = (float) size.y / screen.y;
      float px = (x * 2 + size.x) / screen.x - 1;
      float py = 1 - (y * 2 + size.y) / screen.y;
      gl.uniform4f(uSizePos, sx, sy, px, py);
      setScreenSize(gl, screen);
    }
  }

  class ConstColor extends Shader2d {
    final GLApi.UniformLocation uColor;

    ConstColor(GLApi.Context gl) {
      super(gl, vsCode2d, psCodeConstColor, GL.VertexLayout.POS2_UV2);
      uColor = gl.getUniformLocation(program, "uColor");
    }

    void setColor(GLApi.Context gl, V4f color) {
      gl.uniform4f(uColor, color);
    }
  }

  class ShowUV extends Shader2d {
    ShowUV(GLApi.Context gl) {
      super(gl, vsCode2d, psCodeShowUV, GL.VertexLayout.POS2_UV2);
    }
  }

  String psCodeLineFill = shaderHeader + psShaderPrecision +
      """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColor1, uColor2;
          uniform vec4 uPoints;
          in vec2 outScreenPos;

          void main() {
            vec2 pt = outScreenPos;
            vec2 p1 = uPoints.xy;
            vec2 p2 = uPoints.zw;
            float p12d = distance(p1, p2);
            float sd = ((p2.x - p1.x) * (p1.y - pt.y) - (p1.x - pt.x) * (p2.y - p1.y)) / p12d;
            float t = clamp(sd / 1.0 + .5, 0.0, 1.0);
            outColor = mix(uColor1, uColor2, t);
          }""";

  class LineFill extends Shader2d {
    final GLApi.UniformLocation uColor1;
    final GLApi.UniformLocation uColor2;
    final GLApi.UniformLocation uPoints;

    LineFill(GLApi.Context gl) {
      super(gl, vsCode2d, psCodeLineFill, GL.VertexLayout.POS2_UV2);
      uColor1 = gl.getUniformLocation(program, "uColor1");
      uColor2 = gl.getUniformLocation(program, "uColor2");
      uPoints = gl.getUniformLocation(program, "uPoints");
    }

    void setColors(GLApi.Context gl, V4f color1, V4f color2) {
      gl.uniform4f(uColor1, color1);
      gl.uniform4f(uColor2, color2);
    }

    void setPoints(GLApi.Context gl, V2i p1, V2i p2) {
      gl.uniform4f(uPoints, p1.x, p1.y, p2.x, p2.y);
    }
  }

  class SimpleTexture extends Shader2d {
    final GLApi.UniformLocation sDiffuse;

    SimpleTexture(GLApi.Context gl) {
      this(gl, psCodeTexture);
    }

    SimpleTexture(GLApi.Context gl, String psCode) {
      this(gl, vsCode2d, psCode);
    }

    SimpleTexture(GLApi.Context gl, String vsCode, String psCode) {
      super(gl, vsCode, psCode, GL.VertexLayout.POS2_UV2);
      sDiffuse = gl.getUniformLocation(program, "sDiffuse");
    }

    void setTexture(GLApi.Context gl, GL.Texture texture) {
      // todo: "uniform1i(sDiffuse, 0)" needed only once
      //   maybe optimize this later
      gl.uniform1i(sDiffuse, 0);
      gl.activeTexture(gl.TEXTURE0);
      gl.bindTexture(gl.TEXTURE_2D, texture.texture);
    }
  }

  class TextureShowAlpha extends SimpleTexture {
    final GLApi.UniformLocation uContrast;

    TextureShowAlpha(GLApi.Context gl) {
      super(gl, psCodeTextureShowAlpha);
      uContrast = gl.getUniformLocation(program, "uContrast");
    }

    void setContrast(GLApi.Context gl, float value) {
      gl.uniform2f(uContrast, value, 0);
    }
  }

  class Text extends SimpleTexture {
    final GLApi.UniformLocation uTexTransform;
    final GLApi.UniformLocation uColor, uBgColor;
    final GLApi.UniformLocation uContrast;

    Text(GLApi.Context gl) {
      super(gl, vsCode2dTexTransform, psCodeText);
      uTexTransform = gl.getUniformLocation(program, "uTexTransform");
      uColor = gl.getUniformLocation(program, "uColor");
      uBgColor = gl.getUniformLocation(program, "uBgColor");
      uContrast = gl.getUniformLocation(program, "uContrast");
    }

    void set(GLApi.Context gl, V4f color, V4f bgColor, float contrast) {
      gl.uniform4f(uColor, color);
      gl.uniform4f(uBgColor, bgColor);
      gl.uniform2f(uContrast, contrast, 0);
    }

    public void setTextureRect(GLApi.Context gl, GL.Texture texture, V4f texRect) {
      int sx = texture.size.x, sy = texture.size.y;
      float dx = texRect.x / sx;
      float dy = texRect.y / sy;
      float mx = texRect.z / sx;
      float my = texRect.w / sy;
      gl.uniform4f(uTexTransform, dx, dy, mx, my);
    }
  }

  class GrayIcon extends SimpleTexture {
    final GLApi.UniformLocation uColorB, uColorF;
    final GLApi.UniformLocation uContrast;

    GrayIcon(GLApi.Context gl) {
      super(gl, psCodeGrayIcon);
      uColorB = gl.getUniformLocation(program, "uColorB");
      uColorF = gl.getUniformLocation(program, "uColorF");
      uContrast = gl.getUniformLocation(program, "uContrast");
    }

    void set(GLApi.Context gl, V4f colorB, V4f colorF, float contrast) {
      gl.uniform4f(uColorB, colorB);
      gl.uniform4f(uColorF, colorF);
      gl.uniform2f(uContrast, contrast, 0);
    }
  }
}
