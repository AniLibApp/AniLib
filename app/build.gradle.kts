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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
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

    implementation("androidx.core:core-ktx:1.12.0")

    implementation("androidx.activity:activity-compose:1.8.0-beta01")

    //compose
    implementation("androidx.compose.ui:ui-tooling-preview:${Libs.composeUi}")

    //material
    implementation("androidx.compose.material3:material3:${Libs.material}")

    //voyager
    implementation("cafe.adriel.voyager:voyager-navigator:${Libs.voyager}")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:${Libs.voyager}")
    implementation("cafe.adriel.voyager:voyager-androidx:${Libs.voyager}")

    //koin
    implementation("io.insert-koin:koin-android:${Libs.koin}")
    implementation("io.insert-koin:koin-androidx-compose:${Libs.koin}")
    implementation("io.insert-koin:koin-androidx-workmanager:${Libs.koin}")


    //apollo
    implementation("com.apollographql.apollo3:apollo-runtime:3.7.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //paging
    implementation("androidx.paging:paging-runtime:${Libs.paging}")
    implementation("androidx.paging:paging-compose:${Libs.paging}")

    //timber
    implementation("com.jakewharton.timber:timber:${Libs.timber}")

    //desugar
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Libs.desugar}")


    //coil
    implementation("io.coil-kt:coil-compose:${Libs.coil}")
    implementation("io.coil-kt:coil-gif:${Libs.coil}")
    implementation("io.coil-kt:coil-svg:${Libs.coil}")

    //kotlin-serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //dataStore
    implementation("androidx.datastore:datastore:${Libs.dataStore}")
    implementation("androidx.datastore:datastore-preferences:${Libs.dataStore}")

    //splashscreen
    implementation("androidx.core:core-splashscreen:${Libs.splashscreen}")

    //markwon
    implementation("io.noties.markwon:core:${Libs.markwon}")
    implementation("io.noties.markwon:html:${Libs.markwon}")
    implementation("io.noties.markwon:editor:${Libs.markwon}")
    implementation("io.noties.markwon:ext-strikethrough:${Libs.markwon}")
    implementation("io.noties.markwon:linkify:${Libs.markwon}")
    implementation("io.noties.markwon:inline-parser:${Libs.markwon}")
    implementation("io.noties.markwon:simple-ext:${Libs.markwon}")

    //eventbus
    implementation("org.greenrobot:eventbus:3.3.1")


    //browser
    implementation("androidx.browser:browser:${Libs.customTabs}")


    //jwtdecode
    implementation("com.auth0.android:jwtdecode:${Libs.jwtdecode}")

    //pretty time
    implementation("org.ocpsoft.prettytime:prettytime:${Libs.prettytime}")

    //chart
    implementation("com.patrykandpatrick.vico:core:${Libs.vico}")
    implementation("com.patrykandpatrick.vico:compose:${Libs.vico}")
    implementation("com.patrykandpatrick.vico:compose-m3:${Libs.vico}")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Libs.composeUi}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Libs.composeUi}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Libs.composeUi}")
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
