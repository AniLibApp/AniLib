package com.revolgenx.anilib.review.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.revolgenx.anilib.common.ui.component.appbar.CollapsingAppbar
import com.revolgenx.anilib.common.ui.component.common.MediaTitleType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.theme.inverseOnSurface
import com.revolgenx.anilib.common.ui.theme.onSurface
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.common.ui.theme.typography
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewViewModel
import com.revolgenx.anilib.type.ReviewRating
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
                    .padding(vertical = 6.dp, horizontal = 8.dp),
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

                    LightText(
                        modifier = Modifier.weight(1f),
                        text = review.createdAtPrettyTime,
                        fontSize = 11.sp,
                        textAlign = TextAlign.End
                    )

                }

                MarkdownText(spanned = review.bodySpanned)

                ReviewLikeDislike(viewModel, review)

                MediumText(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.d_out_of_d_liked_this_review).format(
                        review.rating, review.ratingAmount
                    ),
                )
            }
        }
    }
}


@Composable
private fun ColumnScope.ReviewLikeDislike(viewModel: ReviewViewModel, review: ReviewModel) {
    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
    ) {
        ReviewLikeDislikeButton(true, review.userRating.value) {}
        ReviewLikeDislikeButton(false, review.userRating.value) {}
    }
}

// todo mutation
@Composable
fun ReviewLikeDislikeButton(
    likeButton: Boolean, rating: ReviewRating, onCheckChange: (Boolean) -> Unit
) {
    IconToggleButton(
        checked = if (likeButton) rating == ReviewRating.UP_VOTE else rating == ReviewRating.DOWN_VOTE,
        onCheckedChange = onCheckChange
    ) {
        Icon(
            painter = painterResource(id = if (likeButton) R.drawable.ic_thumb_up else R.drawable.ic_thumb_down),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewScreenTopAppBar(
    viewModel: ReviewViewModel, scrollBehavior: TopAppBarScrollBehavior
) {
    val resourceValue = viewModel.resource.value
    val review = resourceValue?.stateValue

    val media = review?.media
    val containerHeight = 200.dp

    CollapsingAppbar(
        scrollBehavior = scrollBehavior,
        containerHeight = containerHeight,
        containerContent = { isCollapsed ->
            AsyncImage(
                modifier = Modifier
                    .height(containerHeight)
                    .fillMaxWidth(),
                imageUrl = media?.bannerImage,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop, alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach,
                failure = {},
                viewable = true
            )
            Box(
                modifier = Modifier
                    .height(containerHeight)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent, surfaceContainer.copy(0.8f)
                            )
                        )
                    )
            ) {
                if (!isCollapsed) {
                    MediaTitleType { type ->
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 14.dp),
                            color = onSurface,
                            text = media?.title?.title(type) ?: stringResource(id = R.string.review),
                            textAlign = TextAlign.Center,
                            style = typography().titleLarge.copy(
                                shadow = Shadow(
                                    color = inverseOnSurface,
                                    offset = Offset(2.0f, 2.0f),
                                    blurRadius = 1f
                                )
                            )
                        )
                    }
                }
            }
        },
        title = { isCollapsed ->
            if (isCollapsed) {
                MediaTitleType {
                    Text(
                        text = media?.title?.title(it) ?: stringResource(id = R.string.review),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
        },
        actions = { isCollapsed ->
            ActionMenu(imageVector = Icons.Filled.Search, tonalButton = !isCollapsed) {

            }
            ActionMenu(
                imageVector = Icons.Filled.Notifications, tonalButton = !isCollapsed
            ) {

            }
        }
    )
}