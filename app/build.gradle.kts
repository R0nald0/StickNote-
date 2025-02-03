plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id ("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "com.example.lembretes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lembretes"
        minSdk = 24
        targetSdk = 35
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

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    // Jetpack Compose Integration
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.text.google.fonts)


    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    // https://github.com/kizitonwose/Calendar?tab=readme-ov-file
    implementation("com.kizitonwose.calendar:compose:2.5.4")


    implementation(libs.androidx.appcompat)
    // hiltViewModel
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation("org.testng:testng:6.9.6")

    val lifecycle_version = "2.7.0"

    implementation (libs.accompanist.systemuicontroller)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    //Navigation

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    implementation (libs.gson)



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v270)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation (libs.truth.v142)
    //Dependências biblioteca Truth
    testImplementation (libs.truth.v113)
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