package com.revolgenx.anilib.list.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.constant.AlMediaListStatus
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.user.data.model.UserModel

class MediaListModel:BaseModel(){
    var userId: Int = -1
    var mediaId: Int = -1
    var status: Int? = null
    var score: Double? = null
    var progress: Int? = null
    var progressVolumes: Int? = null
    var repeat: Int? = null
    var priority: Int? = null
    var private: Boolean = false
    var notes:String? = null
    var hiddenFromStatusLists = false

    var startedAt:FuzzyDateModel? = null
    var completedAt:FuzzyDateModel? = null
    var updatedAt:Int? = null
    var createdAt:Int? = null

    var media:MediaModel? = null
    var user: UserModel? = null
}