package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.infrastructure.service.ActivityUnionService
import com.revolgenx.anilib.social.infrastructure.source.ActivityUnionSource
import com.revolgenx.anilib.ui.viewmodel.SourceViewModel

class ActivityUnionFragmentViewModel(private val activityUnionService: ActivityUnionService) :
    SourceViewModel<ActivityUnionSource, ActivityUnionField>() {
    override var field: ActivityUnionField = ActivityUnionField()

    override fun createSource(): ActivityUnionSource {
        source = ActivityUnionSource(field, activityUnionService, compositeDisposable)
        return source!!
    }
}