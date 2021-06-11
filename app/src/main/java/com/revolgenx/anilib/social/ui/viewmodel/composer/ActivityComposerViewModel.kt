package com.revolgenx.anilib.social.ui.viewmodel.composer

import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class ActivityComposerViewModel : BaseViewModel() {


    var text: String = ""
        set(value) {
            if (field != value) {
                hasTextChanged = true
            }
            field = value
        }

    var hasTextChanged = false

    var activeActivityUnionModel: ActivityUnionModel? = null
}
