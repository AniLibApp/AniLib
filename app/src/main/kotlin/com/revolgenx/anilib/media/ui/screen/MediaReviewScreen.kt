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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ext.reviewScreen
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.LocalMainNavigator
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.viewmodel.MediaReviewViewModel
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.common.ui.component.image.ImageOptions

@Composable
fun MediaReviewScreen(viewModel: MediaReviewViewModel) {
    val navigator = LocalMainNavigator.current
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        }
    ) { staffEdgeModel ->
        staffEdgeModel ?: return@LazyPagingList
        MediaReviewItem(staffEdgeModel) { reviewId ->
            navigator.reviewScreen(reviewId)
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
        ImageAsync(
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
                    .clickable {
                        onClick.invoke(reviewModel.id)
                    }
                    .padding(8.dp)
            ) {
                Text(reviewModel.summary.naText())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CompositionLocalProvider(LocalContentColor provides onSurfaceVariant) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Rounded.ThumbUp,
                            contentDescription = reviewModel.rating.orZero().toString(),
                        )
                        Text(reviewModel.rating.orZero().prettyNumberFormat())
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