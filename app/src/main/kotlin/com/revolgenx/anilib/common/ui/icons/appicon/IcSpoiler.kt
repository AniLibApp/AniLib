package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.revolgenx.anilib.common.ui.icons.AppIcons


val AppIcons.IcSpoiler: ImageVector
    get() {
        if (_icSpoiler != null) {
            return _icSpoiler!!
        }
        _icSpoiler = materialIcon(name = "IcSpoiler") {
            materialPath {
                moveTo(12.0F, 7.0F)
                curveToRelative(2.76F, 0.0F, 5.0F, 2.24F, 5.0F, 5.0F)
                curveToRelative(0.0F, 0.65F, -0.13F, 1.26F, -0.36F, 1.83F)
                lineToRelative(2.92F, 2.92F)
                curveToRelative(1.51F, -1.26F, 2.7F, -2.89F, 3.43F, -4.75F)
                curveToRelative(-1.73F, -4.39F, -6.0F, -7.5F, -11.0F, -7.5F)
                curveToRelative(-1.4F, 0.0F, -2.74F, 0.25F, -3.98F, 0.7F)
                lineToRelative(2.16F, 2.16F)
                curveTo(10.74F, 7.13F, 11.35F, 7.0F, 12.0F, 7.0F)

                moveTo(2.0F, 4.27F)
                lineToRelative(2.28F, 2.28F)
                lineToRelative(0.46F, 0.46F)
                curveTo(3.08F, 8.3F, 1.78F, 10.02F, 1.0F, 12.0F)
                curveToRelative(1.73F, 4.39F, 6.0F, 7.5F, 11.0F, 7.5F)
                curveToRelative(1.55F, 0.0F, 3.03F, -0.3F, 4.38F, -0.84F)
                lineToRelative(0.42F, 0.42F)
                lineTo(19.73F, 22.0F)
                lineTo(21.0F, 20.73F)
                lineTo(3.27F, 3.0F)
                lineTo(2.0F, 4.27F)

                moveTo(7.53F, 9.8F)
                lineToRelative(1.55F, 1.55F)
                curveToRelative(-0.05F, 0.21F, -0.08F, 0.43F, -0.08F, 0.65F)
                curveToRelative(0.0F, 1.66F, 1.34F, 3.0F, 3.0F, 3.0F)
                curveToRelative(0.22F, 0.0F, 0.44F, -0.03F, 0.65F, -0.08F)
                lineToRelative(1.55F, 1.55F)
                curveToRelative(-0.67F, 0.33F, -1.41F, 0.53F, -2.2F, 0.53F)
                curveToRelative(-2.76F, 0.0F, -5.0F, -2.24F, -5.0F, -5.0F)
                curveToRelative(0.0F, -0.79F, 0.2F, -1.53F, 0.53F, -2.2F)

                moveTo(11.84F, 9.02F)
                lineToRelative(3.15F, 3.15F)
                lineToRelative(0.02F, -0.16F)
                curveToRelative(0.0F, -1.66F, -1.34F, -3.0F, -3.0F, -3.0F)
                lineToRelative(-0.17F, 0.01F)
                close()
            }
        }
        return _icSpoiler!!
    }

private var _icSpoiler: ImageVector? = null

