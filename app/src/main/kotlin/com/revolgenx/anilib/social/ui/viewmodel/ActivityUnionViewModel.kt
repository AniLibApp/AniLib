package com.revolgenx.anilib.social.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.service.ToggleService
import com.revolgenx.anilib.common.data.store.ActivityUnionFilterDataStore
import com.revolgenx.anilib.common.ext.get
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.field.ToggleActivitySubscriptionField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.data.source.ActivityUnionPagingSource
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.type.LikeableType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single

open class ActivityUnionViewModel(
    private val activityUnionService: ActivityUnionService,
    private val toggleService: ToggleService
) :
    PagingViewModel<ActivityModel, ActivityUnionField, ActivityUnionPagingSource>() {

    override val field: ActivityUnionField = ActivityUnionField()

    var showToggleError by mutableStateOf(false)
    var showDeleteError by mutableStateOf(false)

    override val pagingSource: ActivityUnionPagingSource
        get() = ActivityUnionPagingSource(this.field, activityUnionService)

    var activityId by mutableIntStateOf(-1)

    fun toggleSubscription(model: ActivityModel) {
        if (model.id == -1) return

        val isSubscribed = model.isSubscribed.value
        model.isSubscribed.value = !isSubscribed

        launch {
            val successful = toggleService.toggleActivitySubscription(
                ToggleActivitySubscriptionField(activityId = model.id, isSubscribed = !isSubscribed)
            ).single()

            if (!successful) {
                model.isSubscribed.value = isSubscribed
                showToggleError = true
            }
        }
    }

    fun toggleLike(model: ActivityModel) {
        if (model.id == -1) return

        val isLiked = model.isLiked.value
        model.isLiked.value = !isLiked

        val toggleLikeField =
            ToggleLikeV2Field(id = model.id, type = LikeableType.ACTIVITY)

        toggleService.toggleLikeV2(toggleLikeField)
            .onEach {
                it ?: return@onEach
                model.likeCount.intValue = it.likeCount
                model.isLiked.value = it.isLiked
            }.catch {
                model.isLiked.value = isLiked
                showToggleError = true
            }.launchIn(viewModelScope)
    }

    fun delete(model: ActivityModel) {
        if (model.id == -1) return
        activityUnionService.deleteActivity(model.id)
            .onEach {
                model.isDeleted.value = it
            }.catch {
                showDeleteError = true
            }.launchIn(viewModelScope)
    }
}

class MainActivityUnionViewModel(
    activityUnionService: ActivityUnionService,
    private val activityUnionFilterDataStore: ActivityUnionFilterDataStore,
    toggleService: ToggleService
) : ActivityUnionViewModel(activityUnionService, toggleService = toggleService) {
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