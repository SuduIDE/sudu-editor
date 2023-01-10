#include <d2d1.h>
#include <d2d1helper.h>
#include <dwrite_3.h>
#include <Wincodec.h>

#include "org_sudu_experiments_win32_d2d_ID2D1Factory.h"
#include "org_sudu_experiments_win32_d2d_ID2D1RenderTarget.h"
#include "org_sudu_experiments_win32_d2d_ID2D1SolidColorBrush.h"
#include "org_sudu_experiments_win32_d2d_IDWriteFactory5.h"
#include "org_sudu_experiments_win32_d2d_IDWriteFont.h"
#include "org_sudu_experiments_win32_d2d_IDWriteFontCollection.h"
#include "org_sudu_experiments_win32_d2d_IDWriteFontFamily.h"
#include "org_sudu_experiments_win32_d2d_IDWriteFontSetBuilder1.h"
#include "org_sudu_experiments_win32_d2d_IDWriteInMemoryFontFileLoader.h"
#include "org_sudu_experiments_win32_d2d_IDWriteLocalizedStrings.h"
#include "org_sudu_experiments_win32_d2d_IDWriteTextFormat.h"
#include "org_sudu_experiments_win32_d2d_IDWriteTextLayout.h"
#include "org_sudu_experiments_win32_d2d_IWICBitmap.h"
#include "org_sudu_experiments_win32_d2d_IWICBitmapLock.h"
#include "org_sudu_experiments_win32_d2d_IWICImagingFactory.h"

#pragma comment(lib, "D2d1.lib")
#pragma comment(lib, "DWrite.lib")
#pragma warning( error : 715 ) // not all control paths return a value

static_assert(sizeof(HRESULT) == sizeof(jint), "Fatal: sizeof(HRESULT) != sizeof(jint)");
static_assert(sizeof(UINT32) == sizeof(jint), "Fatal: sizeof(UINT32) != sizeof(jint)");
static_assert(sizeof(void*) == sizeof(jlong), "Fatal: sizeof(void*) != sizeof(jlong)");
static_assert(sizeof(wchar_t) == sizeof(jchar), "Fatal: sizeof(wchar_t) != sizeof(jchar)");

typedef void** ppvoid;

#include "../javaHR.h"

jlong Java_org_sudu_experiments_win32_d2d_IWICImagingFactory_CoCreateInstance(JNIEnv *j, jclass, jintArray pHR) {
  void* res[1] = { 0 };
  auto hr = CoCreateInstance(CLSID_WICImagingFactory, nullptr, CLSCTX_INPROC_SERVER,
                            __uuidof(IWICImagingFactory), res);
  return toJava(j, pHR, hr, res[0]);
}

jlong Java_org_sudu_experiments_win32_d2d_IWICImagingFactory_CreateBitmapPreRGBA(
  JNIEnv *j, jclass, jlong _this, jint w, jint h, jintArray pHR
) {
  IWICBitmap* res[1] = { 0 };
  auto hr = ((IWICImagingFactory*)_this)->CreateBitmap(w, h,
  // GUID_WICPixelFormat32bppRGBA
    GUID_WICPixelFormat32bppPRGBA, WICBitmapCacheOnDemand, res);
  return toJava(j, pHR, hr, res[0]);
}

typedef IWICBitmap* PIWICBitmap;

jlong Java_org_sudu_experiments_win32_d2d_IWICBitmap_Lock(
  JNIEnv* j, jclass, jlong _this, jint width, jint height, jint flags, jintArray pHR
) {
  IWICBitmapLock* res[1] = { 0 };
  WICRect rect = { 0, 0, width, height };
  auto hr = PIWICBitmap(_this)->Lock(&rect, flags, res);
  return toJava(j, pHR, hr, res[0]);
}

typedef IWICBitmapLock* PIWICBitmapLock;

jint Java_org_sudu_experiments_win32_d2d_IWICBitmapLock_GetStride(
  JNIEnv* j, jclass, jlong _this, jintArray pHR
) {
  UINT res[1] = { 0 };
  auto hr = PIWICBitmapLock(_this)->GetStride(res);
  return toJavaR(j, pHR, hr, jint(res[0]));
}

jlong Java_org_sudu_experiments_win32_d2d_IWICBitmapLock_GetDataPointer__JI_3I(
  JNIEnv* j, jclass, jlong _this, jint expectedSize, jintArray jHR
) {
  WICInProcPointer p = 0;
  UINT size = 0;

  auto hr = PIWICBitmapLock(_this)->GetDataPointer(&size, &p);
  if (hr >= 0 && expectedSize == size) {
    return jlong(p);
  }
  if (hr >= 0 && expectedSize != size) hr = E_INVALIDARG;
  return toJavaR(j, jHR, hr, 0);
}

jint Java_org_sudu_experiments_win32_d2d_IWICBitmapLock_GetDataPointer__J_3J(
  JNIEnv* j, jclass, jlong _this, jlongArray jResult
) {

  if (j->GetArrayLength(jResult) < 2) return E_INVALIDARG;
  WICInProcPointer p = 0;
  UINT size = 0;

  auto hr = PIWICBitmapLock(_this)->GetDataPointer(&size, &p);

  if (hr >= 0) {
    jlong result[2] = { jlong(p), jlong(size) };
    j->SetLongArrayRegion(jResult, 0, 2, result);
  }

  return hr;
}

jlong Java_org_sudu_experiments_win32_d2d_ID2D1Factory_D2D1CreateFactory(
  JNIEnv* j, jclass, jint factoryType, jintArray pHR
) {
  void* res[1] = { 0 };
  auto hr = D2D1CreateFactory(D2D1_FACTORY_TYPE(factoryType), __uuidof(ID2D1Factory), nullptr, res);
  return toJava(j, pHR, hr, res[0]);
}

typedef ID2D1Factory * PD2D1Factory;

jlong Java_org_sudu_experiments_win32_d2d_ID2D1Factory_CreateWicBitmapRenderTarget(
  JNIEnv *j, jclass, jlong _this, jlong target, jint format, jint alphaMode, jintArray pHR
) {
  ID2D1RenderTarget* res[1] = { 0 };
  
  D2D1_RENDER_TARGET_PROPERTIES rtp = {
    D2D1_RENDER_TARGET_TYPE_DEFAULT, { DXGI_FORMAT(format), D2D1_ALPHA_MODE(alphaMode) },
    0, 0, D2D1_RENDER_TARGET_USAGE_NONE, D2D1_FEATURE_LEVEL_DEFAULT };
  
  auto hr = PD2D1Factory(_this)->CreateWicBitmapRenderTarget(
    (IWICBitmap*)target, &rtp, res);

  return toJava(j, pHR, hr, res[0]);
}

typedef ID2D1RenderTarget* PD2D1RenderTarget;

jlong Java_org_sudu_experiments_win32_d2d_ID2D1RenderTarget_CreateSolidColorBrush(
  JNIEnv* j, jclass, jlong _this, float r, float g, float b, float a, jintArray pHR
) {
  ID2D1SolidColorBrush* res[1] = { 0 };
  D2D_COLOR_F color = { r, g, b, a };
  auto hr = PD2D1RenderTarget(_this)->CreateSolidColorBrush(&color, 0, res);
  return toJava(j, pHR, hr, res[0]);
}

void Java_org_sudu_experiments_win32_d2d_ID2D1RenderTarget_BeginDraw(JNIEnv*, jclass, jlong _this) {
  PD2D1RenderTarget(_this)->BeginDraw();
}

void Java_org_sudu_experiments_win32_d2d_ID2D1RenderTarget_Clear__JFFFF(
  JNIEnv*, jclass, jlong _this, jfloat r, jfloat g, jfloat b, jfloat a
) {
  D2D_COLOR_F color = { r, g, b, a };
  PD2D1RenderTarget(_this)->Clear(&color);
}

void Java_org_sudu_experiments_win32_d2d_ID2D1RenderTarget_Clear__J(JNIEnv*, jclass, jlong _this) {
  PD2D1RenderTarget(_this)->Clear();
}

void Java_org_sudu_experiments_win32_d2d_ID2D1RenderTarget_DrawText(
  JNIEnv* j, jclass, jlong _this, jcharArray jString, jint begin, jint stringLength,
  jlong textFormat, jfloat rcLeft, jfloat rcTop, jfloat rcRight, jfloat rcBottom, jlong brush
) {
  auto cString = (wchar_t*)j->GetPrimitiveArrayCritical(jString, 0);

  D2D1_RECT_F layoutRect = { rcLeft, rcTop, rcRight, rcBottom };

  PD2D1RenderTarget(_this)->DrawTextW(
    cString + begin, stringLength, (IDWriteTextFormat*)textFormat, &layoutRect, (ID2D1Brush*)brush);

  j->ReleasePrimitiveArrayCritical(jString, cString, 0);
}


jint Java_org_sudu_experiments_win32_d2d_ID2D1RenderTarget_EndDraw(JNIEnv*, jclass, jlong _this) {
  return PD2D1RenderTarget(_this)->EndDraw();
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_DWriteCreateFactory5(JNIEnv* j, jclass, jintArray pHR) {
  IUnknown* res[1] = { 0 };
  auto hr = DWriteCreateFactory(DWRITE_FACTORY_TYPE_SHARED, __uuidof(IDWriteFactory5), res);
  return toJava(j, pHR, hr, res[0]);
}

typedef ID2D1SolidColorBrush* PD2D1SolidColorBrush;

void Java_org_sudu_experiments_win32_d2d_ID2D1SolidColorBrush_SetColor(
  JNIEnv*, jclass, jlong _this, jfloat r, jfloat g, jfloat b, jfloat a
) {
  D2D_COLOR_F color = { r, g, b, a };
  PD2D1SolidColorBrush(_this)->SetColor(&color);
}

typedef IDWriteFactory5* PDWriteFactory5;

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_CreateInMemoryFontFileLoader(
  JNIEnv* j, jclass, jlong _this, jintArray pHR
) {
  IDWriteInMemoryFontFileLoader* res[1] = { 0 };
  auto hr = PDWriteFactory5(_this)->CreateInMemoryFontFileLoader(res);
  return toJava(j, pHR, hr, res[0]);
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_CreateFontSetBuilder1(
  JNIEnv* j, jclass, jlong _this, jintArray pHR
) {
  IDWriteFontSetBuilder1* res[1] = { 0 };
  auto hr = PDWriteFactory5(_this)->CreateFontSetBuilder(res);
  return toJava(j, pHR, hr, res[0]);
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_CreateFontCollectionFromFontSet(
  JNIEnv* j, jclass, jlong _this, jlong fontSet, jintArray pHR
) {
  IDWriteFontCollection1* res[1] = { 0 };
  auto hr = PDWriteFactory5(_this)->CreateFontCollectionFromFontSet((IDWriteFontSet*)fontSet, res);
  return toJava(j, pHR, hr, res[0]);
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_GetSystemFontCollection(
  JNIEnv* j, jclass, jlong _this, 
  jboolean includeDownloadableFonts, jboolean checkForUpdates, jintArray pHR
) {
  IDWriteFontCollection1* res[1] = { 0 };
  auto hr = PDWriteFactory5(_this)->GetSystemFontCollection(
    includeDownloadableFonts, res, checkForUpdates);
  return toJava(j, pHR, hr, res[0]);
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_CreateTextFormat(
  JNIEnv* j, jclass, jlong _this, jcharArray fontFamily, 
  jlong fontCollection, jint fontWeight, jint fontStyle, jint fontStretch, 
  jfloat fontSize, jcharArray locale, jintArray pHR
) {
  auto pFontFamily = (wchar_t*)j->GetPrimitiveArrayCritical(fontFamily, 0);
  auto pLocale = (wchar_t*)j->GetPrimitiveArrayCritical(locale, 0);
  
  IDWriteTextFormat* res[1] = { 0 };
  auto hr = PDWriteFactory5(_this)->CreateTextFormat(
    pFontFamily, (IDWriteFontCollection*)fontCollection,
    DWRITE_FONT_WEIGHT(fontWeight), DWRITE_FONT_STYLE(fontStyle),
    DWRITE_FONT_STRETCH(fontStretch), fontSize, pLocale, res);

  j->ReleasePrimitiveArrayCritical(fontFamily, pFontFamily, 0);
  j->ReleasePrimitiveArrayCritical(locale, pLocale, 0);

  return toJava(j, pHR, hr, res[0]);
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_CreateTextLayout(
  JNIEnv* j, jclass, jlong _this, jcharArray text,
  jint offset, jint length, jlong textFormat,
  jfloat maxWidth, jfloat maxHeight, jintArray pHR
) {
  auto pText = (wchar_t*)j->GetPrimitiveArrayCritical(text, 0);
  IDWriteTextLayout* res[1] = { 0 };
  auto hr = PDWriteFactory5(_this)->CreateTextLayout(
    pText + offset, length, (IDWriteTextFormat*)textFormat,
    maxWidth, maxHeight, res);
  j->ReleasePrimitiveArrayCritical(text, pText, 0);
  return toJava(j, pHR, hr, res[0]);
}

jint Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_RegisterFontFileLoader(JNIEnv*, jclass, jlong _this, jlong fontFileLoader) {
  return PDWriteFactory5(_this)->RegisterFontFileLoader((IDWriteFontFileLoader*)fontFileLoader);
}

jint Java_org_sudu_experiments_win32_d2d_IDWriteFactory5_UnregisterFontFileLoader(JNIEnv*, jclass, jlong _this, jlong fontFileLoader) {
  return PDWriteFactory5(_this)->UnregisterFontFileLoader((IDWriteFontFileLoader*)fontFileLoader);
}

typedef IDWriteInMemoryFontFileLoader* PDWriteInMemoryFontFileLoader;

jlong Java_org_sudu_experiments_win32_d2d_IDWriteInMemoryFontFileLoader_CreateInMemoryFontFileReference(
  JNIEnv* j, jclass, jlong _this, jlong pDWriteFactory, jbyteArray font, jintArray pHR
) {
  IDWriteFontFile* res[1] = { 0 };
  auto fontSize = j->GetArrayLength(font);
  auto pFont = j->GetPrimitiveArrayCritical(font, 0);
  auto hr = PDWriteInMemoryFontFileLoader(_this)->CreateInMemoryFontFileReference(
    (IDWriteFactory*)pDWriteFactory, pFont, fontSize, 0, res);
  j->ReleasePrimitiveArrayCritical(font, pFont, 0);
  return toJava(j, pHR, hr, res[0]);
}

typedef IDWriteFontSetBuilder1* PDWriteFontSetBuilder1;

jint Java_org_sudu_experiments_win32_d2d_IDWriteFontSetBuilder1_AddFontFile(
  JNIEnv*, jclass, jlong _this, jlong fontFile
) {
  return PDWriteFontSetBuilder1(_this)->AddFontFile((IDWriteFontFile*)fontFile);
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFontSetBuilder1_CreateFontSet(
  JNIEnv* j, jclass, jlong _this, jintArray pHR
) {
  IDWriteFontSet* res[1] = { 0 };
  auto hr = PDWriteFontSetBuilder1(_this)->CreateFontSet(res);
  return toJava(j, pHR, hr, res[0]);
}

typedef IDWriteFont* PDWriteFont;

jint Java_org_sudu_experiments_win32_d2d_IDWriteFont_GetMetrics(
  JNIEnv* j, jclass, jlong _this, jcharArray jMetrics
) {
  if (j->GetArrayLength(jMetrics) * sizeof(jchar) < sizeof(DWRITE_FONT_METRICS)) return E_INVALIDARG;

  DWRITE_FONT_METRICS m;
  PDWriteFont(_this)->GetMetrics(&m);
  j->SetCharArrayRegion(jMetrics, 0, sizeof(DWRITE_FONT_METRICS) / sizeof(jchar), (jchar*)(&m));
  return 0;
}

typedef IDWriteTextLayout* PDWriteTextLayout;

jint Java_org_sudu_experiments_win32_d2d_IDWriteTextLayout_GetMetrics(
  JNIEnv* j, jclass, jlong _this, jfloatArray result
) {
  if (j->GetArrayLength(result) * sizeof(jfloat) < sizeof(DWRITE_TEXT_METRICS)) return E_INVALIDARG;
  
  DWRITE_TEXT_METRICS mtx;
  auto hr = PDWriteTextLayout(_this)->GetMetrics(&mtx);
  if (hr >= 0) {
    j->SetFloatArrayRegion(result, 0, sizeof(DWRITE_TEXT_METRICS) / sizeof(jfloat), (jfloat*)&mtx);
  }
  return hr;
}

typedef IDWriteFontCollection* PDWriteFontCollection;

jint Java_org_sudu_experiments_win32_d2d_IDWriteFontCollection_FindFamilyName(
  JNIEnv* j, jclass, jlong _this, jcharArray jName, jintArray jIndex, jintArray jHR
) {
  UINT32 index[1] = { 0 };
  BOOL exists[1] = { 0 };
  auto name = (wchar_t*)j->GetPrimitiveArrayCritical(jName, 0);
  auto hr = PDWriteFontCollection(_this)->FindFamilyName(name, index, exists);
  j->ReleasePrimitiveArrayCritical(jName, name, 0);

  if (hr >= 0 && jIndex && j->GetArrayLength(jIndex) > 0) {
    j->SetIntArrayRegion(jIndex, 0, 1, (jint*)index);
  }

  return toJavaR(j, jHR, hr, exists[0]);
}

jint Java_org_sudu_experiments_win32_d2d_IDWriteFontCollection_GetFontFamilyCount(JNIEnv*, jclass, jlong _this) {
  return PDWriteFontCollection(_this)->GetFontFamilyCount();
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFontCollection_GetFontFamily(
  JNIEnv* j, jclass, jlong _this, jint index, jintArray jHR
) {
  IDWriteFontFamily* res[1] = { 0 };
  auto hr = PDWriteFontCollection(_this)->GetFontFamily(index, res);
  return toJava(j, jHR, hr, res[0]);
}

typedef IDWriteFontFamily* PDWriteFontFamily;

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFontFamily_GetFirstMatchingFont(
  JNIEnv* j, jclass, jlong _this, jint fontWeight, jint fontStretch, jint fontStyle, jintArray jHR
) {
  IDWriteFont* res[1] = { 0 };
  auto hr = PDWriteFontFamily(_this)->GetFirstMatchingFont(
    DWRITE_FONT_WEIGHT(fontWeight), DWRITE_FONT_STRETCH(fontStretch), DWRITE_FONT_STYLE(fontStyle), res);
  return toJava(j, jHR, hr, res[0]);
}

jlong Java_org_sudu_experiments_win32_d2d_IDWriteFontFamily_GetFamilyNames(
  JNIEnv* j, jclass, jlong _this, jintArray jHR
) {
  IDWriteLocalizedStrings* res[1] = { 0 };
  auto hr = PDWriteFontFamily(_this)->GetFamilyNames(res);
  return toJava(j, jHR, hr, res[0]);
}

typedef IDWriteLocalizedStrings* PDWriteLocalizedStrings;

jint Java_org_sudu_experiments_win32_d2d_IDWriteLocalizedStrings_GetCount(JNIEnv*, jclass, jlong _this) {
  return PDWriteLocalizedStrings(_this)->GetCount();
}

jint Java_org_sudu_experiments_win32_d2d_IDWriteLocalizedStrings_FindLocaleName(
  JNIEnv* j, jclass, jlong _this, jcharArray jName, jintArray jIndex, jintArray jHR
) {
  UINT32 index[1] = { 0 };
  BOOL exists[1] = { 0 };
  auto name = (wchar_t*)j->GetPrimitiveArrayCritical(jName, 0);
  auto hr = PDWriteLocalizedStrings(_this)->FindLocaleName(name, index, exists);
  j->ReleasePrimitiveArrayCritical(jName, name, 0);

  if (hr >= 0 && jIndex && j->GetArrayLength(jIndex) > 0) {
    j->SetIntArrayRegion(jIndex, 0, 1, (jint*)index);
  }

  return toJavaR(j, jHR, hr, exists[0]);
}

jint Java_org_sudu_experiments_win32_d2d_IDWriteLocalizedStrings_GetStringLength(
  JNIEnv* j, jclass, jlong _this, jint index, jintArray pHR
) {
  UINT32 length[1] = { 0 };
  auto hr = PDWriteLocalizedStrings(_this)->GetStringLength(index, length);
  return toJavaR(j, pHR, hr, length[0]);
}

jint Java_org_sudu_experiments_win32_d2d_IDWriteLocalizedStrings_GetString(
  JNIEnv* j, jclass, jlong _this, jint index, jcharArray jString
) {
  auto cString = (wchar_t*)j->GetPrimitiveArrayCritical(jString, 0);
  auto hr = PDWriteLocalizedStrings(_this)->GetString(UINT32(index), cString, j->GetArrayLength(jString));
  j->ReleasePrimitiveArrayCritical(jString, cString, 0);
  return hr;
}

typedef IDWriteTextFormat* PDWriteTextFormat;

jint Java_org_sudu_experiments_win32_d2d_IDWriteTextFormat_GetFontFamilyNameLength(JNIEnv*, jclass, jlong _this) {
  return PDWriteTextFormat(_this)->GetFontFamilyNameLength();
}

jint Java_org_sudu_experiments_win32_d2d_IDWriteTextFormat_GetFontFamilyName(
  JNIEnv* j, jclass, jlong _this, jcharArray jString
) {
  auto cString = (wchar_t*)j->GetPrimitiveArrayCritical(jString, 0);
  auto hr = PDWriteTextFormat(_this)->GetFontFamilyName(cString, j->GetArrayLength(jString));
  j->ReleasePrimitiveArrayCritical(jString, cString, 0);
  return hr;
}
