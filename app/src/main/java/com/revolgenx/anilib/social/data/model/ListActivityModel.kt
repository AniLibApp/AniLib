package com.revolgenx.anilib.social.data.model

import com.revolgenx.anilib.media.data.model.MediaModel

class ListActivityModel : ActivityUnionModel() {
    var status: String = ""
    var progress: String? = null
    var media: MediaModel? = null

    val getProgressStatus:String
        get() = "${status.capitalize()}${if(progress.isNullOrBlank()) " " else " $progress of "}${media!!.title!!.userPreferred}"
}