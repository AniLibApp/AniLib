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

public val AppIcons.IcMedia: ImageVector
    get() {
        if (_icMedia != null) {
            return _icMedia!!
        }
        _icMedia = Builder(
            name = "IcMedia", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(167.0f, 169.0f)
                lineTo(243.5f, 323.0f)
                lineTo(361.5f, 323.0f)
                lineTo(285.0f, 169.0f)
                lineTo(363.5f, 169.0f)
                lineTo(440.5f, 323.0f)
                lineTo(558.5f, 323.0f)
                lineTo(482.0f, 169.0f)
                lineTo(560.5f, 169.0f)
                lineTo(637.5f, 323.0f)
                lineTo(755.5f, 323.0f)
                lineTo(679.0f, 169.0f)
                lineTo(795.0f, 169.0f)
                quadTo(826.0f, 169.0f, 848.0f, 191.0f)
                quadTo(870.0f, 213.0f, 870.0f, 244.0f)
                lineTo(870.0f, 716.0f)
                quadTo(870.0f, 747.0f, 848.0f, 769.0f)
                quadTo(826.0f, 791.0f, 795.0f, 791.0f)
                lineTo(165.0f, 791.0f)
                quadTo(134.0f, 791.0f, 112.0f, 769.0f)
                quadTo(90.0f, 747.0f, 90.0f, 716.0f)
                lineTo(90.0f, 244.0f)
                quadTo(90.0f, 211.5f, 112.75f, 190.25f)
                quadTo(135.5f, 169.0f, 167.0f, 169.0f)
                lineTo(167.0f, 169.0f)
                close()
            }
        }
            .build()
        return _icMedia!!
    }

private var _icMedia: ImageVector? = null
