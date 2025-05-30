import com.android.build.api.dsl.ApkSigningConfig

plugins {
    id("com.android.application")
    id("com.sidneysimmons.gradle-plugin-external-properties")
}

externalProperties {
    propertiesFileResolver(file("signing.properties"))
}

android {
    namespace = "de.buttercookie.disabletargetapiblock"
    compileSdk = 35

    defaultConfig {
        applicationId = "de.buttercookie.disabletargetapiblock"
        minSdk = 34
        targetSdk = 35
        versionCode = 4
        versionName = "1.1"
        base.archivesName = "$applicationId-$versionName"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // This doesn't support Mercurial right now and also only causes confusion with
            // reproducible builds, which will likely build from the Github repository.
            vcsInfo.include = false
        }
    }
    packaging {
        resources {
            excludes.add("META-INF/**/annotation/LICENSE.txt")
            // Not needed as long as we don't use reflection with Kotlin
            excludes.add("**/*.kotlin_builtins")
            excludes.add("**/*.kotlin_module")
        }
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_16
        targetCompatibility = JavaVersion.VERSION_16
    }

    signingConfigs {
        named("debug") {
            if (checkExternalSigningConfig()) {
                applyExternalSigningConfig()
            } else {
                defaultConfig.signingConfig
            }
        }
        create("release") {
            if (checkExternalSigningConfig()) {
                applyExternalSigningConfig()
                android.buildTypes.getByName("release").signingConfig = this
            }
        }
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.9.1")
    compileOnly ("de.robv.android.xposed:api:82")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

fun ApkSigningConfig.checkExternalSigningConfig(): Boolean {
    return props.exists("$name.keyStore") &&
            file(props.get("$name.keyStore")).exists() &&
            props.exists("$name.storePassword") &&
            props.exists("$name.keyAlias") &&
            props.exists("$name.keyPassword")
}

fun ApkSigningConfig.applyExternalSigningConfig() {
    storeFile = file(props.get("$name.keyStore"))
    storePassword = props.get("$name.storePassword")
    keyAlias = props.get("$name.keyAlias")
    keyPassword = props.get("$name.keyPassword")
}