package com.revolgenx.anilib.common.ui.component.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun TabContent(
    horizontalPadding: Dp = 8.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    text: @Composable (() -> Unit)? = null
) {
    val styledText: @Composable (() -> Unit)? = text?.let {
        @Composable {
            val style = textStyle.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(style, content = text)
        }
    }

    Layout(
        {
            if (styledText != null) {
                Box(
                    Modifier
                        .layoutId("text")
                        .padding(horizontal = horizontalPadding)
                ) { styledText() }
            }
        }
    ) { measurables, constraints ->
        val textPlaceable = styledText?.let {
            measurables.first { it.layoutId == "text" }.measure(
                // Measure with loose constraints for height as we don't want the text to take up more
                // space than it needs
                constraints.copy(minHeight = 0)
            )
        }


        val tabWidth = textPlaceable?.width ?: 0

        val specHeight = SmallTabHeight.roundToPx()

        val tabHeight = max(
            specHeight, textPlaceable?.height ?: 0
        )

        layout(tabWidth, tabHeight) {
            when {
                textPlaceable != null -> placeTextOrIcon(textPlaceable, tabHeight)
                else -> {
                }
            }
        }
    }
}

private fun Placeable.PlacementScope.placeTextOrIcon(
    textOrIconPlaceable: Placeable,
    tabHeight: Int
) {
    val contentY = (tabHeight - textOrIconPlaceable.height) / 2
    textOrIconPlaceable.placeRelative(0, contentY)
}


private val SmallTabHeight = 48.dp
private val LargeTabHeight = 72.dp

