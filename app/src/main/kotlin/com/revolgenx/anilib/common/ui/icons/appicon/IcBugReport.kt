package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

public val AppIcons.IcBugReport: ImageVector
    get() {
        if (_icBugReport != null) {
            return _icBugReport!!
        }
        _icBugReport = Builder(
            name = "IcBugReport", 
            defaultWidth = 24.0.dp, 
            defaultHeight = 24.0.dp, 
            viewportWidth = 24.0f, 
            viewportHeight = 24.0f
            ).apply {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(19.0f, 8.0f)
                    horizontalLineToRelative(-1.81f)
                    curveToRelative(-0.45f, -0.78f, -1.07f, -1.45f, -1.82f, -1.96f)
                    lineToRelative(0.93f, -0.93f)
                    curveToRelative(0.39f, -0.39f, 0.39f, -1.02f, 0.0f, -1.41f)
                    curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                    lineToRelative(-1.47f, 1.47f)
                    curveTo(12.96f, 5.06f, 12.49f, 5.0f, 12.0f, 5.0f)
                    reflectiveCurveToRelative(-0.96f, 0.06f, -1.41f, 0.17f)
                    lineTo(9.11f, 3.7f)
                    curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                    curveToRelative(-0.39f, 0.39f, -0.39f, 1.02f, 0.0f, 1.41f)
                    lineToRelative(0.92f, 0.93f)
                    curveTo(7.88f, 6.55f, 7.26f, 7.22f, 6.81f, 8.0f)
                    horizontalLineTo(5.0f)
                    curveTo(4.45f, 8.0f, 4.0f, 8.45f, 4.0f, 9.0f)
                    reflectiveCurveToRelative(0.45f, 1.0f, 1.0f, 1.0f)
                    horizontalLineToRelative(1.09f)
                    curveTo(6.04f, 10.33f, 6.0f, 10.66f, 6.0f, 11.0f)
                    verticalLineToRelative(1.0f)
                    horizontalLineTo(5.0f)
                    curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                    reflectiveCurveToRelative(0.45f, 1.0f, 1.0f, 1.0f)
                    horizontalLineToRelative(1.0f)
                    verticalLineToRelative(1.0f)
                    curveToRelative(0.0f, 0.34f, 0.04f, 0.67f, 0.09f, 1.0f)
                    horizontalLineTo(5.0f)
                    curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                    reflectiveCurveToRelative(0.45f, 1.0f, 1.0f, 1.0f)
                    horizontalLineToRelative(1.81f)
                    curveToRelative(1.04f, 1.79f, 2.97f, 3.0f, 5.19f, 3.0f)
                    reflectiveCurveToRelative(4.15f, -1.21f, 5.19f, -3.0f)
                    horizontalLineTo(19.0f)
                    curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                    reflectiveCurveToRelative(-0.45f, -1.0f, -1.0f, -1.0f)
                    horizontalLineToRelative(-1.09f)
                    curveToRelative(0.05f, -0.33f, 0.09f, -0.66f, 0.09f, -1.0f)
                    verticalLineToRelative(-1.0f)
                    horizontalLineToRelative(1.0f)
                    curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                    reflectiveCurveToRelative(-0.45f, -1.0f, -1.0f, -1.0f)
                    horizontalLineToRelative(-1.0f)
                    verticalLineToRelative(-1.0f)
                    curveToRelative(0.0f, -0.34f, -0.04f, -0.67f, -0.09f, -1.0f)
                    horizontalLineTo(19.0f)
                    curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                    reflectiveCurveToRelative(-0.45f, -1.0f, -1.0f, -1.0f)
                    close()
                    moveToRelative(-6.0f, 8.0f)
                    horizontalLineToRelative(-2.0f)
                    curveToRelative(-0.55f, 0.0f, -1.0f, -0.45f, -1.0f, -1.0f)
                    reflectiveCurveToRelative(0.45f, -1.0f, 1.0f, -1.0f)
                    horizontalLineToRelative(2.0f)
                    curveToRelative(0.55f, 0.0f, 1.0f, 0.45f, 1.0f, 1.0f)
                    reflectiveCurveToRelative(-0.45f, 1.0f, -1.0f, 1.0f)
                    close()
                    moveToRelative(0.0f, -4.0f)
                    horizontalLineToRelative(-2.0f)
                    curveToRelative(-0.55f, 0.0f, -1.0f, -0.45f, -1.0f, -1.0f)
                    reflectiveCurveToRelative(0.45f, -1.0f, 1.0f, -1.0f)
                    horizontalLineToRelative(2.0f)
                    curveToRelative(0.55f, 0.0f, 1.0f, 0.45f, 1.0f, 1.0f)
                    reflectiveCurveToRelative(-0.45f, 1.0f, -1.0f, 1.0f)
                    close()
                }
            }
            .build()
            return _icBugReport!!
        }

    private var _icBugReport: ImageVector? = null

    @Preview
    @Composable
    private fun Preview() {
        Box(modifier = Modifier.padding(12.dp)) {
            Image(imageVector = AppIcons.IcBugReport, contentDescription = "")
        }
    }
