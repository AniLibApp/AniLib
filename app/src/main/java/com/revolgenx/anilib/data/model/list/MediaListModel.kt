package com.revolgenx.anilib.data.model.list

import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.data.model.DateModel

class MediaListModel : CommonMediaModel() {
    var mediaListId: Int? = null
    var score: Double? = null
    var scoreFormat: Int? = null
    var progress: Int? = null
    var listStartDate: DateModel? = null
    var listCompletedDate: DateModel? = null
    var listUpdatedDate:Int? = null
    var listCreatedDate:Int? = null
}
