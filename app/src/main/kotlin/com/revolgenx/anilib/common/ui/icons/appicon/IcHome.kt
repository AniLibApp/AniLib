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

public val AppIcons.IcHome: ImageVector
    get() {
        if (_icHome != null) {
            return _icHome!!
        }
        _icHome = Builder(
            name = "IcHome", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(244.0f, 831.0f)
                quadTo(213.0f, 831.0f, 191.0f, 809.0f)
                quadTo(169.0f, 787.0f, 169.0f, 756.0f)
                lineTo(169.0f, 401.0f)
                quadTo(169.0f, 383.0f, 176.75f, 367.25f)
                quadTo(184.5f, 351.5f, 199.0f, 341.0f)
                lineTo(435.0f, 164.0f)
                quadTo(445.0f, 156.5f, 456.5f, 152.75f)
                quadTo(468.0f, 149.0f, 480.0f, 149.0f)
                quadTo(492.0f, 149.0f, 503.5f, 152.75f)
                quadTo(515.0f, 156.5f, 525.0f, 164.0f)
                lineTo(761.0f, 341.0f)
                quadTo(775.5f, 351.5f, 783.25f, 367.25f)
                quadTo(791.0f, 383.0f, 791.0f, 401.0f)
                lineTo(791.0f, 756.0f)
                quadTo(791.0f, 787.0f, 769.0f, 809.0f)
                quadTo(747.0f, 831.0f, 716.0f, 831.0f)
                lineTo(562.5f, 831.0f)
                lineTo(562.5f, 555.0f)
                lineTo(397.5f, 555.0f)
                lineTo(397.5f, 831.0f)
                lineTo(244.0f, 831.0f)
                close()
            }
        }
            .build()
        return _icHome!!
    }

private var _icHome: ImageVector? = null
