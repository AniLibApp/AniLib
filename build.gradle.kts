// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins{
    kotlin("android") version "1.8.0" apply false
}


buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.5.2")
        classpath("com.android.tools.build:gradle:8.0.2")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
        mavenCentral()
    }
}

tasks{
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}
