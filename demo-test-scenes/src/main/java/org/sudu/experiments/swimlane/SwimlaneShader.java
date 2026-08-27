package org.sudu.experiments.swimlane;

import org.sudu.experiments.GL;
import org.sudu.experiments.GLApi;
import org.sudu.experiments.Shaders;
import org.sudu.experiments.math.V4f;

import static org.sudu.experiments.Shaders.*;

class SwimlaneShader extends Shaders.Shader2d {
  final GLApi.UniformLocation uColor;
//  final GLApi.UniformLocation uParameters;

  SwimlaneShader(GLApi.Context gl) {
    super(gl, vsCode(), psCode(), GL.VertexLayout.POS2_UV2);
    uColor = gl.getUniformLocation(program, "uColor");
   // uParameters = gl.getUniformLocation(program, "uParameters");
  }

  // x1,-1, x0,1, /**/ x1,1, x0,1, /**/ x0,-1, x1,0, /**/ x0,1, x1,0
  static void setVbSquare(int p, float x0, float x1, float[] vb) {
    vb[p   ] = x1; vb[p+1 ] = -1;  vb[p+2 ] = x0;  vb[p+3 ] = 1;
    vb[p+4 ] = x1; vb[p+5 ] =  1;  vb[p+6 ] = x0;  vb[p+7 ] = 1;
    vb[p+8 ] = x0; vb[p+9 ] = -1;  vb[p+10] = x1;  vb[p+11] = 0;
    vb[p+12] = x0; vb[p+13] =  1;  vb[p+14] = x1;  vb[p+15] = 0;
  }

  static void setIbSquare(int p, int n, char[] ib) {
    ib[p  ] = (char) (n  ); ib[p+1] = (char) (n+1); ib[p+2] = (char) (n+2);
    ib[p+3] = (char) (n+1); ib[p+4] = (char) (n+2); ib[p+5] = (char) (n+3);
  }

  static GL.Mesh createSwimlaneMesh(GLApi.Context gl, float[] tsBE) {
    int numSquares = Math.min(tsBE.length / 2, 0x1_00_00 / 4);
    float[] vb  = new float[numSquares * 4 * 4];
    char[] ib = new char[numSquares * 6];
    for (int i = 0; i < numSquares; i++) {
      int vbp = i * 16, ibp = i * 6;
      float x0 = tsBE[i * 2], x1 = tsBE[i * 2 + 1];
      setVbSquare(vbp, x0, x1, vb);
      setIbSquare(ibp, i * 4, ib);
    }
    return new GL.Mesh(gl, GL.VertexLayout.POS2_UV2, vb, ib);
  }

  static GL.Mesh createSwRectangle(GLApi.Context gl, float x0, float x1) {
    float[] vbData = { x1,-1, x0,1, /**/ x1,1, x0,1, /**/ x0,-1, x1,0, /**/ x0,1, x1,0  };
    char[] index = { 0, 1, 2, /**/ 1, 2, 3, /**/  /* 0, 2, 1, */ /**/ /* 1, 3, 2 */ };
    return new GL.Mesh(gl, GL.VertexLayout.POS2_UV2, vbData, index);
  }

  // vTex.x - opposite coordinate
  // vTex.y - factor:  0 - left vertex, 1 - right vertex
  static String vsCode() {
    return shaderHeader + psShaderPrecision + screenPixelPos +
        """
            uniform vec4 uSizePos;
            uniform vec2 uResolution;
            uniform vec2 uParameters;
            in vec2 vPos, vTex;
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
  
              float screenX = glToPixelX(pos.x);
              float screenY = glToPixelY(pos.y);
            
              // extend left/right edge to left/right pixel bound
              screenX = mix(floor(screenX), ceil(screenX), vTex.y);
              // convert back to gl space
              pos.x = pixelToGlX(screenX);

              screenPos = vec2(screenX, screenY);
              lrScreen = vec2(lPx, rPx);
              gl_Position = vec4(pos, 0.0, 1.0);
            }""";
  }

  static String psCode() {
    return shaderHeader + psShaderPrecision +
        """
            layout(location = 0) out vec4 outColor;
            uniform vec4 uColor;
            in vec2 screenPos;
            in vec2 lrScreen;
            void main() {
              float lPx = max(lrScreen.x, screenPos.x - 0.5);
              float rPx = min(lrScreen.y, screenPos.x + 0.5);
              float inside = rPx - lPx;
              outColor = vec4(uColor.xyz * inside, 1.0);
            }""";
  }

  void setColor(GLApi.Context gl, V4f color) {
    gl.uniform4f(uColor, color);
  }
}

class SwimlaneFromTextureShader extends Shaders.SimpleTextureTransformed {
  final GLApi.UniformLocation uColorB, uColorF;
  SwimlaneFromTextureShader(GLApi.Context gl) {
    super(gl, psCode());
    uColorB = gl.getUniformLocation(program, "uColorB");
    uColorF = gl.getUniformLocation(program, "uColorF");
  }

  void set(GLApi.Context gl, V4f colorB, V4f colorF) {
    gl.uniform4f(uColorB, colorB);
    gl.uniform4f(uColorF, colorF);
  }

  static String psCode() {
    return shaderHeader + psShaderPrecision +
        """
          layout(location = 0) out vec4 outColor;
          uniform vec4 uColorB;
          uniform vec4 uColorF;
          uniform sampler2D sDiffuse;
          in vec2 textureUV;
          void main() {
            vec3 t = texture(sDiffuse, textureUV).rgb;
            float gray = (t.r + t.b + t.g) / 3.0;
            outColor = vec4(mix(uColorB.rgb, uColorF.rgb, gray), 1.0);
          }""";
  }
}