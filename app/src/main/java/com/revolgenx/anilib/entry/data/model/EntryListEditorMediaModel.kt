package com.revolgenx.anilib.entry.data.model

import com.revolgenx.anilib.common.data.model.FuzzyDateModel

class EntryListEditorMediaModel {
    var mediaId: Int? = null
    var listId: Int? = null
    var userId: Int? = null
    var type: Int? = null
    var status: Int? = null
    var score: Double? = null
    var advancedScoring: List<AdvancedScoreModel>? = null
    var progress: Int? = null
    var progressVolumes: Int? = null
    var repeat: Int? = null
    var notes: String? = null
    var private: Boolean? = null
    var startDate: FuzzyDateModel? = null
    var endDate: FuzzyDateModel? = null

    var isUserList = false
    var hasData = false
}