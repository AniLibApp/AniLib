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


val AppIcons.IcVideo: ImageVector
    get() {
        if (_icVideo != null) {
            return _icVideo!!
        }
        _icVideo = materialIcon(name = "IcVideo") {
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
                moveTo(17.0F, 10.5F)
                verticalLineTo(7.0F)
                curveToRelative(0.0F, -0.55F, -0.45F, -1.0F, -1.0F, -1.0F)
                horizontalLineTo(4.0F)
                curveToRelative(-0.55F, 0.0F, -1.0F, 0.45F, -1.0F, 1.0F)
                verticalLineToRelative(10.0F)
                curveToRelative(0.0F, 0.55F, 0.45F, 1.0F, 1.0F, 1.0F)
                horizontalLineToRelative(12.0F)
                curveToRelative(0.55F, 0.0F, 1.0F, -0.45F, 1.0F, -1.0F)
                verticalLineToRelative(-3.5F)
                lineToRelative(4.0F, 4.0F)
                verticalLineToRelative(-11.0F)
                lineToRelative(-4.0F, 4.0F)
                close()
            }
        }
        return _icVideo!!
    }

private var _icVideo: ImageVector? = null
