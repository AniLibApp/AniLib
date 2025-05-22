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
import com.revolgenx.anilib.common.ui.icons.appicon.IcCompare

public val AppIcons.IcCompare: ImageVector
    get() {
        if (_icCompare != null) {
            return _icCompare!!
        }
        _icCompare =
            Builder(
                    name = "IcCompare",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 24.0.dp,
                    viewportWidth = 960.0f,
                    viewportHeight = 960.0f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFFe3e3e3)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(367.0f, 640.0f)
                        lineTo(120.0f, 640.0f)
                        quadToRelative(-17.0f, 0.0f, -28.5f, -11.5f)
                        reflectiveQuadTo(80.0f, 600.0f)
                        quadToRelative(0.0f, -17.0f, 11.5f, -28.5f)
                        reflectiveQuadTo(120.0f, 560.0f)
                        horizontalLineToRelative(247.0f)
                        lineToRelative(-75.0f, -75.0f)
                        quadToRelative(-11.0f, -11.0f, -11.0f, -27.5f)
                        reflectiveQuadToRelative(11.0f, -28.5f)
                        quadToRelative(12.0f, -12.0f, 28.5f, -12.0f)
                        reflectiveQuadToRelative(28.5f, 12.0f)
                        lineToRelative(143.0f, 143.0f)
                        quadToRelative(6.0f, 6.0f, 8.5f, 13.0f)
                        reflectiveQuadToRelative(2.5f, 15.0f)
                        quadToRelative(0.0f, 8.0f, -2.5f, 15.0f)
                        reflectiveQuadToRelative(-8.5f, 13.0f)
                        lineTo(348.0f, 772.0f)
                        quadToRelative(-12.0f, 12.0f, -28.0f, 11.5f)
                        reflectiveQuadTo(292.0f, 771.0f)
                        quadToRelative(-11.0f, -12.0f, -11.5f, -28.0f)
                        reflectiveQuadToRelative(11.5f, -28.0f)
                        lineToRelative(75.0f, -75.0f)
                        close()
                        moveTo(593.0f, 400.0f)
                        lineTo(668.0f, 475.0f)
                        quadToRelative(11.0f, 11.0f, 11.0f, 27.5f)
                        reflectiveQuadTo(668.0f, 531.0f)
                        quadToRelative(-12.0f, 12.0f, -28.5f, 12.0f)
                        reflectiveQuadTo(611.0f, 531.0f)
                        lineTo(468.0f, 388.0f)
                        quadToRelative(-6.0f, -6.0f, -8.5f, -13.0f)
                        reflectiveQuadToRelative(-2.5f, -15.0f)
                        quadToRelative(0.0f, -8.0f, 2.5f, -15.0f)
                        reflectiveQuadToRelative(8.5f, -13.0f)
                        lineToRelative(144.0f, -144.0f)
                        quadToRelative(12.0f, -12.0f, 28.0f, -11.5f)
                        reflectiveQuadToRelative(28.0f, 12.5f)
                        quadToRelative(11.0f, 12.0f, 11.5f, 28.0f)
                        reflectiveQuadTo(668.0f, 245.0f)
                        lineToRelative(-75.0f, 75.0f)
                        horizontalLineToRelative(247.0f)
                        quadToRelative(17.0f, 0.0f, 28.5f, 11.5f)
                        reflectiveQuadTo(880.0f, 360.0f)
                        quadToRelative(0.0f, 17.0f, -11.5f, 28.5f)
                        reflectiveQuadTo(840.0f, 400.0f)
                        lineTo(593.0f, 400.0f)
                        close()
                    }
                }
                .build()
        return _icCompare!!
    }

private var _icCompare: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = AppIcons.IcCompare, contentDescription = null)
    }
}
