package com.revolgenx.anilib.common.ui.component.appbar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarLayout(
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: AppBarLayoutColors = AlAppBarLayoutDefaults.appBarLayoutColors(),
    containerHeight: Dp = AppBarHeight,
    content: @Composable () -> Unit
) {
    val topPadding = windowInsets.getTop(LocalDensity.current)
    val heightOffsetLimit =
        with(LocalDensity.current) { -(containerHeight.toPx() + topPadding) }
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

    Surface(modifier = Modifier.then(appBarDragModifier),color = appBarContainerColor) {
        val height = LocalDensity.current.run {
            containerHeight.toPx() + (scrollBehavior?.state?.heightOffset
                ?: 0f) + topPadding
        }
        TopAppBarContent(windowInsets = windowInsets, heightPx = height, content)
    }

}


@Composable
fun TopAppBarContent(windowInsets: WindowInsets, heightPx: Float, content: @Composable () -> Unit) {
    Layout(
        {
            Box(
                Modifier
                    .layoutId("appBarLayoutContent")
                    .windowInsetsPadding(windowInsets)
            ) {
                content()
            }
        }) { measurables, constraints ->
        val layoutHeight = heightPx.roundToInt()
        val appBarLayoutPlaceable =
            measurables.first { it.layoutId == "appBarLayoutContent" }
                .measure(constraints.copy(minWidth = 0))

        layout(constraints.maxWidth, layoutHeight) {
            appBarLayoutPlaceable.placeRelative(
                x = 0,
                y = (layoutHeight - appBarLayoutPlaceable.height) / 2
            )
        }
    }
}

@ExperimentalMaterial3Api
object AlAppBarLayoutDefaults {
    @Composable
    fun appBarLayoutColors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        scrolledContainerColor: Color = MaterialTheme.colorScheme.surface,
    ): AppBarLayoutColors =
        AppBarLayoutColors(
            containerColor,
            scrolledContainerColor
        )
}


@OptIn(ExperimentalMaterial3Api::class)
private suspend fun settleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
): Velocity {
    // Check if the app bar is completely collapsed/expanded. If so, no need to settle the app bar,
    // and just return Zero Velocity.
    // Note that we don't check for 0f due to float precision with the collapsedFraction
    // calculation.
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 &&
            state.heightOffset > state.heightOffsetLimit
        ) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) { state.heightOffset = value }
        }
    }

    return Velocity(0f, remainingVelocity)
}

@ExperimentalMaterial3Api
@Stable
class AppBarLayoutColors internal constructor(
    private val containerColor: Color,
    private val scrolledContainerColor: Color,
) {

    @Composable
    internal fun containerColor(colorTransitionFraction: Float): Color {
        return lerp(
            containerColor,
            scrolledContainerColor,
            FastOutLinearInEasing.transform(colorTransitionFraction)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is AppBarLayoutColors) return false

        if (containerColor != other.containerColor) return false
        if (scrolledContainerColor != other.scrolledContainerColor) return false
        return true
    }

    override fun hashCode(): Int {
        var result = containerColor.hashCode()
        result = 31 * result + scrolledContainerColor.hashCode()

        return result
    }
}

internal fun ColorScheme.applyTonalElevation(backgroundColor: Color, elevation: Dp): Color {
    return if (backgroundColor == surface) {
        surfaceColorAtElevation(elevation)
    } else {
        backgroundColor
    }
}