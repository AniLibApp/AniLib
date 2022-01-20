package com.revolgenx.anilib.app.setting.data.model

import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.entry.data.model.AdvancedScoreModel

class MediaListOptionTypeModel : BaseModel() {
    var advancedScoringEnabled: Boolean = false
    var advancedScoring: MutableList<AdvancedScoreModel>? = null
    var customLists: List<String>? = null
    var sectionOrder: List<String>? = null
}