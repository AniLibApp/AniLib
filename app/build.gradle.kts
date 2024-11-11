import com.android.build.api.dsl.ApplicationBuildType
import java.io.FileInputStream
import java.util.*


plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
    kotlin("plugin.parcelize")
    kotlin("plugin.compose")
    id("com.apollographql.apollo3") version "3.7.3"
    id("com.mikepenz.aboutlibraries.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}



android {
    namespace = "com.revolgenx.anilib"

    defaultConfig {
        applicationId = "com.revolgenx.anilib"

        versionCode = 44
        versionName = "2.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    buildTypes {
        named("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            loadConfig("secret.debug.properties")
        }
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            loadConfig("secret.properties")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

    kotlin {
        sourceSets.all {
            languageSettings {
                enableLanguageFeature("DataObjects")
            }
        }
    }


    packaging {
        resources {
            excludes.addAll(PackagingOptions.excludes)
        }
    }

    lint {
        disable.addAll(listOf("MissingTranslation", "ExtraTranslation"))
        abortOnError = false
        checkReleaseBuilds = false
    }
}

fun ApplicationBuildType.loadConfig(file: String){
    val secretProps = rootProject.loadProperties(file)
    buildConfigField(
        "String",
        "CLIENT_ID",
        secretPropslistOf(secretProps, "client_id")
    )
    buildConfigField(
        "String",
        "INTERSTITIAL_ADS_ID",
        secretPropslistOf(secretProps, "interstitial_ads_id")
    )
    buildConfigField(
        "String",
        "REWARDED_INTERSTITIAL_ADS_ID",
        secretPropslistOf(secretProps, "rewarded_interstitial_ads_id")
    )
}

dependencies {
    implementation(project(":i18n"))
    core()
    compose()
    voyager()
    koin()
    apollo()
    paging()
    timber()
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Versions.desugar}")
    coil()
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    datastore()
    markwon()
    eventbus()
    browser()
    jwtDecode()
    prettytime()
    chart()
    sheetsM3()
    accompanist()
    aboutLibraries()
    reorderable()
    glance()
    firebase()
    admob()

    implementation("com.materialkolor:material-kolor:1.4.4")
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.3.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:color:1.3.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}


apollo {
    service("anilist") {
        packageName.set("com.revolgenx.anilib")
    }
}

fun Project.loadProperties(file: String) = if (file(file).exists()) {
    val fis = FileInputStream(file(file))
    val prop = Properties()
    prop.load(fis)
    prop
} else null


fun secretPropslistOf(secretProps: Properties?, name: String, default: String = ""): String =
    secretProps?.getProperty(name) ?: "\"$default\""
