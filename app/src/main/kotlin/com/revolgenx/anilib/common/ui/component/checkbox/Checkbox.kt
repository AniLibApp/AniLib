package com.revolgenx.anilib.common.ui.component.checkbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun TextCheckbox(
    text: String,
    checked: Boolean,
    onCheckChange: (checked: Boolean) -> Unit
) {
    val (checkedState, onStateChange) = remember(checked) { mutableStateOf(checked) }
    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .toggleable(
                value = checkedState,
                onValueChange = {
                    val newState = !checkedState
                    onStateChange(newState)
                    onCheckChange(newState)
                },
                role = Role.Checkbox
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}


@Composable
fun TextTriStateCheckbox(
    text: String,
    toggleState: ToggleableState,
    onToggleStateChange: (currentState: ToggleableState) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 12.dp)
            .triStateToggleable(
                state = toggleState,
                onClick = {
                    val newState = when(toggleState){
                        ToggleableState.On -> ToggleableState.Indeterminate
                        ToggleableState.Off -> ToggleableState.On
                        ToggleableState.Indeterminate -> ToggleableState.Off
                    }
                    onToggleStateChange(newState)
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TriStateCheckbox(
            state = toggleState,
            onClick = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Preview
@Composable
fun TextCheckboxPreview() {
    TextCheckbox(text = "Text Checkbox", checked = true, onCheckChange = {})
}