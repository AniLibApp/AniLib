package com.revolgenx.anilib.common.ui.component.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.AxisRenderer
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.scroll.InitialScroll

@Composable
fun ColumnChart(
    marker: Marker,
    model: ChartEntryModel,
    bottomAxis: AxisRenderer<AxisPosition.Horizontal.Bottom> = rememberBottomAxis()
) {
    Chart(
        marker = marker,
        chart = columnChart(
            spacing = 12.dp,
            innerSpacing = 2.dp
        ),
        model = model,
        startAxis = rememberStartAxis(
            valueFormatter = { value, _ -> value.toInt().toString() }
        ),
        bottomAxis = bottomAxis
    )
}

@Composable
fun LineChart(
    marker: Marker,
    model: ChartEntryModel,
    startAxis: AxisRenderer<AxisPosition.Vertical.Start> = rememberStartAxis(
        valueFormatter = { value, _ -> value.toInt().toString() }
    ),
    bottomAxis: AxisRenderer<AxisPosition.Horizontal.Bottom> = rememberBottomAxis(),
    spacing: Dp = currentChartStyle.lineChart.spacing,
    initialScroll: InitialScroll = InitialScroll.Start,
) {
    Chart(
        marker = marker,
        chart = lineChart(
            spacing = spacing,
            axisValuesOverrider = AxisValuesOverrider.adaptiveYValues(yFraction = 1.2f)
        ),
        model = model,
        startAxis = startAxis,
        bottomAxis = bottomAxis,
        chartScrollSpec = rememberChartScrollSpec(initialScroll = initialScroll)
    )
}

