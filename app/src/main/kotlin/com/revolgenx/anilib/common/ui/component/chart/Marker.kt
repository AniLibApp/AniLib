package com.revolgenx.anilib.common.ui.component.chart

import android.text.Layout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shape.CorneredShape

@Composable
internal fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(),
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = markerCorneredShape(CorneredShape.Corner.Rounded)
    val labelBackground =
        rememberShapeComponent(
            fill = fill(MaterialTheme.colorScheme.background),
            shape = labelBackgroundShape,
            strokeThickness = 1.dp,
            strokeFill = fill(MaterialTheme.colorScheme.outline),
        )
    val label =
        rememberTextComponent(
            color = MaterialTheme.colorScheme.onSurface,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            padding = insets(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val indicatorFrontComponent =
        rememberShapeComponent(fill(MaterialTheme.colorScheme.surface), CorneredShape.Pill)
    val guideline = rememberAxisGuidelineComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator =
            if (showIndicator) {
                { color ->
                    LayeredComponent(
                        back = ShapeComponent(fill(color.copy(alpha = 0.15f)), CorneredShape.Pill),
                        front =
                            LayeredComponent(
                                back = ShapeComponent(fill = fill(color), shape = CorneredShape.Pill),
                                front = indicatorFrontComponent,
                                padding = insets(5.dp),
                            ),
                        padding = insets(10.dp),
                    )
                }
            } else {
                null
            },
        indicatorSize = 36.dp,
        guideline = guideline,
    )
}

//@Composable
//internal fun rememberMarker(mLabelFormatter: MarkerLabelFormatter? = null): Marker {
//    val surfaceContainer = MaterialTheme.colorScheme.surfaceContainerLowest
//    val labelBackground = remember(surfaceContainer) {
//        ShapeComponent(labelBackgroundShape, surfaceContainer.toArgb()).setShadow(
//            radius = LABEL_BACKGROUND_SHADOW_RADIUS,
//            dy = LABEL_BACKGROUND_SHADOW_DY,
//            applyElevationOverlay = true,
//        )
//    }
//
//    val context = localContext()
//
//    val label = textComponent(
//        background = labelBackground,
//        lineCount = LABEL_LINE_COUNT,
//        padding = labelPadding,
//        typeface = ResourcesCompat.getFont(context, R.font.inter_regular),
//    )
//    val indicatorInnerComponent = shapeComponent(Shapes.pillShape, surfaceContainer)
//    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color.White)
//    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color.White)
//    val indicator = overlayingComponent(
//        outer = indicatorOuterComponent,
//        inner = overlayingComponent(
//            outer = indicatorCenterComponent,
//            inner = indicatorInnerComponent,
//            innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue,
//        ),
//        innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue,
//    )
//    val guideline = lineComponent(
//        MaterialTheme.colorScheme.onSurface.copy(GUIDELINE_ALPHA),
//        guidelineThickness,
//        guidelineShape,
//    )
//    return remember(label, indicator, guideline) {
//        object : MarkerComponent(label, indicator, guideline) {
//            init {
//                labelFormatter = mLabelFormatter ?: DefaultMarkerLabelFormatter
//                indicatorSizeDp = INDICATOR_SIZE_DP
//                onApplyEntryColor = { entryColor ->
//                    indicatorOuterComponent.color = entryColor.copyColor(INDICATOR_OUTER_COMPONENT_ALPHA)
//                    with(indicatorCenterComponent) {
//                        color = entryColor
//                        setShadow(radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS, color = entryColor)
//                    }
//                }
//            }
//
//            override fun getInsets(
//                context: MeasureContext,
//                outInsets: Insets,
//                horizontalDimensions: HorizontalDimensions
//            ) = with(context) {
//                outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
//                        LABEL_BACKGROUND_SHADOW_RADIUS.pixels * SHADOW_RADIUS_MULTIPLIER -
//                        LABEL_BACKGROUND_SHADOW_DY.pixels
//            }
//        }
//    }
//}
//
//
//private object DefaultMarkerLabelFormatter : MarkerLabelFormatter {
//    override fun getLabel(
//        markedEntries: List<Marker.EntryModel>,
//        chartValues: ChartValues
//    ): CharSequence = markedEntries.transformToSpannable(
//        prefix = if (markedEntries.size > 1) markedEntries.sumOf { it.entry.y }.toInt()
//            .toString() + " (" else "",
//        postfix = if (markedEntries.size > 1) ")" else "",
//        separator = "; ",
//    ) { model ->
//        appendCompat(
//            model.entry.y.toInt().toString(),
//            ForegroundColorSpan(model.color),
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
//        )
//    }
//}
//
//
//private const val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
//private const val LABEL_BACKGROUND_SHADOW_DY = 2f
//private const val LABEL_LINE_COUNT = 1
//private const val GUIDELINE_ALPHA = .2f
//private const val INDICATOR_SIZE_DP = 36f
//private const val INDICATOR_OUTER_COMPONENT_ALPHA = 32
//private const val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
//private const val GUIDELINE_DASH_LENGTH_DP = 8f
//private const val GUIDELINE_GAP_LENGTH_DP = 4f
//private const val SHADOW_RADIUS_MULTIPLIER = 1.3f
//
//private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
//private val labelHorizontalPaddingValue = 8.dp
//private val labelVerticalPaddingValue = 4.dp
//private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
//private val indicatorInnerAndCenterComponentPaddingValue = 5.dp
//private val indicatorCenterAndOuterComponentPaddingValue = 10.dp
//private val guidelineThickness = 2.dp
//private val guidelineShape =
//    DashedShape(Shapes.pillShape, GUIDELINE_DASH_LENGTH_DP, GUIDELINE_GAP_LENGTH_DP)