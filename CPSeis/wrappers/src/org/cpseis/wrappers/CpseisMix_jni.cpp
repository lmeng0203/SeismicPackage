//////// DO NOT EDIT THIS FILE - it is machine generated ////////

#include "CpseisMix.hh"
#include <jni.h>

extern "C"
{
  JNIEXPORT jlong JNICALL Java_org_cpseis_wrappers_CpseisMix_nativeCreate
    (JNIEnv *env, jobject obj)
  {
    CpseisMix *module = new CpseisMix();
    return (jlong)module;
  }
}
