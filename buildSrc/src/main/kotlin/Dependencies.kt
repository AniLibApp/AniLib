import org.gradle.api.artifacts.dsl.DependencyHandler

object AndroidConfig {
    const val compileSdk = 34
    const val minSdk = 21
    const val targetSdk = 33
}

object PackagingOptions {
    val excludes =
        listOf("META-INF/LICENSE", "META-INF/LICENSE.txt", "META-INF/NOTICE", "META-INF/NOTICE.txt", "/META-INF/{AL2.0,LGPL2.1}")
}

object Versions{
    const val composeUi = "1.6.8"
    const val composeFoundation = "1.6.8"
    const val composeMaterial = "1.3.0-rc01"
    const val composeActivity = "1.9.1"
    const val voyager = "1.1.0-beta02"
    const val timber = "5.0.1"
    const val koin = "3.5.6"
    const val coil = "2.7.0"
    const val desugar = "1.1.5"
    const val paging = "3.3.1"
    const val dataStore = "1.1.1"
    const val markwon = "4.6.2"
    const val customTabs = "1.8.0"
    const val jwtdecode = "2.0.2"
    const val prettytime = "5.0.4.Final"
    const val vico = "1.15.0"
    const val splashscreen = "1.1.0-alpha02"
    const val coreKtx = "1.13.1"
    const val sheetsM3 = "0.5.4"
    const val accompanistPermissions = "0.34.0" //compose ui 1.6
    const val aboutLibraries = "11.2.2"
    const val reorderable = "2.3.0"
    const val glance = "1.1.0"
}

object Dependencies {
    const val composeMaterial = "androidx.compose.material3:material3:${Versions.composeMaterial}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.composeUi}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.composeActivity}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.composeFoundation}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.composeUi}"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.composeUi}"

    const val voyager = "cafe.adriel.voyager:voyager-navigator:${Versions.voyager}"
    const val voyagerTab = "cafe.adriel.voyager:voyager-tab-navigator:${Versions.voyager}"


    const val koinBom = "io.insert-koin:koin-bom:${Versions.koin}"
    const val koin = "io.insert-koin:koin-android"
    const val koinCompose = "io.insert-koin:koin-androidx-compose"
    const val koinWorkManager = "io.insert-koin:koin-androidx-workmanager"

    const val apollo = "com.apollographql.apollo3:apollo-runtime:3.7.3"
    const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.10.0"

    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    const val pagingCompose = "androidx.paging:paging-compose:${Versions.paging}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val coilCompose =     "io.coil-kt:coil-compose:${Versions.coil}"
    const val coilGif ="io.coil-kt:coil-gif:${Versions.coil}"
    const val coilSvg ="io.coil-kt:coil-svg:${Versions.coil}"


    const val datastore ="androidx.datastore:datastore:${Versions.dataStore}"
    const val datastorePreference ="androidx.datastore:datastore-preferences:${Versions.dataStore}"

    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val coreSplashScreen = "androidx.core:core-splashscreen:${Versions.splashscreen}"

    const val markwon ="io.noties.markwon:core:${Versions.markwon}"
    const val markwonHtml ="io.noties.markwon:html:${Versions.markwon}"
    const val markwonEditor ="io.noties.markwon:editor:${Versions.markwon}"
    const val markwonExtStrike ="io.noties.markwon:ext-strikethrough:${Versions.markwon}"
    const val markwonLinkify ="io.noties.markwon:linkify:${Versions.markwon}"
    const val markwonInlineParser ="io.noties.markwon:inline-parser:${Versions.markwon}"
    const val markwonSimpleExt ="io.noties.markwon:simple-ext:${Versions.markwon}"

    const val eventbus ="org.greenrobot:eventbus:3.3.1"

    const val browser ="androidx.browser:browser:${Versions.customTabs}"

    const val jwtdecode ="com.auth0.android:jwtdecode:${Versions.jwtdecode}"

    //pretty time
    const val prettytime ="org.ocpsoft.prettytime:prettytime:${Versions.prettytime}"

    //chart
    const val chart ="com.patrykandpatrick.vico:core:${Versions.vico}"
    const val chartCompose ="com.patrykandpatrick.vico:compose:${Versions.vico}"
    const val chartComposeM3 ="com.patrykandpatrick.vico:compose-m3:${Versions.vico}"

    const val jwtDecode =  "com.auth0.android:jwtdecode:${Versions.jwtdecode}"

    const val sheetsM3 = "io.github.dokar3:sheets-m3:${Versions.sheetsM3}"

    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:${Versions.accompanistPermissions}"

    const val aboutLibraries = "com.mikepenz:aboutlibraries-compose-m3:${Versions.aboutLibraries}"

    const val reorderable = "sh.calvin.reorderable:reorderable:${Versions.reorderable}"

    //widget
    const val glance = "androidx.glance:glance-appwidget:${Versions.glance}"
    const val glanceMaterial3 = "androidx.glance:glance-material3:${Versions.glance}"
}


fun DependencyHandler.compose() {
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeActivity)
    //remove it later
    implementation(Dependencies.composeFoundation)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeUiTooling)
    debugImplementation(Dependencies.composeUiToolingPreview)
}
fun DependencyHandler.koin() {
    dependencyPlatform(Dependencies.koinBom)
    implementation(Dependencies.koin)
    implementation(Dependencies.koinCompose)
    implementation(Dependencies.koinWorkManager)
}

fun DependencyHandler.apollo() {
    implementation(Dependencies.apollo)
    implementation(Dependencies.loggingInterceptor)
}

fun DependencyHandler.chart() {
    implementation(Dependencies.chart)
    implementation(Dependencies.chartCompose)
    implementation(Dependencies.chartComposeM3)
}

fun DependencyHandler.eventbus() {
    implementation(Dependencies.eventbus)
}

fun DependencyHandler.prettytime() {
    implementation(Dependencies.prettytime)
}

fun DependencyHandler.browser() {
    implementation(Dependencies.browser)
}

fun DependencyHandler.markwon() {
    implementation(Dependencies.markwon)
    implementation(Dependencies.markwonHtml)
    implementation(Dependencies.markwonEditor)
    implementation(Dependencies.markwonExtStrike)
    implementation(Dependencies.markwonLinkify)
    implementation(Dependencies.markwonInlineParser)
    implementation(Dependencies.markwonSimpleExt)
}

fun DependencyHandler.voyager() {
    implementation(Dependencies.voyager)
    implementation(Dependencies.voyagerTab)
}
fun DependencyHandler.paging() {
    implementation(Dependencies.paging)
    implementation(Dependencies.pagingCompose)
}

fun DependencyHandler.timber() {
    implementation(Dependencies.timber)
}

fun DependencyHandler.coil() {
    implementation(Dependencies.coilCompose)
    implementation(Dependencies.coilGif)
    implementation(Dependencies.coilSvg)
}

fun DependencyHandler.datastore() {
    implementation(Dependencies.datastore)
    implementation(Dependencies.datastorePreference)
}

fun DependencyHandler.core() {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.coreSplashScreen)
}

fun DependencyHandler.jwtDecode() {
    implementation(Dependencies.jwtDecode)
}

fun DependencyHandler.sheetsM3() {
    implementation(Dependencies.sheetsM3)
}

fun DependencyHandler.accompanist() {
    implementation(Dependencies.accompanistPermissions)
}

fun DependencyHandler.aboutLibraries() {
    implementation(Dependencies.aboutLibraries)
}

fun DependencyHandler.reorderable() {
    implementation(Dependencies.reorderable)
}

fun DependencyHandler.glance(){
    implementation(Dependencies.glance)
    implementation(Dependencies.glanceMaterial3)
}