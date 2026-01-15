package com.revolgenx.anilib.common.ui.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAbout
import com.revolgenx.anilib.common.util.OnClick


@Composable
fun BadgeIconButton(
    icon: ImageVector,
    showBadge: Boolean = false,
    badgeText: String? = null,
    contentDescription: String? = null,
    onClick: OnClick
) {

    IconButton (
        modifier = Modifier,
        shape = RectangleShape,
        onClick = onClick
    ) {
    BadgedBox(
        badge = {
            if (showBadge) {
                Badge(
                    modifier = badgeText?.let { Modifier.offset((-8).dp) } ?: Modifier.size(10.dp),
                    content = badgeText?.let {
                        {
                            Text(text = it)
                        }
                    })
            }
        }
    ) {
            Icon(modifier = Modifier.padding(4.dp), imageVector = icon, contentDescription = contentDescription)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeIconButtonPreview() {
    BadgeIconButton(AppIcons.IcAbout, showBadge = true, badgeText = "1") {

    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeIconButtonPreview1() {
    BadgeIconButton(AppIcons.IcAbout, showBadge = true) {

    }
}