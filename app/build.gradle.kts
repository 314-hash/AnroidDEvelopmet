plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.io.realm.kotlin)
}

android {
    namespace = "com.pd.field_staff"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pd.field_staff"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("signKey") {
            storeFile = rootProject.file("app/pd-field-staff.jks")
            storePassword = "mkGz5wHdNJYacP4r"
            keyAlias = "pdfieldstaff"
            keyPassword = "mkGz5wHdNJYacP4r"
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("signKey")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("signKey")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,INDEX.LIST}"
        }
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            optIn("kotlin.experimental.ExperimentalTypeInference")
            optIn("kotlin.uuid.ExperimentalUUIDApi")
        }
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)

    // lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // di - koin
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.viewmodel)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.androidx.compose.navigation)

    // ktor client for http request
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.logback.classic)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    // Realm DB
    implementation(libs.realm.kotlin)

    // app updates
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    // Google Maps Compose - https://github.com/googlemaps/android-maps-compose
    implementation(libs.maps.compose)
    // Google Play Services Maps
    implementation(libs.play.services.maps)
    // Play Services Location
    implementation(libs.play.services.location)
    // splash screen
    implementation(libs.core.splashscreen)
    //crypto
    implementation(libs.androidx.security.crypto)
    // Add Timber library
    implementation(libs.timber)
    // Accompanist
    implementation(libs.coil.compose)
    implementation(libs.accompanist.permissions)
    // lottie animation
    implementation(libs.lottie.compose)




    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}