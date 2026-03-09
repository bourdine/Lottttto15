plugins {
    id("com.android.application")
    // Если используешь Kotlin, добавь:
    // id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.lottttto15" // TODO: замени на свой package name (тот же, что в манифесте)
    compileSdk = 34 // или 33, в зависимости от твоих настроек

    defaultConfig {
        applicationId = "com.example.lottttto15" // TODO: замени на свой applicationId
        minSdk = 21 // TODO: укажи минимальную SDK
        targetSdk = 34 // TODO: укажи целевую SDK
        versionCode = 1
        versionName = "1.0"

        // Исправленный блок для указания поддерживаемых архитектур
        ndk {
            // Добавляем ABI-фильтры правильным способом
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a"))
            // Если нужно добавить ещё, например "x86", укажи:
            // abiFilters.add("x86")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // включи ProGuard/R8 если нужно
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Настройки компиляции (если используешь Java)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Если используешь Kotlin, раскомментируй:
    /*
    kotlinOptions {
        jvmTarget = "11"
    }
    */

    // Если используешь viewBinding или dataBinding:
    /*
    buildFeatures {
        viewBinding = true
    }
    */
}

dependencies {
    // Здесь оставь свои зависимости (они у тебя уже есть)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Добавь все свои зависимости, которые были в исходном файле
    // Например:
    // implementation("com.github.bumptech.glide:glide:4.16.0")
    // testImplementation("junit:junit:4.13.2")
    // androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
