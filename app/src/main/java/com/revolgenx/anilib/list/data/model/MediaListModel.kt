package com.revolgenx.anilib.list.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.fragment.MediaListEntry
import com.revolgenx.anilib.common.repository.network.converter.toModel
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.user.data.model.UserModel

class MediaListModel : BaseModel() {
    var userId: Int = -1
    var mediaId: Int = -1
    var status: Int? = null
    var score: Double? = null
    var progress: Int? = null
    var progressVolumes: Int? = null
    var repeat: Int? = null
    var priority: Int? = null
    var private: Boolean = false
    var notes: String? = null
    var hiddenFromStatusLists = false

    var startedAt: FuzzyDateModel? = null
    var completedAt: FuzzyDateModel? = null
    var updatedAt: Int? = null
    var createdAt: Int? = null

    var customLists: List<MutablePair<String, Boolean>>? = null
    var advanceScores: List<MutablePair<String, Double>>? = null

    var media: MediaModel? = null
    var user: UserModel? = null

    var onDataChanged: ((Resource<MediaListModel>) -> Unit)? = null
}

fun MediaListEntry.toModel() = MediaListModel().also { model ->
    model.id = id
    model.mediaId = mediaId
    model.status = status?.ordinal
    model.score = score
    model.progress = progress
    model.progressVolumes = progressVolumes
    model.repeat = repeat
    model.priority = priority
    model.private = private == true
    model.hiddenFromStatusLists = hiddenFromStatusLists == true
    model.customLists =
        (customLists as? Map<String, Boolean>)?.map { MutablePair(it.key, it.value) }
//    model.advanceScores = advancedScores as? Map<String, Double>
    model.notes = notes
    model.updatedAt = updatedAt
    model.createdAt = createdAt
    model.startedAt = startedAt?.fuzzyDate?.toModel()
    model.completedAt = completedAt?.fuzzyDate?.toModel()
}

