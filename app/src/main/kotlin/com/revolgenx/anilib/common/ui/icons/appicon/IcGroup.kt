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

public val AppIcons.IcGroup: ImageVector
    get() {
        if (_icGroup != null) {
            return _icGroup!!
        }
        _icGroup = Builder(
            name = "IcGroup", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 960.0f, viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(40.0f, 720.0f)
                quadTo(23.0f, 720.0f, 11.5f, 708.5f)
                quadTo(0.0f, 697.0f, 0.0f, 680.0f)
                lineTo(0.0f, 657.0f)
                quadTo(0.0f, 613.0f, 44.0f, 586.5f)
                quadTo(88.0f, 560.0f, 160.0f, 560.0f)
                quadTo(173.0f, 560.0f, 184.5f, 561.0f)
                quadTo(196.0f, 562.0f, 207.0f, 564.0f)
                quadTo(193.0f, 584.0f, 186.5f, 607.0f)
                quadTo(180.0f, 630.0f, 180.0f, 656.0f)
                lineTo(180.0f, 720.0f)
                lineTo(40.0f, 720.0f)
                close()
                moveTo(280.0f, 720.0f)
                quadTo(263.0f, 720.0f, 251.5f, 708.5f)
                quadTo(240.0f, 697.0f, 240.0f, 680.0f)
                lineTo(240.0f, 656.0f)
                quadTo(240.0f, 591.0f, 306.5f, 550.5f)
                quadTo(373.0f, 510.0f, 480.0f, 510.0f)
                quadTo(588.0f, 510.0f, 654.0f, 550.5f)
                quadTo(720.0f, 591.0f, 720.0f, 656.0f)
                lineTo(720.0f, 680.0f)
                quadTo(720.0f, 697.0f, 708.5f, 708.5f)
                quadTo(697.0f, 720.0f, 680.0f, 720.0f)
                lineTo(280.0f, 720.0f)
                close()
                moveTo(780.0f, 720.0f)
                lineTo(780.0f, 656.0f)
                quadTo(780.0f, 630.0f, 773.0f, 607.0f)
                quadTo(766.0f, 584.0f, 753.0f, 564.0f)
                quadTo(764.0f, 562.0f, 775.5f, 561.0f)
                quadTo(787.0f, 560.0f, 800.0f, 560.0f)
                quadTo(872.0f, 560.0f, 916.0f, 586.5f)
                quadTo(960.0f, 613.0f, 960.0f, 657.0f)
                lineTo(960.0f, 680.0f)
                quadTo(960.0f, 697.0f, 948.5f, 708.5f)
                quadTo(937.0f, 720.0f, 920.0f, 720.0f)
                lineTo(780.0f, 720.0f)
                close()
                moveTo(160.0f, 520.0f)
                quadTo(127.0f, 520.0f, 103.5f, 496.5f)
                quadTo(80.0f, 473.0f, 80.0f, 440.0f)
                quadTo(80.0f, 407.0f, 103.5f, 383.5f)
                quadTo(127.0f, 360.0f, 160.0f, 360.0f)
                quadTo(193.0f, 360.0f, 216.5f, 383.5f)
                quadTo(240.0f, 407.0f, 240.0f, 440.0f)
                quadTo(240.0f, 473.0f, 216.5f, 496.5f)
                quadTo(193.0f, 520.0f, 160.0f, 520.0f)
                close()
                moveTo(800.0f, 520.0f)
                quadTo(767.0f, 520.0f, 743.5f, 496.5f)
                quadTo(720.0f, 473.0f, 720.0f, 440.0f)
                quadTo(720.0f, 407.0f, 743.5f, 383.5f)
                quadTo(767.0f, 360.0f, 800.0f, 360.0f)
                quadTo(833.0f, 360.0f, 856.5f, 383.5f)
                quadTo(880.0f, 407.0f, 880.0f, 440.0f)
                quadTo(880.0f, 473.0f, 856.5f, 496.5f)
                quadTo(833.0f, 520.0f, 800.0f, 520.0f)
                close()
                moveTo(480.0f, 480.0f)
                quadTo(430.0f, 480.0f, 395.0f, 445.0f)
                quadTo(360.0f, 410.0f, 360.0f, 360.0f)
                quadTo(360.0f, 310.0f, 395.0f, 275.0f)
                quadTo(430.0f, 240.0f, 480.0f, 240.0f)
                quadTo(530.0f, 240.0f, 565.0f, 275.0f)
                quadTo(600.0f, 310.0f, 600.0f, 360.0f)
                quadTo(600.0f, 410.0f, 565.0f, 445.0f)
                quadTo(530.0f, 480.0f, 480.0f, 480.0f)
                close()
            }
        }
            .build()
        return _icGroup!!
    }

private var _icGroup: ImageVector? = null
