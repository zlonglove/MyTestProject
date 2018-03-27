/**
 * c++写法时必须加上extern "C"
 */
#include "include/myinclude.h"
/**
 * autor:zhanglong
 * time:2014/8
 */
/************************************************************************************************
 * JNI OnLoad    init
 * JNI OnUnLoad	 realse
 ************************************************************************************************/
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGI("%s IN JNI_OnLoad.....", LOG);
    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        LOGE("%s Unable to get the env at JNI_OnLoad", LOG);
        return JNI_ERR;
    }
    if (!registerNatives(env)) { //注册
        LOGE("%s registerNatives() error", LOG);
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGI("%s IN JNI_Unload.....", LOG);
}

/********************************************************************************************
 *字符串操作函数
 *string
 *GetStringChars--->ReleaseStringChars
 *GetStringUTFChars--->ReleaseStringUTFChars
 *GetStringLength
 *GetStringUTFLength
 *NewString
 *NewStringUTF
 *GetStringCritical--->ReleaseStringCritical
 *GetStringRegion---->SetStringRegion
 *GetStringUTFRegion--->SetStringUTFRegion
 ********************************************************************************************/
JNIEXPORT jstring JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_stringFromJNI(JNIEnv *env, jobject thiz) {
    LOGI("%s stringFromJNI()", LOG);
    jstring str = env->NewStringUTF("Hello from JNI !");
    if (str == NULL) {
        LOGE("%s stringFromJNI() error,out of memory", LOG);
        return NULL;
    }
    return str;
}

JNIEXPORT void JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_stringPutJNI(JNIEnv *env, jobject thiz, jstring prompt) {
    if (prompt == NULL) {
        LOGE("%s arg is null,check it!", LOG);
        return;
    }
    const char *str = NULL;
    char *buffer = NULL;
    /**
     * second arg(JNI_TRUE,JNI_FALSE,NULL)
     */
    str = env->GetStringUTFChars(prompt, NULL);
    if (str == NULL) {
        LOGE("%s string put jni,out of memory", LOG);
        return;
    }
    jint len = env->GetStringUTFLength(prompt);
    int nystrlen = strlen(str);
    int charlen = sizeof(char);
    LOGI("%s string put jni==%s len==%d charlen==%d\n", LOG, str, nystrlen, charlen);
    buffer = (char *) malloc(5 * sizeof(char));
    env->GetStringUTFRegion(prompt, 0, 5, buffer);
    LOGI("%s string put jni cut==%s\n", LOG, buffer);
    free(buffer);
    env->ReleaseStringUTFChars(prompt, str);
}

/********************************************************************************************
 * 初始化和资源释放函数
 ********************************************************************************************/
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_init(JNIEnv *env, jobject thiz) {
    LOGI("%s init version==%d\n", LOG, env->GetVersion());
    globalObject = env->NewGlobalRef(thiz);
    jclass cls = env->GetObjectClass(thiz);
    if (cls == NULL) {
        LOGE("%s get object class error", LOG);
        return;
    }
    globalClass = (jclass) env->NewGlobalRef(cls);
    if (globalClass == NULL) {
        LOGE("%s new global ref error", LOG);
        return;
    }
    jint vmFlag = env->GetJavaVM(&jvm);
    if (JNI_OK != vmFlag) {
        LOGE("%s get java vm error", LOG);
    }
    LOGI("%s get global class successful", LOG);
    /*
     int result;
     result=system("mkdir /data/data/cn.zlonglove.myjniproject/temp");
     if(result==-1||result==127)
     {
     LOGE("%s mkdir temp error!",LOG);
     }
     */
}


JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_Release(JNIEnv *env, jobject thiz) {
    LOGI("%s Release Begin!!", LOG);
    if (globalObject != NULL) {
        env->DeleteGlobalRef(globalObject);
    }
    if (globalClass != NULL) {
        env->DeleteGlobalRef(globalClass);
    }
    LOGI("%s Release End!!", LOG);
}

/***************************************************************************************************
 * Array
 *Get<Type>ArrayRegion----------Set<Type>ArrayRegion
 *get<Type>ArrayElements--------Release<Type>ArrayElements
 *
 *GetArrayLength
 *New<Type>Array
 *第二种方法
 jint buf[]={0,0,0,0,0};//定义一个jint类型的buffer把原始的数组copy到这个buf中去
 env->GetIntArrayRegion(array1,0,length,buf);
 **************************************************************************************************/
JNIEXPORT jint JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_sumArray(JNIEnv *env, jobject thiz, jintArray intArray) {
    jint *str = env->GetIntArrayElements(intArray, NULL);
    if (str == NULL) {
        LOGE("%s getIntArrayElements error", LOG);
    }
    jint sum = 0;
    int len = env->GetArrayLength(intArray);
    LOGI("%s Int Array Length==%d\n", LOG, len);
    for (int i = 0; i < len; i++) {
        sum += str[i];
    }
    env->ReleaseIntArrayElements(intArray, str, 0);
    return sum;
}

JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_setStringArray(JNIEnv *env, jobject thiz,
                                                                         jobjectArray stringArray) {
    if (stringArray == NULL) {
        LOGE("%s args is null", LOG);
        return;
    }
    int size = env->GetArrayLength(stringArray);
    for (int i = 0; i < size; i++) {
        jstring obja = (jstring) env->GetObjectArrayElement(stringArray, i);
        const char *chars = env->GetStringUTFChars(obja, NULL);
        LOGI("%s element[%d]==[%s]\n", LOG, i, chars);
    }
}

/************GetObjectArrayElement SetObjectArrayElement************************/
JNIEXPORT jobjectArray JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_getStringArray(JNIEnv *env, jobject thiz) {
    jobjectArray objectArray;
    int size = 4;
    jclass stringClass = env->FindClass("java/lang/String");
    objectArray = env->NewObjectArray(size, stringClass, 0);
    if (objectArray == NULL) {
        LOGE("%s new object Array error!", LOG);
        return NULL;
    }
    for (int i = 0; i < size; i++) {
        jstring str = env->NewStringUTF("zhanglong");
        env->SetObjectArrayElement(objectArray, i, str);
    }
    return objectArray;

}

/*********************************************************************************************
 *
 * class Transfer Relevant
 *
 *********************************************************************************************/
JNIEXPORT jobject JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_getStudentInfo(JNIEnv *env, jobject thiz) {
    //关于包描述符，这儿可以是cn/zlonglove/data/Student 或者是 Lcn/zlonglove/data/Student;
    //这两种类型 都可以获得class引用
    //或得Student类引用
    jclass stucls = env->FindClass("im/icbc/com/jnisdk/Student");
    if (stucls == NULL) {
        LOGE("%s can not find class im.icbc.com.jnisdk.Student!\n", LOG);
        return NULL;
    }
    //获得得该类型的构造函数  函数名为 <init> 返回类型必须为 void即V
    jmethodID constrocMID = env->GetMethodID(stucls, "<init>", "(ILjava/lang/String;)V");
    jstring str = env->NewStringUTF("come from Native");
    jint age = 23;
    //构造一个对象，调用该类的构造函数，并且传递参数
    jobject stu_ojb = env->NewObject(stucls, constrocMID, age, str);
    return stu_ojb;
}

JNIEXPORT jobject JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_getListStudents(JNIEnv *env, jobject thiz) {
    jclass list_cls = env->FindClass("java/util/ArrayList");
    if (list_cls == NULL) {
        LOGE("%s can not find class ArrayList!!\n", LOG);
        return NULL;
    }
    //获得得构造函数Id
    jmethodID list_costruct = env->GetMethodID(list_cls, "<init>", "()V");
    jobject list_obj = env->NewObject(list_cls, list_costruct); //创建一个Arraylist集合对象

    //或得Arraylist类中的 add()方法ID，其方法原型为：boolean add(Object object) ;
    jmethodID list_add = env->GetMethodID(list_cls, "add", "(Ljava/lang/Object;)Z");

    jclass stu_cls = env->FindClass("im/icbc/com/jnisdk/Student"); //获得Student类引用
    //获得该类型的构造函数  函数名为 <init> 返回类型必须为 void 即 V
    jmethodID stu_costruct = env->GetMethodID(stu_cls, "<init>", "(ILjava/lang/String;)V");

    for (int i = 0; i < 3; i++) {
        jstring str = env->NewStringUTF("Native");
        //通过调用该对象的构造函数来new 一个 Student实例
        jobject stu_obj = env->NewObject(stu_cls, stu_costruct, 10, str);
        //执行Arraylist类实例的add方法，添加一个stu对象
        env->CallBooleanMethod(list_obj, list_add, stu_obj);
    }
    return list_obj;
}

JNIEXPORT void JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_setStudentInfo(JNIEnv *env, jobject thiz, jobject obj) {
    jclass stu_cls = env->GetObjectClass(obj);
    if (stu_cls == NULL) {
        LOGE("%s can not find Student class!", LOG);
        return;
    }
    jfieldID ageFieldID = env->GetFieldID(stu_cls, "age", "I");
    jfieldID nameFieldID = env->GetFieldID(stu_cls, "name",
                                           "Ljava/lang/String;");
    jint age = env->GetIntField(obj, ageFieldID);
    jstring name = (jstring) env->GetObjectField(obj, nameFieldID);

    jmethodID toStringMethodID = env->GetMethodID(stu_cls, "toString",
                                                  "()Ljava/lang/String;");
    jstring toString = (jstring) env->CallObjectMethod(obj, toStringMethodID);

    const char *c_name = env->GetStringUTFChars(name, NULL);
    const char *c_tostring = env->GetStringUTFChars(toString, NULL);
    LOGI("%s info==%s\n", LOG, c_tostring);
    env->ReleaseStringUTFChars(name, c_name);
    env->ReleaseStringUTFChars(toString, c_tostring);
}

/****************************************************************************************************
 * File
 ***************************************************************************************************/
JNIEXPORT void JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_writeFile(JNIEnv *env, jobject thiz, jstring buffer) {
    if (buffer == NULL) {
        LOGE("%s arg is null,check it!", LOG);
        return;
    }
    const char *c_buffer = env->GetStringUTFChars(buffer, NULL);
    if (c_buffer == NULL) {
        LOGE("%s get string utf buffer error,out of memory!", LOG);
        return;
    }
    size_t count = strlen(c_buffer);
    LOGI("%s write buffer size==%lu\n", LOG, count);//lu 无符号长整型整数
    FILE *stream = fopen("/data/data/com.example.ishelloword/temp", "w");
    if (stream == NULL) {
        LOGE("%s open file error!", LOG);
    }
    if (count != fwrite(c_buffer, sizeof(char), count, stream)) {
        LOGE("%s write buffer error!", LOG);
    }

    fflush(stream);
    fclose(stream);
    env->ReleaseStringUTFChars(buffer, c_buffer);
}

JNIEXPORT jstring JNICALL Java_im_icbc_com_jnisdk_Native_JNI_readFile(JNIEnv *env, jobject thiz) {
    char *buffer = (char *) malloc(100 * sizeof(char));
    if (buffer == NULL) {
        LOGE("%s malloc buffer error,out of memory!", LOG);
        return NULL;
    }
    FILE *stream = fopen("/data/data/com.example.ishelloword/temp", "r");
    if (stream == NULL) {
        LOGE("%s open file error!", LOG);
    }
    int count = fread(buffer, sizeof(char), 100, stream);
    if (count < 1) {
        LOGE("%s fread buffer error!", LOG);
    }
    jstring jbuffer = env->NewStringUTF(buffer);
    free(buffer);
    return jbuffer;
}

/***************************************************************************************************
 * 作用域[静态和非静态]
 ***************************************************************************************************/
JNIEXPORT jstring JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_getInstanceField(JNIEnv *env, jobject thiz) {
    jclass clazz;
    /**
     * 用对象引用获得类
     */
    clazz = (env)->GetObjectClass(thiz);
    jfieldID instanceFieldId = (env)->GetFieldID(clazz, "instanceField", "Ljava/lang/String;");
    jstring instanceField;
    if (instanceFieldId == NULL) {
        LOGE("%s Get instanceField FieldID Error!", LOG);
        return NULL;
    }
    instanceField = (jstring) (env->GetObjectField(thiz, instanceFieldId));
    return instanceField;
}

JNIEXPORT jstring JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_getStaticField(JNIEnv *env, jobject thiz) {
    jclass clazz;
    /**
     * 用对象引用获得类
     */
    clazz = (env)->GetObjectClass(thiz);
    jfieldID staticFieldId = (env)->GetStaticFieldID(clazz, "staticField", "Ljava/lang/String;");
    if (staticFieldId == NULL) {
        LOGE("%s Get staticField FieldID Error!", LOG);
        return NULL;
    }
    jstring instanceField;
    instanceField = (jstring) env->GetStaticObjectField(clazz, staticFieldId);
    return instanceField;
}

JNIEXPORT jint JNICALL Java_im_icbc_com_jnisdk_Native_JNI_getIntField(JNIEnv *env, jobject thiz) {
    jclass clazz;
    /**
     * 用对象引用获得类
     */
    clazz = (env)->GetObjectClass(thiz);
    jfieldID intFieldId = (env)->GetFieldID(clazz, "intInstance", "I");
    if (intFieldId == NULL) {
        LOGE("%s Get intInstance FieldID Error!", LOG);
        return -1;
    }
    jint intInstanceField;
    intInstanceField = env->GetIntField(thiz, intFieldId);
    //env->GetStaticIntField();
    LOGI("%s getIntField==%d\n", LOG, intInstanceField);

    env->SetIntField(thiz, intFieldId, 100);
    //env->SetStaticIntField();
    return intInstanceField;
}

/*********************************************************************************************************
 *Fuction
 *jmethodID = GetMethodID()/GetStaticMethodID();
 *Call(return value type)Method()/CallStatic(return value type)Method()
 * *******************************************************************************************************/
JNIEXPORT jstring JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_getInstanceMethod(JNIEnv *env, jobject thiz) {
    jclass cls = env->GetObjectClass(thiz);
    jmethodID instanceMethodID;
    instanceMethodID = env->GetMethodID(cls, "instanceMethod", "()Ljava/lang/String;");
    if (instanceMethodID == NULL) {
        LOGE("%s get instance Method error", LOG);
        return NULL;
    }
    jstring instanceString = (jstring) env->CallObjectMethod(thiz, instanceMethodID);
    return instanceString;
}

JNIEXPORT jstring JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_getStaticMethod(JNIEnv *env, jobject thiz) {
    jclass cls = env->GetObjectClass(thiz);
    jmethodID staticMethodID;
    staticMethodID = env->GetStaticMethodID(cls, "staticMethod", "()Ljava/lang/String;");
    if (staticMethodID == NULL) {
        LOGE("%s get static Method error", LOG);
        return NULL;
    }
    jstring staticString = (jstring) env->CallStaticObjectMethod(cls, staticMethodID);
    return staticString;
}

JNIEXPORT jint JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_GetIntInstanceMethod(JNIEnv *env, jobject thiz) {
    printf("GetIntInstanceMethod call!\n");
    jclass cls = env->GetObjectClass(thiz);
    jmethodID instanceMethodID;
    instanceMethodID = env->GetMethodID(cls, "IntInstanceMethod", "(I)I");
    if (instanceMethodID == NULL) {
        LOGE("%s get IntInstance Method error", LOG);
        return (jint) -1;
    }
    jint intvalue = env->CallIntMethod(thiz, instanceMethodID, 12);
    return intvalue;
}

JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_accessMethod(JNIEnv *env, jobject thiz) {
    jclass cls = env->GetObjectClass(thiz);
    jmethodID methodId = env->GetMethodID(cls, "throwingMethod", "()V");
    if (methodId == NULL) {
        LOGE("%s get accessMethod error", LOG);
    }
    env->CallVoidMethod(thiz, methodId);
    jthrowable ex = env->ExceptionOccurred();
    if (0 != ex) {
        env->ExceptionClear();
        LOGE("%s throw Exception\n", LOG);
        /**
         * Exception handler
         */
    }
}

JNIEXPORT void JNICALL
Java_im_icbc_com_jnisdk_Native_JNI_throwException(JNIEnv *env, jobject thiz) {
    jclass cls = env->FindClass("java/lang/NullPointerException");
    if (cls == NULL) {
        LOGE("%s find class java/lang/NullPointerException error", LOG);
        return;
    }
    env->ThrowNew(cls, "My Null Exception");
    /**
     * 刪除一個局部引用
     */
    env->DeleteLocalRef(cls);
}

/**************************************************************************************
 * JNI Thread
 **************************************************************************************/
JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_syncThread(JNIEnv *env, jobject thiz) {
    jint id = env->MonitorEnter(thiz);
    if (id == JNI_ERR) {
        LOGI("%s MonitorEnter error %d\n", LOG, id);
    }
    /**
     * sync  code
     */
    jint exitId = env->MonitorExit(thiz);
    if (JNI_ERR == exitId) {
        LOGI("%s MonitorExit error %d\n", LOG, exitId);
    }
}

void *callBack(void *arg) {
    JNIEnv *env;
    jmethodID mid = NULL;
    jstring value = NULL;
    jstring data = NULL;
    if (jvm != NULL) {
        if (jvm->AttachCurrentThread(&env, NULL) != JNI_OK) {
            LOGE("%s: AttachCurrentThread() failed", __FUNCTION__);
            return NULL;
        }
        LOGE("%s callBack Thread Id==%u\n", LOG, (unsigned) pthread_self());
        int param = *(int *) arg;
        /**
         * JNI回调Java方法[static和非static]
         */
        mid = (env)->GetMethodID(globalClass, "callBackJava", "(I)V");
        if (mid == NULL) {
            LOGE("%s get callBackJava() method ID error", LOG);
            goto err;
        } else {
            LOGE("get callBackJava() method ID success");
        }
        (env)->CallVoidMethod(globalObject, mid, param);


        mid = env->GetStaticMethodID(globalClass, "callBackFromJNI",
                                     "(ILjava/lang/String;Ljava/lang/String;)V");
        if (mid == NULL) {
            LOGE("%s get callBackFromJNI() method ID error", LOG);
            goto err;
        }
        const char *cValue = "zhanglong";
        const char *cData = "hello word";
        value = env->NewStringUTF(cValue);
        data = env->NewStringUTF(cData);
        env->CallStaticVoidMethod(globalClass, mid, param, value, data);
    }
    err:
    if (jvm->DetachCurrentThread() != JNI_OK) {
        LOGE("%s: DetachCurrentThread() failed", __FUNCTION__);
    }
    jvm->DetachCurrentThread();
    //pthread_exit(0);
    return ((void *) 0);
}

JNIEXPORT void JNICALL Java_im_icbc_com_jnisdk_Native_JNI_startThread(JNIEnv *env, jobject thiz) {
    pthread_t t;
    int i = 1234;
    int id = pthread_create(&t, NULL, &callBack, &i);
    if (id != 0) {
        LOGE("pthread_create() error=%d", id);
    }
    pthread_join(t, NULL);
}

/****************************************************************************************
 *dynamic register
 ****************************************************************************************/

jstring native_hello(JNIEnv *env, jobject thiz) {
    return (env)->NewStringUTF("动态注册JNI1");
}

jstring native_register(JNIEnv *env, jobject thiz, jint i) {
    LOGI("%s native register int==%d", LOG, i);
    return (env)->NewStringUTF("动态注册JNI2");
}

/*
 * 为某一个类注册本地方法
 */
int registerNativeMethods(JNIEnv *env, const char *className, JNINativeMethod *gMethods,
                          int numMethods) {
    jclass clazz;
    clazz = (env)->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if ((env)->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }
    env->DeleteLocalRef(clazz);
    return JNI_TRUE;
}

/*
 * 为所有类注册本地方法
 */
int registerNatives(JNIEnv *env) {
    LOGI("%s dynamic register method length==%d\n", LOG,
         (int) (sizeof(gMethods) / sizeof(gMethods[0])));
    return registerNativeMethods(env, kClassName, gMethods, sizeof(gMethods) / sizeof(gMethods[0]));
}

/************************************************************************************
 *
 ************************************************************************************/

