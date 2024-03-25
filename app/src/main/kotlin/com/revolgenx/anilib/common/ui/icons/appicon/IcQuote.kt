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

val AppIcons.IcQuote: ImageVector
    get() {
        if (_icQuote != null) {
            return _icQuote!!
        }
        _icQuote = materialIcon(name = "IcQuote") {
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
                moveTo(6.0F, 17.0F)
                horizontalLineToRelative(3.0F)
                lineToRelative(2.0F, -4.0F)
                lineTo(11.0F, 7.0F)
                lineTo(5.0F, 7.0F)
                verticalLineToRelative(6.0F)
                horizontalLineToRelative(3.0F)

                moveTo(14.0F, 17.0F)
                horizontalLineToRelative(3.0F)
                lineToRelative(2.0F, -4.0F)
                lineTo(19.0F, 7.0F)
                horizontalLineToRelative(-6.0F)
                verticalLineToRelative(6.0F)
                horizontalLineToRelative(3.0F)
                close()
            }
        }
        return _icQuote!!
    }

private var _icQuote: ImageVector? = null
