package com.revolgenx.anilib.app.setting.data.model

import com.revolgenx.anilib.common.data.model.BaseModel

class MediaListOptionModel : BaseModel() {
    var scoreFormat:Int? = null
    var rowOrder:Int? = null
    var animeList: MediaListOptionTypeModel? = null
    var mangaList: MediaListOptionTypeModel? = null
}


fun getRowOrder(rowOrder: String?): Int {
    return when (rowOrder) {
        "score" -> {
            0
        }
        "title" -> {
            1
        }
        "updatedAt" -> {
            2
        }
        else -> {
            3
        }
    }
}

fun getRowOrderString(rowOrder: Int): String {
    return when (rowOrder) {
        0 -> {
            "score"
        }
        1 -> {
            "title"
        }
        2 -> {
            "updatedAt"
        }
        else -> {
            "id"
        }
    }
}

