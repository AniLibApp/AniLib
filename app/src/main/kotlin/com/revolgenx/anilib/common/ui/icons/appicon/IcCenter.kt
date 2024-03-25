package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcCenter: ImageVector
    get() {
        if (_icCenter != null) {
            return _icCenter!!
        }
        _icCenter = materialIcon(name = "IcCenter") {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 1.0F,
                strokeAlpha = 1.0F,
                strokeLineWidth = 0.0F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 4.0F,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(7.0F, 15.0F)
                verticalLineToRelative(2.0F)
                horizontalLineToRelative(10.0F)
                verticalLineToRelative(-2.0F)
                lineTo(7.0F, 15.0F)

                moveTo(3.0F, 21.0F)
                horizontalLineToRelative(18.0F)
                verticalLineToRelative(-2.0F)
                lineTo(3.0F, 19.0F)
                verticalLineToRelative(2.0F)

                moveTo(3.0F, 13.0F)
                horizontalLineToRelative(18.0F)
                verticalLineToRelative(-2.0F)
                lineTo(3.0F, 11.0F)
                verticalLineToRelative(2.0F)

                moveTo(7.0F, 7.0F)
                verticalLineToRelative(2.0F)
                horizontalLineToRelative(10.0F)
                lineTo(17.0F, 7.0F)
                lineTo(7.0F, 7.0F)

                moveTo(3.0F, 3.0F)
                verticalLineToRelative(2.0F)
                horizontalLineToRelative(18.0F)
                lineTo(21.0F, 3.0F)
                lineTo(3.0F, 3.0F)
                close()
            }
        }
        return _icCenter!!
    }

private var _icCenter: ImageVector? = null
