package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
) {
    val scoreIcon = if (score >= 75) {
        AppIcons.IcHappy
    } else if (score > 60) {
        AppIcons.IcNeutral
    } else {
        AppIcons.IcSad
    }
    Icon(
        modifier = Modifier.size(15.dp),
        imageVector = scoreIcon,
        contentDescription = null
    )
    MediumText(text = "$score%", fontSize = 12.sp, lineHeight = 13.sp)
}