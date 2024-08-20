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

public val AppIcons.IcPublic: ImageVector
    get() {
        if (_icPublic != null) {
            return _icPublic!!
        }
        _icPublic = Builder(
            name = "IcPublic", 
            defaultWidth = 24.0.dp, 
            defaultHeight = 24.0.dp, 
            viewportWidth = 960.0f, 
            viewportHeight = 960.0f
            ).apply {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(480.0f, 880.0f)
                    quadToRelative(-83.0f, 0.0f, -156.0f, -31.5f)
                    reflectiveQuadTo(197.0f, 763.0f)
                    quadToRelative(-54.0f, -54.0f, -85.5f, -127.0f)
                    reflectiveQuadTo(80.0f, 480.0f)
                    quadToRelative(0.0f, -83.0f, 31.5f, -156.0f)
                    reflectiveQuadTo(197.0f, 197.0f)
                    quadToRelative(54.0f, -54.0f, 127.0f, -85.5f)
                    reflectiveQuadTo(480.0f, 80.0f)
                    quadToRelative(83.0f, 0.0f, 156.0f, 31.5f)
                    reflectiveQuadTo(763.0f, 197.0f)
                    quadToRelative(54.0f, 54.0f, 85.5f, 127.0f)
                    reflectiveQuadTo(880.0f, 480.0f)
                    quadToRelative(0.0f, 83.0f, -31.5f, 156.0f)
                    reflectiveQuadTo(763.0f, 763.0f)
                    quadToRelative(-54.0f, 54.0f, -127.0f, 85.5f)
                    reflectiveQuadTo(480.0f, 880.0f)
                    close()
                    moveToRelative(-40.0f, -82.0f)
                    verticalLineToRelative(-78.0f)
                    quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                    reflectiveQuadTo(360.0f, 640.0f)
                    verticalLineToRelative(-40.0f)
                    lineTo(168.0f, 408.0f)
                    quadToRelative(-3.0f, 18.0f, -5.5f, 36.0f)
                    reflectiveQuadToRelative(-2.5f, 36.0f)
                    quadToRelative(0.0f, 121.0f, 79.5f, 212.0f)
                    reflectiveQuadTo(440.0f, 798.0f)
                    close()
                    moveToRelative(276.0f, -102.0f)
                    quadToRelative(20.0f, -22.0f, 36.0f, -47.5f)
                    reflectiveQuadToRelative(26.5f, -53.0f)
                    quadToRelative(10.5f, -27.5f, 16.0f, -56.5f)
                    reflectiveQuadToRelative(5.5f, -59.0f)
                    quadToRelative(0.0f, -98.0f, -54.5f, -179.0f)
                    reflectiveQuadTo(600.0f, 184.0f)
                    verticalLineToRelative(16.0f)
                    quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
                    reflectiveQuadTo(520.0f, 280.0f)
                    horizontalLineToRelative(-80.0f)
                    verticalLineToRelative(80.0f)
                    quadToRelative(0.0f, 17.0f, -11.5f, 28.5f)
                    reflectiveQuadTo(400.0f, 400.0f)
                    horizontalLineToRelative(-80.0f)
                    verticalLineToRelative(80.0f)
                    horizontalLineToRelative(240.0f)
                    quadToRelative(17.0f, 0.0f, 28.5f, 11.5f)
                    reflectiveQuadTo(600.0f, 520.0f)
                    verticalLineToRelative(120.0f)
                    horizontalLineToRelative(40.0f)
                    quadToRelative(26.0f, 0.0f, 47.0f, 15.5f)
                    reflectiveQuadToRelative(29.0f, 40.5f)
                    close()
                }
            }
            .build()
            return _icPublic!!
        }

    private var _icPublic: ImageVector? = null

    @Preview
    @Composable
    private fun Preview() {
        Box(modifier = Modifier.padding(12.dp)) {
            Image(imageVector = AppIcons.IcPublic, contentDescription = "")
        }
    }
