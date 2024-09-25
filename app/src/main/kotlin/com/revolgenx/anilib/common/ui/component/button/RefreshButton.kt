package com.revolgenx.anilib.common.ui.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.revolgenx.anilib.common.util.OnClick

@Composable
fun BoxScope.RefreshButton(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onClick: OnClick
) {
    if (visible) {
        Button(
            modifier = modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp),
            onClick = onClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                Text(
                    text = stringResource(id = R.string.refresh),
                    fontSize = 14.sp
                )
            }
        }
    }
}