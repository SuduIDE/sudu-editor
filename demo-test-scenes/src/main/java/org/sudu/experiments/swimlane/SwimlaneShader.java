package org.sudu.experiments.swimlane;

import org.sudu.experiments.GL;
import org.sudu.experiments.GLApi;
import org.sudu.experiments.Shaders;
import org.sudu.experiments.math.V4f;

import static org.sudu.experiments.Shaders.*;

class SwimlaneShader extends Shaders.Shader2d {
  SwimlaneShader(GLApi.Context gl) {
    super(gl, vsCode(), psCode(), GL.VertexLayout.POS2_UV2_DATA2);
  }

  // x1,-1, x0,1, /**/ x1,1, x0,1, /**/ x0,-1, x1,0, /**/ x0,1, x1,0
  static void setVbSquare(int p, float x0, float x1, float[] vb) {
    vb[p   ] = x1; vb[p+1 ] = -1;  vb[p+2 ] = x0;  vb[p+3 ] = 1;
    vb[p+4 ] = x1; vb[p+5 ] =  1;  vb[p+6 ] = x0;  vb[p+7 ] = 1;
    vb[p+8 ] = x0; vb[p+9 ] = -1;  vb[p+10] = x1;  vb[p+11] = 0;
    vb[p+12] = x0; vb[p+13] =  1;  vb[p+14] = x1;  vb[p+15] = 0;
  }

  // x1,-1, x0,1, gapPrev,gapNext, /**/ x1,1, x0,1, gapPrev,gapNext, /**/ x0,-1, x1,0, gapPrev,gapNext, /**/ x0,1, x1,0, gapPrev,gapNext
  static void setVbSquareWithGaps(int p, float x0, float x1, float gapPrev, float gapNext, float[] vb) {
    vb[p   ] = x1; vb[p+1 ] = -1;  vb[p+2 ] = x0;  vb[p+3 ] = 1;  vb[p+4 ] = gapPrev;  vb[p+5 ] = gapNext;
    vb[p+6 ] = x1; vb[p+7 ] =  1;  vb[p+8 ] = x0;  vb[p+9 ] = 1;  vb[p+10] = gapPrev;  vb[p+11] = gapNext;
    vb[p+12] = x0; vb[p+13] = -1;  vb[p+14] = x1;  vb[p+15] = 0;  vb[p+16] = gapPrev;  vb[p+17] = gapNext;
    vb[p+18] = x0; vb[p+19] =  1;  vb[p+20] = x1;  vb[p+21] = 0;  vb[p+22] = gapPrev;  vb[p+23] = gapNext;
  }

  static void setIbSquare(int p, int n, char[] ib) {
    ib[p  ] = (char) (n  ); ib[p+1] = (char) (n+1); ib[p+2] = (char) (n+2);
    ib[p+3] = (char) (n+1); ib[p+4] = (char) (n+2); ib[p+5] = (char) (n+3);
  }

  static GL.Mesh createSwimlaneMesh(GLApi.Context gl, float[] tsBE) {
    int numSquares = Math.min(tsBE.length / 2, 0x1_00_00 / 4);
    float[] vb  = new float[numSquares * 4 * 6];
    char[] ib = new char[numSquares * 6];
    for (int i = 0; i < numSquares; i++) {
      int vbp = i * 24, ibp = i * 6;
      float x0 = tsBE[i * 2], x1 = tsBE[i * 2 + 1];
      // distance from previous event end (fake large gap for first event)
      float gapPrev = i == 0 ? 999.0f : x0 - tsBE[i * 2 - 1];
      // distance to next event start (fake large gap for last event)
      float gapNext = i == numSquares - 1 ? 999.0f : tsBE[i * 2 + 2] - x1;
      setVbSquareWithGaps(vbp, x0, x1, gapPrev, gapNext, vb);
      setIbSquare(ibp, i * 4, ib);
    }
    return new GL.Mesh(gl, GL.VertexLayout.POS2_UV2_DATA2, vb, ib);
  }

  static GL.Mesh createSwRectangle(GLApi.Context gl, float x0, float x1) {
    float[] vbData = { x1,-1, x0,1, /**/ x1,1, x0,1, /**/ x0,-1, x1,0, /**/ x0,1, x1,0  };
    char[] index = { 0, 1, 2, /**/ 1, 2, 3, /**/  /* 0, 2, 1, */ /**/ /* 1, 3, 2 */ };
    return new GL.Mesh(gl, GL.VertexLayout.POS2_UV2, vbData, index);
  }

  static GL.Mesh createSwRectangleWithGaps(GLApi.Context gl, float x0, float x1, float gapPrev, float gapNext) {
    float[] vbData = {
        x1,-1, x0,1, gapPrev,gapNext,
        x1, 1, x0,1, gapPrev,gapNext,
        x0,-1, x1,0, gapPrev,gapNext,
        x0, 1, x1,0, gapPrev,gapNext
    };
    char[] index = { 0, 1, 2, /**/ 1, 2, 3 };
    return new GL.Mesh(gl, GL.VertexLayout.POS2_UV2_DATA2, vbData, index);
  }

  // vTex.x - opposite coordinate
  // vTex.y - factor:  0 - left vertex, 1 - right vertex
  static String vsCode() {
    return shaderHeader + psShaderPrecision + screenPixelPos +
        """
            uniform vec4 uSizePos;
            uniform vec2 uResolution;
            uniform vec2 uParameters;
            in vec2 vPos, vTex, vData;
            out vec2 screenPos;
            out vec2 lrScreen;
            
            float translateScaleX(float x) { return x * uSizePos.x + uSizePos.z; }
            float translateScaleY(float y) { return y * uSizePos.y + uSizePos.w; }

            float glToPixelX(float x) { return (x + 1.0) * 0.5 * uResolution.x; }
            float glToPixelY(float y) { return (1.0 - y) * 0.5 * uResolution.y; }
            float pixelToGlX(float x) { return x * 2.0 / uResolution.x - 1.0; }
            float pixelToGlY(float y) { return 1.0 - y * 2.0 / uResolution.y; }
            
            vec2 glToPixel(vec2 gl) { return vec2(glToPixelX(gl.x), glToPixelY(gl.y)); }
            vec2 pixelToGl(vec2 px) { return vec2(pixelToGlX(px.x), pixelToGlY(px.y)); }
            
            void main() {
              float lX = mix(vPos.x, vTex.x, vTex.y);
              float rX = mix(vTex.x, vPos.x, vTex.y);

              vec2 pos = vec2(translateScaleX(vPos.x), translateScaleY(vPos.y));
              float lPx = glToPixelX(translateScaleX(lX));
              float rPx = glToPixelX(translateScaleX(rX));

              // gap to prev event end / next event start, in device pixels
              float gapPrevPx = vData.x * uSizePos.x * 0.5 * uResolution.x;
              float gapNextPx = vData.y * uSizePos.x * 0.5 * uResolution.x;

              // extend event if both gaps >= margin
              const float MARGIN = 3.0;
              float eventSize = rPx - lPx;
              float extendedL = lPx, extendedR = rPx;
              if (gapPrevPx >= MARGIN && gapNextPx >= MARGIN && eventSize < 1.0) {
                float extend = (1.0 - eventSize) * 0.5;
                extendedL = lPx - extend;
                extendedR = rPx + extend;
              }

              float screenY = glToPixelY(pos.y);
              // snap quad to pixel boundaries of the extended event
              float screenX = mix(floor(extendedL), ceil(extendedR), vTex.y);
              pos.x = pixelToGlX(screenX);

              screenPos = vec2(screenX, screenY);
              lrScreen = vec2(extendedL, extendedR);
              gl_Position = vec4(pos, 0.0, 1.0);
            }""";
  }

  static String psCode() {
    return shaderHeader + psShaderPrecision +
        """
            layout(location = 0) out vec4 outColor;
            
            // center of current pixel in screen space
            // center +- 0.5 - full current pixel
            in vec2 screenPos;
            
            // {left, right} event bounds in screen pixel space
            in vec2 lrScreen;
            
            void main() {
             // left event bound in current screen pixel
              float lPx = max(lrScreen.x, screenPos.x - 0.5);
              // right event bound in current screen pixel
              float rPx = min(lrScreen.y, screenPos.x + 0.5);
              // coverage of event for current screen pixel
              float inside = rPx - lPx;
              outColor = vec4(inside, inside, inside, 1.0);
            }""";
  }
}

class SwimlaneFromTextureShader extends Shaders.SimpleTextureTransformed {
  final GLApi.UniformLocation uColorB, uColorF, uMinValue;
  SwimlaneFromTextureShader(GLApi.Context gl) {
    super(gl, vsCode(), psCode());
    uColorB = gl.getUniformLocation(program, "uColorB");
    uColorF = gl.getUniformLocation(program, "uColorF");
    uMinValue = gl.getUniformLocation(program, "uMinValue");
  }

  void set(GLApi.Context gl, V4f colorB, V4f colorF) {
    gl.uniform4f(uColorB, colorB);
    gl.uniform4f(uColorF, colorF);
  }

  void setMinValue(GLApi.Context gl, float minValue) {
    gl.uniform2f(uMinValue, minValue, minValue);
  }

  private static String vsCode() {
    return shaderHeader + psShaderPrecision + screenPixelPos +
        """
            uniform vec4 uSizePos;
            uniform vec2 uResolution;
            uniform vec4 uTexTransform;
            in vec2 vPos, vTex;
            out vec2 outScreenPos;
            out vec2 textureUV;
            out vec2 vPos_vPixel;

            void main() {
              vec2 pos = vec2(vPos.x * uSizePos.x + uSizePos.z, vPos.y * uSizePos.y + uSizePos.w);
              outScreenPos = pixelPos(pos, uResolution.xy);
              textureUV = uTexTransform.xy + vTex * uTexTransform.zw;
            
              // uSizePos.y = 1.f * sizeY / uResolution.y
              vPos_vPixel.x = vPos.y * 0.5 + 0.5; // 0..1
              vPos_vPixel.y = uSizePos.y * uResolution.y;
            
              gl_Position = vec4(pos, 0.0, 1.0);
            }""";
  }

  static String psCode() {
    return shaderHeader + psShaderPrecision +
        """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColorB;
          uniform vec4 uColorF;
          uniform vec2 uMinValue;
          uniform sampler2D sDiffuse;
          in vec2 textureUV;
          in vec2 vPos_vPixel; // x - vertical pos (0..1), y - vertical size in pixels
          void main() {
            vec3 t = texture(sDiffuse, textureUV).rgb;
            float gray = (t.r + t.b + t.g) / 3.0;
            float vSizePx = vPos_vPixel.y / 255.0;
            float pixelLowEdge = vPos_vPixel.x - 0.5 * (1.0 / vPos_vPixel.y);
            float pixelCoverage = max(min((gray - pixelLowEdge) * vPos_vPixel.y, 1.0), 0.0);
            pixelCoverage = gray > 0.0 ? uMinValue.x + gray * (1.0 - uMinValue.x) : 0.0;
            outColor = vec4(mix(uColorB.rgb, uColorF.rgb, pixelCoverage), 1.0);
            outColor = vec4(mix(uColorB.rgb, uColorF.rgb, gray), 1.0);
          }""";
  }
}