package com.revolgenx.anilib.user.ui.screen.userStats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ext.emptyWindowInsets
import com.revolgenx.anilib.common.ext.hideBottomSheet
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.radio.TextRadioButton
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.theme.background
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsGenreViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsOverviewViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsStaffViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsStudioViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsTagsViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsType
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsTypeMediaType
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsViewModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsVoiceActorsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStatsScreen(userId: Int?, type: MediaType) {
    val isAnime = type.isAnime()
    val viewModel: UserStatsViewModel = koinViewModel(named(if (isAnime) UserStatsTypeMediaType.USER_STATS_ANIME else UserStatsTypeMediaType.USER_STATS_MANGA))
    val overviewViewModel: UserStatsOverviewViewModel =
        koinViewModel(named(if (isAnime) UserStatsTypeMediaType.USER_STATS_OVERVIEW_ANIME else UserStatsTypeMediaType.USER_STATS_OVERVIEW_MANGA))
    val genreViewModel: UserStatsGenreViewModel =
        koinViewModel(named(if (isAnime) UserStatsTypeMediaType.USER_STATS_GENRE_ANIME else UserStatsTypeMediaType.USER_STATS_GENRE_MANGA))
    val tagsViewModel: UserStatsTagsViewModel =
        koinViewModel(named(if (isAnime) UserStatsTypeMediaType.USER_STATS_TAGS_ANIME else UserStatsTypeMediaType.USER_STATS_TAGS_MANGA))
    val staffViewModel: UserStatsStaffViewModel =
        koinViewModel(named(if (isAnime) UserStatsTypeMediaType.USER_STATS_STAFF_ANIME else UserStatsTypeMediaType.USER_STATS_STAFF_MANGA))
     val studioViewModel: UserStatsStudioViewModel = koinViewModel()
     val voiceActorsViewModel: UserStatsVoiceActorsViewModel = koinViewModel()

    overviewViewModel.field.userId = userId
    genreViewModel.field.userId = userId
    tagsViewModel.field.userId = userId
    staffViewModel.field.userId = userId
    studioViewModel.field.userId = userId
    voiceActorsViewModel.field.userId = userId


    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomNestedScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    val openUserStatsTypeBottomSheet = rememberSaveable { mutableStateOf(false) }


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
                    openUserStatsTypeBottomSheet.value = true
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
                UserStatsType.GENRE -> UserStatsTypeScreen(type, genreViewModel)
                UserStatsType.TAGS -> UserStatsTypeScreen(type, tagsViewModel)
                UserStatsType.VOICE_ACTORS -> UserStatsTypeScreen(type, voiceActorsViewModel)
                UserStatsType.STUDIO -> UserStatsTypeScreen(type, studioViewModel)
                UserStatsType.STAFF -> UserStatsTypeScreen(type, staffViewModel)
            }
        }
    }


    UserStatsTypeBottomSheet(
        openBottomSheet = openUserStatsTypeBottomSheet,
        bottomSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        userStatsTypes = UserStatsType.values()
            .filter { if (isAnime) true else it != UserStatsType.STUDIO && it != UserStatsType.VOICE_ACTORS }
            .associateBy { stringResource(id = it.res) },
        selectedUserStatsType = viewModel.statsScreenType.value,
        onUserStatsTypeSelected = {
            viewModel.statsScreenType.value = it
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserStatsTypeBottomSheet(
    openBottomSheet: MutableState<Boolean>,
    bottomSheetState: SheetState,
    userStatsTypes: Map<String, UserStatsType>,
    selectedUserStatsType: UserStatsType,
    onUserStatsTypeSelected: (type: UserStatsType) -> Unit
) {
    val scope = rememberCoroutineScope()

    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState,
            containerColor = background
        ) {
            Column(
                modifier = Modifier
                    .selectableGroup()
                    .verticalScroll(rememberScrollState())
            ) {
                userStatsTypes.forEach { (k, v) ->
                    TextRadioButton(text = k, selected = selectedUserStatsType == v) {
                        scope.hideBottomSheet(bottomSheetState, openBottomSheet)
                        onUserStatsTypeSelected(v)
                    }
                }
            }
        }
    }
}

