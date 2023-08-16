package com.revolgenx.anilib.review.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naInt
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.appbar.AppBar
import com.revolgenx.anilib.common.ui.component.appbar.AppBarDefaults
import com.revolgenx.anilib.common.ui.component.appbar.AppBarHeight
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.appbar.CollapsibleAppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.collapseProgress
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.navigation.NavigationIcon
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.review.ui.viewmodel.ReviewViewModel
import com.skydoves.landscapist.ImageOptions
import org.koin.androidx.compose.koinViewModel

class ReviewScreen(private val reviewId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel: ReviewViewModel = koinViewModel()
        viewModel.field.reviewId = reviewId
        ReviewScreenContent(viewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewScreenContent(viewModel: ReviewViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val navigator = localNavigator()

    ScreenScaffold(topBar = {
        ReviewScreenTopAppBar(
            viewModel, scrollBehavior = scrollBehavior
        )
    }) {
        ResourceScreen(viewModel = viewModel) { review ->
            val user = review.user
            Column(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .verticalScroll(rememberScrollState())
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                        .clickable {
                            navigator.userScreen(review.userId)
                        }, horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .size(48.dp)
                            .width(48.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        imageUrl = user?.avatar?.large,
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop, alignment = Alignment.Center
                        ),
                        previewPlaceholder = R.drawable.bleach
                    )


                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = stringResource(id = R.string.review_by_user).format(user?.name.naText()))
                        Text(text = stringResource(id = R.string.score_d_100).format(review.score.naInt()))
                    }

                    Text(
                        modifier = Modifier.weight(1f),
                        text = review.createdAtPrettyTime,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 0.2.sp,
                        textAlign = TextAlign.End
                    )

                }

                MarkdownText(spanned = review.bodySpanned)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewScreenTopAppBar(
    viewModel: ReviewViewModel,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val review = viewModel.resource.value?.stateValue

    val media = review?.media
    val imageAppbarHeight = 200.dp

    val topAppBarState = scrollBehavior.state
    val progress = topAppBarState.collapseProgress().value
    val imageAlpha = if (progress >= 0.8f) 0.8f else progress
    val isCollapsed = progress > 0.7f


    Box {
        CollapsibleAppBarLayout(
            containerHeight = imageAppbarHeight,
            maxHeightOffsetLimit = AppBarHeight,
            scrollBehavior = scrollBehavior,
            colors = AppBarLayoutDefaults.transparentColors()
        ) {
            AsyncImage(modifier = Modifier
                .height(imageAppbarHeight)
                .fillMaxWidth(),
                imageUrl = media?.bannerImage,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop, alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach,
                failure = {}
            )

            Box(
                modifier = Modifier
                    .height(imageAppbarHeight)
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = imageAlpha
                    }
                    .background(surfaceContainer)
            ) {}
        }

        AppBarLayout(
            colors = AppBarLayoutDefaults.transparentColors()
        ) {
            AppBar(colors = AppBarDefaults.appBarColors(containerColor = Color.Transparent),
                title = {
                    if (isCollapsed) {
                        MediaTitleType {
                            Text(
                                text = media?.title?.title(it)
                                    ?: stringResource(id = R.string.review),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }
                    }
                },
                navigationIcon = {
                    NavigationIcon()
                },
                actions = {
                    ActionMenu(imageVector = Icons.Filled.Search) {

                    }
                    ActionMenu(imageVector = Icons.Filled.Notifications) {

                    }
                })

        }
    }
}