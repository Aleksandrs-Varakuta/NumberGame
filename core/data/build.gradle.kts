plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rtu.number.game.data"
    compileSdk = 36
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(libs.androidx.appcompat)
}
