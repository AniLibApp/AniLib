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

public val AppIcons.IcPlay: ImageVector
    get() {
        if (_icPlay != null) {
            return _icPlay!!
        }
        _icPlay = Builder(
            name = "IcPlay", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(383.0f, 760.0f)
                quadTo(351.0f, 780.0f, 318.5f, 762.5f)
                quadTo(286.0f, 745.0f, 286.0f, 707.0f)
                lineTo(286.0f, 253.0f)
                quadTo(286.0f, 215.0f, 318.5f, 197.5f)
                quadTo(351.0f, 180.0f, 383.0f, 200.0f)
                lineTo(739.0f, 427.0f)
                quadTo(768.0f, 446.0f, 768.0f, 480.5f)
                quadTo(768.0f, 515.0f, 739.0f, 533.0f)
                lineTo(383.0f, 760.0f)
                close()
            }
        }
            .build()
        return _icPlay!!
    }

private var _icPlay: ImageVector? = null
