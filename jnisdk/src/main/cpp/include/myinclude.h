#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <stdlib.h>
#include <time.h>

#include <unistd.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <fcntl.h>
/* Header for class im_icbc_com_jnisdk_Native_JNI */

#ifndef _Included_im_icbc_com_jnisdk_Native_JNI
#define _Included_im_icbc_com_jnisdk_Native_JNI
#ifdef __cplusplus

#define LOG "[--Native Code--]"
/**
 * 需要在Android.mk
 * LOCAL_LDLIBS := -llog
 */
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG,__VA_ARGS__)   // 对应Android中Debug模式
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG,__VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG,__VA_ARGS__)
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG,__VA_ARGS__)

//定义随机数产生宏 表示产生0~x之间的随机数
#define random(x) (rand()%x)

#ifndef NULL
#define NULL   ((void *) 0)
#endif

extern "C" {
#endif
/**
 * 常量定义
 */
const char *kClassName = "im/icbc/com/jnisdk/Native/JNI"; //指定要注册的类

jclass globalClass = NULL;
jobject globalObject = NULL;
JavaVM *jvm = NULL;
/**
 * 内部方法定义
 */
int registerNatives(JNIEnv *env);
void *callBack(void *arg);

jstring native_hello(JNIEnv *env, jobject thiz);
jstring native_register(JNIEnv *env, jobject thiz, jint i);

int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gMethods,
                          int numMethods);


/**
 * 方法对应表
 */
JNINativeMethod gMethods[] = {{"dynamicRegister",  "()Ljava/lang/String;",  (void *) native_hello},
                              {"dynamicRegister2", "(I)Ljava/lang/String;", (void *) native_register}, //绑定
};

/**
 * Native方法定义
 */

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_init
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    Release
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_Release
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    stringFromJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_stringFromJNI
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    stringPutJNI
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_stringPutJNI
        (JNIEnv *, jobject, jstring);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    sumArray
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL Java_im_icbc_com_jnisdk_Native_JNI_sumArray
        (JNIEnv *, jobject, jintArray);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    setStringArray
 * Signature: ([Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_setStringArray
        (JNIEnv *, jobject, jobjectArray);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getStringArray
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getStringArray
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getStudentInfo
 * Signature: ()Lcn/zlonglove/data/Student;
 */
JNIEXPORT jobject JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getStudentInfo
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getListStudents
 * Signature: ()Ljava/util/ArrayList;
 */
JNIEXPORT jobject JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getListStudents
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    setStudentInfo
 * Signature: (Lcn/zlonglove/data/Student;)V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_setStudentInfo
        (JNIEnv *, jobject, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    writeFile
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_writeFile
        (JNIEnv *, jobject, jstring);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    readFile
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_readFile
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getInstanceField
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getInstanceField
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getStaticField
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getStaticField
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getIntField
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getIntField
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getInstanceMethod
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getInstanceMethod
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    getStaticMethod
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getStaticMethod
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    GetIntInstanceMethod
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_im_icbc_com_jnisdk_Native_JNI_GetIntInstanceMethod
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    accessMethod
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_accessMethod
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    throwException
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_throwException
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    syncThread
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_syncThread
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    startThread
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_startThread
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    dynamicRegister
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_dynamicRegister
        (JNIEnv *, jobject);

/*
 * Class:     cn_zlonglove_myjniproject_JNI
 * Method:    dynamicRegister2
 * Signature: (I)Ljava/lang/String;
 */
/**
JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_dynamicRegister2
        (JNIEnv *, jobject, jint);

**/
#ifdef __cplusplus
}
#endif
#endif
