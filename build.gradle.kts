plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

// Add test task that delegates to the composeApp subproject
tasks.register("test") {
    dependsOn(":composeApp:jsTest")
    group = "verification"
    description = "Run tests for all subprojects"
}