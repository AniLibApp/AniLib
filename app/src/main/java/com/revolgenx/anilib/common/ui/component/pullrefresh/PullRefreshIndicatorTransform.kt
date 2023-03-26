package com.revolgenx.anilib.common.ui.component.pullrefresh


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.debugInspectorInfo

/**
 * A modifier for translating the position and scaling the size of a pull-to-refresh indicator
 * based on the given [PullRefreshState].
 *
 * @sample androidx.compose.material.samples.PullRefreshIndicatorTransformSample
 *
 * @param state The [PullRefreshState] which determines the position of the indicator.
 * @param scale A boolean controlling whether the indicator's size scales with pull progress or not.
 */
// TODO: Consider whether the state parameter should be replaced with lambdas.
fun Modifier.pullRefreshIndicatorTransform(
    state: PullRefreshState,
    scale: Boolean = false,
) = composed(inspectorInfo = debugInspectorInfo {
    name = "pullRefreshIndicatorTransform"
    properties["state"] = state
    properties["scale"] = scale
}) {
    var height by remember { mutableStateOf(0) }

    Modifier
        .onSizeChanged { height = it.height }
        .graphicsLayer {
            translationY = state.position - height

            if (scale && !state.refreshing) {
                val scaleFraction = LinearOutSlowInEasing
                    .transform(state.position / state.threshold)
                    .coerceIn(0f, 1f)
                scaleX = scaleFraction
                scaleY = scaleFraction
            }
        }
}