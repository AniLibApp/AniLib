package com.revolgenx.anilib.data.model.list

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.data.model.CommonMediaModel
import com.revolgenx.anilib.common.data.model.FuzzyDateModel

class AlMediaListModel : CommonMediaModel() {
    var mediaListId: Int? = null
    var score: Double? = null
    var scoreFormat: Int? = null
    var progress: Int? = null
    var listStartDate: FuzzyDateModel? = null
    var listCompletedDate: FuzzyDateModel? = null
    var listUpdatedDate: Int? = null
    var listCreatedDate: Int? = null
}

data class MediaListCountTypeModel(
    val type: Int?,
    val mediaListCountModels: List<MediaListCountModel>
) : BaseModel()

data class MediaListCountModel(val count: Int, val status: Int) : BaseModel()