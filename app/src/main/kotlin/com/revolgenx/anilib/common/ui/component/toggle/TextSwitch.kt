package com.revolgenx.anilib.common.ui.component.toggle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.setting.ui.component.PrefsHorizontalPadding
import com.revolgenx.anilib.setting.ui.component.PrefsVerticalPadding
import com.revolgenx.anilib.setting.ui.component.TitleFontSize

@Composable
fun TextSwitch(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
) {
    val isChecked = remember { mutableStateOf(checked) }
    Row(
        modifier = modifier
            .clickable(onClick = {
                isChecked.value.not().let { newCheckValue ->
                    isChecked.value = newCheckValue
                    onCheckedChanged(newCheckValue)
                }
            }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(vertical = PrefsVerticalPadding),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = PrefsHorizontalPadding),
                text = text,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.titleLarge,
                fontSize = TitleFontSize,
            )
        }
        Box(
            modifier = Modifier.padding(end = PrefsHorizontalPadding)
        )
        {
            Switch(
                checked = isChecked.value,
                onCheckedChange = null,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextSwitchPreview() {
    TextSwitch(text = "Switch", checked = false) {

    }
}