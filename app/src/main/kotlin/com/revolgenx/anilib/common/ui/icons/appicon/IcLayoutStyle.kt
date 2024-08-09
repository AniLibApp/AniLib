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

public val AppIcons.IcLayoutStyle: ImageVector
    get() {
        if (_gridview24px != null) {
            return _gridview24px!!
        }
        _gridview24px = Builder(
            name = "Gridview24px",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 960.0f,
            viewportHeight = 960.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(200.0f, 440.0f)
                quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                reflectiveQuadTo(120.0f, 360.0f)
                verticalLineTo(200.0f)
                quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
                reflectiveQuadTo(200.0f, 120.0f)
                horizontalLineToRelative(160.0f)
                quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
                reflectiveQuadTo(440.0f, 200.0f)
                verticalLineToRelative(160.0f)
                quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
                reflectiveQuadTo(360.0f, 440.0f)
                horizontalLineTo(200.0f)
                close()
                moveToRelative(0.0f, 400.0f)
                quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                reflectiveQuadTo(120.0f, 760.0f)
                verticalLineTo(600.0f)
                quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
                reflectiveQuadTo(200.0f, 520.0f)
                horizontalLineToRelative(160.0f)
                quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
                reflectiveQuadTo(440.0f, 600.0f)
                verticalLineToRelative(160.0f)
                quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
                reflectiveQuadTo(360.0f, 840.0f)
                horizontalLineTo(200.0f)
                close()
                moveToRelative(400.0f, -400.0f)
                quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                reflectiveQuadTo(520.0f, 360.0f)
                verticalLineTo(200.0f)
                quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
                reflectiveQuadTo(600.0f, 120.0f)
                horizontalLineToRelative(160.0f)
                quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
                reflectiveQuadTo(840.0f, 200.0f)
                verticalLineToRelative(160.0f)
                quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
                reflectiveQuadTo(760.0f, 440.0f)
                horizontalLineTo(600.0f)
                close()
                moveToRelative(0.0f, 400.0f)
                quadToRelative(-33.0f, 0.0f, -56.5f, -23.5f)
                reflectiveQuadTo(520.0f, 760.0f)
                verticalLineTo(600.0f)
                quadToRelative(0.0f, -33.0f, 23.5f, -56.5f)
                reflectiveQuadTo(600.0f, 520.0f)
                horizontalLineToRelative(160.0f)
                quadToRelative(33.0f, 0.0f, 56.5f, 23.5f)
                reflectiveQuadTo(840.0f, 600.0f)
                verticalLineToRelative(160.0f)
                quadToRelative(0.0f, 33.0f, -23.5f, 56.5f)
                reflectiveQuadTo(760.0f, 840.0f)
                horizontalLineTo(600.0f)
                close()
                moveTo(200.0f, 360.0f)
                horizontalLineToRelative(160.0f)
                verticalLineTo(200.0f)
                horizontalLineTo(200.0f)
                verticalLineToRelative(160.0f)
                close()
                moveToRelative(400.0f, 0.0f)
                horizontalLineToRelative(160.0f)
                verticalLineTo(200.0f)
                horizontalLineTo(600.0f)
                verticalLineToRelative(160.0f)
                close()
                moveToRelative(0.0f, 400.0f)
                horizontalLineToRelative(160.0f)
                verticalLineTo(600.0f)
                horizontalLineTo(600.0f)
                verticalLineToRelative(160.0f)
                close()
                moveToRelative(-400.0f, 0.0f)
                horizontalLineToRelative(160.0f)
                verticalLineTo(600.0f)
                horizontalLineTo(200.0f)
                verticalLineToRelative(160.0f)
                close()
                moveToRelative(400.0f, -400.0f)
                close()
                moveToRelative(0.0f, 240.0f)
                close()
                moveToRelative(-240.0f, 0.0f)
                close()
                moveToRelative(0.0f, -240.0f)
                close()
            }
        }
            .build()
        return _gridview24px!!
    }

private var _gridview24px: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = AppIcons.IcLayoutStyle, contentDescription = "")
    }
}
