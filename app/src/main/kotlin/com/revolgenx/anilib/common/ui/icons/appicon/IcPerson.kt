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

public val AppIcons.IcPerson: ImageVector
    get() {
        if (_icPerson != null) {
            return _icPerson!!
        }
        _icPerson = Builder(
            name = "IcPerson", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(480.0f, 481.0f)
                quadTo(415.5f, 481.0f, 370.25f, 435.75f)
                quadTo(325.0f, 390.5f, 325.0f, 326.0f)
                quadTo(325.0f, 261.5f, 370.25f, 216.25f)
                quadTo(415.5f, 171.0f, 480.0f, 171.0f)
                quadTo(544.5f, 171.0f, 589.75f, 216.25f)
                quadTo(635.0f, 261.5f, 635.0f, 326.0f)
                quadTo(635.0f, 390.5f, 589.75f, 435.75f)
                quadTo(544.5f, 481.0f, 480.0f, 481.0f)
                close()
                moveTo(244.0f, 787.0f)
                quadTo(213.06f, 787.0f, 191.03f, 764.97f)
                quadTo(169.0f, 742.94f, 169.0f, 712.0f)
                lineTo(169.0f, 680.97f)
                quadTo(169.0f, 648.0f, 185.75f, 620.75f)
                quadTo(202.5f, 593.5f, 231.02f, 578.99f)
                quadTo(292.0f, 549.0f, 354.25f, 533.75f)
                quadTo(416.5f, 518.5f, 480.0f, 518.5f)
                quadTo(543.5f, 518.5f, 605.75f, 533.75f)
                quadTo(668.0f, 549.0f, 728.98f, 578.99f)
                quadTo(757.5f, 593.5f, 774.25f, 620.75f)
                quadTo(791.0f, 648.0f, 791.0f, 680.97f)
                lineTo(791.0f, 712.0f)
                quadTo(791.0f, 742.94f, 768.97f, 764.97f)
                quadTo(746.94f, 787.0f, 716.0f, 787.0f)
                lineTo(244.0f, 787.0f)
                close()
            }
        }
            .build()
        return _icPerson!!
    }

private var _icPerson: ImageVector? = null
