package com.revolgenx.anilib.common.ui.component.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomBarLayout(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomNestedScrollConnection = remember {
        BottomNestedScrollConnection(state = scrollState)
    }

    Box(
        modifier = modifier
            .nestedScroll(bottomNestedScrollConnection)
    ) {
        content()
        AnimatedVisibility(
            visible = scrollState.value == ScrollState.ScrollDown,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box() {
                bottomBar()
            }
        }
    }
}

sealed class ScrollState {
    object ScrollUp : ScrollState()
    object ScrollDown : ScrollState()
}

class BottomNestedScrollConnection(
    private val state: MutableState<ScrollState> = mutableStateOf(ScrollState.ScrollDown)
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (available.y > 2) {
            state.value = ScrollState.ScrollDown
        } else if (available.y < -2) {
            state.value = ScrollState.ScrollUp
        }
        return super.onPreScroll(available, source)
    }
}

@Preview
@Composable
fun BottomBarLayoutPreview() {
    BottomBarLayout(bottomBar = {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Bottom Bar")
        }
    }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Bottom Bar content")
        }
    }
}