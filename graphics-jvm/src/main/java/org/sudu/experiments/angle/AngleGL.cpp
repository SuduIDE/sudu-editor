#include "org_sudu_experiments_angle_AngleGL.h"
#include "entry_points_gles_2_0_autogen.h"
#include "entry_points_gles_3_0_autogen.h"

#include "org_sudu_experiments_angle_AngleEGL.h"
#include "entry_points_egl_autogen.h"

static_assert(sizeof(EGLAttrib) == sizeof(jlong), "Fatal: sizeof(EGLAttrib) != sizeof(jlong)");
static_assert(sizeof(EGLint) == sizeof(jint), "Fatal: sizeof(EGLint) != sizeof(jint)");
static_assert(sizeof(GLenum) == sizeof(jint), "Fatal: sizeof(GLenum) != sizeof(jint)");
static_assert(sizeof(GLuint) == sizeof(jint), "Fatal: sizeof(GLuint) != sizeof(jint)");
static_assert(sizeof(GLchar) == sizeof(jbyte), "Fatal: sizeof(GLchar) != sizeof(jbyte)");

typedef EGLint * PEGLint;
typedef EGLint const* PCEGLint;
typedef GLchar * PGLchar;

jint Java_org_sudu_experiments_angle_AngleGL_glCreateShader(JNIEnv *, jclass, jint type) {
  return GL_CreateShader(type);
}

void Java_org_sudu_experiments_angle_AngleGL_glDeleteShader(JNIEnv *, jclass, jint shader) {
  GL_DeleteShader(shader);
}

void Java_org_sudu_experiments_angle_AngleGL_glShaderSource(JNIEnv*j, jclass, jint shader, jbyteArray jArray) {
  auto source = j->GetPrimitiveArrayCritical(jArray, 0);
  GLchar const* pointers[1] = { PGLchar(source) };
  GL_ShaderSource(shader, 1, pointers, 0);
  j->ReleasePrimitiveArrayCritical(jArray, source, 0);
}

void Java_org_sudu_experiments_angle_AngleGL_glCompileShader(JNIEnv *, jclass, jint shader) {
  GL_CompileShader(shader);
}

jint Java_org_sudu_experiments_angle_AngleGL_glCreateProgram(JNIEnv *, jclass) {
  return GL_CreateProgram();
}

void Java_org_sudu_experiments_angle_AngleGL_glDeleteProgram(JNIEnv*, jclass, jint program) {
  GL_DeleteProgram(program);
}

void Java_org_sudu_experiments_angle_AngleGL_glLinkProgram(JNIEnv*, jclass, jint program) {
  GL_LinkProgram(program);
}

extern "C" JNIEXPORT jint sudu_glGetProgramiv(jint program, jint name) {
  GLint value[1] = { 0 };
  GL_GetProgramiv(program, name, value);
  return value[0];
}

jint Java_org_sudu_experiments_angle_AngleGL_getProgramiv(JNIEnv*, jclass, jint program, jint name) {
  return sudu_glGetProgramiv(program, name);
}

extern "C" JNIEXPORT jint sudu_glGetShaderiv(jint shader, jint name) {
  GLint value[1] = { 0 };
  GL_GetShaderiv(shader, name, value);
  return value[0];
}

jint Java_org_sudu_experiments_angle_AngleGL_glGetShaderiv(JNIEnv *, jclass, jint shader, jint name) {
  return sudu_glGetShaderiv(shader, name);
}

void Java_org_sudu_experiments_angle_AngleGL_glAttachShader(JNIEnv*, jclass, jint program, jint shader) {
  GL_AttachShader(program, shader);
}

jint Java_org_sudu_experiments_angle_AngleGL_glGetShaderInfoLog(JNIEnv *j, jclass, jint shader, jbyteArray jArray) {
  GLsizei result[1] = {0};
  auto infoLog = j->GetPrimitiveArrayCritical(jArray, 0);
  auto infoLength = j->GetArrayLength(jArray);
  GL_GetShaderInfoLog(shader, infoLength, result, PGLchar(infoLog));
  j->ReleasePrimitiveArrayCritical(jArray, infoLog, 0);
  return result[0];
}

jint Java_org_sudu_experiments_angle_AngleGL_glGetProgramInfoLog(JNIEnv *j, jclass, jint program, jbyteArray jArray) {
  GLsizei result[1] = {0};
  auto infoLog = j->GetPrimitiveArrayCritical(jArray, 0);
  auto infoLength = j->GetArrayLength(jArray);
  GL_GetProgramInfoLog(program, infoLength, result, PGLchar(infoLog));
  j->ReleasePrimitiveArrayCritical(jArray, infoLog, 0);
  return result[0];
}

void Java_org_sudu_experiments_angle_AngleGL_glBindAttribLocation(
  JNIEnv* j, jclass, jint program, jint index, jbyteArray jName
) {
  auto name = j->GetPrimitiveArrayCritical(jName, 0);
  GL_BindAttribLocation(program, index, PGLchar(name));
  j->ReleasePrimitiveArrayCritical(jName, name, 0);
}

void Java_org_sudu_experiments_angle_AngleGL_glUseProgram(JNIEnv*, jclass, jint program) {
  GL_UseProgram(program);
}

jint Java_org_sudu_experiments_angle_AngleGL_glGetUniformLocation(JNIEnv* j, jclass, jint program, jbyteArray jName) {
  auto name = j->GetPrimitiveArrayCritical(jName, 0);
  auto r = GL_GetUniformLocation(program, PGLchar(name));
  j->ReleasePrimitiveArrayCritical(jName, name, 0);
  return r;
}

extern "C" JNIEXPORT jint sudu_glGenBuffer() {
  GLuint buffer[1] = { 0 };
  GL_GenBuffers(1, buffer);
  return buffer[0];
}

jint Java_org_sudu_experiments_angle_AngleGL_glGenBuffer(JNIEnv*, jclass) {
  return sudu_glGenBuffer();
}

extern "C" JNIEXPORT void sudu_glDeleteBuffer(jint buffer) {
  GLuint buffers[1] = { GLuint(buffer) };
  GL_DeleteBuffers(1, buffers);
}

void Java_org_sudu_experiments_angle_AngleGL_glDeleteBuffer(JNIEnv *, jclass, jint buffer) {
  sudu_glDeleteBuffer(buffer);
}

void Java_org_sudu_experiments_angle_AngleGL_glBindBuffer(JNIEnv *, jclass, jint target, jint buffer) {
  GL_BindBuffer(target, buffer);
}

inline void sudu_glBufferData(JNIEnv *j, jint target, size_t elementSize, jarray jData, jint usage) {
  auto data = j->GetPrimitiveArrayCritical(jData, 0);
  auto length = j->GetArrayLength(jData);
  GL_BufferData(target, elementSize * length, data, usage);
  j->ReleasePrimitiveArrayCritical(jData, data, 0);
}

void Java_org_sudu_experiments_angle_AngleGL_glBufferData__I_3FI(
  JNIEnv *j, jclass, jint target, jfloatArray jData, jint usage
) { sudu_glBufferData(j, target, sizeof(jfloat), jData, usage); }

void Java_org_sudu_experiments_angle_AngleGL_glBufferData__I_3CI(
  JNIEnv *j, jclass, jint target, jcharArray jData, jint usage
) { sudu_glBufferData(j, target, sizeof(jchar), jData, usage); }

void Java_org_sudu_experiments_angle_AngleGL_glBufferData__I_3BI(
  JNIEnv *j, jclass, jint target, jbyteArray jData, jint usage
) { sudu_glBufferData(j, target, sizeof(jbyte), jData, usage); }

extern "C" JNIEXPORT jint sudu_glGenTexture() {
  GLuint textures[1] = { 0 };
  GL_GenTextures(1, textures);
  return textures[0];
}

jint Java_org_sudu_experiments_angle_AngleGL_glGenTexture(JNIEnv *, jclass) {
  return sudu_glGenTexture();
}

void Java_org_sudu_experiments_angle_AngleGL_glDeleteTexture(JNIEnv *, jclass, jint texture) {
  GLuint textures[1] = { GLuint(texture) };
  GL_DeleteTextures(1, textures);
}

void Java_org_sudu_experiments_angle_AngleGL_glBindTexture(JNIEnv *, jclass, jint target, jint texture) {
  GL_BindTexture(target, texture);
}

void Java_org_sudu_experiments_angle_AngleGL_glUniform1i(JNIEnv*, jclass, jint location, jint v0) {
  GL_Uniform1i(location, v0);
}

void Java_org_sudu_experiments_angle_AngleGL_glUniform2f(JNIEnv*, jclass, jint location, jfloat v0, jfloat v1) {
  GL_Uniform2f(location, v0, v1);
}

void Java_org_sudu_experiments_angle_AngleGL_glUniform4f(JNIEnv*, jclass, jint location, jfloat v0, jfloat v1, jfloat v2, jfloat v3) {
  GL_Uniform4f(location, v0, v1, v2, v3);
}

void Java_org_sudu_experiments_angle_AngleGL_activeTexture(JNIEnv*, jobject, jint texture) {
  GL_ActiveTexture(texture);
}

void Java_org_sudu_experiments_angle_AngleGL_texStorage2D(JNIEnv*, jobject, jint target, jint levels, jint internalformat, jint width, jint height) {
  GL_TexStorage2D(target, levels, internalformat, width, height);
}

jint Java_org_sudu_experiments_angle_AngleGL_getError(JNIEnv*, jobject) {
  return GL_GetError();
}

void Java_org_sudu_experiments_angle_AngleGL_viewport(JNIEnv*, jobject, jint x, jint y, jint width, jint height) {
  GL_Viewport(x, y, width, height);
}

void Java_org_sudu_experiments_angle_AngleGL_scissor(JNIEnv*, jobject, jint x, jint y, jint width, jint height) {
  GL_Scissor(x, y, width, height);
}

void Java_org_sudu_experiments_angle_AngleGL_clearColor(JNIEnv*, jobject, jfloat r, jfloat g, jfloat b, jfloat a) {
  GL_ClearColor(r, g, b, a);
}

void Java_org_sudu_experiments_angle_AngleGL_clear(JNIEnv*, jobject, jint mask) {
  GL_Clear(mask);
}

void Java_org_sudu_experiments_angle_AngleGL_enable(JNIEnv*, jobject, jint cap) {
  GL_Enable(cap);
}

void Java_org_sudu_experiments_angle_AngleGL_disable(JNIEnv*, jobject, jint cap) {
  GL_Disable(cap);
}

void Java_org_sudu_experiments_angle_AngleGL_blendFuncSeparate(JNIEnv*, jobject, 
  jint sfactorRGB, jint dfactorRGB, jint sfactorAlpha, jint dfactorAlpha
) {
  GL_BlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
}

typedef void* pvoid;

void Java_org_sudu_experiments_angle_AngleGL_drawElements(JNIEnv*, jobject, jint mode, jint count, jint type, jint offset) {
  GL_DrawElements(mode, count, type, pvoid(jlong(offset)));
}

void Java_org_sudu_experiments_angle_AngleGL_drawArrays(JNIEnv*, jobject, jint mode, jint first, jint count) {
  GL_DrawArrays(mode, first, count);
}

void Java_org_sudu_experiments_angle_AngleGL_enableVertexAttribArray(JNIEnv*, jobject, jint index) {
  GL_EnableVertexAttribArray(index);
}

void Java_org_sudu_experiments_angle_AngleGL_disableVertexAttribArray(JNIEnv*, jobject, jint index) {
  GL_DisableVertexAttribArray(index);
}

void Java_org_sudu_experiments_angle_AngleGL_vertexAttribPointer(JNIEnv*, jobject,
  jint index, jint size, jint type, jboolean normalized, jint stride, jint offset
) {
  GL_VertexAttribPointer(index, size, type, normalized, stride, pvoid(jlong(offset)));
}

void Java_org_sudu_experiments_angle_AngleGL_texParameteri(JNIEnv*, jobject, jint target, jint pname, jint param) {
  GL_TexParameteri(target, pname, param);
}

extern "C" JNIEXPORT jint sudu_glGetRenderbufferParameteri(jint target, jint name) {
  GLint value[1] = { 0 };
  GL_GetRenderbufferParameteriv(target, name, value);
  return value[0];
}

jint Java_org_sudu_experiments_angle_AngleGL_getRenderbufferParameteri(JNIEnv*, jobject, jint target, jint name) {
  return sudu_glGetRenderbufferParameteri(target, name);
}

void Java_org_sudu_experiments_angle_AngleGL_texSubImage2D(JNIEnv* j, jobject,
  jint target, jint level, jint xoffset, jint yoffset,
  jint width, jint height, jint format, jint type,
  jbyteArray jArray
) {
  auto pixels = j->GetPrimitiveArrayCritical(jArray, 0);
  GL_TexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  j->ReleasePrimitiveArrayCritical(jArray, pixels, 0);
}

void Java_org_sudu_experiments_angle_AngleGL_texSubImage2DPtr(JNIEnv*, jclass,
  jint target, jint level, jint xoffset, jint yoffset,
  jint width, jint height, jint format, jint type,
  jlong pixels
) {
  GL_TexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pvoid(pixels));
}

jlong Java_org_sudu_experiments_angle_AngleGL_getString(JNIEnv*, jclass, jint name) {
  return jlong(GL_GetString(name));
}

jint Java_org_sudu_experiments_angle_AngleGL_getParameteri(JNIEnv*, jobject, jint name) {
  GLint data[1] = {0};
  GL_GetIntegerv(name, data);
  return data[0];
}

jfloat Java_org_sudu_experiments_angle_AngleGL_getParameterf(JNIEnv*, jobject, jint name) {
  GLfloat data[1] = {0};
  GL_GetFloatv(name, data);
  return data[0];
}

jint Java_org_sudu_experiments_angle_AngleEGL_getError(JNIEnv *, jclass) {
  return EGL_GetError();
}

jlong Java_org_sudu_experiments_angle_AngleEGL_getPlatformDisplay(
  JNIEnv *j, jclass, jint platform, jlong native_display, jlongArray jArray
) {
  auto attrib_list = (jlong *)j->GetPrimitiveArrayCritical(jArray, 0);
  auto r = EGL_GetPlatformDisplay(platform, pvoid(native_display), attrib_list);
  j->ReleasePrimitiveArrayCritical(jArray, attrib_list, 0);
  return jlong(r);
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_initialize(JNIEnv *, jclass, jlong display) {
  return EGL_Initialize(EGLDisplay(display), 0, 0);
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_terminate(JNIEnv *, jclass, jlong display) {
  return EGL_Terminate(EGLDisplay(display));
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_chooseConfig(JNIEnv *j, jclass,
  jlong display, jintArray jAttribList, jlongArray jConfigs, jintArray jNumConfig
) {

  auto attrib_list = (jint *)j->GetPrimitiveArrayCritical(jAttribList, 0);
  auto configs = (jlong *)j->GetPrimitiveArrayCritical(jConfigs, 0);
  auto configsLength = j->GetArrayLength(jConfigs);
  auto num_config = (jint *)j->GetPrimitiveArrayCritical(jNumConfig, 0);

  auto r = EGL_ChooseConfig(EGLDisplay(display), PCEGLint(attrib_list),
      (EGLConfig *)configs, configsLength, (EGLint *)num_config);

  j->ReleasePrimitiveArrayCritical(jNumConfig, num_config, 0);
  j->ReleasePrimitiveArrayCritical(jConfigs, configs, 0);
  j->ReleasePrimitiveArrayCritical(jAttribList, attrib_list, 0);

  return r;
}

jlong Java_org_sudu_experiments_angle_AngleEGL_queryString(JNIEnv *, jclass, jlong display, jint name) {
  return jlong(EGL_QueryString(EGLDisplay(display), name));
}

jlong Java_org_sudu_experiments_angle_AngleEGL_createWindowSurface(
  JNIEnv *j, jclass, jlong display, jlong config, jlong hWnd, jintArray jAttribList
) {
  auto attrib_list = jAttribList ? (jint*)j->GetPrimitiveArrayCritical(jAttribList, 0) : nullptr;
  auto r = EGL_CreateWindowSurface(EGLDisplay(display), EGLConfig(config), 
      EGLNativeWindowType(hWnd), PCEGLint(attrib_list));
  if (jAttribList) j->ReleasePrimitiveArrayCritical(jAttribList, attrib_list, 0);
  return jlong(r);
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_destroySurface(JNIEnv *, jclass, jlong display, jlong surface) {
  return EGL_DestroySurface(EGLDisplay(display), EGLSurface(surface));
}

jlong Java_org_sudu_experiments_angle_AngleEGL_createContext(
  JNIEnv *j, jclass, jlong display, jlong config, jlong share_context, jintArray jAttribList
) {
  auto attrib_list = jAttribList ? (jint*)j->GetPrimitiveArrayCritical(jAttribList, 0) : nullptr;
  auto r = EGL_CreateContext(EGLDisplay(display), EGLConfig(config), EGLContext(share_context), PCEGLint(attrib_list));
  if (jAttribList) j->ReleasePrimitiveArrayCritical(jAttribList, attrib_list, 0);
  return jlong(r);
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_destroyContext(
  JNIEnv *, jclass, jlong display, jlong context
) {
  return EGL_DestroyContext(EGLDisplay(display), EGLContext(context));
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_makeCurrent(
  JNIEnv *, jclass, jlong display, jlong draw, jlong read, jlong context
) {
  return EGL_MakeCurrent(EGLDisplay(display), EGLSurface(draw), EGLSurface(read), EGLContext(context));
}

jlong Java_org_sudu_experiments_angle_AngleEGL_getCurrentContext(JNIEnv *, jclass) {
  return jlong(EGL_GetCurrentContext());
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_swapInterval(JNIEnv*, jclass, jlong display, jint interval) {
  return EGL_SwapInterval(EGLDisplay(display), interval);
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_swapBuffers(JNIEnv*, jclass, jlong display, jlong surface) {
  return EGL_SwapBuffers(EGLDisplay(display), EGLSurface(surface));
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_querySurface(JNIEnv *j, jclass,
  jlong display, jlong surface, jint attribute, jintArray jArray
) {
  EGLint value[1] = { 0 };
  auto r = EGL_QuerySurface(EGLDisplay(display), EGLSurface(surface), attribute, value);
  j->SetIntArrayRegion(jArray, 0, 1, (jint*)value);
  return r;
}

jboolean Java_org_sudu_experiments_angle_AngleEGL_querySurfaceSize(
  JNIEnv *j, jclass, jlong display, jlong surface, jintArray jArray
) {
  if (j->GetArrayLength(jArray) < 2) return false;
  EGLint value[2] = { 0, 0 };
  auto r1 = EGL_QuerySurface(EGLDisplay(display), EGLSurface(surface), EGL_WIDTH, value);
  auto r2 = EGL_QuerySurface(EGLDisplay(display), EGLSurface(surface), EGL_HEIGHT, value + 1);
  j->SetIntArrayRegion(jArray, 0, 2, (jint*)value);
  return r1 && r2;
}
