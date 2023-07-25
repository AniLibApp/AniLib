package com.revolgenx.anilib.user.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.viewmodel.UserStatsMediaType
import com.revolgenx.anilib.user.ui.viewmodel.UserStatsOverviewViewModel
import com.revolgenx.anilib.user.ui.viewmodel.UserStatsType
import com.revolgenx.anilib.user.ui.viewmodel.UserStatsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStatsScreen(userId: Int?, type: MediaType) {
    val viewModel: UserStatsViewModel = koinViewModel()
    val overviewViewModel: UserStatsOverviewViewModel =
        koinViewModel(named(if (type.isAnime()) UserStatsMediaType.USER_STATS_ANIME else UserStatsMediaType.USER_STATS_MANGA))
    overviewViewModel.field.userId = userId

    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomNestedScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    ScreenScaffold(
        floatingActionButton = {
            DisappearingFAB(
                scrollState = scrollState,
                content = {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(text = stringResource(id = viewModel.statsScreenType.value.res))
                    }
                },
                onClick = {

                }
            )
        },
        topBar = {},
        navigationIcon = {},
        actions = {},
        contentWindowInsets = emptyWindowInsets(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomNestedScrollConnection)
        ) {
            when (viewModel.statsScreenType.value) {
                UserStatsType.OVERVIEW -> UserStatsOverviewScreen(type, overviewViewModel)
                UserStatsType.GENRE -> TODO()
                UserStatsType.TAGS -> TODO()
                UserStatsType.VOICE_ACTORS -> TODO()
                UserStatsType.STUDIO -> TODO()
                UserStatsType.STAFF -> TODO()
            }
        }
    }
}

