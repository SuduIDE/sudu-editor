#include "org_sudu_experiments_CString.h"

typedef char const* PChar8;
typedef char16_t const* PChar16;

template <class Char> int stringLength(Char const* s) {
  int l = 0;
  while (s[l] != 0) ++l;
  return l;
}

jint Java_org_sudu_experiments_CString_strlen(JNIEnv*, jclass, jlong address) {
  return stringLength(PChar8(address));
}

jint Java_org_sudu_experiments_CString_strlenChar16t(JNIEnv*, jclass, jlong address) {
  return stringLength(PChar16(address));
}

void Java_org_sudu_experiments_CString_setByteArrayRegion(
  JNIEnv* j, jclass, jbyteArray array, jint start, jint len, jlong ptr
) {
  j->SetByteArrayRegion(array, start, len, (jbyte*)(ptr));
}

void Java_org_sudu_experiments_CString_getByteArrayRegion(
  JNIEnv* j, jclass, jbyteArray array, jint start, jint len, jlong ptr
) {
  j->GetByteArrayRegion(array, start, len, (jbyte*)(ptr));
}

void Java_org_sudu_experiments_CString_setCharArrayRegion(
  JNIEnv* j, jclass, jcharArray array, jint start, jint len, jlong ptr
) {
  j->SetCharArrayRegion(array, start, len, (jchar*)(ptr));
}

void Java_org_sudu_experiments_CString_getCharArrayRegion(
  JNIEnv* j, jclass, jcharArray array, jint start, jint len, jlong ptr
) {
  j->GetCharArrayRegion(array, start, len, (jchar*)(ptr));
}

void Java_org_sudu_experiments_CString_getSetPrimitiveArrayCriticalTest___3II(
  JNIEnv* j, jclass, jintArray array, jint value
) {
  auto data = (jint*)j->GetPrimitiveArrayCritical(array, 0);
  *data = value;
  j->ReleasePrimitiveArrayCritical(array, data, 0);
}

void Java_org_sudu_experiments_CString_getSetPrimitiveArrayCriticalTest___3FI(
  JNIEnv* j, jclass, jfloatArray array, jint size
) {
  auto data = (jfloat*)j->GetPrimitiveArrayCritical(array, 0);
  for (int i = 0; i < size; i++) data[i] = float(i);
  j->ReleasePrimitiveArrayCritical(array, data, 0);
}

void Java_org_sudu_experiments_CString_setIntArrayRegionTest(
  JNIEnv* j, jclass, jintArray array, jint value
) {
  jint region[1] = { value };
  j->SetIntArrayRegion(array, 0, 1, region);
}

void Java_org_sudu_experiments_CString_setFloatArrayRegionTest9(
  JNIEnv* j, jclass, jfloatArray array, jint
) {
  auto l = j->GetArrayLength(array);
  if (l >= 9) {
    jfloat region[9] = { 0,1,2,3,4,5,6,7,8 };
    j->SetFloatArrayRegion(array, 0, 9, region);
  }
}

jlong Java_org_sudu_experiments_CString_operatorNew(JNIEnv*, jclass, jlong size) {
  return jlong(operator new(size));
}

void Java_org_sudu_experiments_CString_operatorDelete(JNIEnv*, jclass, jlong ptr) {
  operator delete((void*)ptr);
}
