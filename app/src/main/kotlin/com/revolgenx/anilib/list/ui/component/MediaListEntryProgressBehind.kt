package com.revolgenx.anilib.list.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import anilib.i18n.R
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.media.ui.model.MediaModel

@Composable
fun MediaListEntryProgressBehind(
    modifier: Modifier = Modifier,
    list: MediaListModel,
    compIfNotPresent: @Composable (() ->  Unit)? = null
) {
    val  media = list.media?: return

    val progressBehind = list.progressState?.value?.let { progress ->
        media.currentEpisode?.minus(progress)
    }?.takeIf { it > 0 }

    progressBehind?.let {
        LightText(
            modifier = modifier,
            text = pluralStringResource(
                id = R.plurals.s_episodes_behind,
                progressBehind
            ).format(progressBehind)
        )
    }

    if(progressBehind == null){
        compIfNotPresent?.invoke()
    }

}