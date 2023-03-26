package com.revolgenx.anilib.common.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Button
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

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    retry: () -> Unit
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
                painter = painterResource(id = R.drawable.ic_error_anilib),
                contentDescription = stringResource(
                    id = R.string.its_empty
                )
            )
            Text(
                stringResource(id = R.string.something_went_wrong),
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(onClick = { retry.invoke() }) {
                Text(stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
fun LazyItemScope.ErrorScreen(retry: () -> Unit) {
    ErrorScreen(
        modifier = Modifier.fillParentMaxSize(),
        retry = retry
    )
}


@Composable
fun ErrorLayout(
    retry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(56.dp),
                painter = painterResource(id = R.drawable.ic_error_anilib),
                contentDescription = stringResource(
                    id = R.string.its_empty
                )
            )
            Text(
                stringResource(id = R.string.something_went_wrong),
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(onClick = { retry.invoke() }) {
                Text(stringResource(id = R.string.retry))
            }
        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen {

    }
}