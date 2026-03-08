# ProGuard configuration for Android app
-dontwarn kotlin.**

# Keep all public classes and their public members
-keep public class * {
    public *;
}

# Keep annotations
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# Keep Room database classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep class **.data.** { *; }

# Keep Gson
-keep class com.google.gson.** { *; }

# Keep OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}
