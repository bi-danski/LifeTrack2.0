import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.io.FileInputStream


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotzilla)
    id("kotlin-parcelize")
}

val ltKeystorePropertiesFile = rootProject.file("keystore.properties")
val ltKeystoreProperties = Properties().apply {
    if (ltKeystorePropertiesFile.exists()) {
        load(FileInputStream(ltKeystorePropertiesFile))
    }
}

android {
    namespace = "org.lifetrack.ltapp"
    //noinspection GradleDependency
    compileSdk = 36

    defaultConfig {
        applicationId = "org.lifetrack.ltapp"
        minSdk = 30
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(ltKeystoreProperties["ltStoreFile"] as String? ?: "lt-release-key.jks")
            storePassword = ltKeystoreProperties["ltStorePassword"] as String?
            keyAlias = ltKeystoreProperties["ltKeyAlias"] as String?
            keyPassword = ltKeystoreProperties["ltKeyPassword"] as String?
        }
    }

    buildTypes {
//        debug {
//            buildConfigField("String", "ASS_ISTANT","\"$assistantServiceString\"")
//        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-P", "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.rootDir}/compose-metrics",
                    "-P", "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.rootDir}/compose-reports",
                    "-Xdata-flow-based-exhaustiveness"
//                    "-Xbuild-cache"
                )
            )
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    buildToolsVersion = "36.0.0"
    ndkVersion = "29.0.13599879 rc2"
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains:annotations:23.0.0")
        eachDependency {
            if (requested.group == "com.intellij" && requested.name == "annotations") {
                useTarget("org.jetbrains:annotations:23.0.0")
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.lint)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.viewbinding)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.material)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.coil.compose)
    implementation(libs.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.logging)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.androidx.startup)
    implementation(libs.kotzilla.sdk.compose)

    implementation(libs.charts)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.room.compiler)
    implementation(libs.mpandroidchart)

//    implementation("io.jsonwebtoken:jjwt-api:0.12.6")

    ksp(libs.room.compiler)
    implementation(libs.slf4j.nop)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.navigation.animation)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kotzilla {
    composeInstrumentation = true
}