// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Just let gradle know that these plugins exist - apply them in the corresponding gradle module
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.junit5) apply false
}