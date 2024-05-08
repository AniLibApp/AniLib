package com.revolgenx.anilib.setting.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import anilib.i18n.R as I18nR


data class ListPreferenceEntry<T>(val title: String, val value: T?)

@Composable
fun <T> ListPreferenceItem(
    value: T?,
    title: String,
    entries: List<ListPreferenceEntry<out T>>,
    subtitle: String? = entries.firstOrNull { it.value == value }?.title,
    icon: ImageVector? = null,
    onValueChange: (T?) -> Unit,
) {
    var isDialogShown by remember { mutableStateOf(false) }

    TextPreferenceItem(
        title = title,
        subtitle = subtitle,
        icon = icon,
        onClick = { isDialogShown = true },
    )

    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = { isDialogShown = false },
            title = { Text(text = title) },
            text = {
                Box {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        entries.forEach { current ->
                            val isSelected = value == current.value
                            DialogRow(
                                label = current.title,
                                isSelected = isSelected,
                                onSelected = {
                                    onValueChange(current.value)
                                    isDialogShown = false
                                },
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { isDialogShown = false }) {
                    Text(text = stringResource(I18nR.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun DialogRow(
    label: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .selectable(
                selected = isSelected,
                onClick = { if (!isSelected) onSelected() },
            )
            .fillMaxWidth()
            .minimumInteractiveComponentSize(),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.merge(),
            modifier = Modifier.padding(start = 24.dp),
        )
    }
}