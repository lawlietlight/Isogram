# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\androidver2.4\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
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

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-ignorewarnings
-dontoptimize

-dump obfuscation/class_files.txt
-printseeds obfuscation/seeds.txt
-printusage obfuscation/unused.txt
-printmapping obfuscation/mapping.txt

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.vungle.** { public *; }

-keep class javax.inject.*

-keepattributes *Annotation*
-libraryjars /libs/
#-libraryjars /AndroidViewAnimations-1.1.2.jar
#-libraryjars /AndroidEasingFunctions-1.0.0.jar
#-libraryjars /dagger-1.2.2.jar
#-libraryjars /nineoldandroids-2.4.0.jar
#-libraryjars /ruucdprrwp.jar
#-libraryjars /StartAppInApp-2.4.9.jar
#-libraryjars /vungle-publisher-adaptive-id-3.2.2.jar
-libraryjars <java.home>/lib/rt.jar
-keepattributes Signature
-keepnames class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn com.startapp.**
-keep class com.startapp.** {
      *;
}
-keep class com.daimajia.** {
      *;
}
-keep class com.nineoldandroids.**{
 	  *;
}
-keepattributes Exceptions, InnerClasses, Signature, Deprecated,  SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-keep public class com.efficientsciences.cowsandbulls.wordwars.domain.** {
  *;
}
-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility { public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *; }
-keep class com.ruucdprrwp.** { *; }
-keepclassmembers class **.R$*
{
 public static <fields>;
}
-keep class **.R$*
-keepattributes *Annotation*,EnclosingMethod
-keep class org.codehaus.** { *; }
-keep class !com.efficientsciences.cowsandbulls.**

-dontwarn android.support.**
-dontwarn com.badlogic.gdx.backends.android.AndroidFragmentApplication
-dontwarn com.badlogic.gdx.utils.GdxBuild
-dontwarn com.badlogic.gdx.physics.box2d.utils.Box2DBuild
-dontwarn com.badlogic.gdx.jnigen.BuildTarget*
-dontwarn org.codehaus.jackson.map.ext.**

-keepclassmembers class com.badlogic.gdx.backends.android.AndroidInput* {
   <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object, com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}

-keepclassmembers class com.badlogic.gdx.physics.box2d.World {
   boolean contactFilter(long, long);
   void    beginContact(long);
   void    endContact(long);
   void    preSolve(long, long);
   void    postSolve(long, long);
   boolean reportFixture(long);
   float   reportRayFixture(long, float, float, float, float, float);
}

-keep class com.android.vending.billing.**

-keep class com.badlogic.** { *; }
-keep class * implements com.badlogic.gdx.utils.Json*

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
   # public static int e(...);
}
-keep class com.facebook.** { *; }
-keep class com.sromku.simple.fb.** { *; }
-keepattributes Signature
-keep class dagger.*