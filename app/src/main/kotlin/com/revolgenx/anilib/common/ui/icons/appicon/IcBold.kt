package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.revolgenx.anilib.common.ui.icons.AppIcons


val AppIcons.IcBold: ImageVector
    get() {
        if (_icBold != null) {
            return _icBold!!
        }
        _icBold = materialIcon(name = "IcBold") {
            materialPath {
                moveTo(15.6F, 10.79F)
                curveToRelative(0.97F, -0.67F, 1.65F, -1.77F, 1.65F, -2.79F)
                curveToRelative(0.0F, -2.26F, -1.75F, -4.0F, -4.0F, -4.0F)
                lineTo(7.0F, 4.0F)
                verticalLineToRelative(14.0F)
                horizontalLineToRelative(7.04F)
                curveToRelative(2.09F, 0.0F, 3.71F, -1.7F, 3.71F, -3.79F)
                curveToRelative(0.0F, -1.52F, -0.86F, -2.82F, -2.15F, -3.42F)

                moveTo(10.0F, 6.5F)
                horizontalLineToRelative(3.0F)
                curveToRelative(0.83F, 0.0F, 1.5F, 0.67F, 1.5F, 1.5F)
                reflectiveCurveToRelative(-0.67F, 1.5F, -1.5F, 1.5F)
                horizontalLineToRelative(-3.0F)
                verticalLineToRelative(-3.0F)

                moveTo(13.5F, 15.5F)
                lineTo(10.0F, 15.5F)
                verticalLineToRelative(-3.0F)
                horizontalLineToRelative(3.5F)
                curveToRelative(0.83F, 0.0F, 1.5F, 0.67F, 1.5F, 1.5F)
                reflectiveCurveToRelative(-0.67F, 1.5F, -1.5F, 1.5F)
                close()
            }
        }
        return _icBold!!
    }

private var _icBold: ImageVector? = null