package com.revolgenx.anilib.common.ui.icons.appicon


import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcHistory: ImageVector
    get() {
        if (_icHistory != null) {
            return _icHistory!!
        }
        _icHistory = materialIcon(name = "IcHistory") {
            materialPath {
                moveTo(13.26F, 3.0F)
                curveTo(8.17F, 2.86F, 4.0F, 6.95F, 4.0F, 12.0F)
                lineTo(2.21F, 12.0F)
                curveToRelative(-0.45F, 0.0F, -0.67F, 0.54F, -0.35F, 0.85F)
                lineToRelative(2.79F, 2.8F)
                curveToRelative(0.2F, 0.2F, 0.51F, 0.2F, 0.71F, 0.0F)
                lineToRelative(2.79F, -2.8F)
                curveToRelative(0.31F, -0.31F, 0.09F, -0.85F, -0.36F, -0.85F)
                lineTo(6.0F, 12.0F)
                curveToRelative(0.0F, -3.9F, 3.18F, -7.05F, 7.1F, -7.0F)
                curveToRelative(3.72F, 0.05F, 6.85F, 3.18F, 6.9F, 6.9F)
                curveToRelative(0.05F, 3.91F, -3.1F, 7.1F, -7.0F, 7.1F)
                curveToRelative(-1.61F, 0.0F, -3.1F, -0.55F, -4.28F, -1.48F)
                curveToRelative(-0.4F, -0.31F, -0.96F, -0.28F, -1.32F, 0.08F)
                curveToRelative(-0.42F, 0.42F, -0.39F, 1.13F, 0.08F, 1.49F)
                curveTo(9.0F, 20.29F, 10.91F, 21.0F, 13.0F, 21.0F)
                curveToRelative(5.05F, 0.0F, 9.14F, -4.17F, 9.0F, -9.26F)
                curveToRelative(-0.13F, -4.69F, -4.05F, -8.61F, -8.74F, -8.74F)

                moveTo(12.75F, 8.0F)
                curveToRelative(-0.41F, 0.0F, -0.75F, 0.34F, -0.75F, 0.75F)
                verticalLineToRelative(3.68F)
                curveToRelative(0.0F, 0.35F, 0.19F, 0.68F, 0.49F, 0.86F)
                lineToRelative(3.12F, 1.85F)
                curveToRelative(0.36F, 0.21F, 0.82F, 0.09F, 1.03F, -0.26F)
                curveToRelative(0.21F, -0.36F, 0.09F, -0.82F, -0.26F, -1.03F)
                lineToRelative(-2.88F, -1.71F)
                verticalLineToRelative(-3.4F)
                curveToRelative(0.0F, -0.4F, -0.34F, -0.74F, -0.75F, -0.74F)
                close()
            }
        }
        return _icHistory!!
    }

private var _icHistory: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconIcHistoryPreview() {
    Icon(imageVector = AppIcons.IcHistory, contentDescription = null)
}