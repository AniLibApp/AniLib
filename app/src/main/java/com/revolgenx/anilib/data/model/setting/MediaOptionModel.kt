package com.revolgenx.anilib.data.model.setting


data class MediaOptionModel(
    var titleLanguage: Int,
    var displayAdultContent: Boolean,
    var airingNotifications: Boolean,
    var profileColor: String? //blue, purple, pink, orange, red, green, gray
)

