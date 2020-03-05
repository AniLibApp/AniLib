package com.revolgenx.anilib.repository.network.converter

import com.revolgenx.anilib.fragment.MediaContent
import com.revolgenx.anilib.fragment.NarrowMediaContent
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.CoverImageModel
import com.revolgenx.anilib.model.DateModel
import com.revolgenx.anilib.model.TitleModel

fun NarrowMediaContent.getCommonMedia() =
    CommonMediaModel().also {
        it.id = id()
        it.title = TitleModel().also { title ->
            title.english = title()!!.english() ?: title()!!.romaji()
            title.romaji = title()!!.romaji()
            title.native = title()!!.native_() ?: title()!!.romaji()
            title.userPreferred = title()!!.userPreferred()
        }
        it.format = format()!!.ordinal
        it.episodes = episodes()?.toString() ?: ""
        it.duration = duration()?.toString() ?: ""
        it.status = status()!!.ordinal
        it.coverImage = CoverImageModel().also { image ->
            image.medium = coverImage()!!.medium()
            image.large = coverImage()!!.large()
            image.extraLarge = coverImage()!!.extraLarge()
        }
        it.genres = genres()
        it.averageScore = averageScore()
        startDate()?.let { date ->
            it.startDate = DateModel().also { dateModel ->
                dateModel.year = date.year()
                dateModel.month = date.month()
                dateModel.day = date.day()
                dateModel.date = dateModel.toString()
            }
        }
        endDate()?.let { date ->
            it.endDate = DateModel().also { dateModel ->
                dateModel.year = date.year()
                dateModel.month = date.month()
                dateModel.day = date.day()
                dateModel.date = dateModel.toString()
            }
        }
        it.bannerImage = bannerImage() ?: it.coverImage!!.extraLarge
    }

