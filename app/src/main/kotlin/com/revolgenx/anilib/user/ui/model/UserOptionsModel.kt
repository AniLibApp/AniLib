package com.revolgenx.anilib.user.ui.model

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.fragment.UserMediaOptions
import com.revolgenx.anilib.media.ui.model.MediaTitleModel
import com.revolgenx.anilib.type.UserTitleLanguage

data class UserOptionsModel(
    val titleLanguage: UserTitleLanguage,
    val displayAdultContent: Boolean = false,
    val airingNotifications: Boolean,
    val profileColor: String? = null, //blue, purple, pink, orange, red, green, gray
    val activityMergeTime: Int
)

class UserOptionsUiModel(
    val titleLanguage: MutableState<Int>,
    val displayAdultContent: MutableState<Boolean>,
    val airingNotifications: MutableState<Boolean>,
    val profileColor: MutableState<String?>,
    val activityMergeTime: MutableIntState
)

fun UserMediaOptions.toModel() = UserOptionsModel(
    titleLanguage = titleLanguage ?: UserTitleLanguage.ROMAJI,
    displayAdultContent = displayAdultContent == true,
    airingNotifications = airingNotifications == true,
    profileColor = profileColor,
    activityMergeTime = this.activityMergeTime ?: 0
)

fun UserOptionsModel.toUiModel() = UserOptionsUiModel(
    titleLanguage = mutableStateOf(titleLanguage.toMediaTitleType()),
    displayAdultContent = mutableStateOf(displayAdultContent),
    airingNotifications = mutableStateOf(airingNotifications),
    profileColor = mutableStateOf(profileColor),
    activityMergeTime = mutableIntStateOf(activityMergeTime),
)

fun UserTitleLanguage.toMediaTitleType() = when (this) {
    UserTitleLanguage.ROMAJI, UserTitleLanguage.ROMAJI_STYLISED -> MediaTitleModel.type_romaji
    UserTitleLanguage.ENGLISH, UserTitleLanguage.ENGLISH_STYLISED -> MediaTitleModel.type_english
    UserTitleLanguage.NATIVE, UserTitleLanguage.NATIVE_STYLISED -> MediaTitleModel.type_native
    UserTitleLanguage.UNKNOWN__ -> MediaTitleModel.type_romaji
}