package com.revolgenx.anilib.media.ui.screen

import androidx.compose.runtime.Composable
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.common.util.OnClickWithId
import com.revolgenx.anilib.social.ui.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.model.TextActivityModel
import com.revolgenx.anilib.social.ui.screen.ActivityUnionScreenContent
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel

@Composable
fun MediaSocialActivityScreen(
    viewModel: ActivityUnionViewModel,
    onShowReplies: OnClickWithId,
    onEditTextActivity: (model: TextActivityModel) -> Unit,
    onEditMessageActivity: (model: MessageActivityModel) -> Unit
) {
    ActivityUnionScreenContent(
        viewModel = viewModel,
        onShowReplies = onShowReplies,
        onEditTextActivity = onEditTextActivity,
        onEditMessageActivity = onEditMessageActivity,
        scrollTarget = ScrollTarget.MEDIA_SOCIAL_ACTIVITY
    )
}