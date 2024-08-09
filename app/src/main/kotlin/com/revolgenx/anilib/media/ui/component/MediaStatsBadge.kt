package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.list.ui.model.toColor
import com.revolgenx.anilib.list.ui.model.toImageVector
import com.revolgenx.anilib.media.ui.model.MediaModel

@Composable
fun MediaStatsBadge(
    modifier: Modifier = Modifier,
    media: MediaModel,
    iconSize: Dp = 15.dp,
    fontSize: TextUnit = 12.sp
) {
    if (media.averageScore == null && media.mediaListEntry == null) return
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            media.averageScore?.let { score ->
                MediaScore(score = score, iconSize = iconSize, fontSize = fontSize)
            }

            media.mediaListEntry?.let {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = it.status.value.toImageVector(),
                    tint = it.status.value.toColor(),
                    contentDescription = null
                )
            }
        }
    }
}