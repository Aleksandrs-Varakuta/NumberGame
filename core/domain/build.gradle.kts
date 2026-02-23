plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rtu.number.game.domain"
    compileSdk = 36
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
}
