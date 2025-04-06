import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.sparshchadha.workout_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sparshchadha.workout_app"
        minSdk = 26
        targetSdk = 35
        versionCode = 102
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "FOOD_API_KEY", "\"${properties["FOOD_API_KEY"]}\"")
        buildConfigField("String", "PEXELS_API_KEY", "\"${properties["PEXELS_API_KEY"]}\"")
        buildConfigField("String", "GYM_WORKOUTS_API_KEY", "\"${properties["GYM_WORKOUTS_API_KEY"]}\"")
        buildConfigField("String", "NEWS_API_KEY", "\"${properties["NEWS_API_KEY"]}\"")
        buildConfigField("String", "GOOGLE_WEB_SERVER_CLIENT_ID", "\"${properties["GOOGLE_WEB_SERVER_CLIENT_ID"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-analytics:21.5.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // region constraint layout compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    // endregion

    // region coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    // endregion

    // region Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // endregion

    // region Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.5.0")
    implementation("com.squareup.okhttp3:okhttp-dnsoverhttps:4.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    // endregion

    // region Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.1")
    // endregion

    // region Chucker
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0")
    // endregion

    // region accompanist
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    // endregion

    // region lottie
    implementation("com.airbnb.android:lottie-compose:6.3.0")
    // endregion

    // region compose collectAsStateWithLifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    // endregion

    // region Room DB -
    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")
    // endregion

    // region Preference Datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // endregion

    // region intuit sdp
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    // endregion

    // region date and time picker
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    // endregion

    // region CameraX
    val cameraxVersion = "1.3.1"
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    implementation("androidx.camera:camera-video:${cameraxVersion}")

    implementation("androidx.camera:camera-view:${cameraxVersion}")
    implementation("androidx.camera:camera-extensions:${cameraxVersion}")
    // endregion

    // region firebase auth
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    // endregion

    // region splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")
    // endregion

    // region firebase performance monitoring
    implementation("com.google.firebase:firebase-perf-ktx:20.5.2")
    // endregion

    // region accompanist for permissions
    implementation ("com.google.accompanist:accompanist-permissions:0.23.1")
    // endregion

    // region Vico charts
    implementation("com.patrykandpatrick.vico:compose:2.0.0-alpha.20")
    implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.20")
    implementation("com.patrykandpatrick.vico:core:2.0.0-alpha.20")
    // endregion
}