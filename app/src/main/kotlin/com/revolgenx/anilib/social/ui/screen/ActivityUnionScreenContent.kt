package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.colorScheme
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.common.MediaCoverImageType
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.social.ui.model.ListActivityModel
import com.revolgenx.anilib.social.ui.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.model.TextActivityModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.skydoves.landscapist.ImageOptions

@Composable
fun ActivityUnionScreenContent(
    viewModel: ActivityUnionViewModel
) {
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        },
    ) { activityUnionModel ->
        activityUnionModel ?: return@LazyPagingList
        Box(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            activityUnionModel.apply {
                if (listActivityModel != null) {
                    ListActivityItem(listActivityModel)
                } else if (messageActivityModel != null) {
                    MessageActivityItem(messageActivityModel)
                } else if (textActivityModel != null) {
                    TextActivityItem(textActivityModel)
                }
            }
        }
    }
}


@Composable
fun ListActivityItem(model: ListActivityModel) {
    val media = model.media
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            MediaCoverImageType { imageType ->
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(90.dp),
                    imageUrl = media?.coverImage?.image(imageType),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ActivityItemTop(model = model)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = model.progressStatus,
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                }
                ActivityItemBottom(model = model)
            }
        }
    }
}

@Composable
fun MessageActivityItem(model: MessageActivityModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ActivityItemTop(model)
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                spanned = model.messageSpanned
            )
            ActivityItemBottom(model)
        }
    }
}


@Composable
fun TextActivityItem(model: TextActivityModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ActivityItemTop(model)
            MarkdownText(
                modifier = Modifier.fillMaxWidth(),
                spanned = model.textSpanned
            )
            ActivityItemBottom(model)
        }
    }
}


@Composable
fun ActivityItemTop(model: ActivityModel) {
    val navigator = localNavigator()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    model.userId?.let {
                        navigator.userScreen(it)
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(38.dp)
                    .width(38.dp)
                    .clip(RoundedCornerShape(12.dp)),
                imageUrl = model.user?.avatar?.large,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )


            MediumText(
                text = model.user?.name.naText(),
            )
        }

        Row {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = if (model.isSubscribed) R.drawable.ic_notification else R.drawable.ic_notification_outline),
                    contentDescription = null
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_horiz),
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
private fun ActivityItemBottom(model: ActivityModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        LightText(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp),
            text = model.createdAtPrettyTime
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = colorScheme().onSurface),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_message),
                    contentDescription = null
                )
                Text(text = model.replyCount.prettyNumberFormat())
            }


            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = colorScheme().onSurface),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_outline),
                    contentDescription = null
                )
                Text(text = model.likeCount.prettyNumberFormat())
            }
        }
    }
}