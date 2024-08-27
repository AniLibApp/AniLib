package com.revolgenx.anilib.social.ui.viewmodel

import com.revolgenx.anilib.social.data.service.ActivityUnionService

class ReplyComposerViewModel (activityUnionService: ActivityUnionService): BaseActivityComposerViewModel(activityUnionService) {
    override val activityType: ActivityType = ActivityType.REPLY
}