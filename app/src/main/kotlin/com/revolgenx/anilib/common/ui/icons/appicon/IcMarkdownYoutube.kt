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

public val AppIcons.IcMarkdownYoutube: ImageVector
    get() {
        if (_icMarkdownYoutube != null) {
            return _icMarkdownYoutube!!
        }
        _icMarkdownYoutube = Builder(
            name = "IcMarkdownYoutube", defaultWidth = 48.0.dp,
            defaultHeight = 33.600197.dp, viewportWidth = 161.98f, viewportHeight =
            113.38667f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFff0000)), stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveToRelative(158.595f, 17.705f)
                curveToRelative(-1.863f, -6.969f, -7.352f, -12.457f, -14.321f, -14.32f)
                curveToRelative(-12.632f, -3.385f, -63.284f, -3.385f, -63.284f, -3.385f)
                curveToRelative(0.0f, -0.0f, -50.652f, -0.0f, -63.284f, 3.385f)
                curveToRelative(-6.969f, 1.863f, -12.459f, 7.351f, -14.321f, 14.32f)
                curveToRelative(-3.384f, 12.632f, -3.384f, 38.988f, -3.384f, 38.988f)
                curveToRelative(0.0f, -0.0f, 0.0f, 26.356f, 3.384f, 38.987f)
                curveToRelative(1.863f, 6.969f, 7.352f, 12.459f, 14.321f, 14.321f)
                curveToRelative(12.632f, 3.385f, 63.284f, 3.385f, 63.284f, 3.385f)
                curveToRelative(0.0f, -0.0f, 50.652f, -0.0f, 63.284f, -3.385f)
                curveToRelative(6.969f, -1.863f, 12.459f, -7.352f, 14.321f, -14.321f)
                curveToRelative(3.385f, -12.631f, 3.385f, -38.987f, 3.385f, -38.987f)
                curveToRelative(0.0f, -0.0f, 0.0f, -26.356f, -3.385f, -38.988f)
            }
            path(
                fill = SolidColor(Color(0xFFffffff)), stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(64.791f, 80.991f)
                lineTo(106.874f, 56.693f)
                lineTo(64.791f, 32.396f)
                close()
            }
        }
            .build()
        return _icMarkdownYoutube!!
    }

private var _icMarkdownYoutube: ImageVector? = null
