package com.revolgenx.anilib.common.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.theme.onSecondaryContainer
import com.revolgenx.anilib.common.ui.theme.onSurface
import com.revolgenx.anilib.common.ui.theme.secondaryContainer
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.common.ui.theme.typography


@Composable
fun SegmentedButton(
    items: Array<String>,
    selectedPosition: Int,
    onItemSelected: (index: Int) -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, item ->
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            onItemSelected(index)
                        },
                    color = if (index == selectedPosition) secondaryContainer else surfaceContainer
                ) {
                    Box {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = item,
                            style = typography().labelLarge,
                            color = if (index == selectedPosition) onSecondaryContainer else onSurface
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SegmentedButtonPreview() {
    SegmentedButton(items = arrayOf("Day", "Week", "Month"), selectedPosition = 0) {

    }
}