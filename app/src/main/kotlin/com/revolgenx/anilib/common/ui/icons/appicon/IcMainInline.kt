package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.StrokeJoin.Companion.Bevel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

public val AppIcons.IcMainInline: ImageVector
    get() {
        if (_icMainInline != null) {
            return _icMainInline!!
        }
        _icMainInline = Builder(
            name = "IcMainInline", 
            defaultWidth = 24.0.dp, 
            defaultHeight = 24.0.dp, 
            viewportWidth = 512.0f, 
            viewportHeight = 512.0f
            ).apply {
                path(fill = SolidColor(Color(0xFF7E7E7E)), stroke = SolidColor(Color(0xFF000000)),
                        fillAlpha = 0.9864f, strokeLineWidth = 0.025f, strokeLineCap = Round,
                        strokeLineJoin = Bevel, strokeLineMiter = 4.0f, pathFillType = NonZero) {
                    moveTo(459.4f, 249.9f)
                    curveToRelative(-3.8f, -0.7f, -6.0f, -0.1f, -7.0f, -3.1f)
                    curveToRelative(-0.6f, -1.8f, 0.6f, -5.0f, 0.7f, -6.9f)
                    curveToRelative(0.4f, -5.2f, 0.5f, -10.4f, 0.4f, -15.6f)
                    curveToRelative(-0.2f, -9.8f, -1.3f, -19.5f, -3.0f, -29.1f)
                    curveToRelative(-1.4f, -7.8f, -3.2f, -15.5f, -5.6f, -23.0f)
                    curveToRelative(-1.4f, -4.4f, -4.3f, -8.8f, -5.3f, -13.1f)
                    curveToRelative(-1.7f, -7.1f, 0.9f, -17.1f, 0.7f, -24.7f)
                    curveToRelative(-0.5f, -14.8f, -2.5f, -29.6f, -5.3f, -44.2f)
                    curveToRelative(-3.5f, -18.2f, -8.6f, -36.2f, -15.5f, -53.4f)
                    curveToRelative(-2.3f, -5.7f, -5.6f, -19.1f, -11.9f, -20.7f)
                    curveToRelative(-3.4f, -0.8f, -12.6f, 3.1f, -16.0f, 4.2f)
                    curveToRelative(-4.7f, 1.4f, -9.4f, 3.1f, -14.0f, 4.9f)
                    curveToRelative(-22.6f, 9.1f, -39.8f, 23.3f, -57.4f, 39.5f)
                    curveToRelative(-2.1f, 1.9f, -4.3f, 4.2f, -6.8f, 5.6f)
                    curveToRelative(-6.6f, 3.5f, -12.3f, -2.7f, -18.5f, -5.5f)
                    curveToRelative(-16.8f, -7.4f, -35.3f, -9.8f, -53.6f, -8.1f)
                    curveToRelative(-11.6f, 1.1f, -22.4f, 5.6f, -33.7f, 7.2f)
                    curveToRelative(-5.2f, 0.7f, -4.9f, -0.5f, -8.9f, -4.4f)
                    curveToRelative(-14.4f, -14.0f, -36.1f, -35.2f, -57.8f, -34.8f)
                    curveToRelative(-2.6f, 0.0f, -4.5f, 0.8f, -6.9f, 1.3f)
                    curveToRelative(-0.5f, 0.3f, -2.1f, 0.1f, -4.1f, 0.6f)
                    curveToRelative(-2.5f, 1.0f, -0.9f, 0.0f, -3.9f, 2.6f)
                    curveToRelative(-7.4f, 6.4f, -12.6f, 17.0f, -17.2f, 25.4f)
                    curveToRelative(-8.4f, 15.5f, -14.4f, 32.1f, -18.7f, 49.2f)
                    curveToRelative(-1.8f, 7.1f, -3.4f, 14.3f, -4.5f, 21.5f)
                    curveToRelative(-1.1f, 7.4f, -0.7f, 8.4f, -5.8f, 14.3f)
                    curveToRelative(-9.7f, 11.1f, -16.2f, 24.6f, -20.4f, 38.7f)
                    curveToRelative(-2.8f, 9.5f, -4.5f, 19.3f, -5.4f, 29.1f)
                    curveToRelative(-0.5f, 5.7f, -0.7f, 11.4f, -0.6f, 17.2f)
                    curveToRelative(0.0f, 0.5f, 0.7f, 6.5f, 0.2f, 7.3f)
                    curveToRelative(-1.3f, 2.2f, -5.7f, 2.8f, -7.5f, 4.7f)
                    curveToRelative(-6.6f, 6.7f, 3.2f, 21.0f, 6.7f, 28.0f)
                    curveToRelative(1.7f, 3.2f, 3.9f, 9.0f, 7.4f, 10.6f)
                    curveToRelative(4.4f, 1.9f, 9.1f, -1.6f, 13.1f, -3.4f)
                    curveToRelative(-18.2f, 37.5f, -35.3f, 82.2f, -20.6f, 124.0f)
                    curveToRelative(5.1f, 14.5f, 14.2f, 29.1f, 27.0f, 38.0f)
                    curveToRelative(-0.4f, -1.2f, -0.9f, -2.4f, -1.3f, -3.6f)
                    curveToRelative(1.9f, 1.5f, -0.4f, -3.3f, -0.8f, -4.3f)
                    curveToRelative(-1.6f, -5.1f, -3.1f, -10.2f, -4.4f, -15.3f)
                    curveToRelative(-1.1f, -4.1f, -9.3f, -29.9f, -3.2f, -31.7f)
                    curveToRelative(8.7f, 15.1f, 18.1f, 29.9f, 29.0f, 43.5f)
                    curveToRelative(4.5f, 5.7f, 9.5f, 10.9f, 15.5f, 15.1f)
                    curveToRelative(6.2f, 4.4f, 13.9f, 6.2f, 19.7f, 10.9f)
                    curveToRelative(-1.4f, -2.9f, 5.7f, 2.2f, 6.7f, 2.9f)
                    curveToRelative(-9.3f, -17.8f, -18.0f, -36.0f, -24.3f, -55.2f)
                    curveToRelative(8.7f, 1.8f, 17.2f, 6.6f, 25.8f, 9.3f)
                    curveToRelative(8.7f, 2.8f, 17.6f, 5.0f, 26.6f, 6.6f)
                    curveToRelative(15.9f, 2.9f, 32.0f, 3.9f, 48.1f, 3.4f)
                    curveToRelative(27.5f, -0.9f, 54.2f, -6.7f, 80.1f, -15.7f)
                    curveToRelative(-11.0f, 11.6f, -24.9f, 20.6f, -38.6f, 28.6f)
                    curveToRelative(33.7f, -0.1f, 66.6f, -15.7f, 87.7f, -42.2f)
                    curveToRelative(9.8f, 32.1f, -18.2f, 62.7f, -41.1f, 80.7f)
                    curveToRelative(21.3f, -2.7f, 54.1f, -9.1f, 66.2f, -30.0f)
                    curveToRelative(5.7f, 22.0f, -19.9f, 48.1f, -34.7f, 60.8f)
                    curveToRelative(46.1f, -6.4f, 91.2f, -28.0f, 108.6f, -76.7f)
                    curveToRelative(8.8f, -24.7f, 10.5f, -52.0f, 8.5f, -77.9f)
                    curveToRelative(-1.0f, -13.1f, -3.1f, -26.1f, -6.0f, -38.9f)
                    curveToRelative(-0.8f, -3.5f, -1.6f, -13.5f, -5.1f, -15.5f)
                    curveToRelative(7.7f, -3.5f, 15.8f, -12.5f, 20.2f, -19.5f)
                    curveToRelative(5.3f, -8.5f, 9.4f, -16.8f, -3.5f, -19.2f)
                    close()
                }
            }
            .build()
            return _icMainInline!!
        }

    private var _icMainInline: ImageVector? = null

    @Preview
    @Composable
    private fun Preview() {
        Box(modifier = Modifier.padding(12.dp)) {
            Image(imageVector = AppIcons.IcMainInline, contentDescription = "")
        }
    }
