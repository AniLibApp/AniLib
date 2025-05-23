package com.revolgenx.anilib.media.ui.model

import androidx.annotation.IntDef
import com.revolgenx.anilib.fragment.MediaTitle
import kotlinx.serialization.Serializable

@Serializable
data class MediaTitleModel(
    val english: String? = null,
    val romaji: String? = null,
    val native: String? = null,
    val userPreferred: String? = null
) {
    companion object {
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(type_romaji, type_english, type_native)
        annotation class MediaTitleType

        const val type_romaji = 0
        const val type_english = 1
        const val type_native = 2
    }

    fun title(@MediaTitleType which: Int) = when (which) {
        type_romaji -> romaji
        type_english -> english ?: romaji
        type_native -> native ?: romaji
        else -> romaji
    }
}

fun MediaTitle.toModel() = MediaTitleModel(english, romaji, native, userPreferred)

