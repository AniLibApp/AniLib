package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons
val AppIcons.IcYoutube: ImageVector
    get() {
        if (_icYoutube != null) {
            return _icYoutube!!
        }
        _icYoutube = Builder(
            name = "IcYoutube",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 14.0F,
            viewportHeight = 14.0F,
        ).path(
            fill = SolidColor(Color(0xFFFFFFFF)),
            fillAlpha = 1.0F,
            strokeAlpha = 1.0F,
            strokeLineWidth = 0.0F,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 4.0F,
            pathFillType = PathFillType.NonZero,
        ) {
            moveToRelative(10.0F, 7.0049F)
            quadToRelative(0.0F, -0.2891F, -0.2344F, -0.4219F)
            lineToRelative(-4.0F, -2.5F)
            quadTo(5.5234F, 3.9268F, 5.2578F, 4.0674F)
            quadTo(5.0F, 4.208F, 5.0F, 4.5049F)
            lineToRelative(0.0F, 5.0F)
            quadToRelative(0.0F, 0.2969F, 0.2578F, 0.4375F)
            quadToRelative(0.125F, 0.0625F, 0.2422F, 0.0625F)
            quadToRelative(0.1563F, 0.0F, 0.2656F, -0.0781F)
            lineToRelative(4.0F, -2.5F)
            quadTo(10.0F, 7.294F, 10.0F, 7.0049F)

            moveTo(14.0F, 7.0049F)
            quadToRelative(0.0F, 0.75F, -0.0078F, 1.1719F)
            quadToRelative(-0.0078F, 0.4219F, -0.0664F, 1.0664F)
            quadToRelative(-0.0586F, 0.6445F, -0.1758F, 1.1524F)
            quadToRelative(-0.125F, 0.5703F, -0.5391F, 0.9609F)
            quadToRelative(-0.4141F, 0.3906F, -0.9688F, 0.4531F)
            quadToRelative(-1.7344F, 0.1953F, -5.2422F, 0.1953F)
            quadToRelative(-3.5078F, 0.0F, -5.2422F, -0.1953F)
            quadTo(1.2031F, 11.7471F, 0.7852F, 11.3565F)
            quadTo(0.3672F, 10.9658F, 0.2422F, 10.3955F)
            quadTo(0.1328F, 9.8877F, 0.0742F, 9.2432F)
            quadTo(0.0156F, 8.5987F, 0.0078F, 8.1768F)
            quadTo(0.0F, 7.7549F, 0.0F, 7.0049F)
            quadToRelative(0.0F, -0.75F, 0.0078F, -1.1719F)
            quadToRelative(0.0078F, -0.4219F, 0.0664F, -1.0664F)
            quadTo(0.1328F, 4.1221F, 0.25F, 3.6143F)
            quadToRelative(0.125F, -0.5703F, 0.5391F, -0.9609F)
            quadToRelative(0.4141F, -0.3906F, 0.9688F, -0.4531F)
            quadTo(3.4922F, 2.0049F, 7.0F, 2.0049F)
            quadToRelative(3.5078F, 0.0F, 5.2422F, 0.1953F)
            quadToRelative(0.5547F, 0.0625F, 0.9727F, 0.4531F)
            quadToRelative(0.418F, 0.3906F, 0.543F, 0.9609F)
            quadToRelative(0.1094F, 0.5078F, 0.168F, 1.1523F)
            quadToRelative(0.0586F, 0.6445F, 0.0664F, 1.0664F)
            quadTo(14.0F, 6.2549F, 14.0F, 7.0049F)
            close()
        }.build()
        return _icYoutube!!
    }
private var _icYoutube: ImageVector? = null
