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

public val AppIcons.IcFall: ImageVector
    get() {
        if (_icFall != null) {
            return _icFall!!
        }
        _icFall = Builder(
            name = "IcFall", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 48.0f, viewportHeight = 48.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(24.0f, 39.8f)
                quadTo(21.05f, 39.8f, 18.475f, 38.825f)
                quadTo(15.9f, 37.85f, 13.8f, 36.1f)
                lineTo(10.7f, 39.15f)
                quadTo(10.25f, 39.6f, 9.675f, 39.6f)
                quadTo(9.1f, 39.6f, 8.65f, 39.15f)
                quadTo(8.2f, 38.7f, 8.2f, 38.125f)
                quadTo(8.2f, 37.55f, 8.65f, 37.1f)
                lineTo(11.7f, 34.0f)
                quadTo(9.95f, 31.9f, 8.975f, 29.325f)
                quadTo(8.0f, 26.75f, 8.0f, 23.8f)
                quadTo(8.0f, 17.1f, 12.65f, 12.45f)
                quadTo(17.3f, 7.8f, 24.0f, 7.8f)
                horizontalLineTo(40.0f)
                verticalLineTo(23.8f)
                quadTo(40.0f, 30.5f, 35.35f, 35.15f)
                quadTo(30.7f, 39.8f, 24.0f, 39.8f)
                close()
                moveTo(24.0f, 36.8f)
                quadTo(29.4f, 36.8f, 33.2f, 33.0f)
                quadTo(37.0f, 29.2f, 37.0f, 23.8f)
                verticalLineTo(10.8f)
                horizontalLineTo(24.0f)
                quadTo(18.6f, 10.8f, 14.8f, 14.6f)
                quadTo(11.0f, 18.4f, 11.0f, 23.8f)
                quadTo(11.0f, 26.1f, 11.75f, 28.175f)
                quadTo(12.5f, 30.25f, 13.8f, 31.9f)
                lineTo(24.65f, 21.1f)
                quadTo(25.1f, 20.65f, 25.675f, 20.65f)
                quadTo(26.25f, 20.65f, 26.7f, 21.1f)
                quadTo(27.15f, 21.55f, 27.15f, 22.15f)
                quadTo(27.15f, 22.75f, 26.7f, 23.2f)
                lineTo(15.9f, 34.0f)
                quadTo(17.55f, 35.3f, 19.625f, 36.05f)
                quadTo(21.7f, 36.8f, 24.0f, 36.8f)
                close()
                moveTo(24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                quadTo(24.0f, 23.8f, 24.0f, 23.8f)
                close()
            }
        }
            .build()
        return _icFall!!
    }

private var _icFall: ImageVector? = null
