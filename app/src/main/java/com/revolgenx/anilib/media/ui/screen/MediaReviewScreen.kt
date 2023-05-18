package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naInt
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.screen.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.viewmodel.MediaReviewViewModel
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.review.ui.screen.ReviewScreen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaReviewScreen(
    mediaId: Int,
    viewModel: MediaReviewViewModel = koinViewModel()
) {
    val navigator = LocalMainNavigator.current
    LaunchedEffect(mediaId) {
        viewModel.field.mediaId = mediaId
    }
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        }
    ) { staffEdgeModel ->
        staffEdgeModel ?: return@LazyPagingList
        MediaReviewItem(staffEdgeModel) { reviewId ->
            navigator.push(ReviewScreen(reviewId))
        }
    }
}

@Composable
private fun MediaReviewItem(
    reviewModel: ReviewModel,
    onClick: (reviewId: Int) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FrescoImage(
            modifier = Modifier
                .size(height = 50.dp, width = 50.dp)
                .clip(CircleShape),
            imageUrl = reviewModel.user?.avatar?.image,
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            previewPlaceholder = R.drawable.bleach
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onClick.invoke(reviewModel.id)
                    }
            ) {
                Text(reviewModel.summary.naText())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Rounded.ThumbUp,
                            contentDescription = reviewModel.rating.naInt().toString(),
                        )
                        Text(reviewModel.rating.naInt().prettyNumberFormat())
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MediaReviewItemPreview() {
    MediaReviewItem(
        ReviewModel(
            summary = "The finale was actually very good! The emotional parts hit pretty well, characters did good, and it set up the continuation cleanly! That's all you can really ask for in a season finale; however: as previously stated, there's a horrid scene that sags it down right towards the end >:( but uh yeah besides for that I'm looking forward to watching the continuation!"
        )
    ) {}
}