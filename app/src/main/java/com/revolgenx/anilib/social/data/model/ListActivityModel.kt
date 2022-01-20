package com.revolgenx.anilib.social.data.model

import com.revolgenx.anilib.common.data.model.CommonMediaModel

class ListActivityModel : ActivityUnionModel() {
    var status: String = ""
    var progress: String? = null
    var media: CommonMediaModel? = null

    val getProgressStatus:String
        get() = "${status.capitalize()}${if(progress.isNullOrBlank()) " " else " $progress of "}${media!!.title!!.userPreferred}"
}