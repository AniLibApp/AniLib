package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons


val AppIcons.IcHeader: ImageVector
    get() {
        if (_icHeader != null) {
            return _icHeader!!
        }
        _icHeader = ImageVector.Builder(
            name = "IcHeader",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 122.88F,
            viewportHeight = 120.26F,
        ).materialPath {
            moveTo(0.0F, 14.54F)
            lineToRelative(0.0F, -14.54F)
            lineToRelative(49.81F, 0.0F)
            lineToRelative(0.0F, 14.54F)
            lineToRelative(-12.88F, 2.49F)
            lineToRelative(0.0F, 34.67F)
            lineToRelative(49.05F, 0.0F)
            lineToRelative(0.0F, -34.67F)
            lineToRelative(-12.88F, -2.49F)
            lineToRelative(0.0F, -14.54F)
            lineToRelative(12.88F, 0.0F)
            lineToRelative(24.02F, 0.0F)
            lineToRelative(12.88F, 0.0F)
            lineToRelative(0.0F, 14.54F)
            lineToRelative(-12.88F, 2.49F)
            lineToRelative(0.0F, 86.28F)
            lineToRelative(12.88F, 2.48F)
            lineToRelative(0.0F, 14.47F)
            lineToRelative(-49.78F, 0.0F)
            lineToRelative(0.0F, -14.47F)
            lineToRelative(12.88F, -2.48F)
            lineToRelative(0.0F, -33.01F)
            lineToRelative(-49.05F, 0.0F)
            lineToRelative(0.0F, 33.01F)
            lineToRelative(12.88F, 2.48F)
            lineToRelative(0.0F, 14.47F)
            lineToRelative(-49.81F, 0.0F)
            lineToRelative(0.0F, -14.47F)
            lineToRelative(12.8F, -2.48F)
            lineToRelative(0.0F, -86.28F)
            lineToRelative(-12.8F, -2.49F)
            close()
        }.build()
        return _icHeader!!
    }
private var _icHeader: ImageVector? = null
