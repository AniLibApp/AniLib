package com.revolgenx.anilib.common.ui.component.action

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.util.OnClick

@Composable
fun DisappearingFAB(
    scrollState: MutableState<ScrollState>,
    icon: ImageVector,
    onClick: OnClick
) {
    DisappearingFAB(
        scrollState = scrollState,
        content = {
            Icon(imageVector = icon, contentDescription = null)
        },
        onClick = onClick
    )
}


@Composable
fun DisappearingFAB(
    scrollState: MutableState<ScrollState>,
    content: @Composable () -> Unit,
    onClick: OnClick? = null,
) {
    AnimatedVisibility(
        visible = scrollState.value == ScrollState.ScrollDown,
        enter = fadeIn() + expandIn { IntSize(1, 1) }
    ) {
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
            onClick = onClick ?: {}
        ) {
            content()
        }
    }
}