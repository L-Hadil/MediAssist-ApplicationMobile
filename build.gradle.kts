plugins {
    // Plugin Android et Kotlin déclarés sans appliquer
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Google services plugin pour Firebase
    id("com.google.gms.google-services") version "4.4.2" apply false
}