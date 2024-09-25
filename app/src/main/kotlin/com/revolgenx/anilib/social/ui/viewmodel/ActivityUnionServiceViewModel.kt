package com.revolgenx.anilib.social.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.service.ToggleService
import com.revolgenx.anilib.common.ext.launch
import com.revolgenx.anilib.social.data.field.ToggleActivitySubscriptionField
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.type.LikeableType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single

class ActivityUnionServiceViewModel(
    private val activityUnionService: ActivityUnionService,
    private val toggleService: ToggleService
) : ViewModel() {

    var showToggleError by mutableStateOf(false)
    var showDeleteError by mutableStateOf(false)

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