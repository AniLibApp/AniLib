package com.revolgenx.anilib.entry.data.field

import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.SaveMediaListEntryMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.type.FuzzyDateInput
import com.revolgenx.anilib.type.MediaListStatus

class SaveMediaListEntryField : BaseField<SaveMediaListEntryMutation>() {
    var id: Int? = null
    var mediaId: Int? = null
    var status: Int? = null
    var score: Double? = null
    var advancedScores: List<MutablePair<String, Double>>? = null
    var progress: Int? = null
    var progressVolumes: Int? = null
    var private: Boolean = false
    var hiddenFromStatusLists = false
    var repeat: Int? = null
    var notes: String? = null
    var startedAt: FuzzyDateModel? = null
    var completedAt: FuzzyDateModel? = null
    var customLists: List<MutablePair<String, Boolean>>? = null

    override fun toQueryOrMutation(): SaveMediaListEntryMutation {
        return SaveMediaListEntryMutation(
            id = nn(id),
            mediaId = nn(mediaId),
            status = nn(status?.let { MediaListStatus.values()[it] }),
            score = nn(score),
            advanceScores = nn(advancedScores?.map { it.second }),
            progress = nn(progress),
            progressVolumes = nn(progressVolumes),
            private_ = nn(private),
            repeat = nn(repeat),
            notes = nn(notes),
            startedAt = Optional.Present(startedAt?.let {
                FuzzyDateInput(nn(it.year), nn(it.month), nn(it.day))
            }),
            completedAt = Optional.Present(completedAt?.let {
                FuzzyDateInput(nn(it.year), nn(it.month), nn(it.day))
            }),
            hiddenFromStatusLists = nn(hiddenFromStatusLists),
            customLists = nn(customLists?.filter { it.second }?.map { it.first })
        )
    }
}