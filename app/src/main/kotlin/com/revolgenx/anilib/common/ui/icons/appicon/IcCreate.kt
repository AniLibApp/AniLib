package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

public val AppIcons.IcCreate: ImageVector
    get() {
        if (_icCreate != null) {
            return _icCreate!!
        }
        _icCreate = Builder(
            name = "IcCreate", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(120.0f, 611.0f)
                quadToRelative(0.0f, 29.0f, 20.0f, 45.0f)
                reflectiveQuadToRelative(66.0f, 21.0f)
                quadToRelative(16.0f, 2.0f, 25.5f, 14.5f)
                reflectiveQuadTo(240.0f, 720.0f)
                quadToRelative(-1.0f, 17.0f, -12.0f, 28.0f)
                reflectiveQuadToRelative(-27.0f, 9.0f)
                quadToRelative(-81.0f, -10.0f, -121.0f, -46.5f)
                reflectiveQuadTo(40.0f, 611.0f)
                quadToRelative(0.0f, -65.0f, 53.5f, -105.5f)
                reflectiveQuadTo(242.0f, 457.0f)
                quadToRelative(39.0f, -3.0f, 58.5f, -12.5f)
                reflectiveQuadTo(320.0f, 418.0f)
                quadToRelative(0.0f, -22.0f, -21.0f, -34.5f)
                reflectiveQuadTo(230.0f, 364.0f)
                quadToRelative(-16.0f, -2.0f, -25.5f, -15.0f)
                reflectiveQuadToRelative(-7.5f, -29.0f)
                quadToRelative(2.0f, -17.0f, 14.0f, -27.5f)
                reflectiveQuadToRelative(28.0f, -8.5f)
                quadToRelative(83.0f, 12.0f, 122.0f, 44.5f)
                reflectiveQuadToRelative(39.0f, 89.5f)
                quadToRelative(0.0f, 53.0f, -38.5f, 83.0f)
                reflectiveQuadTo(248.0f, 537.0f)
                quadToRelative(-64.0f, 5.0f, -96.0f, 23.5f)
                reflectiveQuadTo(120.0f, 611.0f)
                close()
                moveTo(555.0f, 730.0f)
                lineTo(390.0f, 565.0f)
                lineToRelative(345.0f, -345.0f)
                quadToRelative(20.0f, -20.0f, 47.5f, -20.0f)
                reflectiveQuadToRelative(47.5f, 20.0f)
                lineToRelative(70.0f, 70.0f)
                quadToRelative(20.0f, 20.0f, 20.0f, 47.5f)
                reflectiveQuadTo(900.0f, 385.0f)
                lineTo(555.0f, 730.0f)
                close()
                moveTo(359.0f, 800.0f)
                quadToRelative(-17.0f, 4.0f, -30.0f, -9.0f)
                reflectiveQuadToRelative(-9.0f, -30.0f)
                lineToRelative(31.0f, -151.0f)
                lineToRelative(158.0f, 158.0f)
                lineToRelative(-150.0f, 32.0f)
                close()
            }
        }
            .build()
        return _icCreate!!
    }

private var _icCreate: ImageVector? = null
