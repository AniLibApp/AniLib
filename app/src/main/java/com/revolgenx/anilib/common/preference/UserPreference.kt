package com.revolgenx.anilib.common.preference

import android.content.Context
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.theme.DynamicPalette
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.Constants
import com.revolgenx.anilib.data.field.home.AiringMediaField
import com.revolgenx.anilib.data.model.user.UserPrefModel
import com.revolgenx.anilib.data.model.setting.MediaListOptionModel
import com.revolgenx.anilib.data.model.setting.MediaOptionModel
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.type.UserTitleLanguage
import com.revolgenx.anilib.util.shortcutAction

private const val userModelKey = "user_model_key"
private const val loggedInKey = "logged_in_key"
private const val tokenKey = "token_key"
private const val titleKey = "title_key"
private const val imageQualityKey = "image_quality_key"
private const val userIdKey = "user_id_key"
private const val canShowAdultKey = "can_show_adult_key"
private const val lastNotificationKey = "last_notification_key"
private const val sharedPrefSyncKey = "sharedPrefSyncKey"
private const val updateProfileColorKey = "updateProfileColorKey"
private const val recentAnimeListStatusKey = "recentAnimeListStatusKey"
private const val recentMangaListStatusKey = "recentMangaListStatusKey"
private const val listEditOrBrowseKey = "list_edit_or_browse_key"


fun Context.loggedIn() = getBoolean(loggedInKey, false)
fun Context.loggedIn(logIn: Boolean) = putBoolean(loggedInKey, logIn)

fun Context.token() = getString(tokenKey, "")
fun Context.token(token: String) = putString(tokenKey, token)

fun Context.userId() = getInt(userIdKey, -1)
fun Context.userId(userId: Int) = putInt(userIdKey, userId)

fun Context.titlePref() = getString(titleKey, "0")
fun titlePref(context: Context, pref: String) = context.putString(titleKey, pref)

fun Context.imageQuality() = getString(imageQualityKey, "0")

fun Context.userName() = getUserPrefModel(this).name
fun Context.userScoreFormat() =
    getUserPrefModel(this).mediaListOption!!.scoreFormat!!

private var userPrefModelPref: UserPrefModel? = null

fun Context.saveBasicUserDetail(userPrefModel: UserPrefModel) {
    userPrefModelPref = userPrefModel
    this.putString(userModelKey, Gson().toJson(userPrefModel))

    if (shouldUpdateProfileColor(this)) {
        shouldUpdateProfileColor(this, false)
        userPrefModel.mediaOptions?.profileColor?.let {
            saveUserAccentColor(it)
        }
    }
}

fun saveUserAccentColor(it: String) {
    val colors = DynamicPalette.MATERIAL_COLORS
    val accentColorToSave = when (it) {
        "blue" -> {
            colors[5]
        }
        "purple" -> {
            colors[2]
        }
        "pink" -> {
            colors[1]
        }
        "orange" -> {
            colors[14]
        }
        "red" -> {
            colors[0]
        }
        "green" -> {
            colors[9]
        }
        "gray" -> {
            colors[17]
        }
        else -> {
            colors[5]
        }
    }
    DynamicPreferences.getInstance()
        .save(Constants.PREF_SETTINGS_APP_THEME_COLOR_ACCENT, accentColorToSave)

}

fun getUserPrefModel(context: Context): UserPrefModel {
    if (userPrefModelPref == null) {
        userPrefModelPref =
            Gson().fromJson(context.getString(userModelKey, ""), UserPrefModel::class.java)
                ?: UserPrefModel().also { model ->
                    model.name = context.getString(R.string.app_name)
                }
    }

    if (userPrefModelPref!!.mediaOptions == null) {
        userPrefModelPref!!.mediaOptions =
            MediaOptionModel(UserTitleLanguage.ROMAJI.ordinal, false, false, null)
    }

    if (userPrefModelPref!!.mediaListOption == null) {
        userPrefModelPref!!.mediaListOption = MediaListOptionModel().also { mediaListOptionModel ->
            mediaListOptionModel.scoreFormat = ScoreFormat.POINT_100.ordinal
        }
    }

    return userPrefModelPref!!
}


fun removeBasicUserDetail(context: Context) {
    userPrefModelPref = UserPrefModel().also {
        it.name = context.getString(R.string.app_name)
        it.mediaListOption = MediaListOptionModel().also { option ->
            option.scoreFormat = ScoreFormat.`$UNKNOWN`.ordinal
        }
        it.mediaOptions = MediaOptionModel(UserTitleLanguage.ROMAJI.ordinal, false, false, null)
    }
    context.putString(userModelKey, Gson().toJson(userPrefModelPref))
}

fun Context.logOut() {
    loggedIn(false)
    token("")
    userId(-1)
    titlePref(this, "0")
    removeNotification(this)
    removeBasicUserDetail(this)
    removeAiringField(this)
    shortcutAction(this) {
        it.removeAllDynamicShortcuts()
    }
}

fun removeNotification(context: Context) {
    setNewNotification(context)
}

fun removeAiringField(context: Context) {
    storeDiscoverAiringField(context, AiringMediaField())
    storeAiringField(context, AiringMediaField())
}

fun Context.logIn(accessToken: String) {
    loggedIn(true)
    token(accessToken)
    val userId = JWT(accessToken).subject?.trim()?.toInt() ?: -1
    userId(userId)
    titlePref(this, "3")
    shortcutAction(this) {
        it.removeAllDynamicShortcuts()
    }
    shouldUpdateProfileColor(this, true)
}

private fun shouldUpdateProfileColor(context: Context, update: Boolean) {
    context.putBoolean(updateProfileColorKey, update)
}

private fun shouldUpdateProfileColor(context: Context) =
    context.getBoolean(updateProfileColorKey, false)


fun getLastNotification(context: Context): Int {
    return context.getInt(lastNotificationKey, -1)
}

fun setNewNotification(context: Context, notifId: Int = -1) {
    context.putInt(lastNotificationKey, notifId)
}


fun canShowAdult(context: Context): Boolean {
    return if (userEnabledAdultContent(context)) {
        context.getBoolean(canShowAdultKey, false)
    } else {
        false
    }
}

fun userEnabledAdultContent(context: Context): Boolean {
    return getStoredMediaOptions(context).displayAdultContent
}

fun isSharedPreferenceSynced(context: Context, synced: Boolean? = null) =
    if (synced == null) {
        context.getBoolean(sharedPrefSyncKey, false)
    } else {
        context.putBoolean(sharedPrefSyncKey, synced)
        synced
    }


fun storeMediaOption(context: Context, model: MediaOptionModel?) {
    if (model == null) return

    val userPrefModel = getUserPrefModel(context)
    userPrefModel.mediaOptions = model
    context.saveBasicUserDetail(userPrefModel)
}

fun getStoredMediaOptions(context: Context): MediaOptionModel {
    return getUserPrefModel(context).mediaOptions!!
}

fun storeMediaListOptions(context: Context, model: MediaListOptionModel?) {
    if (model == null) return

    val userPrefModel = getUserPrefModel(context)
    userPrefModel.mediaListOption = model
    context.saveBasicUserDetail(userPrefModel)
}

fun getStoredMediaListOptions(context: Context): MediaListOptionModel {
    return getUserPrefModel(context).mediaListOption!!
}


fun recentAnimeListStatus(context: Context, value: Int) {
    context.putInt(recentAnimeListStatusKey, value)
}

fun recentMangaListStatus(context: Context, value: Int) {
    context.putInt(recentMangaListStatusKey, value)
}

fun recentAnimeListStatus(context: Context) =
    context.getInt(recentAnimeListStatusKey, 0)


fun recentMangaListStatus(context: Context) = context.getInt(recentMangaListStatusKey, 0)

fun listEditOrBrowse(context: Context) = context.getBoolean(listEditOrBrowseKey, true)