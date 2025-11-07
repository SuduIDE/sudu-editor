#define LIBGLESV2_IMPLEMENTATION true

#include <entry_points_gles_2_0_autogen.h>
#include <entry_points_gles_3_0_autogen.h>
#include <entry_points_egl_autogen.h>
#include <entry_points_egl_ext_autogen.h>

void GL_ActiveTexture(GLenum texture) {}

void GL_AttachShader(GLuint program, GLuint shader) {}

void GL_BindAttribLocation(GLuint program, GLuint index, const GLchar *name) {}

void GL_BindTexture(GLenum target, GLuint texture) {}

void GL_Clear(GLbitfield mask) {}

void GL_ClearColor(GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha) {}

const GLubyte * GL_GetString(GLenum name) { return 0; }

void GL_GetFloatv(GLenum name, GLfloat *data) {}

void GL_GetIntegerv(GLenum name, GLint *data) {}

GLuint GL_CreateShader(GLenum type) { return 0; }

GLuint GL_CreateProgram() { return 0; }

void GL_DeleteShader(GLuint shader) {}

void GL_DeleteTextures(GLsizei n, const GLuint *textures) {}

void GL_Disable(GLenum cap) {}

void GL_DisableVertexAttribArray(GLuint index) {}

void GL_DrawArrays(GLenum mode, GLint first, GLsizei count) {}

void GL_DrawElements(GLenum mode, GLsizei count, GLenum type, const void *indices) {}

void GL_ShaderSource(GLuint shader, GLsizei count, const GLchar *const *string, const GLint *length) {}

void GL_CompileShader(GLuint shader) {}

void GL_GetShaderiv(GLuint shader, GLenum pname, GLint *params) {}

void GL_GetShaderInfoLog(GLuint shader, GLsizei bufSize, GLsizei *length, GLchar *infoLog) {}
void GL_GetProgramInfoLog(GLuint program, GLsizei bufSize, GLsizei* length, GLchar* infoLog) {}

void GL_DeleteProgram(GLuint program) {}

void GL_LinkProgram(GLuint program) {}

void GL_GetProgramiv(GLuint program, GLenum pname, GLint *params) {}

void GL_UseProgram(GLuint program) {}

GLint GL_GetUniformLocation(GLuint program, const GLchar *name) { return 0; }

void GL_GenBuffers(GLsizei n, GLuint *buffers) {}

void GL_DeleteBuffers(GLsizei n, const GLuint *buffers) {}

void GL_BindBuffer(GLenum target, GLuint buffer) {}

void GL_BlendFuncSeparate(GLenum sfactorRGB, GLenum dfactorRGB, GLenum sfactorAlpha, GLenum dfactorAlpha) {}

void GL_BufferData(GLenum target, GLsizeiptr size, const void *data, GLenum usage) {}

void GL_GenTextures(GLsizei n, GLuint *textures) {}

void GL_TexParameteri(GLenum target, GLenum pname, GLint param) {}

void GL_TexStorage2D(GLenum target, GLsizei levels, GLenum internalformat,
                     GLsizei width, GLsizei height) {}

void GL_TexSubImage2D(GLenum target, GLint level, GLint xoffset, GLint yoffset,
                      GLsizei width, GLsizei height, GLenum format, GLenum type, const void *pixels) {}

void GL_Uniform1i(GLint location, GLint v0) {}
void GL_Uniform2f(GLint location, GLfloat v0, GLfloat v1) {}
void GL_Uniform4f(GLint location, GLfloat v0, GLfloat v1, GLfloat v2, GLfloat v3) {}

GLenum GL_GetError() { return 0; }

void GL_Viewport(GLint x, GLint y, GLsizei width, GLsizei height) {}
void GL_Scissor(GLint x, GLint y, GLsizei width, GLsizei height) {}

void GL_VertexAttribPointer(GLuint index, GLint size, GLenum type, GLboolean normalized,
                            GLsizei stride, const void *pointer) {}

void GL_Enable(GLenum cap) {}

void GL_EnableVertexAttribArray(GLuint index) {}

void GL_Finish() {}

void GL_Flush() {}

void GL_GetRenderbufferParameteriv(GLenum target, GLenum pname, GLint *params) {}

EGLDisplay EGL_GetPlatformDisplay(EGLenum platform,
                                 void *native_display,
                                 const EGLAttrib *attrib_list){ return 0; }

EGLBoolean EGL_Initialize(EGLDisplay dpy, EGLint *major, EGLint *minor) { return 0; }
EGLBoolean EGL_Terminate(EGLDisplay dpy) { return 0; }

EGLBoolean EGL_ChooseConfig(EGLDisplay dpy,
                            const EGLint *attrib_list,
                            EGLConfig *configs,
                            EGLint config_size,
                            EGLint *num_config) { return 0; }

EGLSurface EGL_CreateWindowSurface(EGLDisplay dpy,
                                   EGLConfig config,
                                   EGLNativeWindowType win,
                                   const EGLint *attrib_list) { return 0; }


EGLBoolean EGL_DestroySurface(EGLDisplay dpy, EGLSurface surface) { return 0; }

EGLContext EGL_CreateContext(EGLDisplay dpy,
                             EGLConfig config,
                             EGLContext share_context,
                             const EGLint *attrib_list) { return 0; }


EGLBoolean EGL_DestroyContext(EGLDisplay dpy, EGLContext ctx) { return 0; }

EGLBoolean EGL_MakeCurrent(EGLDisplay dpy, EGLSurface draw, EGLSurface read, EGLContext ctx) { return 0; }

EGLContext EGL_GetCurrentContext() { return 0; }

EGLint EGL_GetError() { return 0; }

EGLBoolean EGL_SwapInterval(EGLDisplay dpy, EGLint interval) { return 0; }
EGLBoolean EGL_SwapBuffers(EGLDisplay dpy, EGLSurface surface) { return 0; }

EGLBoolean EGL_QuerySurface(EGLDisplay dpy, EGLSurface surface, EGLint attribute, EGLint* value) { return 0; }

const char * EGL_QueryString(EGLDisplay dpy, EGLint name) { return 0; }

EGLSurface EGL_CreatePbufferSurface(EGLDisplay dpy, EGLConfig config, const EGLint *attrib_list) { return 0; }

EGLDisplay EGL_GetPlatformDisplayEXT(EGLenum platform, void *native_display, const EGLint *attrib_list) { return 0; }

void GL_ReadPixels(GLint x, GLint y, GLsizei width, GLsizei height, GLenum format, GLenum type, void *pixels) {}

