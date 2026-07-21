package org.sudu.experiments.swimlane;

import org.sudu.experiments.GL;
import org.sudu.experiments.GLApi;
import org.sudu.experiments.Shaders;
import org.sudu.experiments.math.V4f;

import static org.sudu.experiments.Shaders.*;

class SwimlaneShader extends Shaders.Shader2d {
  final GLApi.UniformLocation uColor;
  final GLApi.UniformLocation uParameters;

  SwimlaneShader(GLApi.Context gl) {
    super(gl, vsCode(), psCode(), GL.VertexLayout.POS2_UV2);
    uColor = gl.getUniformLocation(program, "uColor");
    uParameters = gl.getUniformLocation(program, "uParameters");
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
            out vec2 outScreenPos;
            out vec2 textureUV;
            
            vec2 glToPixel(vec2 pos, vec2 resolution) {
              return vec2((pos.x + 1.0) * 0.5 * resolution.x, (1.0 - pos.y) * 0.5 * resolution.y);
            }

            vec2 pixelToGl(vec2 px, vec2 resolution) {
              return vec2(px.x * 2.0 / resolution.x - 1.0, 1.0 - px.y * 2.0 / resolution.y);
            }

            vec2 translateScale(vec2 pos) {
              return vec2(pos.x * uSizePos.x + uSizePos.z, pos.y * uSizePos.y + uSizePos.w);
            }
            
            void main() {
              vec2 pos = translateScale(vPos);
              float lX = mix(vPos.x, vTex.x, vTex.y);
              float rX = mix(vTex.x, vPos.x, vTex.y);
              outScreenPos = glToPixel(pos, uResolution.xy);
              if (uParameters.x > 0.5) {
                outScreenPos.x = mix(floor(outScreenPos.x), ceil(outScreenPos.x), vTex.y);
                pos = pixelToGl(outScreenPos, uResolution.xy);
              }
              textureUV = vTex;
              gl_Position = vec4(pos, 0.0, 1.0);
            }""";
  }

  static String psCode() {
    return shaderHeader + psShaderPrecision +
        """
            layout(location = 0) out vec4 outColor;
            uniform vec4 uColor;
            in vec2 textureUV;
            in vec2 outScreenPos;
            void main() {
              float f = fract(outScreenPos.y);
              float g = outScreenPos.y - trunc(outScreenPos.y);
              float a = textureUV.y * 0.5 + 0.5;
              outColor = vec4(uColor.xyz * a, 1.0);
            }""";
  }

  void setColor(GLApi.Context gl, V4f color) {
    gl.uniform4f(uColor, color);
  }

  void setParameters(GLApi.Context gl, float x, float y) {
    gl.uniform2f(uParameters, x, y);
  }
}
