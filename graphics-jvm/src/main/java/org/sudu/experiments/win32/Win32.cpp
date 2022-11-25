#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <shellapi.h>
#include <stdint.h>

#include "org_sudu_experiments_win32_Win32.h"
#include "org_sudu_experiments_win32_IUnknown.h"
#include "org_sudu_experiments_win32_TestHelper.h"

static_assert(sizeof(WCHAR) == sizeof(jchar), "Fatal: sizeof(WCHAR) != sizeof(jchar)");

struct Debug {
  struct Hex { int64_t value; Hex(int value) : value(value) {} };

  Debug const & operator << (const WCHAR * str) const;
  Debug const & operator << (const char * str) const;
  Debug const & operator << (void const * ptr) const;
  Debug const & operator << (int x) const;
  Debug const & operator << (Hex hex) const;
};

extern "C" {
  JNIEXPORT jint JNICALL JavaCritical_org_sudu_experiments_win32_Win32_invokeCritical(jint index, jint length, jint* arrayPtr);
  JNIEXPORT jlong JNICALL JavaCritical_org_sudu_experiments_win32_Win32_GetPerformanceCounter();
  JNIEXPORT jlong JNICALL JavaCritical_org_sudu_experiments_win32_Win32_GetPerformanceFrequency();

  typedef jlong (* JWindowProc)(LPVOID, jlong id, jlong hwnd, jint, jlong, jlong);

  JNIEXPORT HWND sudu_createWindow(
    LPVOID context, jlong id, JWindowProc jWindowProc,
    WCHAR* title, int x, int y, int dx, int dy,
    HINSTANCE iconImage, int iconId
  );

  JNIEXPORT bool sudu_dispatchWindowMessages();
}

extern "C" IMAGE_DOS_HEADER __ImageBase;
#pragma comment(lib, "Rpcrt4.lib")
#pragma comment(lib, "Shcore.lib")

static jclass Win32_clazz;
static jmethodID Win32_wndProc_methodID;

static jlong jWindowProcCallback(LPVOID env, jlong id, jlong hwnd, jint msg, jlong wParam, jlong lParam) {
  return ((JNIEnv*)env)->CallStaticLongMethod(
    Win32_clazz, Win32_wndProc_methodID,
    id, hwnd, msg, wParam, lParam);
}

static JWindowProc getWindowProcCallback(JNIEnv* env, jclass clazz) {
  if (Win32_clazz == 0) {
    Win32_clazz = jclass(env->NewGlobalRef(clazz));
    Win32_wndProc_methodID = env->GetStaticMethodID(clazz, "jWindowProc", "(JJIJJ)J");
  }
  return jWindowProcCallback;
}

void Java_org_sudu_experiments_win32_Win32_DeleteClassGlobalRef(JNIEnv *env, jclass) {
  if (Win32_clazz != 0) {
    env->DeleteGlobalRef(Win32_clazz);
    Win32_clazz = 0;
    Win32_wndProc_methodID = 0;
  }
}

struct WindowPayload {
  JWindowProc jWindowProc;
  LPVOID context;
  jlong id;

  WindowPayload(JWindowProc jWindowProc, LPVOID context, jlong id)
    : jWindowProc(jWindowProc), context(context), id(id) {}

  jlong invokeJavaWindowProc(jlong hwnd, jint msg, jlong wParam, jlong lParam) {
    return jWindowProc(context, id, hwnd, msg, wParam, lParam);
  }
};

template<class T> inline T* getWindowPayload(HWND whWnd) {
  return (T*)GetWindowLongPtrW(whWnd, GWLP_USERDATA);
}

static JNIEnv * currentJavaEnv;

static LRESULT CALLBACK windowProc(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam) {
  switch (msg) {
  case WM_NCCREATE:
    SetWindowLongPtrW(hWnd, GWLP_USERDATA, LONG_PTR(LPCREATESTRUCT(lParam)->lpCreateParams));
    break;

  case WM_NCDESTROY:
    delete getWindowPayload<WindowPayload>(hWnd);
    SetWindowLongPtrW(hWnd, GWLP_USERDATA, 0);
    break;

  // for performance reasons the following messages are not sent to Java
  case WM_NCMOUSEMOVE:
  case WM_NCHITTEST:
  case WM_NCACTIVATE:
  case WM_NCPAINT:
  case WM_NCCALCSIZE:

  case WM_GETICON:

  case WM_SETTEXT:
  case WM_GETTEXT:

  case WM_SHOWWINDOW:
  case WM_ACTIVATEAPP:
  case WM_WINDOWPOSCHANGING:
  case WM_SIZING:
  case WM_MOVING:
  case 0xAAE:
    break; // return DefWindowProcW

  case WM_ERASEBKGND:
    return TRUE;

  // the rest goes to Java 
  default:
    if (auto* w = getWindowPayload<WindowPayload>(hWnd)) {
      if (w->context != currentJavaEnv) {
        Debug() << "invokeJavaWindowProc: context = " << w->context
          << ", currentJavaEnv = "
            << currentJavaEnv << "msg = 0x" << Debug::Hex(msg) << "\n";
      }
      return w->invokeJavaWindowProc(jlong(hWnd), msg, wParam, lParam);
    } else {
   //   Debug() << "getWindowPayload return null hWnd = " << hWnd << "\n";
    }
  }
  return DefWindowProcW(hWnd, msg, wParam, lParam);
}

HWND sudu_createWindow(
  LPVOID context, jlong id, JWindowProc jWindowProc,
  WCHAR* title, int x, int y, int dx, int dy,
  HINSTANCE iconImage, int iconId
) {
  static const WCHAR windowClassName[] = L"SuduWindowClass";
  static ATOM classAtom;

  if (!classAtom) {
    WNDCLASSEXW wc = {
      sizeof(WNDCLASSEXW), CS_CLASSDC | CS_DBLCLKS,
      windowProc, 0L, 0L, HMODULE(&__ImageBase),
      LoadIconW(iconImage, MAKEINTRESOURCE(iconId)),
      LoadCursorW(0, IDC_ARROW), 0, 0, windowClassName, 0 };
    classAtom = RegisterClassExW(&wc);
  }

  DWORD style =
    WS_SYSMENU | WS_CAPTION | WS_MINIMIZEBOX | WS_MAXIMIZEBOX | WS_THICKFRAME | 
    WS_CLIPCHILDREN | WS_CLIPSIBLINGS;
  DWORD exStyle = WS_EX_APPWINDOW | WS_EX_ACCEPTFILES | 0*WS_EX_TOOLWINDOW;

  RECT r = { 0, 0, dx, dy };
  if (dx != CW_USEDEFAULT && dy != CW_USEDEFAULT) {
    AdjustWindowRectEx(&r, style, 0, exStyle);
  }

  WindowPayload* payload = new WindowPayload(jWindowProc, context, id);

  HWND window = CreateWindowExW(
    exStyle, windowClassName, title, style,
    x, y, r.right - r.left, r.bottom - r.top,
    GetDesktopWindow(), 0, HMODULE(&__ImageBase), payload);

  if (GetWindowLongW(window, GWL_STYLE) != style) {
    Debug() << "windowStyle != style: newStyle = " << Debug::Hex(GetWindowLongW(window, GWL_STYLE))
      << ", initialStyle" << Debug::Hex(style) << "\n";
  }

  DragAcceptFiles(window, true);
  RegisterTouchWindow(window, 0);

  return window;
}

jlong Java_org_sudu_experiments_win32_Win32_CreateWindow(
  JNIEnv* env, jclass clazz, jlong id, jcharArray title,
  jint x, jint y, jint width, jint height, jlong iconModule, jint iconID
) {

  auto callback = getWindowProcCallback(env, clazz);
  // auto titleLength = env->GetArrayLength(title);
  auto titlePointer = env->GetPrimitiveArrayCritical(title, 0);

  currentJavaEnv = env;

  auto r = sudu_createWindow(env, id, callback, PWCHAR(titlePointer),
    x, y, width, height, HINSTANCE(iconModule), iconID);

  currentJavaEnv = 0;

  env->ReleasePrimitiveArrayCritical(title, titlePointer, 0);

  return jlong(r);
}

jboolean Java_org_sudu_experiments_win32_Win32_DestroyWindow(JNIEnv* env, jclass, jlong hwnd) {
  currentJavaEnv = env;
  auto r = DestroyWindow(HWND(hwnd));
  currentJavaEnv = 0;
  return r;
}

bool sudu_dispatchWindowMessages() {
  MSG msg; msg.message = 0;
  while (PeekMessageW(&msg, 0, 0, 0, PM_REMOVE)) {
    TranslateMessage(&msg);
    DispatchMessageW(&msg);
  }
  return msg.message != WM_QUIT;
}

jboolean Java_org_sudu_experiments_win32_Win32_PeekTranslateDispatchMessage(JNIEnv * env, jclass) {
  currentJavaEnv = env;
  auto r = sudu_dispatchWindowMessages();
  currentJavaEnv = 0;
  return r;
}

jlong Java_org_sudu_experiments_win32_Win32_DefWindowProcW(JNIEnv*, jclass, jlong hWnd, jint Msg, jlong wParam, jlong lParam) {
  return DefWindowProcW(HWND(hWnd), Msg, wParam, lParam);
}

jboolean Java_org_sudu_experiments_win32_Win32_ValidateRect0(JNIEnv*, jclass, jlong hWnd) {
  return ValidateRect(HWND(hWnd), 0);
}

jboolean Java_org_sudu_experiments_win32_Win32_IsZoomed(JNIEnv*, jclass, jlong hWnd) {
  return IsZoomed(HWND(hWnd));
}

jboolean Java_org_sudu_experiments_win32_Win32_GetWindowRect(JNIEnv*j, jclass, jlong hWnd, jintArray jRect) {
  auto length = j->GetArrayLength(jRect);
  if (unsigned(length) * sizeof(jint) < sizeof(RECT)) return false;
  RECT rect = { 0,0,0,0 };
  auto r = GetWindowRect(HWND(hWnd), &rect);
  j->SetIntArrayRegion(jRect, 0, sizeof(RECT) / sizeof(jint), (jint*)(&rect));
  return r;
}

static_assert(sizeof(LRESULT) == sizeof(jlong), "Fatal: sizeof(LRESULT) != sizeof(jlong)");

jlong Java_org_sudu_experiments_win32_Win32_SendMessageW(
  JNIEnv*, jclass, jlong hWnd, jint msg, jlong wParam, jlong lParam
) {
  return SendMessageW(HWND(hWnd), msg, wParam, lParam);
}

jboolean Java_org_sudu_experiments_win32_Win32_ShowWindow(JNIEnv *, jclass, jlong hWnd, jint nCmdShow) {
  return ShowWindow(HWND(hWnd), nCmdShow);
}

jboolean Java_org_sudu_experiments_win32_Win32_SetWindowTextW(JNIEnv *j, jclass, jlong hWnd, jcharArray string) {
  auto lpString = (jchar *)j->GetPrimitiveArrayCritical(string, 0);
  auto r = SetWindowTextW(HWND(hWnd), LPCWSTR(lpString));
  j->ReleasePrimitiveArrayCritical(string, lpString, 0);
  return r;
}

jlong Java_org_sudu_experiments_win32_Win32_SetTimer(
  JNIEnv *, jclass, jlong hWnd, jlong nIDEvent, jint uElapse, jlong lpTimerFunc
) {
  return jlong(SetTimer(HWND(hWnd), nIDEvent, uElapse, TIMERPROC(lpTimerFunc)));
}

jboolean Java_org_sudu_experiments_win32_Win32_KillTimer(JNIEnv *, jclass, jlong hWnd, jlong nIDEvent) {
  return KillTimer(HWND(hWnd), nIDEvent);
}

jlong Java_org_sudu_experiments_win32_Win32_LoadCursorW(JNIEnv *, jclass, jlong hInstance, jlong lpCursorName) {
  return jlong(LoadCursorW(HINSTANCE(hInstance), LPCWSTR(lpCursorName)));
}

jlong Java_org_sudu_experiments_win32_Win32_GetDC(JNIEnv *, jclass, jlong hWnd) {
  return jlong(GetDC(HWND(hWnd)));
}

jlong Java_org_sudu_experiments_win32_Win32_SetCapture(JNIEnv*, jclass, jlong hWnd) {
  return jlong(SetCapture(HWND(hWnd)));
}

jint Java_org_sudu_experiments_win32_Win32_ReleaseCapture(JNIEnv*, jclass) {
  return ReleaseCapture();
}

jint Java_org_sudu_experiments_win32_Win32_GetDpiForWindow(JNIEnv*, jclass, jlong hWnd) {
  return GetDpiForWindow(HWND(hWnd));
}

#include <shellscalingapi.h>

jint Java_org_sudu_experiments_win32_Win32_SetProcessDpiAwareness(JNIEnv*, jclass, jint value) {
  return SetProcessDpiAwareness(PROCESS_DPI_AWARENESS(value));
}

jlong Java_org_sudu_experiments_win32_Win32_GetCommandLineA(JNIEnv*, jclass) {
  return jlong(GetCommandLineA());
}

jlong Java_org_sudu_experiments_win32_Win32_GetCommandLineW(JNIEnv*, jclass) {
  return jlong(GetCommandLineW());
}

jlong Java_org_sudu_experiments_win32_Win32_GetModuleHandle0(JNIEnv*, jclass) {
  return jlong(GetModuleHandle(0));
}

jlong Java_org_sudu_experiments_win32_Win32__1_1ImageBase(JNIEnv*, jclass) {
  return jlong(&__ImageBase);
}

void* operator new (size_t size) {
  return HeapAlloc(GetProcessHeap(), 0, size);
}

void operator delete(void* p) {
  if (!HeapFree(GetProcessHeap(), 0, p)) 
    DebugBreak();
}

void* operator new[](size_t size) {
  return operator new(size);
}

void operator delete(void* p, unsigned __int64) { operator delete(p); }

void operator delete[](void* p) { operator delete(p); }

Debug const & Debug::operator << (const WCHAR * str) const {
  OutputDebugStringW(str);
  return *this;
}

Debug const & Debug::operator << (const char * str) const {
  OutputDebugStringA(str);
  return *this;
}

Debug const & Debug::operator << (void const * ptr) const {
  char text[32];
  wsprintfA(text, "%p", ptr);
  return operator << (text);
}

Debug const & Debug::operator << (Hex hex) const {
  char text[32];
  wsprintfA(text, "%llX", hex.value);
  return operator << (text);
}

Debug const & Debug::operator << (int x) const {
  char text[32];
  wsprintfA(text, "%d", x);
  return operator << (text);
}

jlong JavaCritical_org_sudu_experiments_win32_Win32_GetPerformanceCounter() {
  LARGE_INTEGER r; r.QuadPart = 0;
  QueryPerformanceCounter(&r);
  return r.QuadPart;
}

jlong Java_org_sudu_experiments_win32_Win32_GetPerformanceCounter(JNIEnv *, jclass) {
  return JavaCritical_org_sudu_experiments_win32_Win32_GetPerformanceCounter();
}

jlong JavaCritical_org_sudu_experiments_win32_Win32_GetPerformanceFrequency() {
  LARGE_INTEGER r; r.QuadPart = 0;
  QueryPerformanceFrequency(&r);
  return r.QuadPart;
}

jlong Java_org_sudu_experiments_win32_Win32_GetPerformanceFrequency(JNIEnv *, jclass) {
  return JavaCritical_org_sudu_experiments_win32_Win32_GetPerformanceFrequency();
}

jchar Java_org_sudu_experiments_win32_Win32_GetKeyState(JNIEnv *, jclass, jint nVirtKey) {
  return jchar(GetKeyState(nVirtKey));
}

jint JavaCritical_org_sudu_experiments_win32_TestHelper_invokeCritical(jint index, jint length, jint* arrayPtr) {
  return arrayPtr[index];
}

jint Java_org_sudu_experiments_win32_TestHelper_invokeStandard(JNIEnv *j, jclass, jint index, jintArray array) {
  auto length = j->GetArrayLength(array);
  auto arrPtr = (jint*)j->GetPrimitiveArrayCritical(array, 0);
  auto result = -arrPtr[index];
  j->ReleasePrimitiveArrayCritical(array, arrPtr, 0);
  return result;
}

jint Java_org_sudu_experiments_win32_TestHelper_invokeCritical(JNIEnv *j, jclass c, jint index, jintArray array) {
  return Java_org_sudu_experiments_win32_TestHelper_invokeStandard(j, c, index, array);
}

#include <unknwn.h>

jint Java_org_sudu_experiments_win32_Win32_CoInitialize(JNIEnv*, jclass) {
  return CoInitialize(0);
}

jint Java_org_sudu_experiments_win32_IUnknown_AddRef(JNIEnv *, jclass, jlong ptr) {
  return LPUNKNOWN(ptr)->AddRef();
}

jint Java_org_sudu_experiments_win32_IUnknown_Release(JNIEnv *, jclass, jlong ptr) {
  return LPUNKNOWN(ptr)->Release();
}
