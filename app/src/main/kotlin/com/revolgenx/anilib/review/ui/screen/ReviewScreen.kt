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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.showLoginMsg
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.action.OpenInBrowserOverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.ShareOverflowMenu
import com.revolgenx.anilib.common.ui.component.appbar.CollapsingAppbar
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcThumbDown
import com.revolgenx.anilib.common.ui.icons.appicon.IcThumbUp
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.component.MediaTitleType
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.viewmodel.ReviewViewModel
import com.revolgenx.anilib.type.ReviewRating
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

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
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val navigator = localNavigator()
    val context = localContext()
    val localUser = localUser()
    val scope = rememberCoroutineScope()

    viewModel.getResource()

    ScreenScaffold(
        topBar = {
            ReviewScreenTopAppBar(
                reviewModel = viewModel.getData(),
                scrollBehavior = scrollBehavior,
                onMediaTitleClicked = {
                    viewModel.getData()?.media?.let {
                        navigator.mediaScreen(it.id, it.type)
                    }
                }
            )
        },
        contentWindowInsets = horizontalBottomWindowInsets()
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
                        }, horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ImageAsync(
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
                        Text(text = stringResource(id = I18nR.string.review_by_user).format(user?.name.naText()))
                        Text(text = stringResource(id = I18nR.string.score_d_100).format(review.score.orZero()))
                        LightText(
                            modifier = Modifier.padding(top = 4.dp),
                            text = review.createdAtPrettyTime,
                            fontSize = 11.sp,
                            textAlign = TextAlign.End
                        )
                    }


                }

                MarkdownText(spanned = review.bodySpanned)

                ReviewLikeDislike(
                    review,
                    onLike = {
                        if (localUser.isLoggedIn) {
                            viewModel.likeReview(review)
                        } else {
                            snackbarHostState.showLoginMsg(context, scope)
                        }
                    },
                    onDislike = {
                        if (localUser.isLoggedIn) {
                            viewModel.dislikeReview(review)
                        } else {
                            snackbarHostState.showLoginMsg(context, scope)
                        }
                    }
                )

                MediumText(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = I18nR.string.d_out_of_d_liked_this_review).format(
                        review.rating.intValue, review.ratingAmount.intValue
                    ),
                )
            }
        }
    }
}


@Composable
private fun ColumnScope.ReviewLikeDislike(
    review: ReviewModel,
    onLike: OnClick,
    onDislike: OnClick
) {
    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
    ) {
        ReviewLikeDislikeButton(true, review.userRating, onClick = onLike)
        ReviewLikeDislikeButton(false, review.userRating, onClick = onDislike)
    }
}

@Composable
fun ReviewLikeDislikeButton(
    likeButton: Boolean,
    rating: MutableState<ReviewRating>,
    onClick: OnClick
) {
    IconToggleButton(
        checked = if (likeButton) rating.value == ReviewRating.UP_VOTE else rating.value == ReviewRating.DOWN_VOTE,
        onCheckedChange = {
            onClick()
        }
    ) {
        Icon(
            imageVector = if (likeButton) AppIcons.IcThumbUp else AppIcons.IcThumbDown,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewScreenTopAppBar(
    reviewModel: ReviewModel?,
    scrollBehavior: TopAppBarScrollBehavior,
    onMediaTitleClicked: OnClick
) {
    val media = reviewModel?.media
    val containerHeight = 200.dp

    CollapsingAppbar(
        scrollBehavior = scrollBehavior,
        containerHeight = containerHeight,
        containerContent = { isCollapsed ->
            ImageAsync(
                modifier = Modifier
                    .height(containerHeight)
                    .fillMaxWidth(),
                imageUrl = media?.bannerImage,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop, alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach,
                viewable = true
            )
            Box(
                modifier = Modifier
                    .height(containerHeight)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surfaceContainerLowest.copy(0.8f)
                            )
                        )
                    )
            ) {
                if (!isCollapsed) {
                    MediaTitleType { type ->
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 14.dp)
                                .clickable(onClick = onMediaTitleClicked),
                            color = MaterialTheme.colorScheme.onSurface,
                            text = media?.title?.title(type)
                                ?: stringResource(id = I18nR.string.review),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge.copy(
                                shadow = Shadow(
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
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
                        text = media?.title?.title(it) ?: stringResource(id = I18nR.string.review),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
        },
        actions = { isCollapsed ->
            reviewModel?.siteUrl?.let { site ->
                OverflowMenu(
                    tonalButton = !isCollapsed
                ) {expanded->
                    OpenInBrowserOverflowMenu(link = site){
                        expanded.value = false
                    }
                    ShareOverflowMenu(text = site){
                        expanded.value = false
                    }
                }
            }
        }
    )
}