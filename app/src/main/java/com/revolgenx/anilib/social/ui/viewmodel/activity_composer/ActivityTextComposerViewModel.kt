package com.revolgenx.anilib.social.ui.viewmodel.activity_composer

import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.social.data.field.SaveTextActivityField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.infrastructure.service.ActivityUnionService

class ActivityTextComposerViewModel(private val activityUnionService: ActivityUnionService) :
    ActivityComposerViewModel<Int, ActivityUnionModel, SaveTextActivityField>() {
    override val field: SaveTextActivityField = SaveTextActivityField()


    override fun save(callback: (Resource<Int>) -> Unit) {
        activityUnionService.saveTextActivity(field, compositeDisposable, callback)
    }
}