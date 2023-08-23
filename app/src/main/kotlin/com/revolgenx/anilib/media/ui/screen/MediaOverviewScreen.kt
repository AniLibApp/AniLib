package com.revolgenx.anilib.media.ui.screen

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.naStringResource
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.openLink
import com.revolgenx.anilib.common.ext.orNaString
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.orZeroString
import com.revolgenx.anilib.common.ext.painterResource
import com.revolgenx.anilib.common.ext.stringResource
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.shadow
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.theme.background
import com.revolgenx.anilib.common.ui.theme.inverseOnSurface
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import com.revolgenx.anilib.common.ui.theme.secondary
import com.revolgenx.anilib.common.ui.theme.shapes
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTagModel
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.type.MediaType

@Composable
fun MediaOverviewScreen(
    viewModel: MediaViewModel,
    mediaType: MediaType
) {
    ResourceScreen(resourceState = viewModel.resource.value, refresh = { viewModel.refresh() }) {
        MediaOverview(it, mediaType)
    }
}


@Composable
private fun MediaOverview(
    media: MediaModel,
    mediaType: MediaType
) {

    val isAnime = mediaType.isAnime
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        MediaDescription(media)
        MediaInfo(media, isAnime)
        MediaStats(media)
        MediaGenre(media)
        MediaTag(media)
        MediaExternalLink(media)
    }
}

@Composable
fun MediaHeader(text: String) {
    Text(
        modifier = Modifier,
        text = text,
        fontSize = 18.sp,
        color = onSurfaceVariant,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun MediaStats(media: MediaModel) {
    MediaHeader(text = R.string.stats.stringResource())

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Grid(
            items = listOf(
                MediaInfoItem(
                    title = R.string.average_score.stringResource(),
                    value = R.string.s_percent.stringResource().format(media.averageScore.orZero())
                ),
                MediaInfoItem(
                    title = R.string.mean_score.stringResource(),
                    value = R.string.s_percent.stringResource().format(media.meanScore.orZero())
                ),
                MediaInfoItem(
                    title = R.string.popularity.stringResource(),
                    value = media.popularity.orZeroString()
                ),
                MediaInfoItem(
                    title = R.string.favourites.stringResource(),
                    value = media.popularity.orZeroString()
                ),
            ),
            rowSpacing = 8.dp,
            columnSpacing = 12.dp
        ) { item ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = item.title,
                        fontSize = 15.sp,
                        lineHeight = 16.sp,
                        color = onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = item.value,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MediaExternalLink(media: MediaModel) {
    val context = localContext()
    val snackbarHostState = localSnackbarHostState()
    val scope = rememberCoroutineScope()

    MediaHeader(text = R.string.links.stringResource())

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        media.externalLinks?.forEach { link ->
            Surface(
                shape = shapes().small,
                color = link.color ?: surfaceContainer
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            context.openLink(
                                url = link.url,
                                scope = scope,
                                snackbar = snackbarHostState
                            )
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    link.icon?.let {
                        AsyncImage(
                            modifier = Modifier.size(28.dp),
                            imageUrl = it, failure = {})
                    }
                    Text(
                        text = link.site.naText(), style = LocalTextStyle.current.shadow(
                            inverseOnSurface
                        )
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MediaTag(media: MediaModel) {

    val showSpoilerTags = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MediaHeader(text = R.string.tags.stringResource())

        TextButton(onClick = {
            showSpoilerTags.value = !showSpoilerTags.value
        }) {

            Text(text = (if (!showSpoilerTags.value) R.string.show_spoilers else R.string.hide_spoilers).stringResource())
        }
    }

    val tagDetail = remember {
        mutableStateOf<MediaTagModel?>(null)
    }
    val openSpoilerBottomSheet = remember {
        mutableStateOf(false)
    }

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (if (showSpoilerTags.value) media.tags else media.tagsWithoutSpoiler)?.forEach { tag ->
            Surface(
                modifier = Modifier,
                shape = shapes().small
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            tagDetail.value = tag
                            openSpoilerBottomSheet.value = true
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (tag.isMediaSpoilerTag) {
                        Icon(
                            painter = R.drawable.ic_hide.painterResource(),
                            contentDescription = null,
                            tint = secondary
                        )
                    }
                    Text(text = tag.name)
                    Icon(
                        painter = R.drawable.ic_info.painterResource(),
                        contentDescription = null,
                        tint = secondary
                    )
                }
            }
        }
    }

    MediaTagDetailBottomSheet(
        openBottomSheet = openSpoilerBottomSheet,
        bottomSheetState = bottomSheetState,
        tagState = tagDetail
    )

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MediaGenre(media: MediaModel) {
    MediaHeader(text = R.string.genre.stringResource())

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        media.genres?.forEach { genre ->
            Surface(
                shape = shapes().small
            ) {
                Box(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = genre)
                }

            }
        }
    }
}

@Composable
private fun MediaInfo(media: MediaModel, isAnime: Boolean) {
    Grid(
        items = listOf(
            MediaInfoItem(
                title = R.string.format.stringResource(),
                value = media.format?.toStringRes().naStringResource()
            ),
            MediaInfoItem(
                title = R.string.source.stringResource(),
                value = media.source?.toStringRes().naStringResource()
            ),
            MediaInfoItem(
                title = if (isAnime) R.string.episodes.stringResource() else R.string.chapters.stringResource(),
                value = if (isAnime) media.episodes.orNaString() else media.chapters.orNaString()
            ),
            MediaInfoItem(
                title = if (isAnime) R.string.duration.stringResource() else R.string.volumes.stringResource(),
                value = if (isAnime) R.string.min_s.stringResource()
                    .format(media.duration.orNaString()) else media.volumes.orNaString()
            ),
            MediaInfoItem(
                title = R.string.status.stringResource(),
                value = media.status?.toStringRes().naStringResource()
            ),
            MediaInfoItem(
                title = R.string.season.stringResource(),
                value = media.status?.toStringRes().naStringResource()
            ),
            MediaInfoItem(
                title = R.string.start_date.stringResource(),
                value = media.startDate?.shortDate.orNaString()
            ),
            MediaInfoItem(
                title = R.string.end_date.stringResource(),
                value = media.endDate?.shortDate.orNaString()
            ),
        ),
        rowSpacing = 8.dp,
        columnSpacing = 12.dp
    ) { item ->
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            ) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    lineHeight = 16.sp,
                    color = onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = item.value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            listOf(
                MediaInfoItem(
                    title = R.string.romaji.stringResource(),
                    value = media.title?.romaji.orNaString()
                ),
                MediaInfoItem(
                    title = R.string.english.stringResource(),
                    value = media.title?.english.orNaString()
                ),
                MediaInfoItem(
                    title = R.string._native.stringResource(),
                    value = media.title?.native.orNaString()
                ),
                MediaInfoItem(
                    title = R.string.synonyms.stringResource(),
                    value = media.synonymsString.orNaString()
                ),

                ).forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = it.title,
                        color = onSurfaceVariant,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(width = 22.dp))
                    SelectionContainer {
                        Text(
                            text = it.value,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun MediaDescription(media: MediaModel) {
    var showFullDesc by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .animateContentSize(animationSpec = tween(100))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showFullDesc = !showFullDesc
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            if (showFullDesc) {
                MarkdownText(
                    spanned = media.descriptionSpanned,
                    fontSize = 15.sp,
                ) {
                    showFullDesc = !showFullDesc
                }
            } else {
                MarkdownText(
                    spanned = media.descriptionSpanned,
                    fontSize = 15.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                ) {
                    showFullDesc = !showFullDesc
                }
            }

            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.CenterHorizontally),
                painter = if (showFullDesc) R.drawable.ic_arrow_up.painterResource() else R.drawable.ic_arrow_down.painterResource(),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaTagDetailBottomSheet(
    openBottomSheet: MutableState<Boolean>,
    bottomSheetState: SheetState,
    tagState: MutableState<MediaTagModel?>
) {
    val tag = tagState.value
    if (openBottomSheet.value && tag != null) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState,
            containerColor = background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.tags),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MediaTagDetailItem(R.string.name.stringResource(), tag.name)
                MediaTagDetailItem(
                    R.string.category.stringResource(),
                    tag.category.orNaString()
                )
                MediaTagDetailItem(R.string.rank.stringResource(), tag.rank.orNaString())
                MediaTagDetailItem(
                    R.string.description.stringResource(),
                    tag.description.orNaString()
                )
            }

        }
    } else {
        tagState.value = null
    }
}

@Composable
fun MediaTagDetailItem(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = onSurfaceVariant,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier.weight(3f),
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End
        )
    }
}


data class MediaInfoItem(val title: String, val value: String)