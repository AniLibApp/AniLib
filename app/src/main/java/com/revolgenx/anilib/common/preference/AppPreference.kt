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
private const val mlLanguageToUseKey = "ml_Language_To_Use_Key"
private const val enableMlLanguageToUseKey = "enable_ml_Language_To_Use_Key"
private const val enableAutoMlLanguageToUseKey = "enable_auto_ml_Language_To_Use_Key"

private const val isAiring12hrFormatKey = "is_airing_12_hr_format_key"
private const val mediaInfoAddTransparencyKey = "pref_media_add_transparency"


fun isCrashReportEnabled(): Boolean {
    return load(crashReportKey, true)
}

fun enableCrashReport(enable: Boolean) {
    save(crashReportKey, enable)
}

fun getApplicationLocale(): String {
    return DynamicPreferences.getInstance().load(languagePrefKey, "en")!!
}

fun getVersion(context: Context): String {
    val version = load(versionKey, "") ?: ""
    save(versionKey, DynamicPackageUtils.getVersionName(context))
    return version
}

fun getUpdateVersion(context: Context): String {
    return DynamicPreferences.getInstance()
        .load(updateVersionKey, DynamicPackageUtils.getVersionName(context))!!
}

fun setUpdateVersion(newVersion: String) {
    DynamicPreferences.getInstance().save(updateVersionKey, newVersion)
}

fun disableCardStyleInHomeScreen() = DynamicPreferences.getInstance().load(showCaseLayoutKey, false)

fun disableAds() = DynamicPreferences.getInstance().load(showAdsKey, false)
fun disableAds(disableAds: Boolean) {
    DynamicPreferences.getInstance().save(showAdsKey, disableAds)
}

fun loadBioByDefault() = DynamicPreferences.getInstance().load(loadBioByDefault, false)

fun loadLegacyMediaBrowseTheme() =
    DynamicPreferences.getInstance().load(loadLegacyMediaBrowseHeader, false)

fun inUseMlLanguageModel(context: Context, code: String? = null): String {
    return if (code != null) {
        save(mlLanguageToUseKey, code)
        code
    } else {
        load(mlLanguageToUseKey, "")!!
    }
}

fun enableMlTranslation(enable: Boolean? = null): Boolean {
    return if (enable != null) {
        save(enableMlLanguageToUseKey, enable)
        enable
    } else {
        load(enableMlLanguageToUseKey, false)
    }
}

fun enableAutoMlTranslation(enable: Boolean? = null): Boolean {
    return if (enable != null) {
        save(enableAutoMlLanguageToUseKey, enable)
        enable
    } else {
        load(enableAutoMlLanguageToUseKey, false)
    }
}

val isAiring12hrFormat get()= dynamicPreferences.load(isAiring12hrFormatKey, false)
fun isAiring12hrFormat(bool: Boolean) {
    dynamicPreferences.save(isAiring12hrFormatKey, bool)
}

val mediaInfoAddTransparencyPref get() = load(mediaInfoAddTransparencyKey, 255)