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

public val AppIcons.IcAssignment: ImageVector
    get() {
        if (_icAssignment != null) {
            return _icAssignment!!
        }
        _icAssignment = Builder(
            name = "IcAssignment", 
            defaultWidth = 24.0.dp, 
            defaultHeight = 24.0.dp, 
            viewportWidth = 24.0f, 
            viewportHeight = 24.0f
            ).apply {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(19.0f, 3.0f)
                    horizontalLineToRelative(-4.18f)
                    curveTo(14.4f, 1.84f, 13.3f, 1.0f, 12.0f, 1.0f)
                    curveToRelative(-1.3f, 0.0f, -2.4f, 0.84f, -2.82f, 2.0f)
                    horizontalLineTo(5.0f)
                    curveTo(3.9f, 3.0f, 3.0f, 3.9f, 3.0f, 5.0f)
                    verticalLineToRelative(14.0f)
                    curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                    horizontalLineToRelative(14.0f)
                    curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                    verticalLineTo(5.0f)
                    curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                    close()
                    moveToRelative(-7.0f, 0.0f)
                    curveToRelative(0.55f, 0.0f, 1.0f, 0.45f, 1.0f, 1.0f)
                    reflectiveCurveToRelative(-0.45f, 1.0f, -1.0f, 1.0f)
                    reflectiveCurveToRelative(-1.0f, -0.45f, -1.0f, -1.0f)
                    reflectiveCurveToRelative(0.45f, -1.0f, 1.0f, -1.0f)
                    close()
                    moveToRelative(2.0f, 14.0f)
                    horizontalLineTo(7.0f)
                    verticalLineToRelative(-2.0f)
                    horizontalLineToRelative(7.0f)
                    verticalLineToRelative(2.0f)
                    close()
                    moveToRelative(3.0f, -4.0f)
                    horizontalLineTo(7.0f)
                    verticalLineToRelative(-2.0f)
                    horizontalLineToRelative(10.0f)
                    verticalLineToRelative(2.0f)
                    close()
                    moveToRelative(0.0f, -4.0f)
                    horizontalLineTo(7.0f)
                    verticalLineTo(7.0f)
                    horizontalLineToRelative(10.0f)
                    verticalLineToRelative(2.0f)
                    close()
                }
            }
            .build()
            return _icAssignment!!
        }

    private var _icAssignment: ImageVector? = null

    @Preview
    @Composable
    private fun Preview() {
        Box(modifier = Modifier.padding(12.dp)) {
            Image(imageVector = AppIcons.IcAssignment, contentDescription = "")
        }
    }
