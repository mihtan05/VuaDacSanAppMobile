plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.baicuoiki"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.baicuoiki"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            pickFirsts += "META-INF/NOTICE"
            pickFirsts += "META-INF/LICENSE"
            pickFirsts += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Apache POI for Excel export
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("com.fasterxml.woodstox:woodstox-core:6.5.0")

    // Glide for image loading
    implementation(libs.glide)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}
