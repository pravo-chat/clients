plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
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


