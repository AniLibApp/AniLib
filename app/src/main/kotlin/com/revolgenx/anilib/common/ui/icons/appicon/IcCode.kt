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


val AppIcons.IcCode: ImageVector
    get() {
        if (_icCode != null) {
            return _icCode!!
        }
        _icCode = materialIcon(name = "IcCode") {
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
                moveTo(9.4F, 16.6F)
                lineTo(4.8F, 12.0F)
                lineToRelative(4.6F, -4.6F)
                lineTo(8.0F, 6.0F)
                lineToRelative(-6.0F, 6.0F)
                lineToRelative(6.0F, 6.0F)
                lineToRelative(1.4F, -1.4F)

                moveTo(14.6F, 16.6F)
                lineToRelative(4.6F, -4.6F)
                lineToRelative(-4.6F, -4.6F)
                lineTo(16.0F, 6.0F)
                lineToRelative(6.0F, 6.0F)
                lineToRelative(-6.0F, 6.0F)
                lineToRelative(-1.4F, -1.4F)
                close()
            }
        }
        return _icCode!!
    }

private var _icCode: ImageVector? = null
