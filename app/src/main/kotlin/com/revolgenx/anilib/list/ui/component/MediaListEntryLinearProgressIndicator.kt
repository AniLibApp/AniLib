package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.revolgenx.anilib.list.ui.model.MediaListModel

@Composable
fun MediaListEntryLinearProgressIndicator(
    modifier:Modifier = Modifier,
    list: MediaListModel
) {
    val media = list.media ?: return
    val progress = remember {
        derivedStateOf {
            list.progressState?.value?.toFloat()
                ?.div(media.totalEpisodesOrChapters?.takeIf { it != 0 }?.toFloat() ?: 1f)
        }
    }
    LinearProgressIndicator(
        modifier = modifier.fillMaxWidth(),
        progress = { progress.value ?: 0f },
        trackColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}