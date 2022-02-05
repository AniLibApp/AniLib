import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.parcelize")
    id("com.apollographql.apollo3").version(LibraryVersion.apollo_version)
}

if (gradle.startParameter.taskRequests.toString().contains("Standard")) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

android {
    compileSdk = AndroidSdk.compile
    buildToolsVersion = "30.0.3"

    fun getAniListProperty(name: String, default: String = ""): String {
        val fis = FileInputStream(rootProject.file("anilist.properties"))
        val prop = Properties()
        prop.load(fis)
        return prop.getProperty(name) ?: default
    }

    fun getAniListSecretProperty(name: String, default: String = ""): String {
        return if (rootProject.file("secret.properties").exists()) {
            val fis = FileInputStream(rootProject.file("secret.properties"))
            val prop = Properties()
            prop.load(fis)
            return prop.getProperty(name) ?: "\"$default\""
        } else {
            "\"$default\""
        }
    }

    defaultConfig {
        applicationId = "com.revolgenx.anilib"
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
        versionCode = 30
        versionName = "1.1.11"
        vectorDrawables.useSupportLibrary = true

        addManifestPlaceholders(DefaultConfig.manifestPlaceholders)

        buildConfigField("String", "anilistRedirectUri", getAniListProperty("anilist_redirect_uri"))
        buildConfigField(
                "String",
                "anilistAuthEndPoint",
                getAniListProperty("anilist_auth_endpoint")
        )
        buildConfigField(
                "String",
                "anilistTokenEndPoint",
                getAniListProperty("anilist_token_endpoint")
        )

        buildConfigField("String", "anilistclientId", getAniListSecretProperty("anilist_client_id"))
        buildConfigField(
                "String",
                "anilistclientSecret",
                getAniListSecretProperty("anilist_client_secret")
        )
        buildConfigField(
                "String",
                "adUnitId",
                getAniListSecretProperty("ad_unit_id", "ca-app-pub-3940256099942544/6300978111")
        )
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            extra["enableCrashlytics"] = false
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles("proguard-android-optimize.txt", "proguard-rules.pro")
        }
        create("alpha"){
            initWith(getByName("debug"))
        }
    }

    flavorDimensions.add("default")

    productFlavors {
        create("standard") {
            dimension = "default"
        }
        create("dev") {
            resourceConfigurations.addAll(listOf("en", "xxhdpi"))
            dimension = "default"
        }
    }


    bundle {
        language {
            enableSplit = false
        }
    }

    packagingOptions {
        resources.excludes.addAll(PackagingOptions.excludes)
    }


    lint {
        disable.addAll(listOf("MissingTranslation", "ExtraTranslation"))
        abortOnError = false
        checkReleaseBuilds = false
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${LibraryVersion.kotlinVersion}")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    //apollo
    implementation("com.apollographql.apollo3:apollo-runtime:${LibraryVersion.apollo_version}")
    implementation("com.apollographql.apollo3:apollo-rx2-support:${LibraryVersion.apollo_version}")

    //rx
    implementation("io.reactivex.rxjava2:rxandroid:${LibraryVersion.rx_version}")

    //eventbus
    implementation("org.greenrobot:eventbus:${LibraryVersion.eventbus_version}")

    //preference
    implementation("androidx.preference:preference-ktx:${LibraryVersion.preference_version}")

    //koin
    implementation("io.insert-koin:koin-android:${LibraryVersion.koin_version}")
    implementation("io.insert-koin:koin-android:${LibraryVersion.koin_version}")


    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibraryVersion.coroutine_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibraryVersion.coroutine_version}")

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${LibraryVersion.lifecycle_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${LibraryVersion.lifecycle_version}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${LibraryVersion.lifecycle_version}")
    implementation("androidx.lifecycle:lifecycle-service:${LibraryVersion.lifecycle_version}")


    //dynamic theming
    implementation("com.pranavpandey.android:dynamic-support:${LibraryVersion.dynamic_version}")

    //element ver
    implementation("com.otaliastudios:elements:${LibraryVersion.elements_version}")


    //okhttp
    implementation("com.squareup.okhttp3:okhttp:${LibraryVersion.okhttp_version}")
    implementation("com.squareup.okhttp3:logging-interceptor:${LibraryVersion.okhttp_version}")

    //image fresco
    implementation("com.facebook.fresco:fresco:${LibraryVersion.fresco_version}")
    implementation("com.facebook.fresco:animated-gif:${LibraryVersion.fresco_version}")
    implementation("com.facebook.fresco:animated-webp:${LibraryVersion.fresco_version}")
    implementation("com.facebook.fresco:webpsupport:${LibraryVersion.fresco_version}")
    implementation("com.facebook.fresco:animated-base:${LibraryVersion.fresco_version}")
    implementation("com.facebook.fresco:imagepipeline-okhttp3:2.4.0")


    //timber
    implementation("com.jakewharton.timber:timber:${LibraryVersion.timber_version}")

    //app auth
    implementation("com.github.openid:AppAuth-Android:0.8.1")

    //jwt token
    implementation("com.auth0.android:jwtdecode:${LibraryVersion.jwt_token_version}")

    //mp chart
    implementation("com.github.PhilJay:MPAndroidChart:${LibraryVersion.mpandroid_version}")

    //markwon
    implementation("io.noties.markwon:core:${LibraryVersion.markwon_version}")
    implementation("io.noties.markwon:html:${LibraryVersion.markwon_version}")
    implementation("io.noties.markwon:editor:${LibraryVersion.markwon_version}")
    implementation("io.noties.markwon:ext-strikethrough:${LibraryVersion.markwon_version}")
    implementation("io.noties.markwon:linkify:${LibraryVersion.markwon_version}")
    implementation("io.noties.markwon:inline-parser:${LibraryVersion.markwon_version}")
    implementation("io.noties.markwon:simple-ext:${LibraryVersion.markwon_version}")


    //jsoup
    implementation("org.jsoup:jsoup:${LibraryVersion.jsoup_version}")

    //exoplayer
    implementation("com.google.android.exoplayer:exoplayer-core:${LibraryVersion.exo_version}")
    implementation("com.google.android.exoplayer:exoplayer-ui:${LibraryVersion.exo_version}")


    //searchview /*make own view implementation, remove later*/
    implementation("com.paulrybitskyi.persistentsearchview:persistentsearchview:${LibraryVersion.persistent_search_version}")
    implementation("com.arthurivanets.adapster:adapster:1.0.12")


    //range seekbar
    implementation("com.github.Jay-Goo:RangeSeekBar:${LibraryVersion.range_seekbar_version}")

    //toggle
    implementation("com.llollox:androidtoggleswitch:2.0.1")

    //hauler
    implementation("com.thefuntasty.hauler:core:${LibraryVersion.hauler_version}")

    //bigimageviewer
    implementation("com.github.piasy:BigImageViewer:${LibraryVersion.big_image_viewer_version}")
    implementation("com.github.piasy:FrescoImageLoader:${LibraryVersion.big_image_viewer_version}")
    implementation("com.github.piasy:FrescoImageViewFactory:${LibraryVersion.big_image_viewer_version}")
    implementation("com.github.piasy:ProgressPieIndicator:${LibraryVersion.big_image_viewer_version}")


    //preetytime
    implementation("org.ocpsoft.prettytime:prettytime:${LibraryVersion.preetytime_version}")

    //workmanager
    implementation("androidx.work:work-runtime-ktx:${LibraryVersion.work_version}")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:${LibraryVersion.firebase_bom_version}"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    //ads
    implementation("com.google.android.gms:play-services-ads:19.7.0")

    //draglistview
    implementation("com.github.woxthebox:draglistview:${LibraryVersion.draglist_version}")

    //spinkit
    implementation("com.github.ybq:Android-SpinKit:${LibraryVersion.spin_kit_version}")

    //shimmer layout
    implementation("com.facebook.shimmer:shimmer:${LibraryVersion.shimmer_version}")

    implementation("com.github.kizitonwose:CalendarView:${LibraryVersion.calendarview_version}")

    implementation("com.github.abdularis:androidbuttonprogress:${LibraryVersion.androidbuttonprogress_version}")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("com.squareup.retrofit2:retrofit:${LibraryVersion.retrofit_ver}")

    implementation("com.squareup.moshi:moshi-kotlin:${LibraryVersion.moshi_version}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${LibraryVersion.moshi_version}")


    implementation("com.squareup.retrofit2:converter-moshi:${LibraryVersion.moshi_convt_ver}")


    implementation("androidx.room:room-runtime:${LibraryVersion.room_version}")
    kapt("androidx.room:room-compiler:${LibraryVersion.room_version}")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:${LibraryVersion.room_version}")

    //ml-kit
    implementation("com.google.mlkit:translate:16.1.1")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

apollo {
    packageName.set("com.revolgenx.anilib")
}



