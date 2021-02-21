package com.revolgenx.anilib.common.preference

import android.content.Context
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils

private const val crashReportKey = "crash_report_key"
private const val versionKey = "versionKey"
private const val updateVersionKey = "update_version_key"

const val languagePrefKey = "application_language_key"
const val showCaseLayoutKey = "pref_display_card_in_home"

private const val showAdsKey = "show_ads_key"
private const val loadBioByDefault = "load_bio_by_default"

private const val loadLegacyMediaBrowseHeader = "load_legacy_media_browse_header"


fun isCrashReportEnabled(context: Context): Boolean {
    return context.getBoolean(crashReportKey, true)
}

fun enableCrashReport(context: Context, enable: Boolean) {
    context.putBoolean(crashReportKey, enable)
}

fun getApplicationLocale(): String {
    return DynamicPreferences.getInstance().load(languagePrefKey, "en")!!
}

fun getVersion(context: Context): String {
    val version = context.getString(versionKey) ?: ""
    context.putString(versionKey, DynamicPackageUtils.getAppVersion(context))
    return version
}

fun getUpdateVersion(context: Context): String {
    return DynamicPreferences.getInstance()
        .load(updateVersionKey, DynamicPackageUtils.getAppVersion(context))!!
}

fun setUpdateVersion(newVersion: String) {
    DynamicPreferences.getInstance().save(updateVersionKey, newVersion)
}

fun disableCardStyleInHomeScreen() = DynamicPreferences.getInstance().load(showCaseLayoutKey, false)

fun disableAds() = DynamicPreferences.getInstance().load(showAdsKey, false)
fun disableAds(disableAds: Boolean){
    DynamicPreferences.getInstance().save(showAdsKey, disableAds)
}

fun loadBioByDefault() = DynamicPreferences.getInstance().load(loadBioByDefault, false)

fun loadLegacyMediaBrowseTheme() = DynamicPreferences.getInstance().load(loadLegacyMediaBrowseHeader, false)