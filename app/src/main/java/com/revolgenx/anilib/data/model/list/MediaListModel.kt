package com.revolgenx.anilib.data.model.list

import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.DateModel

class MediaListModel : CommonMediaModel() {
    var mediaListId: Int? = null
    var score: Double? = null
    var scoreFormat: Int? = null
    var progress: Int? = null
    var listStartDate: DateModel? = null
    var listCompletedDate: DateModel? = null
    var listUpdatedDate: Int? = null
    var listCreatedDate: Int? = null
}

data class MediaListCountTypeModel(
    val type: Int?,
    val mediaListCountModels: List<MediaListCountModel>
) : BaseModel()

data class MediaListCountModel(val count: Int, val status: Int) : BaseModel()