# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontwarn
-dontskipnonpubliclibraryclassmembers
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * extends android.app.Activity {
  public void *(android.view.View);
}
-keepattributes Signature
-keepattributes *Annotation
# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepnames class * implements java.io.Serializable
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * implements java.io.Serializable {
    <fields>;
}

-keepclassmembers class * {
    public <methods>;
}

-keep public class org.apache.commons.httpclient.** {*;}
-keep public class org.apache.commons.httpclient.auth.** {*;}
-keep public class org.apache.commons.httpclient.cookie.** {*;}
-keep public class org.apache.commons.httpclient.methods.** {*;}
-keep public class org.apache.commons.httpclient.params.** {*;}
-keep public class org.apache.commons.httpclient.util.** {*;}
-keep public class org.apache.commons.codec.net.** {*;}
-keep public class org.apache.commons.logging.** {*;}
-keep public class org.apache.commons.logging.impl.** {*;}
-keep public class org.apache.commons.codec.** {*;}
-keep public class org.apache.commons.codec.binary.** {*;}
-keep public class org.apache.http.NameValuePair {*;}

-keep  class net.sqlcipher.** {*;}
-keep  class net.sqlcipher.database.** {*;}

#极光推送 混淆
-ignorewarnings#忽略警告
-dontoptimize#表示不进行优化,建议使用此选项,因为根据proguard-android-optimize.txt中的描述，
             #优化可能会造成一些潜在风险,不能保证在所有版本的Dalvik上都正常运行

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**


# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


# 这里需要改成解析到的javabea
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
#-keep class com.ISHello.Module.** { *; }
-keep class com.ISHello.GsonModule.** { *; }
#-keep class zlonglove.cn.network.bean.** { *; }
-keep class im.icbc.com.jnisdk.Native.** { *;}
-keep class im.icbc.com.jnisdk.**{ *;}
-keep class im.icbc.com.indexbarlayout.datas.**{ *;}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class * extends com.in.zlonglove.commonutil.minify.Gsonable