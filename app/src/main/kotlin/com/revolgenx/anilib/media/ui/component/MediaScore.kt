package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad
import com.revolgenx.anilib.list.ui.model.toColor
import com.revolgenx.anilib.list.ui.model.toImageVector
import com.revolgenx.anilib.media.ui.model.MediaModel

@Composable
fun MediaInfoBadge(
    modifier: Modifier = Modifier,
    media: MediaModel
) {
        if(media.averageScore == null && media.mediaListEntry == null)  return
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
                    MediaScore(score = score)
                }

                media.mediaListEntry?.let {
                    Icon(
                        modifier = Modifier.size(14.dp),
                        imageVector = it.status.toImageVector(),
                        tint = it.status.toColor(),
                        contentDescription = null
                    )
                }
            }
    }
}


@Composable
fun MediaScore(
    score: Int,
) {
    val scoreIcon = if (score >= 75) {
        AppIcons.IcHappy
    } else if (score > 60) {
        AppIcons.IcNeutral
    } else {
        AppIcons.IcSad
    }
    Icon(
        modifier = Modifier.size(14.dp),
        imageVector = scoreIcon,
        contentDescription = null
    )
    MediumText(text = "$score%", fontSize = 11.sp, lineHeight = 12.sp)
}