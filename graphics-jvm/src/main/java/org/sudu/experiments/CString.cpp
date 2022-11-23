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

void Java_org_sudu_experiments_CString_setCharArrayRegion(
  JNIEnv* j, jclass, jcharArray array, jint start, jint len, jlong ptr
) {
  j->SetCharArrayRegion(array, start, len, (jchar*)(ptr));
}

void Java_org_sudu_experiments_CString_getSetPrimitiveArrayCriticalTest(
  JNIEnv* j, jclass, jintArray array, jint value
) {
  auto data = (jint*)j->GetPrimitiveArrayCritical(array, 0);
  *data = value;
  j->ReleasePrimitiveArrayCritical(array, data, 0);
}

void Java_org_sudu_experiments_CString_setIntArrayRegionTest(
  JNIEnv* j, jclass, jintArray array, jint value
) {
  jint region[1] = { value };
  j->SetIntArrayRegion(array, 0, 1, region);
}
