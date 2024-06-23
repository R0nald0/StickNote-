plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id ("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.lembretes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lembretes"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        kapt{
            arguments {
                arg(name = "room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    val nav_version = "2.7.7"

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3.android)
    testImplementation("org.testng:testng:6.9.6")
    val roomVersion = "2.6.1"
    val lifecycle_version = "2.7.0"

    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    //Navigation

    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
    implementation(libs.androidx.room.runtime)
    kapt("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
  //  implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation (libs.truth.v142)
    //Dependências biblioteca Truth
    testImplementation ("com.google.truth:truth:1.1.3")
    androidTestImplementation ("com.google.truth:truth:1.4.2")
    //Kotlin-coroutineTeste
    testImplementation("app.cash.turbine:turbine:1.1.0")
    androidTestImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation (libs.kotlinx.coroutines.test)
    //Dependência da Biblioteca Mockito
    testImplementation (libs.mockito.core)

    // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

}
kapt {
    correctErrorTypes = true
}