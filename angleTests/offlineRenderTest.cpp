
#define EGL_EGLEXT_PROTOTYPES

#include <entry_points_egl_autogen.h>
#include <entry_points_egl_ext_autogen.h>
#include <entry_points_gles_2_0_autogen.h>

#include <EGL/egl.h>
#include <EGL/eglext.h>
#include <GLES2/gl2.h>
#include <stdio.h>
#include <stdlib.h>



#ifdef EGL_EGLEXT_PROTOTYPES
  #pragma message("EGL_EGLEXT_PROTOTYPES defined")
#else
  #pragma message("EGL_EGLEXT_PROTOTYPES not defined")
#endif


#if !defined(_WIN32)
// BMP file header structures (same as before)
typedef struct {
    unsigned short bfType;
    unsigned int bfSize;
    unsigned short bfReserved1;
    unsigned short bfReserved2;
    unsigned int bfOffBits;
} BITMAPFILEHEADER;

typedef struct {
    unsigned int biSize;
    int biWidth;
    int biHeight;
    unsigned short biPlanes;
    unsigned short biBitCount;
    unsigned int biCompression;
    unsigned int biSizeImage;
    int biXPelsPerMeter;
    int biYPelsPerMeter;
    unsigned int biClrUsed;
    unsigned int biClrImportant;
} BITMAPINFOHEADER;

#else
  #pragma message("_WIN32 defined")
#endif

int main() {

EGLAttrib displayAttribs[] = { 
   EGL_PLATFORM_ANGLE_TYPE_ANGLE, EGL_PLATFORM_ANGLE_TYPE_D3D11_ANGLE,
   EGL_PLATFORM_ANGLE_MAX_VERSION_MAJOR_ANGLE, 11,
   EGL_PLATFORM_ANGLE_MAX_VERSION_MINOR_ANGLE, 1,
   EGL_NONE
}; 

    const char * sEGL_VERSION = (const char *)EGL_QueryString(0, EGL_VERSION);
    printf("sEGL_VERSION = %s\n", sEGL_VERSION);

    // Initialize EGL without a window
//    EGL_GetPlatformDisplay; EGL_GetPlatformDisplayEXT;                  EGL_DEFAULT_DISPLAY
    EGLDisplay display = EGL_GetPlatformDisplay(EGL_PLATFORM_ANGLE_ANGLE, 0, displayAttribs);

    printf("sizeof(EGLAttrib) = %zd\n", sizeof(EGLAttrib));
    printf("sizeof(GLsizei) = %zd\n", sizeof(GLsizei));

    printf("EGL display %p\n", display);

    if (display == EGL_NO_DISPLAY) {
        printf("Failed to get EGL display\n");
        return 1;
    }

    EGLint major, minor;
    if (!EGL_Initialize(display, &major, &minor)) {
        printf("Failed to initialize EGL\n");
        return 1;
    }

    // Choose EGL configuration
    EGLint attribs[] = {
        EGL_SURFACE_TYPE, EGL_PBUFFER_BIT,
        EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
        EGL_RED_SIZE, 8,
        EGL_GREEN_SIZE, 8,
        EGL_BLUE_SIZE, 8,
        EGL_ALPHA_SIZE, 8,
        EGL_NONE
    };

    EGLConfig config;
    EGLint numConfigs;
    if (!EGL_ChooseConfig(display, attribs, &config, 1, &numConfigs)) {
        printf("Failed to choose EGL config\n");
        return 1;
    }

    // Create offscreen surface
    EGLint pbufferAttribs[] = {
        EGL_WIDTH, 512,
        EGL_HEIGHT, 512,
        EGL_NONE
    };

    EGLSurface surface = EGL_CreatePbufferSurface(display, config, pbufferAttribs);
    if (surface == EGL_NO_SURFACE) {
        printf("Failed to create EGL surface\n");
        return 1;
    }

    // Create EGL context
    EGLContext context = EGL_CreateContext(display, config, EGL_NO_CONTEXT, NULL);
    if (context == EGL_NO_CONTEXT) {
        printf("Failed to create EGL context\n");
        return 1;
    }

    // Make context current
    if (!EGL_MakeCurrent(display, surface, surface, context)) {
        printf("Failed to make EGL context current\n");
        return 1;
    }

    // Render something (example: red triangle)
    GL_ClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    GL_Clear(GL_COLOR_BUFFER_BIT);

    // Read pixels from EGL surface
    unsigned char* pixels = new unsigned char[512 * 512 * 4];
    GL_ReadPixels(0, 0, 512, 512, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

    // Clean up EGL
    EGL_MakeCurrent(display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
    EGL_DestroySurface(display, surface);
    EGL_DestroyContext(display, context);
    EGL_Terminate(display);

    // Convert and save to BMP
    BITMAPFILEHEADER bfh = {0};
    bfh.bfType = 0x4D42;
    bfh.bfSize = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + 512 * 512 * 4;
    bfh.bfReserved1 = 0;
    bfh.bfReserved2 = 0;
    bfh.bfOffBits = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);

    BITMAPINFOHEADER bih = {0};
    bih.biSize = sizeof(BITMAPINFOHEADER);
    bih.biWidth = 512;
    bih.biHeight = 512;
    bih.biPlanes = 1;
    bih.biBitCount = 32;
    bih.biCompression = BI_RGB;
    bih.biSizeImage = 512 * 512 * 4;

    FILE* file = fopen("output.bmp", "wb");
    if (file) {
        fwrite(&bfh, sizeof(BITMAPFILEHEADER), 1, file);
        fwrite(&bih, sizeof(BITMAPINFOHEADER), 1, file);
        // BMP stores pixels top-to-bottom, so we need to flip vertically
        for (int y = 0; y < 512; y++) {
            unsigned char* row = pixels + (511 - y) * 512 * 4;
            fwrite(row, 512 * 4, 1, file);
        }
        fclose(file);
    }

    delete[] pixels;

    return 0;
}