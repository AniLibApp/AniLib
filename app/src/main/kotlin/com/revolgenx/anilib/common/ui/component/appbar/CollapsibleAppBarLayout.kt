package com.revolgenx.anilib.common.ui.component.appbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.revolgenx.anilib.common.ext.emptyWindowInsets


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsibleAppBarLayout(
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: AppBarLayoutColors = AppBarLayoutDefaults.appBarLayoutColors(),
    containerHeight: Dp = AppBarHeight,
    maxHeightOffsetLimit: Dp,
    content: @Composable () -> Unit
) {
    val topPadding = windowInsets.getTop(LocalDensity.current)
    val heightOffsetLimit =
        with(LocalDensity.current) { maxHeightOffsetLimit.toPx() + topPadding - containerHeight.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior?.state?.heightOffsetLimit = heightOffsetLimit
        }
    }

    val colorTransitionFraction = scrollBehavior?.state?.overlappedFraction ?: 0f
    val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
    val appBarContainerColor by animateColorAsState(
        targetValue = colors.containerColor(fraction),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )
    val appBarDragModifier = if (scrollBehavior != null && !scrollBehavior.isPinned) {
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffset + delta
            },
            onDragStopped = { velocity ->
                settleAppBar(
                    scrollBehavior.state,
                    velocity,
                    scrollBehavior.flingAnimationSpec,
                    scrollBehavior.snapAnimationSpec
                )
            }
        )
    } else {
        Modifier
    }

    Surface(modifier = Modifier.then(appBarDragModifier), color = appBarContainerColor) {
        val height = LocalDensity.current.run {
            containerHeight.toPx() + (scrollBehavior?.state?.heightOffset ?: 0f)
        }
        TopAppBarContent(windowInsets = emptyWindowInsets(), heightPx = height, content)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
suspend fun TopAppBarState.collapse(snapAnimationSpec: AnimationSpec<Float>?) {
    if (snapAnimationSpec != null) {
        AnimationState(initialValue = heightOffset).animateTo(
            heightOffsetLimit,
            animationSpec = snapAnimationSpec
        ) { heightOffset = value }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarState.collapseProgress(): State<Float> {
    return remember {
        derivedStateOf {
            if (heightOffsetLimit != 0f) {
                heightOffset / heightOffsetLimit
            } else {
                0f
            }
        }
    }
}