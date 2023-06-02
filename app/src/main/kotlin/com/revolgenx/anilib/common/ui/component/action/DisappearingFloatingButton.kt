package com.revolgenx.anilib.common.ui.component.action

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.util.onMediaClick

@Composable
fun DisappearingFloatingButton(
    scrollState: MutableState<ScrollState>,
    @DrawableRes iconRes: Int,
    onClick: onMediaClick
) {
    AnimatedVisibility(
        visible = scrollState.value == ScrollState.ScrollDown,
        enter = fadeIn() + expandIn { IntSize(1, 1) }
    ) {
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            onClick = onClick
        ) {
            Icon(painter = painterResource(id = iconRes), contentDescription = null)
        }
    }
}