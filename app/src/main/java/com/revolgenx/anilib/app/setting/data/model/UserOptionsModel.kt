package com.revolgenx.anilib.app.setting.data.model


data class UserOptionsModel(
    var titleLanguage: Int,
    var displayAdultContent: Boolean,
    var airingNotifications: Boolean,
    var profileColor: String? //blue, purple, pink, orange, red, green, gray
)

