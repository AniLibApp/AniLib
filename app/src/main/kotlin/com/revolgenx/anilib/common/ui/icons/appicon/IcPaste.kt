package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcPaste: ImageVector
    get() {
        if (_icPaste != null) {
            return _icPaste!!
        }
        _icPaste = materialIcon(name = "IcPaste") {
            materialPath {
                moveTo(19.0F, 2.0F)
                horizontalLineToRelative(-4.18F)
                curveTo(14.4F, 0.84F, 13.3F, 0.0F, 12.0F, 0.0F)
                curveToRelative(-1.3F, 0.0F, -2.4F, 0.84F, -2.82F, 2.0F)
                lineTo(5.0F, 2.0F)
                curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
                verticalLineToRelative(16.0F)
                curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
                horizontalLineToRelative(14.0F)
                curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
                lineTo(21.0F, 4.0F)
                curveToRelative(0.0F, -1.1F, -0.9F, -2.0F, -2.0F, -2.0F)

                moveTo(12.0F, 2.0F)
                curveToRelative(0.55F, 0.0F, 1.0F, 0.45F, 1.0F, 1.0F)
                reflectiveCurveToRelative(-0.45F, 1.0F, -1.0F, 1.0F)
                reflectiveCurveToRelative(-1.0F, -0.45F, -1.0F, -1.0F)
                reflectiveCurveToRelative(0.45F, -1.0F, 1.0F, -1.0F)

                moveTo(19.0F, 20.0F)
                lineTo(5.0F, 20.0F)
                lineTo(5.0F, 4.0F)
                horizontalLineToRelative(2.0F)
                verticalLineToRelative(3.0F)
                horizontalLineToRelative(10.0F)
                lineTo(17.0F, 4.0F)
                horizontalLineToRelative(2.0F)
                verticalLineToRelative(16.0F)
                close()
            }
        }
        return _icPaste!!
    }

private var _icPaste: ImageVector? = null
