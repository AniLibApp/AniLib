import java.io.FileInputStream
import java.util.*


plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
    id("com.apollographql.apollo3") version "3.7.3"
    kotlin("plugin.parcelize")
}



android {
    namespace = "com.revolgenx.anilib"

    defaultConfig {
        applicationId = "com.revolgenx.anilib"

        versionCode = 41
        versionName = "2.0.0"

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

            val secretProps = rootProject.loadProperties("secret.debug.properties")
            buildConfigField(
                "String",
                "CLIENT_ID",
                secretPropslistOf(secretProps, "client_id")
            )
            buildConfigField(
                "String",
                "REDIRECT_URI",
                secretPropslistOf(secretProps, "redirect_uri")
            )
        }
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )

            val secretProps = rootProject.loadProperties("secret.properties")
            buildConfigField(
                "String",
                "CLIENT_ID",
                secretPropslistOf(secretProps, "client_id")
            )
            buildConfigField(
                "String",
                "REDIRECT_URI",
                secretPropslistOf(secretProps, "redirect_uri")
            )
        }
        create("alpha") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".alpha"
        }
    }

    flavorDimensions.add("default")
    productFlavors {
        create("production") {
            dimension = "default"
        }
        create("develop") {
            resourceConfigurations += listOf("en", "xxhdpi")
            dimension = "default"
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
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

    implementation("com.materialkolor:material-kolor:1.4.4")
    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.3.0")
    implementation("com.maxkeppeler.sheets-compose-dialogs:color:1.3.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.composeUi}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.composeUi}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Versions.composeUi}")
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
