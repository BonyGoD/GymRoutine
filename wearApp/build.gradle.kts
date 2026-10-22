import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

val localProps = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localProps.load(localPropsFile.reader())
}

android {
    namespace = "dev.bonygod.gymroutine.wear"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "dev.bonygod.gymroutine"
        minSdk = 30
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 10018
        versionName = "0.0.1"
    }

    signingConfigs {
        val storeFile = localProps.getProperty("STORE_FILE", "")
        val storePassword = localProps.getProperty("KEYSTORE_PASSWORD", "")
        val keyAlias = localProps.getProperty("KEY_ALIAS", "")
        val keyPassword = localProps.getProperty("KEY_PASSWORD", "")

        if (storeFile.isNotBlank()) {
            create("release") {
                this.storeFile = rootProject.file(storeFile)
                this.storePassword = storePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        } else {
            logger.warn(
                "AVISO: falta STORE_FILE en local.properties; el release se compilará sin firmar. " +
                    "Claves necesarias: STORE_FILE, KEYSTORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD.",
            )
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            val releaseSigning = signingConfigs.findByName("release")
            if (releaseSigning != null) {
                signingConfig = releaseSigning
            }
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.wear.compose.material3)
    implementation(libs.wear.compose.foundation)
    implementation(libs.wear.compose.navigation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.play.services.wearable)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.serialization.json)
}
