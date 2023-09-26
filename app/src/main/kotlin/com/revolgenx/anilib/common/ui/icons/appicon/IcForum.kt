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

public val AppIcons.IcForum: ImageVector
    get() {
        if (_icForum != null) {
            return _icForum!!
        }
        _icForum = Builder(
            name = "IcForum", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(91.0f, 623.5f)
                lineTo(91.0f, 128.0f)
                quadTo(91.0f, 112.0f, 102.5f, 100.5f)
                quadTo(114.0f, 89.0f, 130.0f, 89.0f)
                lineTo(636.0f, 89.0f)
                quadTo(652.0f, 89.0f, 663.5f, 100.5f)
                quadTo(675.0f, 112.0f, 675.0f, 128.0f)
                lineTo(675.0f, 476.0f)
                quadTo(675.0f, 492.0f, 663.5f, 503.5f)
                quadTo(652.0f, 515.0f, 636.0f, 515.0f)
                lineTo(245.0f, 515.0f)
                lineTo(123.5f, 636.5f)
                quadTo(114.5f, 645.5f, 102.75f, 641.0f)
                quadTo(91.0f, 636.5f, 91.0f, 623.5f)
                close()
                moveTo(284.0f, 709.0f)
                quadTo(268.0f, 709.0f, 256.5f, 697.5f)
                quadTo(245.0f, 686.0f, 245.0f, 670.0f)
                lineTo(245.0f, 590.0f)
                lineTo(750.0f, 590.0f)
                lineTo(750.0f, 590.0f)
                lineTo(750.0f, 243.0f)
                lineTo(830.0f, 243.0f)
                quadTo(846.0f, 243.0f, 857.5f, 254.5f)
                quadTo(869.0f, 266.0f, 869.0f, 282.0f)
                lineTo(869.0f, 817.5f)
                quadTo(869.0f, 830.5f, 857.25f, 835.0f)
                quadTo(845.5f, 839.5f, 836.5f, 830.5f)
                lineTo(715.0f, 709.0f)
                lineTo(284.0f, 709.0f)
                close()
            }
        }
            .build()
        return _icForum!!
    }

private var _icForum: ImageVector? = null
