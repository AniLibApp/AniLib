package com.revolgenx.anilib.setting.ui.component


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun BasePreferenceItem(
    modifier: Modifier = Modifier,
    title: String? = null,
    subcomponent: @Composable (ColumnScope.() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    widget: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .let { onClick?.let { c -> it.clickable { c.invoke() } } ?: it }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier.padding(start = PrefsHorizontalPadding, end = 8.dp),
                content = { icon() },
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = PrefsVerticalPadding),
        ) {
            if (!title.isNullOrBlank()) {
                Text(
                    modifier = Modifier.padding(horizontal = PrefsHorizontalPadding),
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = TitleFontSize,
                )
            }
            subcomponent?.invoke(this)
        }
        if (widget != null) {
            Box(
                modifier = Modifier.padding(end = PrefsHorizontalPadding),
                content = { widget() },
            )
        }
    }
}

fun Modifier.prefPadding() = this.padding(horizontal = PrefsHorizontalPadding, vertical = PrefsVerticalPadding)

internal val PrefsHorizontalPadding = 16.dp
internal val PrefsVerticalPadding = 16.dp
internal val TitleFontSize = 16.sp