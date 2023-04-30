package com.revolgenx.anilib.list.ui.model

import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.type.MediaListStatus

data class MediaListModel(
    val id: Int = -1,
    var userId: Int = -1,
    var mediaId: Int = -1,
    var status: MediaListStatus? = null,
    var score: Double? = null,
    var progress: Int? = null,
    var progressVolumes: Int? = null,
    var repeat: Int? = null,
    var priority: Int? = null,
    var private: Boolean? = false,
    var notes: String? = null,
    var hiddenFromStatusLists:Boolean = false,
    var startedAt: FuzzyDateModel? = null,
    var completedAt: FuzzyDateModel? = null,
    var updatedAt: Int? = null,
    var createdAt: Int? = null,

    var customLists: List<MutablePair<String, Boolean>>? = null,
    var advanceScores: List<MutablePair<String, Double>>? = null,

    var media: MediaModel? = null,
//    var user: UserModel? = null,
)
