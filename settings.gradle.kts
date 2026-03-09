pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // необходимо для библиотек с GitHub, например com.github.novacrypto:BIP39
    }
}

rootProject.name = "Lottttto15" // можно заменить на название твоего проекта
include(":app")
