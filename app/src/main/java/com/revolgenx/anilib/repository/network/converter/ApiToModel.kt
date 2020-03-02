package com.revolgenx.anilib.repository.network.converter

import com.revolgenx.anilib.fragment.MediaContent
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.CoverImageModel
import com.revolgenx.anilib.model.DateModel
import com.revolgenx.anilib.model.TitleModel

fun MediaContent.getCommonMedia() =
    CommonMediaModel().also {
        it.id = id()
        it.title = TitleModel().also { title ->
            title.english = title()!!.english() ?: title()!!.romaji()
            title.romaji = title()!!.romaji()
            title.native = title()!!.native_() ?: title()!!.romaji()
            title.userPreferred = title()!!.userPreferred()
        }
        it.episodes = episodes()?.toString() ?: ""
        it.status = status()!!.ordinal
        it.coverImage = CoverImageModel().also { image ->
            image.medium = coverImage()!!.medium()
            image.large = coverImage()!!.large()
            image.extraLarge = coverImage()!!.extraLarge()
        }
        it.genres = genres()
        it.averageScore = averageScore()
        it.startDate = DateModel().also { dateModel ->
            dateModel.year = startDate()!!.year() ?: 0
            dateModel.month = startDate()!!.month() ?: 0
            dateModel.day = startDate()!!.day() ?: 0
        }
        it.description = description() ?: ""
        it.bannerImage = bannerImage() ?: it.coverImage!!.large
    }

