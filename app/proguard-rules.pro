-keepattributes SourceFile,LineNumberTable,Signature,InnerClasses,*Annotation*
-renamesourcefileattribute SourceFile
-keep class kotlin.Metadata { *; }

-keep class org.koin.androix.** { *; }
-keepclassmembers class * { @org.koin.core.annotation.KoinInternalApi *; }
-keep class org.lifetrack.ltapp.di.** { *; }

-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keep class * extends androidx.navigation.Navigator
-keep class * extends androidx.navigation.NavDestination

-keep class io.kotzilla.sdk.** { *; }
-keep class io.kotzilla.data.json.** { *; }

-keep class io.ktor.client.engine.** { *; }
-keep class io.ktor.client.plugins.** { *; }
-dontwarn io.ktor.**

-keep @kotlinx.serialization.Serializable class ** { *; }
-keepclassmembers class ** {
    *** Companion;
    *** serializer(...);
}
-keepclassmembers class kotlinx.** { volatile <fields>; }
-dontwarn kotlinx.coroutines.debug.**

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }
-keep class * extends androidx.room.RoomOpenHelper
-keep class androidx.sqlite.db.framework.FrameworkSQLiteOpenHelper { *; }
-dontwarn androidx.room.paging.**

-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
    @androidx.compose.runtime.ReadOnlyComposable *;
}

-dontwarn org.slf4j.**