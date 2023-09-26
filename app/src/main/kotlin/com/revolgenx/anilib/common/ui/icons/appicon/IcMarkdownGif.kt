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

public val AppIcons.IcMarkdownGif: ImageVector
    get() {
        if (_icMarkdownGif != null) {
            return _icMarkdownGif!!
        }
        _icMarkdownGif = Builder(
            name = "IcMarkdownGif", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(11.5f, 9.0f)
                horizontalLineToRelative(1.5f)
                verticalLineToRelative(6.0f)
                horizontalLineToRelative(-1.5f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(9.0f, 9.0f)
                horizontalLineTo(6.0f)
                curveToRelative(-0.6f, 0.0f, -1.0f, 0.5f, -1.0f, 1.0f)
                verticalLineToRelative(4.0f)
                curveToRelative(0.0f, 0.5f, 0.4f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(3.0f)
                curveToRelative(0.6f, 0.0f, 1.0f, -0.5f, 1.0f, -1.0f)
                verticalLineToRelative(-2.0f)
                horizontalLineTo(8.5f)
                verticalLineToRelative(1.5f)
                horizontalLineToRelative(-2.0f)
                verticalLineToRelative(-3.0f)
                horizontalLineTo(10.0f)
                verticalLineTo(10.0f)
                curveTo(10.0f, 9.5f, 9.6f, 9.0f, 9.0f, 9.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(19.0f, 10.5f)
                lineToRelative(0.0f, -1.5f)
                lineToRelative(-4.5f, 0.0f)
                lineToRelative(0.0f, 6.0f)
                lineToRelative(1.5f, 0.0f)
                lineToRelative(0.0f, -2.0f)
                lineToRelative(2.0f, 0.0f)
                lineToRelative(0.0f, -1.5f)
                lineToRelative(-2.0f, 0.0f)
                lineToRelative(0.0f, -1.0f)
                close()
            }
        }
            .build()
        return _icMarkdownGif!!
    }

private var _icMarkdownGif: ImageVector? = null
