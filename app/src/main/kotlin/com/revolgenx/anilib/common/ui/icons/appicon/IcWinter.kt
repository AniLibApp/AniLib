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

public val AppIcons.IcWinter: ImageVector
    get() {
        if (_icWinter != null) {
            return _icWinter!!
        }
        _icWinter = Builder(
            name = "IcWinter", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 48.0f, viewportHeight = 48.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(22.5f, 44.0f)
                verticalLineTo(34.25f)
                lineTo(15.05f, 41.7f)
                lineTo(12.9f, 39.6f)
                lineTo(22.5f, 30.0f)
                verticalLineTo(25.5f)
                horizontalLineTo(18.0f)
                lineTo(8.6f, 34.9f)
                lineTo(6.4f, 32.85f)
                lineTo(13.75f, 25.5f)
                horizontalLineTo(4.0f)
                verticalLineTo(22.5f)
                horizontalLineTo(13.75f)
                lineTo(6.25f, 15.0f)
                lineTo(8.4f, 12.85f)
                lineTo(18.0f, 22.5f)
                horizontalLineTo(22.5f)
                verticalLineTo(17.95f)
                lineTo(13.1f, 8.55f)
                lineTo(15.2f, 6.35f)
                lineTo(22.5f, 13.7f)
                verticalLineTo(4.0f)
                horizontalLineTo(25.5f)
                verticalLineTo(13.7f)
                lineTo(33.0f, 6.2f)
                lineTo(35.1f, 8.35f)
                lineTo(25.5f, 17.95f)
                verticalLineTo(22.5f)
                horizontalLineTo(30.05f)
                lineTo(39.5f, 13.05f)
                lineTo(41.65f, 15.15f)
                lineTo(34.3f, 22.5f)
                horizontalLineTo(44.0f)
                verticalLineTo(25.5f)
                horizontalLineTo(34.3f)
                lineTo(41.7f, 32.95f)
                lineTo(39.65f, 35.1f)
                lineTo(30.05f, 25.5f)
                horizontalLineTo(25.5f)
                verticalLineTo(30.0f)
                lineTo(35.1f, 39.65f)
                lineTo(33.05f, 41.8f)
                lineTo(25.5f, 34.25f)
                verticalLineTo(44.0f)
                close()
            }
        }
            .build()
        return _icWinter!!
    }

private var _icWinter: ImageVector? = null
