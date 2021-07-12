package com.revolgenx.anilib.social.ui.viewmodel.activity_composer

import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.social.data.field.SaveMessageActivityField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.infrastructure.service.ActivityUnionService

class ActivityMessageComposerViewModel(private val activityUnionService: ActivityUnionService) :
    ActivityComposerViewModel<Int, ActivityUnionModel, SaveMessageActivityField>() {
    override val field = SaveMessageActivityField()

    var message:String
    set(value){text = value}
    get() = text

    override fun save(callback: (Resource<Int>) -> Unit) {
        activityUnionService.saveMessageActivity(field, compositeDisposable, callback)
    }
}