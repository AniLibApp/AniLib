package com.revolgenx.anilib.social.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.service.ToggleService
import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.social.data.field.ActivityReplyField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.data.source.ActivityReplyPagingSource
import com.revolgenx.anilib.social.ui.model.ActivityReplyModel
import com.revolgenx.anilib.type.LikeableType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ActivityReplyViewModel(
    activityId: Int,
    private val activityUnionService: ActivityUnionService,
    private val toggleService: ToggleService
) : PagingViewModel<ActivityReplyModel, ActivityReplyField, ActivityReplyPagingSource>() {

    override val field: ActivityReplyField = ActivityReplyField(activityId = activityId)
    override val pagingSource: ActivityReplyPagingSource
        get() = ActivityReplyPagingSource(field = this.field, service = activityUnionService)

    var showToggleError by mutableStateOf(false)
    var showDeleteError by mutableStateOf(false)

    fun toggleLike(model: ActivityReplyModel) {
        if (model.id == -1) return

        val toggleLikeField = ToggleLikeV2Field(id = model.id, type = LikeableType.ACTIVITY_REPLY)

        val isLiked = model.isLiked.value
        model.isLiked.value = !isLiked

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

    fun delete(model: ActivityReplyModel) {
        if (model.id == -1) return

        activityUnionService.deleteActivityReply(model.id)
            .onEach {
                model.isDeleted.value = it
            }.catch {
                showDeleteError = true
            }.launchIn(viewModelScope)
    }
}