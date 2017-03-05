# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes *Annotation*
-keep class butterknife.** { *; }
-keep class **$$ViewInjector { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn butterknife.internal.**

-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keepattributes Signature
-keep class com.google.gson.** { *; }

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.internal.huc.**
-dontwarn retrofit.appengine.UrlFetchClient
-dontwarn rx.**
-dontwarn okio.**

-keep class dagger.** { *; }
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection

-keep class javax.inject.** { *; }
-dontwarn dagger.internal.codegen.**

-keepclassmembers, includedescriptorclasses class ** {
    public void onEvent*(**);
}
-keepclassmembers, includedescriptorclasses class ** {
    public void onEventMainThread*(**);
}

-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, InnerClasses

-keep class com.jumio.** { *; }
-keep class com.mobsandgeeks.saripaar.** {*;}
-keep class commons.validator.routines.** {*;}

-dontobfuscate

-keep public class MainActivity

