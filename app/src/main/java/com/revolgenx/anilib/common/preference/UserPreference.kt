package com.revolgenx.anilib.common.preference

import android.content.Context
import androidx.work.WorkManager
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.theme.DynamicPalette
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.airing.data.field.AiringMediaField
import com.revolgenx.anilib.app.setting.data.model.MediaListOptionModel
import com.revolgenx.anilib.app.setting.data.model.UserOptionsModel
import com.revolgenx.anilib.notification.service.NotificationWorker
import com.revolgenx.anilib.type.ScoreFormat
import com.revolgenx.anilib.type.UserTitleLanguage
import com.revolgenx.anilib.user.data.model.UserModel
import com.revolgenx.anilib.util.shortcutAction

private const val userModelKey = "save_user_model_key"
private const val loggedInKey = "logged_in_key"
private const val tokenKey = "token_key"
private const val titleKey = "title_key"
private const val imageQualityKey = "image_quality_key"
private const val userIdKey = "user_id_key"
private const val canShowAdultKey = "can_show_adult_key"
private const val lastNotificationKey = "last_notification_key"
private const val sharedPrefSyncKey = "sharedPrefSyncKey"
private const val updateProfileColorKey = "updateProfileColorKey"
private const val media_info_or_list_editor_key = "media_info_or_list_editor_key"


private const val recent_anime_list_status_key = "recent_anime_list_status_key"
private const val recent_manga_list_status_key = "recent_manga_list_status_key"

object UserPreference {
    val userId get() = DynamicPreferences.getInstance().load(userIdKey, -1)

}

fun Context.loggedIn() = load(loggedInKey, false)
fun Context.loggedIn(logIn: Boolean) = save(loggedInKey, logIn)

fun Context.token() = load(tokenKey, "")
fun Context.token(token: String) = save(tokenKey, token)

fun Context.userId(userId: Int) = save(userIdKey, userId)

fun Context.titlePref() = load(titleKey, "0")
fun titlePref(pref: String) = save(titleKey, pref)

fun Context.imageQuality() = load(imageQualityKey, "0")

fun Context.userName() = getUserPrefModel().name
fun Context.userScoreFormat() =
    getUserPrefModel().mediaListOptions!!.scoreFormat!!

private var userModel: UserModel? = null

fun Context.saveBasicUserDetail(userPrefModel: UserModel) {
    userModel = userPrefModel
    save(userModelKey, Gson().toJson(userPrefModel))

    if (shouldUpdateProfileColor()) {
        shouldUpdateProfileColor(false)
//        userPrefModel.mediaOptions?.profileColor?.let {
//            saveUserAccentColor(it)
//        }
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
    ThemeController.accentColor = accentColorToSave
}

fun getUserPrefModel(): UserModel {
    if (userModel == null) {
        userModel =
            Gson().fromJson(load(userModelKey, ""), UserModel::class.java)
                ?: UserModel().also { model ->
                    model.name = "AniLib"
                }
    }

    if (userModel!!.options == null) {
        userModel!!.options =
            UserOptionsModel(UserTitleLanguage.ROMAJI.ordinal, false, false, null)
    }

    if (userModel!!.mediaListOptions == null) {
        userModel!!.mediaListOptions = MediaListOptionModel().also { mediaListOptionModel ->
            mediaListOptionModel.scoreFormat = ScoreFormat.POINT_100.ordinal
        }
    }

    return userModel!!
}


private fun removeBasicUserDetail(context: Context) {
    userModel = UserModel().also {
        it.name = context.getString(R.string.app_name)
        it.options = UserOptionsModel(UserTitleLanguage.ROMAJI.ordinal, false, false, null)
    }
    save(userModelKey, Gson().toJson(userModel))
}

fun Context.logOut() {
    loggedIn(false)
    token("")
    userId(-1)
    titlePref("0")
    removeNotification(this)
    removeBasicUserDetail(this)
    removeAiringField()
    shortcutAction(this) {
        it.removeAllDynamicShortcuts()
    }
    removeNotificationWorker(this)
}

private fun removeNotificationWorker(context: Context) {
    WorkManager.getInstance(context)
        .cancelUniqueWork(NotificationWorker.NOTIFICATION_WORKER_TAG)
}

private fun removeNotification(context: Context) {
    setNewNotification()
}

private fun removeAiringField() {
    storeDiscoverAiringField(AiringMediaField())
    storeAiringField(AiringMediaField())
}

fun Context.logIn(accessToken: String) {
    loggedIn(true)
    token(accessToken)
    val userId = JWT(accessToken).subject?.trim()?.toInt() ?: -1
    userId(userId)
    titlePref("3")
    shortcutAction(this) {
        it.removeAllDynamicShortcuts()
    }
    shouldUpdateProfileColor(true)
}

private fun shouldUpdateProfileColor(update: Boolean) {
    save(updateProfileColorKey, update)
}

private fun shouldUpdateProfileColor() =
    load(updateProfileColorKey, false)


fun getLastNotification(): Int {
    return load(lastNotificationKey, -1)
}

fun setNewNotification(notifId: Int = -1) {
    save(lastNotificationKey, notifId)
}


fun canShowAdult(): Boolean {
    return if (userEnabledAdultContent()) {
        load(canShowAdultKey, false)
    } else {
        false
    }
}

fun userEnabledAdultContent(): Boolean {
    return getStoredMediaOptions().displayAdultContent
}

fun isSharedPreferenceSynced(synced: Boolean? = null) =
    if (synced == null) {
        load(sharedPrefSyncKey, false)
    } else {
        save(sharedPrefSyncKey, synced)
        synced
    }


fun storeMediaOptions(context: Context, model: UserOptionsModel?) {
    if (model == null) return

    val userPrefModel = getUserPrefModel()
    userPrefModel.options = model
    context.saveBasicUserDetail(userPrefModel)
}

fun getStoredMediaOptions(): UserOptionsModel {
    return getUserPrefModel().options!!
}

fun animeListStatusHistory() = dynamicPreferences.load(recent_anime_list_status_key, "All")
fun animeListStatusHistory(value: String) =
    dynamicPreferences.save(recent_anime_list_status_key, value.trim())

fun mangaListStatusHistory() = dynamicPreferences.load(recent_manga_list_status_key, "All")
fun mangaListStatusHistory(value: String) =
    dynamicPreferences.save(recent_manga_list_status_key, value.trim())

fun openMediaInfoOrListEditor() = dynamicPreferences.load(media_info_or_list_editor_key, false)