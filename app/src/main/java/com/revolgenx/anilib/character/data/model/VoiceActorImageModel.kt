package com.revolgenx.anilib.character.data.model


class VoiceActorImageModel {
    var large: String? = null
    var medium: String? = null

    val image: String
        get() {
            return large ?: medium ?: ""
        }
}
