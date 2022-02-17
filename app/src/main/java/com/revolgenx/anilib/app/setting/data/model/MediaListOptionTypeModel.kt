package com.revolgenx.anilib.app.setting.data.model

import com.revolgenx.anilib.common.data.model.BaseModel

class MediaListOptionTypeModel : BaseModel() {
    var advancedScoringEnabled: Boolean = false
    var advancedScoring: MutableList<String>? = null
    var customLists: List<String>? = null
    var sectionOrder: List<String>? = null
    var splitCompletedSectionByFormat:Boolean? = null
}