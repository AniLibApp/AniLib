package com.revolgenx.anilib.data.model.list

import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.entry.AdvancedScore

class MediaListOptionTypeModel : BaseModel() {
    var advancedScoringEnabled: Boolean = false
    var advancedScoring: List<AdvancedScore>? = null
}