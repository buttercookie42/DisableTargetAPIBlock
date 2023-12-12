plugins {
    id("com.android.application")
}

android {
    namespace = "de.buttercookie.disabletargetapiblock"
    compileSdk = 34

    defaultConfig {
        applicationId = "de.buttercookie.disabletargetapiblock"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packaging {
        resources {
            // Not needed as long as we don't use reflection with Kotlin
            excludes.add("**/*.kotlin_builtins")
            excludes.add("**/*.kotlin_module")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.annotation:annotation:1.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}