package com.revolgenx.anilib.common.ui.screen.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCalendar
import com.revolgenx.anilib.common.ui.icons.appicon.IcSadAnilib
import anilib.i18n.R as I18nR


@Composable
fun EmptyScreen() {
    EmptyScreenLayout(modifier = Modifier.fillMaxSize())
}

@Composable
fun LazyItemScope.EmptyScreen() {
    EmptyScreenLayout(modifier = Modifier.fillParentMaxSize())
}

@Composable
fun EmptySection() {
    EmptyScreenLayout(modifier = Modifier.fillMaxWidth())
}

@Composable
fun EmptyScreenLayout(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(56.dp),
                imageVector = AppIcons.IcSadAnilib,
                contentDescription = stringResource(
                    id = I18nR.string.its_empty
                )
            )
            Text(
                stringResource(id = I18nR.string.its_empty),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
fun EmptyScreenPreview() {
    EmptyScreen()
}