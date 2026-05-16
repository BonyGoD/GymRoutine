import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gradleBuildConfig)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
        targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
            binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
                binaryOption("bundleId", "dev.bonygod.gymroutine.ComposeApp")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Navigation 3
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // GitLive Firebase
            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.gitlive.firebase.auth)
            implementation(libs.gitlive.firebase.crashlytics)
            implementation(libs.gitlive.firebase.analytics)

            // SignInKMP
            implementation("com.github.BonyGoD.SignInKMP:signin-kmp:2.0.0")
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.androidx.activity.compose)

            // Koin
            implementation(libs.koin.android)

            // Firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.auth)

            // Sign In with Google
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.googleid)
            implementation(libs.play.services.auth)

            // AdMob
            implementation(libs.play.services.ads)
        }
    }
}

android {
    namespace = "dev.bonygod.gymroutine"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

buildConfig {
    packageName("dev.bonygod.gymroutine")

    val properties = Properties()
    val localProperties = project.rootProject.file("local.properties")
    if (localProperties.exists()) {
        properties.load(localProperties.reader())
    }

    val apiKey = properties.getProperty("FIREBASE_API_KEY", "")
    val clientId = properties.getProperty("CLIENT_ID", "")

    buildConfigField("FIREBASE_API_KEY", apiKey)
    buildConfigField("CLIENT_ID", clientId)
}
