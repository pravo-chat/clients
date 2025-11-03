plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
            @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl::class)
            distribution {
                outputDirectory = file("$projectDir/build/dist/js/productionExecutable")
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.html.core)
            implementation(compose.html.svg)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


