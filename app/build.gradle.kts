import com.android.build.api.dsl.ApplicationBuildType
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.apollo)
    alias(libs.plugins.aboutlibraries)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.revolgenx.anilib"

    defaultConfig {
        applicationId = "com.revolgenx.anilib"
        versionCode = 52
        versionName = "2.0.10"

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

        create("standard") {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            loadConfig("secret.debug.properties")
            matchingFallbacks.add("release")
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
            // Replaced PackagingOptions.excludes with literal list
            excludes.addAll(
                listOf(
                    "META-INF/LICENSE",
                    "META-INF/LICENSE.txt",
                    "META-INF/NOTICE",
                    "META-INF/NOTICE.txt",
                    "/META-INF/{AL2.0,LGPL2.1}"
                )
            )
        }
    }

    lint {
        disable.addAll(listOf("MissingTranslation", "ExtraTranslation"))
        abortOnError = false
        checkReleaseBuilds = false
    }
}

fun ApplicationBuildType.loadConfig(file: String) {
    val secretProps = rootProject.loadProperties(file)
    buildConfigField(
        "String",
        "CLIENT_ID",
        secretPropsFrom(secretProps, "client_id")
    )
    buildConfigField(
        "String",
        "INTERSTITIAL_ADS_ID",
        secretPropsFrom(secretProps, "interstitial_ads_id")
    )
    buildConfigField(
        "String",
        "REWARDED_INTERSTITIAL_ADS_ID",
        secretPropsFrom(secretProps, "rewarded_interstitial_ads_id")
    )
}

dependencies {
    implementation(project(":i18n"))

    // Replaced all extension functions with Catalog Bundles or Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.voyager)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.apollo)
    implementation(libs.bundles.paging)
    implementation(libs.timber)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.bundles.coil)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.datastore)
    implementation(libs.bundles.markwon)
    implementation(libs.eventbus)
    implementation(libs.androidx.browser)
    implementation(libs.jwtdecode)
    implementation(libs.prettytime)
    implementation(libs.bundles.chart)
    implementation(libs.bundles.aboutlibraries)
    implementation(libs.sheets.m3)
    implementation(libs.accompanist.permissions)
    implementation(libs.aboutlibraries.compose)
    implementation(libs.reorderable)
    implementation(libs.bundles.glance)

    // Firebase is a platform/BOM + bundles
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(libs.play.services.ads)
    implementation(libs.billing.ktx)

    implementation(libs.material.kolor)

    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
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


fun secretPropsFrom(secretProps: Properties?, name: String, default: String = ""): String =
    secretProps?.getProperty(name) ?: "\"$default\""
