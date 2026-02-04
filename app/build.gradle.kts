import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.0"
}

android {
    namespace = "com.foss.aihub"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.foss.aihub"
        minSdk = 24
        targetSdk = 36
        versionCode = 6
        versionName = "2.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    //noinspection WrongGradleMethod
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    val keystorePropertiesFile = file("../local.properties")
    val keystoreProperties = Properties()
    val keystoreExists = keystorePropertiesFile.exists()

    if (keystoreExists) {
        keystoreProperties.load(keystorePropertiesFile.inputStream())
        println("Keystore properties loaded successfully.")
    } else {
        println("Keystore properties file not found. No signing configuration will be applied.")
    }

    signingConfigs {
        if (keystoreExists) {
            create("release") {
                storeFile = file("$rootDir/keystore.jks")
                storePassword = keystoreProperties.getProperty("KEYSTORE_PASSWORD") ?: ""
                keyAlias = keystoreProperties.getProperty("KEY_ALIAS") ?: ""
                keyPassword = keystoreProperties.getProperty("KEY_PASSWORD") ?: ""
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            if (keystoreExists) {
                signingConfig = signingConfigs.getByName("release")
            }
        }

        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Manual dependency
    implementation("androidx.webkit:webkit:1.15.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("androidx.compose.material3.adaptive:adaptive:1.2.0")
    implementation("androidx.compose.material3.adaptive:adaptive-layout:1.2.0")
    implementation("androidx.compose.material3.adaptive:adaptive-navigation:1.2.0")
    implementation("com.google.android.material:material:1.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
    implementation("io.ktor:ktor-client-core:3.4.0")
    implementation("io.ktor:ktor-client-cio:3.4.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.4.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

}
