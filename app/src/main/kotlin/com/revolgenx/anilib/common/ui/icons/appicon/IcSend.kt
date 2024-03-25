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

val AppIcons.IcSend: ImageVector
    get() {
        if (_icSend != null) {
            return _icSend!!
        }
        _icSend = materialIcon(name = "IcSend") {
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
                moveTo(2.01F, 21.0F)
                lineTo(23.0F, 12.0F)
                lineTo(2.01F, 3.0F)
                lineTo(2.0F, 10.0F)
                lineToRelative(15.0F, 2.0F)
                lineToRelative(-15.0F, 2.0F)
                close()
            }
        }
        return _icSend!!
    }

private var _icSend: ImageVector? = null