plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.10"
    id("app.cash.sqldelight") version "2.0.2"
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("org.example.db")
        }
    }
}

kotlin {
    //You need to change the default code to this one to use sqldelight
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    //End code
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //Koin
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:4.0.2"))
            implementation(libs.koin.core)
            implementation(libs.koin.android)

            //SqlDelight
            implementation(libs.android.driver)

            //Ktor
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            api(compose.foundation)
            api(compose.animation)
            api(compose.materialIconsExtended)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            //Navigation PreCompose
            api(libs.precompose)
            api(libs.precompose.viewmodel)

            //Koin
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:4.0.2"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            api(libs.precompose.koin)

            //SqlDelightCoroutines
            implementation(libs.runtime)
            implementation(libs.coroutines.extensions)

            //Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.content.negotiation)
            implementation(libs.ktor.content.serialization)
        }
        iosMain.dependencies {
            //Libraries IOS
            //SqlDelight
            implementation(libs.native.driver)
            implementation(libs.stately.common)

            //Ktor
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

}

dependencies {
    debugImplementation(compose.uiTooling)
}
