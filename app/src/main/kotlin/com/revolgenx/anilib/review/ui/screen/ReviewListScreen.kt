package com.revolgenx.anilib.review.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.reviewScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.action.DisappearingFAB
import com.revolgenx.anilib.common.ui.component.bottombar.BottomNestedScrollConnection
import com.revolgenx.anilib.common.ui.component.bottombar.ScrollState
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.SmallLightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.shadow
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.theme.review_list_gradient_bottom
import com.revolgenx.anilib.common.ui.theme.review_list_gradient_top
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewListViewModel
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcThumbUp
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewListScreen(
    viewModel: ReviewListViewModel = koinViewModel()
) {
    val navigator = localNavigator()
    val scrollState = remember { mutableStateOf<ScrollState>(ScrollState.ScrollDown) }
    val bottomScrollConnection =
        remember { BottomNestedScrollConnection(state = scrollState) }

    ScreenScaffold(
        topBar = {},
        floatingActionButton = {
            DisappearingFAB(scrollState = scrollState, icon = AppIcons.IcFilter) {
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
                ReviewListItem(model,
                    onUserClick = {
                        navigator.userScreen(it)
                    },
                    onMediaClick = { id, type ->
                        navigator.mediaScreen(id, type)
                    }) {
                    navigator.reviewScreen(it)
                }
            }
        }
    }
}


@Composable
fun ReviewListItem(
    model: ReviewModel,
    onUserClick: OnClickWithValue<Int>,
    onMediaClick: OnMediaClick,
    onReviewClick: OnClickWithValue<Int>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onReviewClick(model.id) }
        ) {
            Box(
                modifier = Modifier
                    .height(106.dp)
                    .fillMaxWidth()
            ) {
                ImageAsync(
                    modifier = Modifier
                        .fillMaxSize(),
                    imageUrl = model.media?.bannerImage,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    review_list_gradient_top,
                                    review_list_gradient_bottom
                                )
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(start = 8.dp, end = 8.dp, top = 12.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ImageAsync(
                            modifier = Modifier
                                .size(38.dp)
                                .width(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    model.userId.let {
                                        onUserClick(it)
                                    }
                                },
                            imageUrl = model.user?.avatar?.large,
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            ),
                            previewPlaceholder = R.drawable.bleach
                        )

                        Column {
                            MediaTitleType {
                                Text(
                                    modifier = Modifier
                                        .clickable {
                                            onMediaClick(model.mediaId, model.media?.type)
                                        },
                                    text = stringResource(id = I18nR.string.review_of_s_by_s).format(
                                        model.media?.title?.title(it),
                                        model.user?.name.naText()
                                    ),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.2.sp,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    style = LocalTextStyle.current.copy(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(2.0f, 2.0f),
                                            blurRadius = 1f
                                        )
                                    )
                                )
                            }
                            Text(
                                text = stringResource(id = I18nR.string.review_score_format).format(
                                    model.score,
                                ),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                letterSpacing = 0.2.sp,
                                color = Color.White, style = LocalTextStyle.current.shadow()
                            )
                        }
                    }


                }
            }

            MediumText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .padding(top = 6.dp, bottom = 2.dp),
                text = model.summary.naText(),
                maxLines = 4
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .padding(top = 2.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SmallLightText(modifier = Modifier.weight(1f), text = model.createdAtPrettyTime)
                Icon(
                    modifier = Modifier.size(12.dp),
                    imageVector = AppIcons.IcThumbUp,
                    contentDescription = null
                )
                MediumText(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    text = model.rating.orZero().toString(),
                    fontSize = 12.sp
                )
            }
        }
    }
}