plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rtu.number.game.datastore"
    compileSdk = 36

}

dependencies {
    implementation(project(":core:common"))
    api(libs.datastore)
    implementation(libs.tink)
}
