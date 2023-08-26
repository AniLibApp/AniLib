package com.revolgenx.anilib.media.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.toStringResourceOrNa
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.openLink
import com.revolgenx.anilib.common.ext.orNaString
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.orZeroString
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ext.toPainterResource
import com.revolgenx.anilib.common.ext.toStringResource
import com.revolgenx.anilib.common.ui.component.button.SmallTextButton
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.shadow
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.ui.theme.background
import com.revolgenx.anilib.common.ui.theme.inverseOnSurface
import com.revolgenx.anilib.common.ui.theme.onSurfaceVariant
import com.revolgenx.anilib.common.ui.theme.secondary
import com.revolgenx.anilib.common.ui.theme.shapes
import com.revolgenx.anilib.common.ui.theme.surfaceContainer
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationConnectionModel
import com.revolgenx.anilib.media.ui.component.MediaCard
import com.revolgenx.anilib.media.ui.model.MediaConnectionModel
import com.revolgenx.anilib.media.ui.model.MediaEdgeModel
import com.revolgenx.anilib.media.ui.model.MediaExternalLinkModel
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.MediaTagModel
import com.revolgenx.anilib.media.ui.model.MediaTrailerModel
import com.revolgenx.anilib.media.ui.model.TrailerSource
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.media.ui.model.toStringRes
import com.revolgenx.anilib.media.ui.viewmodel.MediaViewModel
import com.revolgenx.anilib.studio.ui.model.StudioConnectionModel
import com.revolgenx.anilib.studio.ui.model.StudioModel
import com.revolgenx.anilib.type.MediaType

@Composable
fun MediaOverviewScreen(
    viewModel: MediaViewModel,
    mediaType: MediaType,
    recommendationScreen: OnClick
) {
    ResourceScreen(resourceState = viewModel.resource.value, refresh = { viewModel.refresh() }) {
        MediaOverview(it, mediaType, recommendationScreen)
    }
}


@Composable
private fun MediaOverview(
    media: MediaModel,
    mediaType: MediaType,
    recommendationScreen: OnClick
) {

    val isAnime = mediaType.isAnime
    val context = localContext()
    val snackbarHostState = localSnackbarHostState()
    val scope = rememberCoroutineScope()
    val navigator = localNavigator()

    fun openLink(url: String?) {
        context.openLink(
            url = url,
            scope = scope,
            snackbar = snackbarHostState
        )
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        MediaDescription(media)
        MediaInfo(media, isAnime)
        MediaGenre(media.genres) {
            // todo open genre
        }
        if (isAnime) {
            MediaTrailer(media.trailer) {
                openLink(it.url)
            }
        }
        MediaStats(media)
        MediaStudio(media.studios) {
            navigator.studioScreen(it.id)
        }
        MediaRelation(media.relations) { id, type ->
            navigator.mediaScreen(id, type)
        }
        MediaRecommendation(
            media.recommendations,
            viewAll = {
                recommendationScreen.invoke()
            }
        ) { id, type ->
            navigator.mediaScreen(id, type)
        }
        MediaTag(media.tags, media.tagsWithoutSpoiler ?: emptyList())
        MediaExternalLink(media.externalLinks) {
            openLink(it.url)
        }
    }
}

@Composable
fun MediaRecommendation(
    recommendations: RecommendationConnectionModel?,
    viewAll: OnClick,
    onMediaClick: OnMediaClick
) {
    val medias = recommendations?.nodes ?: return

    val showViewAll = recommendations.pageInfo.let {
        it?.total.orZero() > it?.perPage.orZero()
    }

    MediaHeaderWithButton(
        header = R.string.recommendations.toStringResource(),
        buttonText = R.string.view_all.toStringResource(),
        showButton = showViewAll,
        onClick = viewAll
    )

    LazyRow {
        items(items = medias) {
            val media = it.mediaRecommendation ?: return@items
            MediaCard(media = media, onMediaClick = onMediaClick)
        }
    }
}

@Composable
fun MediaRelation(relations: MediaConnectionModel?, onMediaClick: OnMediaClick) {
    if (relations?.edges.isNullOrEmpty()) return

    MediaHeader(text = R.string.relations.toStringResource())
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(relations!!.edges!!) {
            MediaRelationCard(mediaEdgeModel = it, onMediaClick = onMediaClick)
        }
    }
}

@Composable
fun MediaRelationCard(
    mediaEdgeModel: MediaEdgeModel,
    onMediaClick: OnMediaClick
) {
    val media = mediaEdgeModel.node ?: return

    MediaCard(
        media = media,
        width = 140.dp,
        bottomContent = {

            val format = media.format.toStringRes().toStringResource()
            val status = media.status.toStringRes().toStringResource()
            val source = mediaEdgeModel.relationType.toStringRes().toStringResource()
            val seasonYear = media.seasonYear.naText()
            val sourceYearText = if (media.isAnime) {
                stringResource(id = R.string.s_dot_s).format(source, seasonYear)
            } else {
                source
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .padding(horizontal = 4.dp)
            ) {
                LightText(
                    text = sourceYearText,
                    lineHeight = 11.sp
                )
                LightText(
                    text = stringResource(id = R.string.s_dot_s).format(format, status),
                    lineHeight = 11.sp
                )
            }
        },
        onMediaClick = onMediaClick
    )
}


@Composable
fun MediaHeaderWithButton(
    header: String,
    buttonText: String,
    showButton: Boolean = true,
    onClick: OnClick
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MediaHeader(text = header)

        if (showButton) {
            SmallTextButton(
                text = buttonText,
                onClick = onClick
            )
        }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MediaStudio(studioConnection: StudioConnectionModel?, onClick: OnClickWithValue<StudioModel>) {
    studioConnection ?: return

    val studios = remember {
        studioConnection.edges?.filter { it.isMain }
    }

    val producers = remember {
        studioConnection.edges?.filter { !it.isMain }
    }

    if (studios.isNullOrEmpty().not()) {
        MediaHeader(text = R.string.studios.toStringResource())
        MediaFlowRow {
            studios!!.forEach {
                MediaInfoChip(text = it.node?.name.orEmpty()) {
                    it.node?.let(onClick)
                }
            }
        }
    }

    if (producers.isNullOrEmpty().not()) {
        MediaHeader(text = R.string.producers.toStringResource())
        MediaFlowRow {
            producers!!.forEach {
                MediaInfoChip(text = it.node?.name.orEmpty()) {
                    it.node?.let(onClick)
                }
            }
        }
    }
}

@Composable
fun MediaTrailer(
    trailer: MediaTrailerModel?,
    onClick: OnClickWithValue<MediaTrailerModel>
) {
    trailer ?: return

    MediaHeader(text = R.string.trailer.toStringResource())
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(shapes().small)
            .clickable {
                onClick(trailer)
            }
    ) {
        ImageAsync(
            modifier = Modifier.fillMaxSize(),
            imageUrl = trailer.thumbnail
        )
        val iconRes = when (trailer.source) {
            TrailerSource.YOUTUBE -> {
                R.drawable.ic_markdown_youtube.toPainterResource()
            }

            TrailerSource.DAILYMOTION -> {
                R.drawable.ic_play.toPainterResource()
            }

            TrailerSource.UNKNOWN -> {
                null
            }
        }
        iconRes?.let {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = iconRes,
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun MediaStats(media: MediaModel) {
    MediaHeader(text = R.string.stats.toStringResource())

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Grid(
            items = listOf(
                MediaInfoItem(
                    title = R.string.average_score.toStringResource(),
                    value = R.string.s_percent.toStringResource()
                        .format(media.averageScore.orZero())
                ),
                MediaInfoItem(
                    title = R.string.mean_score.toStringResource(),
                    value = R.string.s_percent.toStringResource().format(media.meanScore.orZero())
                ),
                MediaInfoItem(
                    title = R.string.popularity.toStringResource(),
                    value = media.popularity.orZeroString()
                ),
                MediaInfoItem(
                    title = R.string.favourites.toStringResource(),
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
fun MediaExternalLink(
    externalLinks: List<MediaExternalLinkModel>?,
    onClick: OnClickWithValue<MediaExternalLinkModel>
) {
    MediaHeader(text = R.string.links.toStringResource())

    MediaFlowRow {
        externalLinks?.forEach { link ->
            Surface(
                shape = shapes().small,
                color = link.color ?: surfaceContainer
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onClick.invoke(link)
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    link.icon?.let {
                        ImageAsync(
                            modifier = Modifier.size(28.dp),
                            imageUrl = it
                        )
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
private fun MediaTag(
    tags: List<MediaTagModel>?,
    tagsWithoutSpoiler: List<MediaTagModel>
) {
    if (tags.isNullOrEmpty()) return

    val showSpoilerTags = remember {
        mutableStateOf(false)
    }

    val showMoreButton = remember {
        tags.size != tagsWithoutSpoiler.size
    }

    MediaHeaderWithButton(
        header = R.string.tags.toStringResource(),
        showButton = showMoreButton,
        buttonText = (if (!showSpoilerTags.value) R.string.show_spoilers else R.string.hide_spoilers).toStringResource()
    ) {
        showSpoilerTags.value = !showSpoilerTags.value
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

    MediaFlowRow {
        (if (showSpoilerTags.value) tags else tagsWithoutSpoiler).forEach { tag ->
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
                            painter = R.drawable.ic_hide.toPainterResource(),
                            contentDescription = null,
                            tint = secondary
                        )
                    }
                    Text(
                        text = R.string.tag_name_percent.toStringResource()
                            .format(tag.name, tag.rank.orZero())
                    )
                    Icon(
                        painter = R.drawable.ic_info.toPainterResource(),
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
private fun MediaGenre(genres: List<String>?, onClick: OnClickWithValue<String>) {
    MediaHeader(text = R.string.genre.toStringResource())

    MediaFlowRow {
        genres?.forEach { genre ->
            MediaInfoChip(text = genre) {
                onClick(genre)
            }
        }
    }
}

@Composable
private fun MediaInfo(media: MediaModel, isAnime: Boolean) {
    Grid(
        items = listOf(
            MediaInfoItem(
                title = R.string.format.toStringResource(),
                value = media.format?.toStringRes().toStringResourceOrNa()
            ),
            MediaInfoItem(
                title = R.string.source.toStringResource(),
                value = media.source?.toStringRes().toStringResourceOrNa()
            ),
            MediaInfoItem(
                title = if (isAnime) R.string.episodes.toStringResource() else R.string.chapters.toStringResource(),
                value = if (isAnime) media.episodes.orNaString() else media.chapters.orNaString()
            ),
            MediaInfoItem(
                title = if (isAnime) R.string.duration.toStringResource() else R.string.volumes.toStringResource(),
                value = if (isAnime) R.string.min_s.toStringResource()
                    .format(media.duration.orNaString()) else media.volumes.orNaString()
            ),
            MediaInfoItem(
                title = R.string.status.toStringResource(),
                value = media.status?.toStringRes().toStringResourceOrNa()
            ),
            MediaInfoItem(
                title = R.string.country.toStringResource(),
                value = media.countryOfOrigin.orNaString()
            ),
            MediaInfoItem(
                title = R.string.start_date.toStringResource(),
                value = media.startDate?.shortDate.orNaString()
            ),
            MediaInfoItem(
                title = R.string.end_date.toStringResource(),
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
                    title = R.string.romaji.toStringResource(),
                    value = media.title?.romaji.orNaString()
                ),
                MediaInfoItem(
                    title = R.string.english.toStringResource(),
                    value = media.title?.english.orNaString()
                ),
                MediaInfoItem(
                    title = R.string._native.toStringResource(),
                    value = media.title?.native.orNaString()
                ),
                MediaInfoItem(
                    title = R.string.hashtag.toStringResource(),
                    value = media.hashtag.orNaString()
                ),
                MediaInfoItem(
                    title = R.string.synonyms.toStringResource(),
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
                painter = if (showFullDesc) R.drawable.ic_arrow_up.toPainterResource() else R.drawable.ic_arrow_down.toPainterResource(),
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
                MediaTagDetailItem(R.string.name.toStringResource(), tag.name)
                MediaTagDetailItem(
                    R.string.category.toStringResource(),
                    tag.category.orNaString()
                )
                MediaTagDetailItem(R.string.rank.toStringResource(), tag.rank.orNaString())
                MediaTagDetailItem(
                    R.string.description.toStringResource(),
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

@Composable
private fun MediaInfoChip(text: String, onClick: OnClick) {
    Surface(
        shape = shapes().small,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = text)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MediaFlowRow(content: @Composable FlowRowScope.() -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

data class MediaInfoItem(val title: String, val value: String)