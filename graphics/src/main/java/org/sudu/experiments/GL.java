package org.sudu.experiments;

import org.sudu.experiments.math.V2i;

public interface GL {
  boolean checkErrorOnTextureUpdate = false;
  boolean checkErrorOnShaderLink = true;
  boolean checkErrorOnMeshCreate = false;
  boolean checkErrorOnMeshDispose = false;

  // only TRIANGLE_LIST for now
  class Mesh {
    final GLApi.Context gl;
    final VertexLayout layout;
    private GLApi.Buffer vb, bb, ib;
    private int nVertices, nIndices;

    public Mesh(GLApi.Context gl, VertexLayout layout, float[] fData, char[] index) {
      this(gl, layout, fData, null, index);
    }

    public Mesh(GLApi.Context gl, VertexLayout layout, float[] fData, byte[] bData, char[] index) {
      this.gl = gl;
      this.layout = layout;
      this.nVertices = fData.length / layout.floatSize;
      this.nIndices = index != null ? index.length : 0;

      vb = gl.createBuffer();
      gl.bufferData(vb, fData, GLApi.Context.ARRAY_BUFFER);

      bb = bData != null ? gl.createBuffer() : null;
      if (bb != null) gl.bufferData(bb, bData, GLApi.Context.ARRAY_BUFFER);

      ib = index != null ? gl.createBuffer() : null;
      if (ib != null) gl.bufferData(ib, index, GLApi.Context.ELEMENT_ARRAY_BUFFER);

      gl.bindBuffer(gl.ARRAY_BUFFER, null);
      gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, null);

      if (checkErrorOnMeshCreate) {
        System.out.println("Mesh::Mesh exit: " + "numVertices = " + nVertices +
            ", numPrimitives = " + nIndices);
        gl.checkError("error = ");
      }
    }

    // todo: performance: separate bind and draw calls, to avoid binding of already bound buffer
    public int draw(int currentAttributes) {
      currentAttributes = bindAttributes(currentAttributes, layout.attributeMask, gl);
      bindData();
      if (ib != null) {
        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, ib);
        gl.drawElements(gl.TRIANGLES, nIndices, gl.UNSIGNED_SHORT, 0);
      } else if (nVertices > 0) {
        gl.drawArrays(gl.TRIANGLES, 0, nVertices);
      }
      return currentAttributes;
    }

    public void dispose() {
      vb = deleteBuffer(vb);
      bb = deleteBuffer(bb);
      ib = deleteBuffer(ib);
      nVertices = nIndices = 0;
      if (checkErrorOnMeshDispose) {
        gl.checkError("Mesh::dispose exit, error = ");
      }
    }

    private GLApi.Buffer deleteBuffer(GLApi.Buffer buffer) {
      if (buffer != null) {
        gl.deleteBuffer(buffer);
      }
      return null;
    }

    public static int bindAttributes(int currentAttributes, int requiredAttributes, GLApi.Context gl) {
      int diff = currentAttributes ^ requiredAttributes;
      for (int i = 0; diff != 0; i++) {
        int mask = 1 << i;
        if ((diff & mask) != 0) {
          if ((requiredAttributes & mask) != 0) {
            gl.enableVertexAttribArray(i);
          } else {
            gl.disableVertexAttribArray(i);
          }
          diff ^= mask;
        }
      }

      return requiredAttributes;
    }

    private void bindData() {
      int floatOffset = 0;
      gl.bindBuffer(gl.ARRAY_BUFFER, vb);

      for (VertexAttribute attr : layout.floatAttributes) {
        gl.vertexAttribPointer(attr.index, attr.size, gl.FLOAT, false, layout.floatSize * 4, floatOffset * 4);
        floatOffset += attr.size;
      }

      if (bb != null) {
        int byteOffset = 0;
        gl.bindBuffer(gl.ARRAY_BUFFER, bb);
        for (VertexAttribute attribute : layout.byteAttributes) {
          gl.vertexAttribPointer(attribute.index, attribute.size, gl.UNSIGNED_BYTE, attribute.normalized, layout.byteSize, byteOffset);
          byteOffset += attribute.size;
        }
      }
    }
  }

  class TextureContext {
    final GLApi.Context gl;

    TextureContext(GLApi.Context gl) {
      this.gl = gl;
    }
  }

  class Texture implements Disposable {
    static int globalCounter;

    final TextureContext ctx;
    GLApi.Texture texture;
    final V2i size = new V2i();

    Texture(TextureContext ctx) {
      this.ctx = ctx;
      texture = ctx.gl.createTexture();
      globalCounter++;
    }

    @Override
    public void dispose() {
      if (texture != null) {
        globalCounter--;
        ctx.gl.deleteTexture(texture);
        texture = null;
      }
    }

    private void getNewHandle() {
      ctx.gl.deleteTexture(texture);
      texture = ctx.gl.createTexture();
    }

    public int width() { return size.x; }

    public int height() { return size.y; }

    public V2i size() { return size; }

    public void allocate(int width, int height) {
      allocate(width, height, GLApi.Context.RGBA8);
    }

    public void allocate(int width, int height, int internalformat) {
      size.x = width;
      size.y = height;
      bind();
      ctx.gl.texStorage2D(GLApi.Context.TEXTURE_2D, 1, internalformat, width, height);
      setupSampler();
    }

    private void bind() {
      ctx.gl.bindTexture(GLApi.Context.TEXTURE_2D, texture);
    }

    public void setContent(ImageData image) {
      checkSizeAndAllocate(image.width, image.height, gl2InternalFormat(image.format));
      ctx.gl.texSubImage2D(GLApi.Context.TEXTURE_2D, 0,
          0, 0,
          image.width, image.height,
          gl2Format(image.format), GLApi.Context.UNSIGNED_BYTE, image.data);
      if (checkErrorOnTextureUpdate) ctx.gl.checkError("Texture.setContent(ImageData image): ");
    }

    public void setContent(Canvas canvas) {
      checkSizeAndAllocate(canvas.width, canvas.height, GLApi.Context.RGBA8);
      doUpdate(canvas, 0, 0);
    }

    private void checkSizeAndAllocate(int newWidth, int newHeight, int internalformat) {
      if (size.x == 0 || size.y == 0) {
        allocate(newWidth, newHeight, internalformat);
      } else {
        if (size.equals(newWidth, newHeight)) {
          bind();
        } else {
          getNewHandle();
          allocate(newWidth, newHeight, internalformat);
        }
      }
    }

    public void update(Canvas canvas, int xOffset, int yOffset) {
      bind();
      doUpdate(canvas, xOffset, yOffset);
    }

    private void doUpdate(Canvas canvas, int xOffset, int yOffset) {
      ctx.gl.texSubImage2D(GLApi.Context.TEXTURE_2D, 0, xOffset, yOffset,
        GLApi.Context.RGBA, GLApi.Context.UNSIGNED_BYTE, canvas);
      if (checkErrorOnTextureUpdate) ctx.gl.checkError("Texture.setContent(Canvas): ");
    }

    interface Updater {
      void texSubImage2dRgba(Texture t, GLApi.Context gl);
    }

    void setContent(int width, int height, Updater updateTexture) {
      allocate(width, height);
      updateTexture.texSubImage2dRgba(this, ctx.gl);
    }

    void setupSampler() {
      GLApi.Context gl = ctx.gl;
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
      gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    }

    static int gl2InternalFormat(ImageData.Format f) {
      return switch (f) {
        case GRAYSCALE -> GLApi.Context.R8;
        case RGBA -> GLApi.Context.RGBA8;
      };
    }

    static int gl2Format(ImageData.Format f) {
      return switch (f) {
        case GRAYSCALE -> GLApi.Context.RED;
        case RGBA -> GLApi.Context.RGBA;
      };
    }
  }

  class ImageData {

    enum Format { GRAYSCALE, RGBA }

    public final Format format;
    public final byte[] data;
    public final int width, height;

    public ImageData(int width, int height, Format f) {
      this.format = f;
      this.width = width;
      this.height = height;
      this.data = new byte[bytesPerLine(width, format) * height];
    }

    public ImageData(int width, int height) {
      this(width, height, Format.RGBA);
    }

    public static int bytesPerLine(int width, Format format) {
      return format == Format.RGBA ? width * 4 : align4(width);
    }

    static int align4(int x) {
      return (x + 3) & ~3;
    }
  }

  enum VertexAttribute {
    POS2("vPos", Type.FLOAT, 2, false, 0),
    TEX2("vTex", Type.FLOAT, 2, false, 1);

    final String name;
    final Type type;
    final int size;
    final boolean normalized;
    final int index;

    VertexAttribute(String name, Type type, int size, boolean normalized, int index) {
      this.name = name;
      this.type = type;
      this.size = size;
      this.normalized = normalized;
      this.index = index;
    }

    enum Type { BYTE, FLOAT }
  }

  enum VertexLayout {
    POS2_UV2( VertexAttribute.POS2, VertexAttribute.TEX2);
    public final VertexAttribute[] attributes;
    public final VertexAttribute[] floatAttributes, byteAttributes;
    public final int floatSize, byteSize;
    public final int attributeMask;

    VertexLayout(VertexAttribute ... attr) {
      int fSize = 0, bSize = 0, nF = 0, nB = 0, mask = 0;
      for (VertexAttribute a : attr) {
        switch (a.type) {
          case BYTE ->  { bSize += a.size; nB++; }
          case FLOAT -> { fSize += a.size; nF++; }
        }
        mask |= 1 << a.index;
      }
      attributes = attr;
      floatSize = fSize;
      byteSize = bSize;
      attributeMask = mask;
      floatAttributes = new VertexAttribute[nF];
      byteAttributes = new VertexAttribute[nB];
      nF = nB = 0;
      for (VertexAttribute a : attr) {
        switch (a.type) {
          case FLOAT ->  floatAttributes[nF++] = a;
          case BYTE -> byteAttributes[nB++] = a;
        }
      }
    }
  }

  class Program {
    final GLApi.Program program;
    final VertexLayout layout;

    protected Program(GLApi.Context gl, String vsCode, String psCode, VertexLayout layout) {
      this.layout = layout;
      program = compileProgram(gl, vsCode, psCode);
      for (VertexAttribute attr : layout.attributes) {
        gl.bindAttribLocation(program, attr.index, attr.name);
      }
      linkProgram(gl, program);
      if (checkErrorOnShaderLink) gl.checkError("compileProgram exit: ");
    }

    static GLApi.Program compileProgram(GLApi.Context gl, String vsCode, String psCode) {
//      System.out.println("vsCode = " + vsCode);
      GLApi.Shader vs = compileShader(gl, gl.VERTEX_SHADER, vsCode);
//      System.out.println("psCode = " + psCode);
      GLApi.Shader ps = compileShader(gl, gl.FRAGMENT_SHADER, psCode);
      GLApi.Program program = gl.createProgram();
      gl.attachShader(program, vs);
      gl.attachShader(program, ps);
      gl.deleteShader(vs);
      gl.deleteShader(ps);
      return program;
    }

    static GLApi.Shader compileShader(GLApi.Context gl, int type, String source) {
      GLApi.Shader shader = gl.createShader(type);
      gl.shaderSource(shader, source);
      gl.compileShader(shader);
      // todo: startup performance note
      //   it is not recommended to query COMPILE_STATUS right after GLContext.compileShader
      //   it is better to do this only after GLContext.linkProgram and only if LINK_STATUS is bad
      //   https://developer.mozilla.org/en-US/docs/Web/API/WebGL_API/WebGL_best_practices#compile_shaders_and_link_programs_in_parallel
      if (gl.getShaderParameteri(shader, gl.COMPILE_STATUS) == 0) {
        String h = type == GLApi.Context.VERTEX_SHADER ? "vertex shader error: " : "pixel shader error: ";
        String message = h + gl.getShaderInfoLog(shader);
        gl.deleteShader(shader);
        System.out.println(message);
        throw new RuntimeException(message);
      }
      return shader;
    }

    static void linkProgram(GLApi.Context gl, GLApi.Program program) {
      gl.linkProgram(program);
      if (gl.getProgramParameteri(program, gl.LINK_STATUS) == 0) {
        String infoLog = gl.getProgramInfoLog(program);
        gl.deleteProgram(program);
        throw new RuntimeException("vs <-> ps link error: " + infoLog);
      }
    }
  }

  static Mesh createRectangle(GLApi.Context gl) {
    float[] vbData = { 1, -1, 1,1, /**/ 1,1, 1,0, /**/ -1,-1, 0,1, /**/ -1,1, 0,0  };
    char[] index = { 0, 1, 2, /**/ 1, 2, 3, /**/  /* 0, 2, 1, */ /**/ /* 1, 3, 2 */ };
    return new Mesh(gl, VertexLayout.POS2_UV2, vbData, index);
  }

}
