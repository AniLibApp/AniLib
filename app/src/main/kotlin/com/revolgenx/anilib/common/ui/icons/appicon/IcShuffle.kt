package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

public val AppIcons.IcShuffle: ImageVector
    get() {
        if (_icShuffle != null) {
            return _icShuffle!!
        }
        _icShuffle =
            Builder(
                    name = "IcShuffle",
                    defaultWidth = 24.0.dp,
                    defaultHeight = 24.0.dp,
                    viewportWidth = 960.0f,
                    viewportHeight = 960.0f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF000000)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(120.0f, 920.0f)
                        quadTo(87.0f, 920.0f, 63.5f, 896.5f)
                        quadTo(40.0f, 873.0f, 40.0f, 840.0f)
                        lineTo(40.0f, 120.0f)
                        quadTo(40.0f, 87.0f, 63.5f, 63.5f)
                        quadTo(87.0f, 40.0f, 120.0f, 40.0f)
                        lineTo(840.0f, 40.0f)
                        quadTo(873.0f, 40.0f, 896.5f, 63.5f)
                        quadTo(920.0f, 87.0f, 920.0f, 120.0f)
                        lineTo(920.0f, 840.0f)
                        quadTo(920.0f, 873.0f, 896.5f, 896.5f)
                        quadTo(873.0f, 920.0f, 840.0f, 920.0f)
                        lineTo(120.0f, 920.0f)
                        close()
                        moveTo(600.0f, 800.0f)
                        lineTo(760.0f, 800.0f)
                        quadTo(777.0f, 800.0f, 788.5f, 788.5f)
                        quadTo(800.0f, 777.0f, 800.0f, 760.0f)
                        lineTo(800.0f, 600.0f)
                        quadTo(800.0f, 583.0f, 788.5f, 571.5f)
                        quadTo(777.0f, 560.0f, 760.0f, 560.0f)
                        quadTo(743.0f, 560.0f, 731.5f, 571.5f)
                        quadTo(720.0f, 583.0f, 720.0f, 600.0f)
                        lineTo(720.0f, 662.0f)
                        lineTo(623.0f, 565.0f)
                        quadTo(611.0f, 553.0f, 594.5f, 553.0f)
                        quadTo(578.0f, 553.0f, 566.0f, 565.0f)
                        quadTo(554.0f, 577.0f, 553.5f, 593.0f)
                        quadTo(553.0f, 609.0f, 565.0f, 621.0f)
                        lineTo(664.0f, 720.0f)
                        lineTo(600.0f, 720.0f)
                        quadTo(583.0f, 720.0f, 571.5f, 731.5f)
                        quadTo(560.0f, 743.0f, 560.0f, 760.0f)
                        quadTo(560.0f, 777.0f, 571.5f, 788.5f)
                        quadTo(583.0f, 800.0f, 600.0f, 800.0f)
                        close()
                        moveTo(172.0f, 788.0f)
                        quadTo(183.0f, 799.0f, 200.0f, 799.0f)
                        quadTo(217.0f, 799.0f, 228.0f, 788.0f)
                        lineTo(720.0f, 296.0f)
                        lineTo(720.0f, 360.0f)
                        quadTo(720.0f, 377.0f, 731.5f, 388.5f)
                        quadTo(743.0f, 400.0f, 760.0f, 400.0f)
                        quadTo(777.0f, 400.0f, 788.5f, 388.5f)
                        quadTo(800.0f, 377.0f, 800.0f, 360.0f)
                        lineTo(800.0f, 200.0f)
                        quadTo(800.0f, 183.0f, 788.5f, 171.5f)
                        quadTo(777.0f, 160.0f, 760.0f, 160.0f)
                        lineTo(600.0f, 160.0f)
                        quadTo(583.0f, 160.0f, 571.5f, 171.5f)
                        quadTo(560.0f, 183.0f, 560.0f, 200.0f)
                        quadTo(560.0f, 217.0f, 571.5f, 228.5f)
                        quadTo(583.0f, 240.0f, 600.0f, 240.0f)
                        lineTo(664.0f, 240.0f)
                        lineTo(172.0f, 732.0f)
                        quadTo(161.0f, 743.0f, 161.0f, 760.0f)
                        quadTo(161.0f, 777.0f, 172.0f, 788.0f)
                        close()
                        moveTo(171.0f, 228.0f)
                        lineTo(339.0f, 395.0f)
                        quadTo(350.0f, 406.0f, 367.0f, 406.0f)
                        quadTo(384.0f, 406.0f, 395.0f, 395.0f)
                        quadTo(407.0f, 383.0f, 406.5f, 366.5f)
                        quadTo(406.0f, 350.0f, 395.0f, 339.0f)
                        lineTo(227.0f, 172.0f)
                        quadTo(215.0f, 161.0f, 198.5f, 161.0f)
                        quadTo(182.0f, 161.0f, 171.0f, 172.0f)
                        quadTo(160.0f, 183.0f, 160.0f, 200.0f)
                        quadTo(160.0f, 217.0f, 171.0f, 228.0f)
                        close()
                    }
                }
                .build()
        return _icShuffle!!
    }

private var _icShuffle: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = AppIcons.IcShuffle, contentDescription = null)
    }
}
