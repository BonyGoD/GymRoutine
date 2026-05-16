plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.gradleBuildConfig) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    id("com.diffplug.spotless") version "7.0.2" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    extensions.configure(com.diffplug.gradle.spotless.SpotlessExtension::class.java) {
        kotlin {
            target("src/**/*.kt")
            targetExclude("**/build/**")
            ktlint().editorConfigOverride(
                mapOf(
                    "ktlint_standard_function-naming" to "disabled",
                    "ktlint_standard_no-wildcard-imports" to "disabled"
                )
            )
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint().editorConfigOverride(
                mapOf(
                    "ktlint_standard_no-wildcard-imports" to "disabled"
                )
            )
        }
        format("misc") {
            target("*.md", ".gitignore")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
