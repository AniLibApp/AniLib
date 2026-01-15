package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcFileExport: ImageVector
    get() {
        if (_icFileExport != null) return _icFileExport!!

        _icFileExport = ImageVector.Builder(
            name = "IcFileExport",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFe3e3e3))
            ) {
                moveTo(480f, 480f)
                close()
                moveTo(202f, 895f)
                lineToRelative(-56f, -57f)
                lineToRelative(118f, -118f)
                horizontalLineToRelative(-90f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(226f)
                verticalLineToRelative(226f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-89f)
                lineTo(202f, 895f)
                close()
                moveToRelative(278f, -15f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(240f)
                verticalLineToRelative(-440f)
                horizontalLineTo(520f)
                verticalLineToRelative(-200f)
                horizontalLineTo(240f)
                verticalLineToRelative(400f)
                horizontalLineToRelative(-80f)
                verticalLineToRelative(-400f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(240f, 80f)
                horizontalLineToRelative(320f)
                lineToRelative(240f, 240f)
                verticalLineToRelative(480f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(720f, 880f)
                horizontalLineTo(480f)
                close()
            }
        }.build()
        
        return _icFileExport!!
    }

private var _icFileExport: ImageVector? = null

