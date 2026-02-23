plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rtu.number.game.common"
    compileSdk = 36
}

dependencies {

    implementation(libs.coil.svg)
    implementation(libs.coil)
}
