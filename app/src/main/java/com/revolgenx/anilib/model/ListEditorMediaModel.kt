package com.revolgenx.anilib.model

import com.revolgenx.anilib.type.MediaListStatus

class ListEditorMediaModel : BaseMediaModel() {
    var listId: Int = -1
    var userId: Int = -1
    var status: Int = MediaListStatus.`$UNKNOWN`.ordinal
    var score:Double = 0.0
    var progress: Int = 0
    var repeat: Int = 0
    var notes: String = ""
    var private: Boolean = false
    var startDate: DateModel? = null
    var endDate: DateModel? = null
    var isFavourite = false
}