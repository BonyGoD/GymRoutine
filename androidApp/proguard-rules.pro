# Reglas de R8 para el build de release.
#
# Se aplican encima de `proguard-android-optimize.txt` (el fichero por defecto de AGP), que ya
# cubre lo estándar de Android: enums, Parcelables, vistas infladas desde XML, `native`, etc.
# Aquí solo va lo que R8 no puede deducir de este proyecto en concreto.
#
# R8 opera en modo full (por defecto desde AGP 8), que es más agresivo que ProGuard: no asume
# que nada se use por reflexión salvo que una regla lo diga.

########################################
# Crashlytics
########################################

# Sin estos dos atributos las trazas llegan a Crashlytics sin fichero ni número de línea, y el
# informe deja de servir para nada. El plugin de Crashlytics sube el mapping automáticamente en
# release, así que la consola desofusca los nombres de clase por su cuenta.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

########################################
# Navegación: migas de pan legibles
########################################

# `Navigator` registra cada navegación en Crashlytics con `route::class.simpleName`. Eso se evalúa
# en tiempo de ejecución y acaba dentro del *texto* de un log, y Crashlytics solo desofusca trazas,
# no el contenido de los mensajes. Sin esta regla todas las migas serían "Navigate to a".
-keepnames class dev.bonygod.gymroutine.core.navigation.Routes
-keepnames class dev.bonygod.gymroutine.core.navigation.Routes$*

########################################
# kotlinx.serialization
########################################

# Reglas oficiales del README de kotlinx.serialization. Los DTO de Firestore (`RoutineDto`,
# `WorkoutLogDto`, ...) se deserializan con `doc.data<T>()` de GitLive, que resuelve el serializer
# vía `T::class.serializer()`; estas reglas evitan que R8 se lleve por delante el `Companion` o la
# clase `$serializer` generada por el plugin.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

########################################
# Inicio de sesión con Google
########################################

# Credential Manager instancia su implementación de Play Services **por nombre**, en tiempo de
# ejecución. Las librerías de AndroidX y Google traen sus propias reglas de consumidor, pero el
# sign-in es la puerta de entrada a la app: si falla, la app entera es inservible, así que aquí se
# duplican a propósito.
-keep class androidx.credentials.playservices.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }

# SignInKMP es una librería propia publicada en JitPack y **no lleva reglas de consumidor**. Se
# mantiene entera de forma preventiva hasta comprobar en un release firmado qué necesita de
# verdad; entonces se puede acotar.
-keep class dev.bonygod.signin.kmp.** { *; }
