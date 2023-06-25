package com.revolgenx.anilib.home.recommendation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.theme.colorScheme
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.ui.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.media.ui.component.MediaItemRowContent
import com.revolgenx.anilib.media.ui.component.MediaRowItemContentEnd
import com.revolgenx.anilib.type.RecommendationRating
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen(viewModel: RecommendationViewModel = koinViewModel()) {
    val navigator = localNavigator()
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            DisappearingFAB(scrollState = scrollState, iconRes = R.drawable.ic_filter) {
                //todo filter
            }
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
    ) {
        val pagingItems = viewModel.collectAsLazyPagingItems()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(bottomScrollConnection)
        ) {
            LazyPagingList(
                pagingItems = pagingItems,
                onRefresh = {
                    viewModel.refresh()
                },
            ) { model ->
                model ?: return@LazyPagingList
                RecommendationItem(model = model) {id, type->
                    navigator.mediaScreen(id, type)
                }
            }
        }
    }
}


@Composable
fun RecommendationItem(model: RecommendationModel, onMediaClick: OnMediaClick) {
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
                        MediaItemRowContent(media = it, onMediaClick = onMediaClick)
                    }
                }

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    model.mediaRecommendation?.let {
                        MediaRowItemContentEnd(media = it, onMediaClick = onMediaClick)
                    }
                }
            }

            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            ) {
                RecommendationButton(model = model)
            }
        }
    }
}

@Composable
private fun RecommendationButton(
    model: RecommendationModel
) {
    Surface(
        shape = ShapeDefaults.ExtraLarge,
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.clickable {
                    /*todo filter*/
                }
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.ic_thumb_up),
                    contentDescription = null,
                    tint = if (model.userRating == RecommendationRating.RATE_UP) colorScheme().primary else colorScheme().onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier.clickable {
                        /*todo filter*/
                }
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(14.dp),
                    painter = painterResource(id = R.drawable.ic_thumb_down),
                    contentDescription = null,
                    tint = if (model.userRating == RecommendationRating.RATE_DOWN) colorScheme().primary else colorScheme().onSurfaceVariant
                )
            }

            MediumText(modifier = Modifier.padding(end = 4.dp), text = model.rating.toString(), fontSize = 11.sp)
        }
    }
}
