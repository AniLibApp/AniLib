package com.revolgenx.anilib.common.ui.icons.appicon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import com.revolgenx.anilib.common.ui.icons.AppIcons


val AppIcons.IcLink: ImageVector
get() {
    if (_icLink != null) {
        return _icLink!!
    }
    _icLink = materialIcon(name = "IcLink") {
        materialPath {
            moveTo(3.9F, 12.0F)
            curveToRelative(0.0F, -1.71F, 1.39F, -3.1F, 3.1F, -3.1F)
            horizontalLineToRelative(4.0F)
            lineTo(11.0F, 7.0F)
            lineTo(7.0F, 7.0F)
            curveToRelative(-2.76F, 0.0F, -5.0F, 2.24F, -5.0F, 5.0F)
            reflectiveCurveToRelative(2.24F, 5.0F, 5.0F, 5.0F)
            horizontalLineToRelative(4.0F)
            verticalLineToRelative(-1.9F)
            lineTo(7.0F, 15.1F)
            curveToRelative(-1.71F, 0.0F, -3.1F, -1.39F, -3.1F, -3.1F)

            moveTo(8.0F, 13.0F)
            horizontalLineToRelative(8.0F)
            verticalLineToRelative(-2.0F)
            lineTo(8.0F, 11.0F)
            verticalLineToRelative(2.0F)

            moveTo(17.0F, 7.0F)
            horizontalLineToRelative(-4.0F)
            verticalLineToRelative(1.9F)
            horizontalLineToRelative(4.0F)
            curveToRelative(1.71F, 0.0F, 3.1F, 1.39F, 3.1F, 3.1F)
            reflectiveCurveToRelative(-1.39F, 3.1F, -3.1F, 3.1F)
            horizontalLineToRelative(-4.0F)
            lineTo(13.0F, 17.0F)
            horizontalLineToRelative(4.0F)
            curveToRelative(2.76F, 0.0F, 5.0F, -2.24F, 5.0F, -5.0F)
            reflectiveCurveToRelative(-2.24F, -5.0F, -5.0F, -5.0F)
            close()
        }
    }
    return _icLink!!
}

private var _icLink: ImageVector? = null

