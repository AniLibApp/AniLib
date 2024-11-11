package com.revolgenx.anilib.home.recommendation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import com.revolgenx.anilib.common.ui.component.card.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.dokar.sheets.rememberBottomSheetState
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.showLoginMsg
import com.revolgenx.anilib.common.ui.component.action.BottomSheetConfirmation
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.toggle.TextSwitch
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import com.revolgenx.anilib.common.ui.composition.LocalUserState
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcThumbDown
import com.revolgenx.anilib.common.ui.icons.appicon.IcThumbUp
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.home.recommendation.data.field.MediaRecommendationSort
import com.revolgenx.anilib.home.recommendation.data.field.RecommendationField
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.ui.viewmodel.RecommendationFilterViewModel
import com.revolgenx.anilib.home.recommendation.ui.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.MediaRowCommonContent
import com.revolgenx.anilib.media.ui.component.MediaRowCommonContentEnd
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
import com.revolgenx.anilib.type.RecommendationRating
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen() {
    val viewModel: RecommendationViewModel = koinViewModel()
    val filterViewModel: RecommendationFilterViewModel = koinViewModel()
    val filterBottomSheetState = rememberBottomSheetState()

    val context = localContext()
    val navigator = localNavigator()
    val scope = rememberCoroutineScope()
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }


    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            DisappearingFAB(scrollState = scrollState, icon = AppIcons.IcFilter) {
                filterViewModel.field = viewModel.field.copy()
                scope.launch {
                    filterBottomSheetState.expand()
                }
            }
        },
        bottomNestedScrollConnection = bottomScrollConnection,
    ) { snackbarHostState ->

        LaunchedEffect(viewModel.showToggleErrorMsg.value) {
            if (viewModel.showToggleErrorMsg.value) {
                snackbarHostState.showSnackbar(
                    context.getString(anilib.i18n.R.string.operation_failed),
                    withDismissAction = true
                )
                viewModel.showToggleErrorMsg.value = false
            }
        }

        val mediaComponentState = rememberMediaComponentState(navigator = navigator)
        RecommendationPagingContent(viewModel, mediaComponentState)
        RecommendationFilterBottomSheet(
            bottomSheetState = filterBottomSheetState,
            viewModel = filterViewModel
        ) {
            viewModel.field = it
            viewModel.refresh()
        }
    }
}

@Composable
private fun RecommendationPagingContent(
    viewModel: RecommendationViewModel,
    mediaComponentState: MediaComponentState
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    val localUser = localUser()
    val isLoggedIn = localUser.isLoggedIn
    val snackbar = localSnackbarHostState()
    val context = localContext()
    val scope = rememberCoroutineScope()

    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        },
    ) { model ->
        model ?: return@LazyPagingList
        RecommendationItem(
            model = model,
            mediaComponentState = mediaComponentState,
            onLike = {
                if (isLoggedIn) {
                    viewModel.likeRecommendation(model)
                } else {
                    snackbar.showLoginMsg(context = context, scope)
                }
            },
            onDislike = {
                if (isLoggedIn) {
                    viewModel.dislikeRecommendation(model)
                } else {
                    snackbar.showLoginMsg(context = context, scope)
                }
            }
        )
    }
}


@Composable
fun RecommendationItem(
    model: RecommendationModel,
    mediaComponentState: MediaComponentState,
    onLike: OnClick,
    onDislike: OnClick
) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .height(124.dp)
    ) {
        Box {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    model.media?.let {
                        MediaRowCommonContent(media = it, mediaComponentState = mediaComponentState)
                    }
                }

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    model.mediaRecommendation?.let {
                        MediaRowCommonContentEnd(
                            media = it,
                            mediaComponentState = mediaComponentState
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            ) {
                RecommendationButton(
                    model = model,
                    onLike = onLike,
                    onDislike = onDislike
                )
            }
        }
    }
}

@Composable
private fun RecommendationButton(
    model: RecommendationModel,
    onLike: OnClick,
    onDislike: OnClick
) {
    Surface(
        shape = CircleShape,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconToggleButton(
                modifier = Modifier.size(32.dp),
                checked = model.userRating.value == RecommendationRating.RATE_UP,
                onCheckedChange = {
                    onLike()
                }) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(14.dp),
                    imageVector = AppIcons.IcThumbUp,
                    contentDescription = null,
                )
            }


            IconToggleButton(
                modifier = Modifier.size(32.dp),
                checked = model.userRating.value == RecommendationRating.RATE_DOWN,
                onCheckedChange = {
                    onDislike()
                }) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(14.dp),
                    imageVector = AppIcons.IcThumbDown,
                    contentDescription = null,
                )
            }

            MediumText(
                modifier = Modifier.padding(end = 4.dp),
                text = model.rating.intValue.toString(),
                fontSize = 11.sp
            )
        }
    }
}


@Composable
private fun RecommendationFilterBottomSheet(
    bottomSheetState: BottomSheetState,
    viewModel: RecommendationFilterViewModel,
    onFilter: (field: RecommendationField) -> Unit
) {
    val scope = rememberCoroutineScope()
    val dismiss: () -> Unit = {
        scope.launch {
            bottomSheetState.collapse()
        }
    }

    BottomSheet(
        state = bottomSheetState,
        skipPeeked = true,
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
    ) {
        RecommendationFilterBottomSheetContent(
            viewModel = viewModel,
            dismiss = dismiss,
            onFilter = onFilter
        )
    }
}

@Composable
private fun RecommendationFilterBottomSheetContent(
    viewModel: RecommendationFilterViewModel,
    dismiss: () -> Unit,
    onFilter: (field: RecommendationField) -> Unit
) {
    val field = viewModel.field
    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
    ) {
        BottomSheetConfirmation(
            onConfirm = {
                onFilter(field)
                dismiss()
            },
            onDismiss = {
                dismiss()
            }
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
        ) {

            SelectMenu(
                entries = stringArrayResource(id = R.array.recommendation_sort_menu),
                selectedItemPosition = field.sort.ordinal
            ) { selectedSort ->
                field.sort = MediaRecommendationSort.entries[selectedSort]
            }

            TextSwitch(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = anilib.i18n.R.string.on_list),
                checked = field.onList,
                onCheckedChanged = {
                    field.onList = it
                })
        }
    }
}