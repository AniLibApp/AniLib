package com.revolgenx.anilib.entry.data.field

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.apollographql.apollo3.api.Optional
import com.revolgenx.anilib.SaveMediaListEntryMutation
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.tuples.MutablePair
import com.revolgenx.anilib.common.ui.model.FuzzyDateModel
import com.revolgenx.anilib.entry.ui.model.AdvancedScoreModel
import com.revolgenx.anilib.type.FuzzyDateInput
import com.revolgenx.anilib.type.MediaListStatus

class SaveMediaListEntryField() : BaseField<SaveMediaListEntryMutation>() {
    var id: Int? = null
    var mediaId: Int? = null
    var status by mutableStateOf(MediaListStatus.CURRENT)
    var score by mutableDoubleStateOf(0.0)
    var advancedScores: List<AdvancedScoreModel>? = null
    var progress by mutableStateOf<Int?>(null)
    var progressVolumes by mutableStateOf<Int?>(null)
    var private: Boolean = false
    var hiddenFromStatusLists: Boolean? = null
    var repeat by mutableStateOf<Int?>(null)
    var notes by mutableStateOf<String?>(null)
    var startedAt by mutableStateOf<FuzzyDateModel?>(null)
    var completedAt by mutableStateOf<FuzzyDateModel?>(null)
    var customLists: List<MutablePair<String, Boolean>>? = null

    override fun toQueryOrMutation(): SaveMediaListEntryMutation {
        val startedAt = Optional.Present(startedAt?.let { fd ->
            FuzzyDateInput(nn(fd.year), nn(fd.month), nn(fd.day))
        })

        val completedAt = Optional.Present(completedAt?.let { fd ->
            FuzzyDateInput(nn(fd.year), nn(fd.month), nn(fd.day))
        })

        return SaveMediaListEntryMutation(
            id = nn(id),
            mediaId = nn(mediaId),
            status = nn(status),
            score = nn(score),
            advanceScores = nn(advancedScores?.map { it.score.value }),
            progress = nn(progress),
            progressVolumes = nn(progressVolumes),
            private_ = nn(private),
            repeat = nn(repeat),
            notes = nnString(notes),
            startedAt = startedAt,
            completedAt = completedAt,
            hiddenFromStatusLists = nn(hiddenFromStatusLists),
            customLists = nnList(customLists?.filter { it.second }?.map { it.first })
        )
    }
}


