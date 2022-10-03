
extern "C" {
  __attribute__((visibility("default"))) int callToCpp1();
  __attribute__((visibility("default"))) float callToCpp2();
  __attribute__((visibility("default"))) char const * getC8String();
  __attribute__((visibility("default"))) char16_t const * getC16String();
  __attribute__((visibility("default"))) double const * getCDoubleArray8();
  __attribute__((visibility("default"))) float const * getCFloatArray8();
  __attribute__((visibility("default"))) int const * getCIntArray8();
}

extern "C" {
  extern int   jsFunction1(int a, int b);
  extern float jsFunction2(float a, float b);
}

int callToCpp1() {
  return jsFunction1(22, 33);
}

float callToCpp2() {
  return jsFunction2(555, 777);
}

char const * getC8String() {
  return "this is a C/C++ string";
}

char16_t const * getC16String() {
  return u"this is a C/C++ char16_t string 新年快乐";
}

static double fp64Matrix8[8] = { 1.111, 2.222, 3.333, 4.444, 5.555, 6.666, 7.777, 8.888 };

double const * getCDoubleArray8() {
  return fp64Matrix8;
}

static float fp32Matrix8[8] = { 1.11, 2.22, 3.33, 4.44, 5.55, 6.66, 7.77, 8.88 };

float const * getCFloatArray8() {
  return fp32Matrix8;
}

static int intArray[8] = { 11, 22, 33, 44, 55, 66, 77, 88 };

int const * getCIntArray8() {
  return intArray;
}
