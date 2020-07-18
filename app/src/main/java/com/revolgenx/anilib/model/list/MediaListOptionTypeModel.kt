package com.revolgenx.anilib.model.list

import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.entry.AdvancedScore

class MediaListOptionTypeModel : BaseModel() {
    var advancedScoringEnabled: Boolean = false
    var advancedScoring: List<AdvancedScore>? = null
}