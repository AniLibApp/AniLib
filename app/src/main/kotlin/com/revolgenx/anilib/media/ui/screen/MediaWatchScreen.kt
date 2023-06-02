package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.GridOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.compose.paging.ListPagingListType
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.theme.review_list_gradient_bottom
import com.revolgenx.anilib.common.ui.theme.review_list_gradient_top
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.media.ui.model.StreamingEpisodeModel
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.skydoves.landscapist.ImageOptions

@Composable
fun MediaWatchScreen(viewModel: MediaViewModel) {
    ResourceScreen(resourceState = viewModel.resource.value, refresh = { viewModel.refresh() }) {
        LazyPagingList(
            items = it.streamingEpisodes ?: emptyList(),
            type = ListPagingListType.GRID,
            gridOptions = GridOptions(GridCells.Adaptive(168.dp))
        ) { ep ->
            ep ?: return@LazyPagingList
            MediaWatchItem(streamingEpisode = ep) {
                /*todo open url*/
            }
        }
    }
}

@Composable
private fun MediaWatchItem(
    streamingEpisode: StreamingEpisodeModel,
    onWatchClick: OnClickWithValue<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(116.dp)
    ) {
        Box(
            modifier = Modifier.clickable {
                streamingEpisode.url?.let {
                    onWatchClick(it)
                }
            }
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                imageUrl = streamingEpisode.thumbnail,
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
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(6.dp),
                ) {
                    Text(
                        text = streamingEpisode.title.naText(),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.2.sp,
                        color = Color.White,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = LocalTextStyle.current.copy(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2.0f, 2.0f),
                                blurRadius = 1f
                            )
                        )
                    )
                    streamingEpisode.site?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.2.sp,
                            color = Color.White,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = LocalTextStyle.current.copy(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(2.0f, 2.0f),
                                    blurRadius = 1f
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}