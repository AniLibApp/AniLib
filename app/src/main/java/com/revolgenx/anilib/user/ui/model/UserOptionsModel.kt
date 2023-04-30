package com.revolgenx.anilib.user.ui.model

data class UserOptionsModel(
    val titleLanguage: Int,
    val displayAdultContent: Boolean,
    val airingNotifications: Boolean,
    val profileColor: String? //blue, purple, pink, orange, red, green, gray
)