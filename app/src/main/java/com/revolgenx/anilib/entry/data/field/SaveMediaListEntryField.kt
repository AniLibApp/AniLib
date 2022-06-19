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
    var private: Boolean? = null
    var hiddenFromStatusLists: Boolean? = null
    var repeat: Int? = null
    var notes: String? = null
    var startedAt: FuzzyDateModel? = null
    var completedAt: FuzzyDateModel? = null
    var customLists: List<MutablePair<String, Boolean>>? = null

    override fun toQueryOrMutation(): SaveMediaListEntryMutation {

        val startedAt = startedAt?.let {
            Optional.Present(it.takeIf { !it.isEmpty() }?.let { fd ->
                FuzzyDateInput(nn(fd.year), nn(fd.month), nn(fd.day))
            })
        } ?: Optional.Absent

        val completedAt = completedAt?.let {
            Optional.Present(it.takeIf { !it.isEmpty() }?.let { fd ->
                FuzzyDateInput(nn(fd.year), nn(fd.month), nn(fd.day))
            })
        } ?: Optional.Absent

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
            startedAt = startedAt,
            completedAt = completedAt,
            hiddenFromStatusLists = nn(hiddenFromStatusLists),
            customLists = nn(customLists?.filter { it.second }?.map { it.first })
        )
    }
}