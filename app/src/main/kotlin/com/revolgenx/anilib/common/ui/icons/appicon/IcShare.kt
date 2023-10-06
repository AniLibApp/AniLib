import androidx.compose.foundation.Image
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcShare: ImageVector
    get() {
        if (_icShare != null) {
            return _icShare!!
        }
        _icShare = ImageVector.Builder(
            name = "IcShare", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).materialPath {
            moveTo(18.0F, 16.08F)
            curveToRelative(-0.76F, 0.0F, -1.44F, 0.3F, -1.96F, 0.77F)
            lineTo(8.91F, 12.7F)
            curveToRelative(0.05F, -0.23F, 0.09F, -0.46F, 0.09F, -0.7F)
            reflectiveCurveToRelative(-0.04F, -0.47F, -0.09F, -0.7F)
            lineToRelative(7.05F, -4.11F)
            curveToRelative(0.54F, 0.5F, 1.25F, 0.81F, 2.04F, 0.81F)
            curveToRelative(1.66F, 0.0F, 3.0F, -1.34F, 3.0F, -3.0F)
            reflectiveCurveToRelative(-1.34F, -3.0F, -3.0F, -3.0F)
            reflectiveCurveToRelative(-3.0F, 1.34F, -3.0F, 3.0F)
            curveToRelative(0.0F, 0.24F, 0.04F, 0.47F, 0.09F, 0.7F)
            lineTo(8.04F, 9.81F)
            curveTo(7.5F, 9.31F, 6.79F, 9.0F, 6.0F, 9.0F)
            curveToRelative(-1.66F, 0.0F, -3.0F, 1.34F, -3.0F, 3.0F)
            reflectiveCurveToRelative(1.34F, 3.0F, 3.0F, 3.0F)
            curveToRelative(0.79F, 0.0F, 1.5F, -0.31F, 2.04F, -0.81F)
            lineToRelative(7.12F, 4.16F)
            curveToRelative(-0.05F, 0.21F, -0.08F, 0.43F, -0.08F, 0.65F)
            curveToRelative(0.0F, 1.61F, 1.31F, 2.92F, 2.92F, 2.92F)
            reflectiveCurveToRelative(2.92F, -1.31F, 2.92F, -2.92F)
            reflectiveCurveToRelative(-1.31F, -2.92F, -2.92F, -2.92F)

            close()
        }.build()
        return _icShare!!
    }
private var _icShare: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconIcSharePreview() {
    Image(imageVector = AppIcons.IcShare, contentDescription = null)
}