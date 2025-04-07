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

val AppIcons.IcPencil: ImageVector
    get() {
        if (_icPencil != null) {
            return _icPencil!!
        }
        _icPencil = materialIcon(name = "IcPencil") {
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
                moveTo(3.0F, 17.25F)
                verticalLineTo(21.0F)
                horizontalLineToRelative(3.75F)
                lineTo(17.81F, 9.94F)
                lineToRelative(-3.75F, -3.75F)
                lineTo(3.0F, 17.25F)

                moveTo(20.71F, 7.04F)
                curveToRelative(0.39F, -0.39F, 0.39F, -1.02F, 0.0F, -1.41F)
                lineToRelative(-2.34F, -2.34F)
                curveToRelative(-0.39F, -0.39F, -1.02F, -0.39F, -1.41F, 0.0F)
                lineToRelative(-1.83F, 1.83F)
                lineToRelative(3.75F, 3.75F)
                lineToRelative(1.83F, -1.83F)
                close()
            }
        }
        return _icPencil!!
    }

private var _icPencil: ImageVector? = null