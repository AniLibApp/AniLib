package com.revolgenx.anilib.common.ui.component.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.CorneredShape.Corner
import kotlin.math.ceil


typealias ChartEntryModel = List<Pair<Number, Number>>
typealias Marker = CartesianMarker

private object AdaptiveRangeProvider : CartesianLayerRangeProvider {
    override fun getMinY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
        return ceil(minY - yPadding(minY, maxY)).takeIf { it >= 0 } ?: 0.0
    }

    override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double {
        return ceil(maxY + yPadding(minY, maxY))
    }

    private fun yPadding(minY: Double, maxY: Double): Double {
        return ceil((maxY - minY) * 0.2)
    }
}

@Composable
fun LineChart(
    marker: Marker? = null,
    series: List<Pair<Number, Number>>,
    bottomAxisValueFormatter: ((value: Double) -> String)? = null,
    initialScroll: Scroll.Absolute = Scroll.Absolute.Start
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(series) {
        modelProducer.runTransaction {
            this.add(LineCartesianLayerModel.Partial(listOf(series.map {
                LineCartesianLayerModel.Entry(
                    it.first,
                    it.second
                )
            })))
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                rangeProvider = AdaptiveRangeProvider,
                lineProvider = LineCartesianLayer.LineProvider.series(
                    vicoTheme.lineCartesianLayerColors.map { color ->
                        LineCartesianLayer.rememberLine(
                            pointConnector = LineCartesianLayer.PointConnector.cubic(),
                            fill = LineCartesianLayer.LineFill.single(fill(color)),
                            areaFill = LineCartesianLayer.AreaFill.single(
                                fill(
                                    ShaderProvider.verticalGradient(
                                        colors = intArrayOf(
                                            color.copy(0.3f).toArgb(),
                                            color.copy(0.0f).toArgb()
                                        )
                                    )
                                )
                            )
                        )
                    }
                ),
            ),
            marker = marker,
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = bottomAxisValueFormatter?.let {
                CartesianValueFormatter { _, value, _ ->
                    bottomAxisValueFormatter(
                        value
                    )
                }
            } ?: CartesianValueFormatter.Default),
        ),
        modelProducer,
        scrollState = rememberVicoScrollState(initialScroll = initialScroll)
    )
}

@Composable
fun ColumnChart(
    marker: Marker? = null,
    series: List<Pair<Number, Number>>,
    bottomAxisValueFormatter: ((value: Double) -> String)? = null,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(series) {
        modelProducer.runTransaction {
            this.add(ColumnCartesianLayerModel.Partial(listOf(series.map {
                ColumnCartesianLayerModel.Entry(
                    it.first,
                    it.second
                )
            })))
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    vicoTheme.columnCartesianLayerColors.map { color ->
                        rememberLineComponent(
                            fill(color),
                            16.dp,
                            shape = CorneredShape(
                                topLeft = Corner.Rounded,
                                topRight = Corner.Rounded
                            )
                        )
                    }
                ),
            ),
            marker = marker,
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = bottomAxisValueFormatter?.let {
                CartesianValueFormatter { _, value, _ ->
                    bottomAxisValueFormatter(
                        value
                    )
                }
            } ?: CartesianValueFormatter.Default),        ),
        modelProducer,
    )
}

