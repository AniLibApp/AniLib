package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad


@Composable
fun MediaScore(
    score: Int,
    iconSize: Dp,
    fontSize: TextUnit
) {
    val scoreIcon = if (score >= 75) {
        AppIcons.IcHappy
    } else if (score > 60) {
        AppIcons.IcNeutral
    } else {
        AppIcons.IcSad
    }
    Icon(
        modifier = Modifier.size(iconSize),
        imageVector = scoreIcon,
        contentDescription = null
    )
    MediumText(text = "$score%", fontSize = fontSize, lineHeight = 13.sp)
}