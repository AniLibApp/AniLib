package com.revolgenx.anilib.social.ui.viewmodel

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import com.revolgenx.anilib.common.data.store.ActivityUnionFilterDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.data.source.ActivityUnionPagingSource
import com.revolgenx.anilib.social.ui.model.ActivityUnionModel

open class ActivityUnionViewModel(private val activityUnionService: ActivityUnionService) :
    PagingViewModel<ActivityUnionModel, ActivityUnionField, ActivityUnionPagingSource>() {

    override val field: ActivityUnionField = ActivityUnionField()
    override val pagingSource: ActivityUnionPagingSource
        get() = ActivityUnionPagingSource(this.field, activityUnionService)

    val activityReplyId = mutableIntStateOf(-1)
}

class MainActivityUnionViewModel(
    activityUnionService: ActivityUnionService,
    private val activityUnionFilterDataStore: ActivityUnionFilterDataStore
) : ActivityUnionViewModel(activityUnionService) {
    private var filter = activityUnionFilterDataStore.data.get()
    override var field: ActivityUnionField = filter.toField()

    init {
        launch {
            activityUnionFilterDataStore.data.collect {
                if (it == filter) return@collect
                filter = it
                field = it.toField()
                refresh()
            }
        }
    }
}