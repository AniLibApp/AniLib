package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.revolgenx.anilib.common.ui.icons.AppIcons


val AppIcons.IcStrikeThrough: ImageVector
    get() {
        if (_icStrikeThrough != null) {
            return _icStrikeThrough!!
        }
        _icStrikeThrough = materialIcon(name = "IcStrikeThrough") {
            materialPath {
                moveTo(6.85F, 7.08F)
                curveTo(6.85F, 4.37F, 9.45F, 3.0F, 12.24F, 3.0F)
                curveToRelative(1.64F, 0.0F, 3.0F, 0.49F, 3.9F, 1.28F)
                curveToRelative(0.77F, 0.65F, 1.46F, 1.73F, 1.46F, 3.24F)
                horizontalLineToRelative(-3.01F)
                curveToRelative(0.0F, -0.31F, -0.05F, -0.59F, -0.15F, -0.85F)
                curveToRelative(-0.29F, -0.86F, -1.2F, -1.28F, -2.25F, -1.28F)
                curveToRelative(-1.86F, 0.0F, -2.34F, 1.02F, -2.34F, 1.7F)
                curveToRelative(0.0F, 0.48F, 0.25F, 0.88F, 0.74F, 1.21F)
                curveTo(10.97F, 8.55F, 11.36F, 8.78F, 12.0F, 9.0F)
                horizontalLineTo(7.39F)
                curveTo(7.18F, 8.66F, 6.85F, 8.11F, 6.85F, 7.08F)

                moveTo(21.0F, 12.0F)
                verticalLineToRelative(-2.0F)
                horizontalLineTo(3.0F)
                verticalLineToRelative(2.0F)
                horizontalLineToRelative(9.62F)
                curveToRelative(1.15F, 0.45F, 1.96F, 0.75F, 1.96F, 1.97F)
                curveToRelative(0.0F, 1.0F, -0.81F, 1.67F, -2.28F, 1.67F)
                curveToRelative(-1.54F, 0.0F, -2.93F, -0.54F, -2.93F, -2.51F)
                horizontalLineTo(6.4F)
                curveToRelative(0.0F, 0.55F, 0.08F, 1.13F, 0.24F, 1.58F)
                curveToRelative(0.81F, 2.29F, 3.29F, 3.3F, 5.67F, 3.3F)
                curveToRelative(2.27F, 0.0F, 5.3F, -0.89F, 5.3F, -4.05F)
                curveToRelative(0.0F, -0.3F, -0.01F, -1.16F, -0.48F, -1.94F)
                horizontalLineTo(21.0F)
                verticalLineTo(12.0F)
                close()
            }
        }
        return _icStrikeThrough!!
    }

private var _icStrikeThrough: ImageVector? = null