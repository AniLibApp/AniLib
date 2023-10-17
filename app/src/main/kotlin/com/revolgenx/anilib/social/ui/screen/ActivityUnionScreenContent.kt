package com.revolgenx.anilib.social.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.revolgenx.anilib.common.ui.component.card.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeartOutline
import com.revolgenx.anilib.common.ui.icons.appicon.IcMessage
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreHoriz
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotification
import com.revolgenx.anilib.common.ui.icons.appicon.IcNotificationOutline
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.media.ui.component.MediaCoverImageType
import com.revolgenx.anilib.social.ui.model.ActivityModel
import com.revolgenx.anilib.social.ui.model.ListActivityModel
import com.revolgenx.anilib.social.ui.model.MessageActivityModel
import com.revolgenx.anilib.social.ui.model.TextActivityModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel

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
                ImageAsync(
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
            ImageAsync(
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
                    modifier = Modifier.size(20.dp),
                    imageVector = if (model.isSubscribed) AppIcons.IcNotification else AppIcons.IcNotificationOutline,
                    contentDescription = null
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = AppIcons.IcMoreHoriz,
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
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                onClick = { /*TODO*/ },
                contentPadding = PaddingValues(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = AppIcons.IcMessage,
                    contentDescription = null
                )
                MediumText(
                    modifier = Modifier.padding(start = 2.dp),
                    text = model.replyCount.prettyNumberFormat(),
                    fontSize = 14.sp
                )
            }


            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                onClick = { /*TODO*/ },
                contentPadding = PaddingValues(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = AppIcons.IcHeartOutline,
                    contentDescription = null
                )
                MediumText(
                    modifier = Modifier.padding(start = 2.dp),
                    text = model.likeCount.prettyNumberFormat(),
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun ActivityItemBottomPreview() {
    ActivityItemBottom(TextActivityModel(0, likeCount = 2, replyCount = 3))
}