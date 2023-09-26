package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

public val AppIcons.IcPersonCheckOutline: ImageVector
    get() {
        if (_icPersonCheckOutline != null) {
            return _icPersonCheckOutline!!
        }
        _icPersonCheckOutline = Builder(
            name = "IcPersonCheckOutline", defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = EvenOdd
            ) {
                moveTo(16.5f, 21.0f)
                curveTo(16.5f, 21.0f, 18.0f, 21.0f, 18.0f, 19.5f)
                curveTo(18.0f, 18.0f, 16.5f, 13.5f, 9.0f, 13.5f)
                curveTo(1.5f, 13.5f, 0.0f, 18.0f, 0.0f, 19.5f)
                curveTo(0.0f, 21.0f, 1.5f, 21.0f, 1.5f, 21.0f)
                close()
                moveTo(1.504f, 19.586f)
                close()
                moveTo(1.531f, 19.5f)
                lineTo(16.469f, 19.5f)
                lineTo(16.5f, 19.492f)
                curveTo(16.5f, 19.121f, 16.27f, 18.008f, 15.254f, 16.996f)
                curveTo(14.273f, 16.016f, 12.43f, 15.0f, 9.0f, 15.0f)
                curveTo(5.563f, 15.0f, 3.727f, 16.016f, 2.746f, 16.996f)
                curveTo(1.73f, 18.016f, 1.504f, 19.125f, 1.5f, 19.496f)
                close()
                moveTo(16.496f, 19.586f)
                close()
                moveTo(9.0f, 10.5f)
                curveTo(10.656f, 10.5f, 12.0f, 9.16f, 12.0f, 7.5f)
                curveTo(12.0f, 5.84f, 10.656f, 4.5f, 9.0f, 4.5f)
                curveTo(7.34f, 4.5f, 6.0f, 5.84f, 6.0f, 7.5f)
                curveTo(6.0f, 9.16f, 7.34f, 10.5f, 9.0f, 10.5f)
                close()
                moveTo(13.5f, 7.5f)
                curveTo(13.5f, 9.984f, 11.484f, 12.0f, 9.0f, 12.0f)
                curveTo(6.516f, 12.0f, 4.5f, 9.984f, 4.5f, 7.5f)
                curveTo(4.5f, 5.016f, 6.516f, 3.0f, 9.0f, 3.0f)
                curveTo(11.484f, 3.0f, 13.5f, 5.016f, 13.5f, 7.5f)
                close()
                moveTo(23.781f, 7.719f)
                curveTo(24.074f, 8.012f, 24.074f, 8.488f, 23.781f, 8.781f)
                lineTo(19.281f, 13.281f)
                curveTo(18.988f, 13.57f, 18.512f, 13.574f, 18.219f, 13.281f)
                lineTo(15.969f, 11.031f)
                curveTo(15.68f, 10.734f, 15.68f, 10.262f, 15.969f, 9.965f)
                curveTo(16.262f, 9.676f, 16.738f, 9.676f, 17.031f, 9.965f)
                lineTo(18.75f, 11.688f)
                lineTo(22.719f, 7.715f)
                curveTo(23.012f, 7.426f, 23.484f, 7.426f, 23.781f, 7.719f)
                close()
                moveTo(23.781f, 7.719f)
            }
        }
            .build()
        return _icPersonCheckOutline!!
    }

private var _icPersonCheckOutline: ImageVector? = null
