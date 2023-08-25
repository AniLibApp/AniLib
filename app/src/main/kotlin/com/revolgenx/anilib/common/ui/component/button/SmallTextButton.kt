package com.revolgenx.anilib.common.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ui.theme.primary
import com.revolgenx.anilib.common.ui.theme.shapes
import com.revolgenx.anilib.common.util.OnClick


@Composable
fun SmallTextButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = primary,
    fontSize: TextUnit = 12.sp,
    onClick: OnClick
) {
    Box(
        modifier = modifier
            .clip(shapes().small)
            .clickable(onClick = onClick)
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize
        )
    }
}