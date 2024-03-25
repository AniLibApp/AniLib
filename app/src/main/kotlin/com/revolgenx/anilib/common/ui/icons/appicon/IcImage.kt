package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.revolgenx.anilib.common.ui.icons.AppIcons


val AppIcons.IcImage: ImageVector
    get() {
        if (_icImage != null) {
            return _icImage!!
        }
        _icImage = materialIcon(name = "IcImage") {
            materialPath {
                moveTo(21.0F, 19.0F)
                verticalLineTo(5.0F)
                curveToRelative(0.0F, -1.1F, -0.9F, -2.0F, -2.0F, -2.0F)
                horizontalLineTo(5.0F)
                curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
                verticalLineToRelative(14.0F)
                curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
                horizontalLineToRelative(14.0F)
                curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)

                moveTo(8.5F, 13.5F)
                lineToRelative(2.5F, 3.01F)
                lineTo(14.5F, 12.0F)
                lineToRelative(4.5F, 6.0F)
                horizontalLineTo(5.0F)
                lineToRelative(3.5F, -4.5F)
                close()
            }
        }
        return _icImage!!
    }

private var _icImage: ImageVector? = null
