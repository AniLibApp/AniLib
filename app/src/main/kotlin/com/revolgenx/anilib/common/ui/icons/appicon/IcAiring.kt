import androidx.compose.foundation.Image
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons

val AppIcons.IcAiring: ImageVector
    get() {
        if (_icAiring != null) {
            return _icAiring!!
        }
        _icAiring = ImageVector.Builder(
            name = "IcAiring",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F,
        ).materialPath {
            moveTo(11.99F, 2.0F)
            curveTo(6.47F, 2.0F, 2.0F, 6.48F, 2.0F, 12.0F)
            reflectiveCurveToRelative(4.47F, 10.0F, 9.99F, 10.0F)
            curveTo(17.52F, 22.0F, 22.0F, 17.52F, 22.0F, 12.0F)
            reflectiveCurveTo(17.52F, 2.0F, 11.99F, 2.0F)

            moveTo(12.0F, 20.0F)
            curveToRelative(-4.42F, 0.0F, -8.0F, -3.58F, -8.0F, -8.0F)
            reflectiveCurveToRelative(3.58F, -8.0F, 8.0F, -8.0F)
            reflectiveCurveToRelative(8.0F, 3.58F, 8.0F, 8.0F)
            reflectiveCurveToRelative(-3.58F, 8.0F, -8.0F, 8.0F)

            moveTo(12.5F, 7.0F)
            horizontalLineTo(11.0F)
            verticalLineToRelative(6.0F)
            lineToRelative(5.25F, 3.15F)
            lineToRelative(0.75F, -1.23F)
            lineToRelative(-4.5F, -2.67F)

            close()
        }.build()
        return _icAiring!!
    }
private var _icAiring: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconIcAiringPreview() {
    Image(imageVector = AppIcons.IcAiring, contentDescription = null)
}