package com.revolgenx.anilib.model

import com.revolgenx.anilib.model.entry.AdvancedScore

class EntryListEditorMediaModel {
    var mediaId: Int? = null
    var listId: Int? = null
    var userId: Int? = null
    var type: Int? = null
    var status: Int? = null
    var score: Double? = null
    var advancedScoring: List<AdvancedScore>? = null
    var progress: Int? = null
    var progressVolumes: Int? = null
    var repeat: Int? = null
    var notes: String? = null
    var private: Boolean? = null
    var startDate: DateModel? = null
    var endDate: DateModel? = null

    var isUserList = false
}