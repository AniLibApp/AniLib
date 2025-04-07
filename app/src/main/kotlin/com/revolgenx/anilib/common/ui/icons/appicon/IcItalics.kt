package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcItalics: ImageVector
    get() {
        if (_icItalics != null) {
            return _icItalics!!
        }
        _icItalics = materialIcon(name = "IcItalics") {
            materialPath {
                moveTo(10.0F, 4.0F)
                verticalLineToRelative(3.0F)
                horizontalLineToRelative(2.21F)
                lineToRelative(-3.42F, 8.0F)
                horizontalLineTo(6.0F)
                verticalLineToRelative(3.0F)
                horizontalLineToRelative(8.0F)
                verticalLineToRelative(-3.0F)
                horizontalLineToRelative(-2.21F)
                lineToRelative(3.42F, -8.0F)
                horizontalLineTo(18.0F)
                verticalLineTo(4.0F)
                close()
            }
        }
        return _icItalics!!
    }

private var _icItalics: ImageVector? = null
