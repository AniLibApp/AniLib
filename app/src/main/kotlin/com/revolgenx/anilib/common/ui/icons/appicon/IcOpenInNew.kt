import androidx.compose.foundation.Image
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcOpenInNew: ImageVector
    get() {
        if (_icOpenInNew != null) {
            return _icOpenInNew!!
        }
        _icOpenInNew = ImageVector.Builder(
            name = "IcOpenInNew", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).materialPath {
            moveTo(18.0F, 19.0F)
            horizontalLineTo(6.0F)
            curveToRelative(-0.55F, 0.0F, -1.0F, -0.45F, -1.0F, -1.0F)
            verticalLineTo(6.0F)
            curveToRelative(0.0F, -0.55F, 0.45F, -1.0F, 1.0F, -1.0F)
            horizontalLineToRelative(5.0F)
            curveToRelative(0.55F, 0.0F, 1.0F, -0.45F, 1.0F, -1.0F)
            reflectiveCurveToRelative(-0.45F, -1.0F, -1.0F, -1.0F)
            horizontalLineTo(5.0F)
            curveToRelative(-1.11F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
            verticalLineToRelative(14.0F)
            curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
            horizontalLineToRelative(14.0F)
            curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
            verticalLineToRelative(-6.0F)
            curveToRelative(0.0F, -0.55F, -0.45F, -1.0F, -1.0F, -1.0F)
            reflectiveCurveToRelative(-1.0F, 0.45F, -1.0F, 1.0F)
            verticalLineToRelative(5.0F)
            curveToRelative(0.0F, 0.55F, -0.45F, 1.0F, -1.0F, 1.0F)

            moveTo(14.0F, 4.0F)
            curveToRelative(0.0F, 0.55F, 0.45F, 1.0F, 1.0F, 1.0F)
            horizontalLineToRelative(2.59F)
            lineToRelative(-9.13F, 9.13F)
            curveToRelative(-0.39F, 0.39F, -0.39F, 1.02F, 0.0F, 1.41F)
            curveToRelative(0.39F, 0.39F, 1.02F, 0.39F, 1.41F, 0.0F)
            lineTo(19.0F, 6.41F)
            verticalLineTo(9.0F)
            curveToRelative(0.0F, 0.55F, 0.45F, 1.0F, 1.0F, 1.0F)
            reflectiveCurveToRelative(1.0F, -0.45F, 1.0F, -1.0F)
            verticalLineTo(4.0F)
            curveToRelative(0.0F, -0.55F, -0.45F, -1.0F, -1.0F, -1.0F)
            horizontalLineToRelative(-5.0F)
            curveToRelative(-0.55F, 0.0F, -1.0F, 0.45F, -1.0F, 1.0F)

            close()
        }.build()
        return _icOpenInNew!!
    }
private var _icOpenInNew: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconIcOpenInNewPreview() {
    Image(imageVector = AppIcons.IcOpenInNew, contentDescription = null)
}