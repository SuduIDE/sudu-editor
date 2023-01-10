#pragma once

template<typename R> inline R toJavaR(JNIEnv* j, jintArray jHR, HRESULT hr, R value) {
  if (hr < 0 && jHR && j->GetArrayLength(jHR)) {
    jint update[1] = { hr };
    j->SetIntArrayRegion(jHR, 0, 1, update);
  }
  return value;
}

template<class P> inline jlong toJava(JNIEnv* j, jintArray jHR, HRESULT hr, P* ptr) {
  return toJavaR(j, jHR, hr, jlong(ptr));
}

