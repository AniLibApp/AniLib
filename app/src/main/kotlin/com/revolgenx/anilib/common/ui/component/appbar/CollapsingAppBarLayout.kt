package com.revolgenx.anilib.common.ui.component.appbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.revolgenx.anilib.common.ui.component.action.NavigationIcon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsibleAppBarLayout(
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    containerHeight: Dp = AppBarHeight,
    appBarHeight: Dp = AppBarHeight,
    content: @Composable () -> Unit
) {
    val topPadding = windowInsets.getTop(LocalDensity.current)
    val heightOffsetLimit =
        with(LocalDensity.current) { appBarHeight.toPx() + topPadding - containerHeight.toPx() }
    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior?.state?.heightOffsetLimit = heightOffsetLimit
        }
    }

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

    Surface(modifier = Modifier.then(appBarDragModifier), color = Color.Transparent) {
        val height = LocalDensity.current.run {
            containerHeight.toPx() + (scrollBehavior?.state?.heightOffset ?: 0f)
        }
        TopAppBarContent(
            windowInsets = windowInsets.only(WindowInsetsSides.Horizontal),
            heightPx = height,
            content
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingAppbar(
    appBarHeight: Dp = AppBarHeight,
    containerHeight: Dp,
    scrollBehavior: TopAppBarScrollBehavior,
    containerContent: @Composable BoxScope.(isCollapsed: Boolean) -> Unit,
    title: @Composable (isCollapsed: Boolean) -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.(isCollapsed: Boolean) -> Unit,
    maxAlpha: Float = 0.8f,
    alphaFraction: Float = maxAlpha,
    collapseFraction: Float = 0.7f,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    contentWindowInsets: WindowInsets = TopAppBarDefaults.windowInsets
) {
    val topAppBarState = scrollBehavior.state
    val progress = topAppBarState.collapseProgress().value
    val containerAlpha = if (progress >= alphaFraction) maxAlpha else progress
    val isCollapsed = progress > collapseFraction

    Box {
        CollapsibleAppBarLayout(
            containerHeight = containerHeight,
            appBarHeight = appBarHeight,
            scrollBehavior = scrollBehavior,
            windowInsets = contentWindowInsets
        ) {
            containerContent(isCollapsed)
            Box(
                modifier = Modifier
                    .height(containerHeight)
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = containerAlpha
                    }
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {}
        }

        AppBarLayout(
            colors = AppBarLayoutDefaults.transparentColors(),
            windowInsets = windowInsets
        ) {
            AppBar(
                title = {
                    title(isCollapsed)
                },
                navigationIcon = navigationIcon ?: {
                    NavigationIcon(tonalButton = !isCollapsed)
                },
                actions = {
                    actions(isCollapsed)
                }
            )
        }
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