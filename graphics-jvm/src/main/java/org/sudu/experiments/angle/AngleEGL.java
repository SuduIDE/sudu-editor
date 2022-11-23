package org.sudu.experiments.angle;

import org.sudu.experiments.CString;
import org.sudu.experiments.math.V2i;

public class AngleEGL {
  public static final long EGL_NO_DISPLAY = 0;
  public static final long EGL_NO_SURFACE = 0;

  public static final int EGL_FALSE      = 0;
  public static final int EGL_TRUE       = 1;

  public static final int EGL_SUCCESS                                = 0x3000;
  public static final int EGL_NOT_INITIALIZED                        = 0x3001;
  public static final int EGL_BAD_MATCH                              = 0x3009;
  public static final int EGL_ALPHA_SIZE                             = 0x3021;
  public static final int EGL_BLUE_SIZE                              = 0x3022;
  public static final int EGL_GREEN_SIZE                             = 0x3023;
  public static final int EGL_RED_SIZE                               = 0x3024;
  public static final int EGL_NONE                                   = 0x3038;
  public static final int EGL_PLATFORM_ANGLE_ANGLE                   = 0x3202;
  public static final int EGL_PLATFORM_ANGLE_TYPE_ANGLE              = 0x3203;
  public static final int EGL_PLATFORM_ANGLE_MAX_VERSION_MAJOR_ANGLE = 0x3204;
  public static final int EGL_PLATFORM_ANGLE_MAX_VERSION_MINOR_ANGLE = 0x3205;
  public static final int EGL_PLATFORM_ANGLE_TYPE_D3D11_ANGLE        = 0x3208;
  public static final int EGL_HEIGHT                                 = 0x3056;
  public static final int EGL_WIDTH                                  = 0x3057;
  public static final int EGL_CONTEXT_CLIENT_VERSION                 = 0x3098;
  public static final int EGL_CONTEXT_OPENGL_DEBUG                   = 0x31B0;

  public static final int EGL_CLIENT_APIS                            = 0x308D;
  public static final int EGL_EXTENSIONS                             = 0x3055;
  public static final int EGL_VENDOR                                 = 0x3053;
  public static final int EGL_VERSION                                = 0x3054;

  public static native int getError();
  // returns EGLDisplay
  public static native long getPlatformDisplay(int platform, long native_display, long[] attrib_list);
  public static native boolean initialize(long display);
  public static native boolean terminate(long display);
  public static native boolean chooseConfig(long display, int[] attrib_list, long[] configs, int[] num_config);
  public static native long queryString(long display, int name);

  public static String getString(long display, int name) {
    return CString.fromNativeStringNullable(queryString(display, name));
  }

  // returns EGLSurface
  public static native long createWindowSurface(long display, long config, long hWnd, int[] attrib_list);
  public static native boolean destroySurface(long display, long surface);

  // return EGLContext
  public static native long createContext(long display, long config, long share_context, int[] attrib_list);

  public static native boolean destroyContext(long display, long context);

  public static native boolean makeCurrent(long display, long draw, long read, long context);

  public static native long getCurrentContext();

  public static native boolean swapInterval(long display, int interval);
  public static native boolean swapBuffers(long display, long surface);
  public static native boolean querySurface(long display, long surface, int attribute, int[] value);

  // Queries EGL_WIDTH and EGL_HEIGHT
  //   widthHeight.length >= 2
  public static native boolean querySurfaceSize(long display, long surface, int[] widthHeight);

  public static long getPlatformDisplayD3D11(long hDC) {
    long[] attrib_list = {
        EGL_PLATFORM_ANGLE_TYPE_ANGLE, EGL_PLATFORM_ANGLE_TYPE_D3D11_ANGLE,
        EGL_PLATFORM_ANGLE_MAX_VERSION_MAJOR_ANGLE, 11,
        EGL_PLATFORM_ANGLE_MAX_VERSION_MINOR_ANGLE, 1,
        EGL_NONE,
    };
    return getPlatformDisplay(EGL_PLATFORM_ANGLE_ANGLE, hDC, attrib_list);
  }

  public static int[] rgba8888Attributes() {
    return a(
        EGL_RED_SIZE, 8,
        EGL_GREEN_SIZE, 8,
        EGL_BLUE_SIZE, 8,
        EGL_ALPHA_SIZE, 8,
        // EGL_DEPTH_SIZE, 24, EGL_STENCIL_SIZE, 8,
        // EGL_SAMPLE_BUFFERS, 1, EGL_SAMPLES, 8
        EGL_NONE);
  }

  public static long chooseConfig8888(long display) {
    long[] configs = new long[1];
    int[] numConfig = new int[1];
    boolean chooseConfig = AngleEGL.chooseConfig(
        display, rgba8888Attributes(), configs, numConfig);
    return chooseConfig && numConfig[0] == 1 ? configs[0] : 0;
  }

  public static long createContext(long display, long config, long share_context, boolean debug) {
    return createContext(display, config, share_context,
        a(EGL_CONTEXT_CLIENT_VERSION, 3,
            EGL_CONTEXT_OPENGL_DEBUG, debug ? EGL_TRUE : EGL_FALSE, EGL_NONE));
  }

  public static V2i querySurfaceSize(long display, long surface) {
    int[] value = new int[2];
    return querySurfaceSize(display, surface, value) ? new V2i(value[0], value[1]) : null;
  }

  public static String getErrorString() {
    return errorToString(getError());
  }

  public static String errorToString(int error) {
    return switch (error) {
      case EGL_SUCCESS -> "EGL_SUCCESS";
      case EGL_BAD_MATCH -> "EGL_BAD_MATCH";
      case EGL_NOT_INITIALIZED -> "EGL_NOT_INITIALIZED";
      default -> "EGL_ERROR_0x".concat(Integer.toHexString(error));
    };
  }

  public static void dumpError() {
    int error = AngleEGL.getError();
    if (error != AngleEGL.EGL_SUCCESS) {
      System.err.println("EGL_GetError = " + AngleEGL.errorToString(error));
    }
  }

  static int[] a(int ... a) { return a; }
}
