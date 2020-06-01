package com.revolgenx.anilib.preference

import android.content.Context
import com.auth0.android.jwt.JWT
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.model.BasicUserModel
import com.revolgenx.anilib.type.ScoreFormat

private const val loggedInKey = "logged_in_key"
private const val tokenKey = "token_key"
private const val titleKey = "title_key"
private const val imageQualityKey = "image_quality_key"
private const val userIdKey = "user_id_key"
private const val userNameKey = "user_name_key"
private const val userAvatarKey = "user_avatar_key"
private const val userBannerImageKey = "user_banner_image_key"
private const val scoreFormatKey = "score_format_key"

private const val lastNotificationKey = "last_notification_key"
private const val versionKey = "versionKey"

fun Context.loggedIn() = getBoolean(loggedInKey, false)
fun Context.loggedIn(logIn: Boolean) = putBoolean(loggedInKey, logIn)

fun Context.token() = getString(tokenKey, "")
fun Context.token(token: String) = putString(tokenKey, token)

fun Context.userId() = getInt(userIdKey, -1)
fun Context.userId(userId: Int) = putInt(userIdKey, userId)

fun Context.titlePref() = getInt(titleKey, 0)
fun Context.titlePref(title: Int) = putInt(titleKey, title)

fun Context.imageQuality() = getInt(imageQualityKey, 0)
fun Context.imageQuality(quality: Int) = putInt(imageQualityKey, quality)

fun Context.userScoreFormat() = getInt(scoreFormatKey, ScoreFormat.POINT_100.ordinal)
fun Context.userScoreFormat(scoreFormat: Int?) =
    putInt(scoreFormatKey, scoreFormat ?: ScoreFormat.POINT_100.ordinal)

fun Context.userName() = getString(userNameKey, getString(R.string.app_name))
fun Context.userName(name: String?) = putString(userNameKey, name)

fun Context.userAvatar() = getString(userAvatarKey, "")
fun Context.userAvatar(avatar: String?) = putString(userAvatarKey, avatar)

fun Context.userBannerImage() = getString(userBannerImageKey, "")
fun Context.userBannerImage(banner: String?) = putString(userBannerImageKey, banner)


fun Context.saveBasicUserDetail(user: BasicUserModel) {
    userName(user.userName)
    userAvatar(user.avatar?.image)
    userBannerImage(user.bannerImage)
    userScoreFormat(user.scoreFormat)
}

fun Context.removeBasicUserDetail() {
    userName(getString(R.string.app_name))
    userAvatar("")
    userBannerImage("")
    userScoreFormat(ScoreFormat.`$UNKNOWN`.ordinal)
}

fun Context.logOut() {
    loggedIn(false)
    token("")
    userId(-1)
    userScoreFormat(ScoreFormat.`$UNKNOWN`.ordinal)
    removeNotification()
    removeBasicUserDetail()
}

fun Context.removeNotification() {
    setNewNotification(this)
}

fun Context.logIn(accessToken: String) {
    loggedIn(true)
    token(accessToken)
    val userId = JWT(accessToken).subject?.trim()?.toInt() ?: -1
    userId(userId)
}

fun getLastNotification(context: Context): Int {
    return context.getInt(lastNotificationKey, -1)
}

fun setNewNotification(context: Context, notifId: Int = -1) {
    context.putInt(lastNotificationKey, notifId)
}

fun getVersion(context: Context): String {
    val version = context.getString(versionKey) ?: ""
    context.putString(versionKey, DynamicPackageUtils.getAppVersion(context))
    return version
}