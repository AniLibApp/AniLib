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

public val AppIcons.IcTranslate: ImageVector
    get() {
        if (_icTranslate != null) {
            return _icTranslate!!
        }
        _icTranslate = Builder(
            name = "IcTranslate",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(12.65f, 15.67f)
                curveToRelative(0.14f, -0.36f, 0.05f, -0.77f, -0.23f, -1.05f)
                lineToRelative(-2.09f, -2.06f)
                lineToRelative(0.03f, -0.03f)
                curveToRelative(1.74f, -1.94f, 2.98f, -4.17f, 3.71f, -6.53f)
                horizontalLineToRelative(1.94f)
                curveTo(16.55f, 6.0f, 17.0f, 5.55f, 17.0f, 5.01f)
                verticalLineTo(4.99f)
                curveTo(17.0f, 4.45f, 16.55f, 4.0f, 16.01f, 4.0f)
                horizontalLineTo(10.0f)
                verticalLineTo(3.0f)
                curveToRelative(0.0f, -0.55f, -0.45f, -1.0f, -1.0f, -1.0f)
                reflectiveCurveTo(8.0f, 2.45f, 8.0f, 3.0f)
                verticalLineToRelative(1.0f)
                horizontalLineTo(1.99f)
                curveTo(1.45f, 4.0f, 1.0f, 4.45f, 1.0f, 4.99f)
                curveToRelative(0.0f, 0.55f, 0.45f, 0.99f, 0.99f, 0.99f)
                horizontalLineToRelative(10.18f)
                curveTo(11.5f, 7.92f, 10.44f, 9.75f, 9.0f, 11.35f)
                curveToRelative(-0.81f, -0.89f, -1.49f, -1.86f, -2.06f, -2.88f)
                curveTo(6.78f, 8.18f, 6.49f, 8.0f, 6.16f, 8.0f)
                curveTo(5.47f, 8.0f, 5.03f, 8.75f, 5.37f, 9.35f)
                curveToRelative(0.63f, 1.13f, 1.4f, 2.21f, 2.3f, 3.21f)
                lineTo(3.3f, 16.87f)
                curveToRelative(-0.4f, 0.39f, -0.4f, 1.03f, 0.0f, 1.42f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.42f, 0.0f)
                lineTo(9.0f, 14.0f)
                lineToRelative(2.02f, 2.02f)
                curveToRelative(0.51f, 0.51f, 1.38f, 0.32f, 1.63f, -0.35f)
                close()
                moveTo(17.5f, 10.0f)
                curveToRelative(-0.6f, 0.0f, -1.14f, 0.37f, -1.35f, 0.94f)
                lineToRelative(-3.67f, 9.8f)
                curveTo(12.24f, 21.35f, 12.7f, 22.0f, 13.35f, 22.0f)
                curveToRelative(0.39f, 0.0f, 0.74f, -0.24f, 0.88f, -0.61f)
                lineTo(15.12f, 19.0f)
                horizontalLineToRelative(4.75f)
                lineToRelative(0.9f, 2.39f)
                curveToRelative(0.14f, 0.36f, 0.49f, 0.61f, 0.88f, 0.61f)
                curveToRelative(0.65f, 0.0f, 1.11f, -0.65f, 0.88f, -1.26f)
                lineToRelative(-3.67f, -9.8f)
                curveTo(18.64f, 10.37f, 18.1f, 10.0f, 17.5f, 10.0f)
                close()
                moveToRelative(-1.62f, 7.0f)
                lineToRelative(1.62f, -4.33f)
                lineTo(19.12f, 17.0f)
                horizontalLineToRelative(-3.24f)
                close()
            }
        }
            .build()
        return _icTranslate!!
    }

private var _icTranslate: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = AppIcons.IcTranslate, contentDescription = "")
    }
}
