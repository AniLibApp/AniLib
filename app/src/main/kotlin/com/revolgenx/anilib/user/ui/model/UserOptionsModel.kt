package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.fragment.UserMediaOptions
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.media.ui.model.MediaTitleModel.Companion.MediaTitleType
import com.revolgenx.anilib.type.UserTitleLanguage

data class UserOptionsModel(
    val titleLanguage: UserTitleLanguage,
    val displayAdultContent: Boolean = false,
    val airingNotifications: Boolean,
    val profileColor: String? = null, //blue, purple, pink, orange, red, green, gray
    val activityMergeTime: Int
)

fun UserMediaOptions.toModel() = UserOptionsModel(
    titleLanguage = titleLanguage ?: UserTitleLanguage.ROMAJI,
    displayAdultContent = displayAdultContent == true,
    airingNotifications = airingNotifications == true,
    profileColor = profileColor,
    activityMergeTime = this.activityMergeTime ?: 0
)

fun UserTitleLanguage.toMediaTitleType() = when (this) {
    UserTitleLanguage.ROMAJI, UserTitleLanguage.ROMAJI_STYLISED -> MediaTitleModel.type_romaji
    UserTitleLanguage.ENGLISH, UserTitleLanguage.ENGLISH_STYLISED -> MediaTitleModel.type_english
    UserTitleLanguage.NATIVE, UserTitleLanguage.NATIVE_STYLISED -> MediaTitleModel.type_native
    UserTitleLanguage.UNKNOWN__ -> MediaTitleModel.type_romaji
}

fun getUserTitleLanguage(@MediaTitleType which: Int?): UserTitleLanguage {
    return when (which) {
        MediaTitleModel.type_english -> {
            UserTitleLanguage.ENGLISH
        }

        MediaTitleModel.type_native -> {
            UserTitleLanguage.NATIVE
        }

        MediaTitleModel.type_romaji -> {
            UserTitleLanguage.ROMAJI
        }

        else -> {
            UserTitleLanguage.ROMAJI
        }
    }
}