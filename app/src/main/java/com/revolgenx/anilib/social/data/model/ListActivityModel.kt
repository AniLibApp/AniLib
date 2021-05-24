package com.revolgenx.anilib.social.data.model

import com.revolgenx.anilib.data.model.CommonMediaModel

class ListActivityModel : ActivityUnionModel() {
    var status: String = ""
    var progress: String? = null
    var media: CommonMediaModel? = null

    val getProgressStatus:String
        get() = status + progress
}