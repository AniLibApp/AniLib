package com.revolgenx.anilib.model.list

import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.DateModel

class MediaListModel : CommonMediaModel() {
    var mediaListId: Int? = null
    var score: Double? = null
    var scoreFormat: Int? = null
    var progress: Int? = null
    var popularity: Int? = null
    var listStartDate: DateModel? = null
    var listCompletedDate: DateModel? = null
    var listUpdatedDate:Int? = null
    var listCreatedDate:Int? = null
}
