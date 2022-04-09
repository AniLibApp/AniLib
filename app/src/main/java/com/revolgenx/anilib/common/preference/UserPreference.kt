package com.revolgenx.anilib.common.preference

import android.content.Context
import androidx.work.WorkManager
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences
import com.pranavpandey.android.dynamic.theme.DynamicPalette
import com.revolgenx.anilib.app.theme.ThemeController
import com.revolgenx.anilib.airing.data.field.AiringMediaField
import com.revolgenx.anilib.app.App
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
private const val first_day_of_week_key = "first_day_of_week_key"

object UserPreference {
    val userId get() = DynamicPreferences.getInstance().load(userIdKey, -1)
}

fun loggedIn() = load(loggedInKey, false)
fun loggedIn(logIn: Boolean) = save(loggedInKey, logIn)

fun token() = load(tokenKey, "")
fun token(token: String) = save(tokenKey, token)

fun userId(userId: Int) = save(userIdKey, userId)

fun titlePref() = load(titleKey, "0")
fun titlePref(pref: String) = save(titleKey, pref)

fun imageQuality() = load(imageQualityKey, "0")

val firstDayOfWeekPref get() = load(first_day_of_week_key, "0")

fun userName() = getUserPrefModel().name
fun userScoreFormat() =
    getUserPrefModel().mediaListOptions!!.scoreFormat!!

private var userModel: UserModel? = null

fun saveBasicUserDetail(userPrefModel: UserModel) {
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


private fun removeBasicUserDetail() {
    userModel = UserModel().also {
        it.name = "AniLib"
        it.options = UserOptionsModel(UserTitleLanguage.ROMAJI.ordinal, false, false, null)
    }
    save(userModelKey, Gson().toJson(userModel))
}

fun logOut() {
    loggedIn(false)
    token("")
    userId(-1)
    titlePref("0")
    removeNotification()
    removeBasicUserDetail()
    removeAiringField()
    shortcutAction {
        it.removeAllDynamicShortcuts()
    }
    removeNotificationWorker()
}

private fun removeNotificationWorker() {
    App.applicationContext?.let {
        WorkManager.getInstance(it)
            .cancelUniqueWork(NotificationWorker.NOTIFICATION_WORKER_TAG)
    }
}

private fun removeNotification() {
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
    shortcutAction {
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


fun storeMediaOptions(model: UserOptionsModel?) {
    if (model == null) return

    val userPrefModel = getUserPrefModel()
    userPrefModel.options = model
    saveBasicUserDetail(userPrefModel)
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