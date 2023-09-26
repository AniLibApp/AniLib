package com.revolgenx.anilib.common.ui.screen.state

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcErrorAnilib
import com.revolgenx.anilib.common.util.OnClick


@Composable
fun ErrorScreen(
    error: String? = null,
    retry: OnClick
) {
    ErrorLayout(
        modifier = Modifier.fillMaxSize(),
        error = error,
        retry = retry
    )
}

@Composable
fun LazyItemScope.ErrorScreen(
    error: String? = null,
    retry: OnClick
) {
    ErrorLayout(
        modifier = Modifier.fillParentMaxSize(),
        error = error,
        retry = retry
    )
}

@Composable
fun ErrorSection(
    modifier: Modifier = Modifier,
    error: String? = null,
    retry: OnClick
) {
    ErrorLayout(
        modifier = modifier.fillMaxWidth(),
        error = error,
        retry = retry
    )
}


@Composable
fun ErrorLayout(
    modifier: Modifier = Modifier,
    error: String? = null,
    retry: OnClick
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
                imageVector = AppIcons.IcErrorAnilib,
                contentDescription = stringResource(
                    id = R.string.something_went_wrong
                )
            )
            Text(
                stringResource(id = R.string.something_went_wrong),
                style = MaterialTheme.typography.bodyLarge,
            )
            error?.let {
                Text(
                    error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
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