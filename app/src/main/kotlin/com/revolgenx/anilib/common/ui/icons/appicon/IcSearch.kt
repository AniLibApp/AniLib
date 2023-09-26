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

public val AppIcons.IcSearch: ImageVector
    get() {
        if (_icSearch != null) {
            return _icSearch!!
        }
        _icSearch = Builder(
            name = "IcSearch", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(10.5f, 16.5f)
                arcTo(6.0f, 6.0f, 0.0f, true, false, 10.5f, 4.5f)
                arcToRelative(6.0f, 6.0f, 0.0f, false, false, 0.0f, 12.0f)
                close()
                moveTo(16.82f, 15.406f)
                lineToRelative(3.58f, 3.58f)
                arcToRelative(1.0f, 1.0f, 0.0f, true, true, -1.415f, 1.413f)
                lineToRelative(-3.58f, -3.58f)
                arcToRelative(8.0f, 8.0f, 0.0f, true, true, 1.414f, -1.414f)
                close()
            }
        }
            .build()
        return _icSearch!!
    }

private var _icSearch: ImageVector? = null
