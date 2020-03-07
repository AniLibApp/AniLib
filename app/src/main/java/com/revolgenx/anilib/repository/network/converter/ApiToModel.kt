package com.revolgenx.anilib.repository.network.converter

import com.revolgenx.anilib.BasicUserQuery
import com.revolgenx.anilib.fragment.MediaListContent
import com.revolgenx.anilib.fragment.NarrowMediaContent
import com.revolgenx.anilib.model.*

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
        it.type = type()!!.ordinal
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

fun BasicUserQuery.User.toBasicUserModel() = BasicUserModel().also {
    it.userId = id()
    it.name = name()
    it.scoreFormat = mediaListOptions()!!.scoreFormat()!!.ordinal
    it.avatar = UserAvatarImageModel().also { img ->
        img.large = avatar()!!.large()
        img.medium = avatar()!!.medium()
    }
    it.bannerImage = bannerImage() ?: ""
}


fun MediaListContent.toListEditorMediaModel() = EntryListEditorMediaModel().also {
    it.id = mediaId()
    it.userId = userId()
    it.listId = id()
    it.status = status()!!.ordinal
    it.notes = notes()
    it.progress = progress()!!
    it.private = private_()!!
    it.repeat = repeat()!!
    it.isFavourite = media()!!.isFavourite
    it.startDate = startedAt()!!.let {
        DateModel().also { date ->
            date.day = it.day()
            date.month = it.month()
            date.year = it.year()
        }
    }
    it.endDate = completedAt()!!.let {
        DateModel().also { date ->
            date.day = it.day()
            date.month = it.month()
            date.year = it.year()
        }
    }

}
