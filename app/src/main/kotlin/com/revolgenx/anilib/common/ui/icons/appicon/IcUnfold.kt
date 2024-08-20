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

public val AppIcons.IcUnfold: ImageVector
    get() {
        if (_icUnfold != null) {
            return _icUnfold!!
        }
        _icUnfold = Builder(
            name = "IcUnfold", 
            defaultWidth = 24.0.dp, 
            defaultHeight = 24.0.dp, 
            viewportWidth = 960.0f, 
            viewportHeight = 960.0f
            ).apply {
                path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                        strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                        pathFillType = NonZero) {
                    moveTo(480.0f, 724.0f)
                    lineToRelative(93.0f, -93.0f)
                    quadToRelative(12.0f, -12.0f, 29.0f, -12.0f)
                    reflectiveQuadToRelative(29.0f, 12.0f)
                    quadToRelative(12.0f, 12.0f, 12.0f, 29.0f)
                    reflectiveQuadToRelative(-12.0f, 29.0f)
                    lineTo(508.0f, 812.0f)
                    quadToRelative(-6.0f, 6.0f, -13.0f, 8.5f)
                    reflectiveQuadToRelative(-15.0f, 2.5f)
                    quadToRelative(-8.0f, 0.0f, -15.0f, -2.5f)
                    reflectiveQuadToRelative(-13.0f, -8.5f)
                    lineTo(329.0f, 689.0f)
                    quadToRelative(-12.0f, -12.0f, -12.0f, -29.0f)
                    reflectiveQuadToRelative(12.0f, -29.0f)
                    quadToRelative(12.0f, -12.0f, 29.0f, -12.0f)
                    reflectiveQuadToRelative(29.0f, 12.0f)
                    lineToRelative(93.0f, 93.0f)
                    close()
                    moveToRelative(0.0f, -484.0f)
                    lineToRelative(-93.0f, 93.0f)
                    quadToRelative(-12.0f, 12.0f, -29.0f, 12.0f)
                    reflectiveQuadToRelative(-29.0f, -12.0f)
                    quadToRelative(-12.0f, -12.0f, -12.0f, -29.0f)
                    reflectiveQuadToRelative(12.0f, -29.0f)
                    lineToRelative(123.0f, -123.0f)
                    quadToRelative(6.0f, -6.0f, 13.0f, -8.5f)
                    reflectiveQuadToRelative(15.0f, -2.5f)
                    quadToRelative(8.0f, 0.0f, 15.0f, 2.5f)
                    reflectiveQuadToRelative(13.0f, 8.5f)
                    lineToRelative(123.0f, 123.0f)
                    quadToRelative(12.0f, 12.0f, 12.0f, 29.0f)
                    reflectiveQuadToRelative(-12.0f, 29.0f)
                    quadToRelative(-12.0f, 12.0f, -29.0f, 12.0f)
                    reflectiveQuadToRelative(-29.0f, -12.0f)
                    lineToRelative(-93.0f, -93.0f)
                    close()
                }
            }
            .build()
            return _icUnfold!!
        }

    private var _icUnfold: ImageVector? = null

    @Preview
    @Composable
    private fun Preview() {
        Box(modifier = Modifier.padding(12.dp)) {
            Image(imageVector = AppIcons.IcUnfold, contentDescription = "")
        }
    }
