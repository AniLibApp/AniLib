package com.revolgenx.anilib.list.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.text.RegularText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.list.ui.model.MediaListModel
import com.revolgenx.anilib.type.ScoreFormat

@Composable
fun MediaListEntryScore(list: MediaListModel) {
    val scoreFormat = list.user?.mediaListOptions?.scoreFormat
    val score = list.score?.takeIf { it != 0.0 }

    val userScore = score.let {
        when (scoreFormat) {
            ScoreFormat.POINT_10_DECIMAL -> it?.toString().naText()
            ScoreFormat.POINT_3 -> if(it == null ) "?" else ""
            else -> it?.toInt()?.toString().naText()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RegularText(
            stringResource(id = R.string.score_s).format(
                userScore
            ),
            fontSize = 12.sp,
        )

        val scoreIcon = when (scoreFormat) {
            ScoreFormat.POINT_5 -> {
                AppIcons.IcStar
            }

            ScoreFormat.POINT_3 -> {
                if (list.isSad) AppIcons.IcSad else if (list.isNeutral) AppIcons.IcNeutral else if(list.isHappy) AppIcons.IcHappy else null
            }

            else -> {
                null
            }
        }
        scoreIcon?.let {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = it,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}