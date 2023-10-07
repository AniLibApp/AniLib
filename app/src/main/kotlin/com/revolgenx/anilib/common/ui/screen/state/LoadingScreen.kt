package com.revolgenx.anilib.common.ui.screen.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics


@Composable
fun LoadingScreen() {
    LoadingLayout(modifier = Modifier.fillMaxSize())
}

@Composable
fun LazyItemScope.LoadingScreen() {
    LoadingLayout(modifier = Modifier.fillParentMaxSize())
}

@Composable
fun LoadingSection(
    modifier: Modifier = Modifier,
    typeRow: Boolean = false,
) {
    LoadingLayout(modifier = if (typeRow) modifier.fillMaxHeight() else modifier.fillMaxWidth())
}

@Composable
fun LinearLoadingSection() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
    )
}

@Composable
fun LoadingLayout(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}