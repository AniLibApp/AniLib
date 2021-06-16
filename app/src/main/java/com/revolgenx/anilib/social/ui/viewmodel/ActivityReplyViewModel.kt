package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.data.model.toggle.LikeableUnionModel
import com.revolgenx.anilib.infrastructure.repository.util.Resource
import com.revolgenx.anilib.infrastructure.service.toggle.ToggleService
import com.revolgenx.anilib.social.data.field.ToggleLikeV2Field
import com.revolgenx.anilib.social.data.model.reply.ActivityReplyModel
import com.revolgenx.anilib.type.LikeableType
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel

class ActivityReplyViewModel(private val toggleService: ToggleService) : BaseViewModel() {
    fun toggleLike(model: ActivityReplyModel, callback: (Resource<LikeableUnionModel>) -> Unit) {
        val field = ToggleLikeV2Field().also {
            it.id = model.id
            it.type = LikeableType.ACTIVITY_REPLY
        }
        toggleService.toggleLikeV2(field, compositeDisposable, callback)
    }
}