plugins {
    id("com.android.application")
    // id("com.google.gms.google-services")  // Google Services отключён
    // Если используешь Kotlin, добавь:
    // id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.lottttto15" // TODO: замени на свой package name (должен совпадать с тем, что в манифесте)
    compileSdk = 34 // или 33 / 35 – укажи актуальную версию

    defaultConfig {
        applicationId = "com.example.lottttto15" // TODO: замени на свой applicationId
        minSdk = 21 // TODO: укажи минимальную SDK
        targetSdk = 34 // TODO: укажи целевую SDK
        versionCode = 1
        versionName = "1.0"

        // Исправленный блок для указания поддерживаемых архитектур
        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a"))
            // Если нужно добавить другие (например, x86), раскомментируй:
            // abiFilters.add("x86")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // включи ProGuard/R8, если нужно
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Настройки компиляции для Java
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
    // Стандартные библиотеки AndroidX
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // 👇 ВНИМАНИЕ: зависимость BIP39 временно отключена, так как её не удаётся найти.
    // Если в коде есть импорты из этой библиотеки, их тоже нужно закомментировать.
    // implementation("com.github.novacrypto:BIP39:2019.01.27")

    // Если у тебя были Firebase-зависимости, они тоже могут мешать, но сборка без google-services.json всё равно пройдёт.
    // Например:
    // implementation("com.google.firebase:firebase-analytics:21.5.0") // можно закомментировать при необходимости

    // Здесь добавь остальные свои зависимости (если они есть)
    // testImplementation("junit:junit:4.13.2")
    // androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
