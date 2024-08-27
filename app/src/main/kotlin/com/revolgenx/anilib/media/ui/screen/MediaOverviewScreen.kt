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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ext.naString
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.browseGenreScreen
import com.revolgenx.anilib.common.ext.openLink
import com.revolgenx.anilib.common.ext.browseTagScreen
import com.revolgenx.anilib.common.ext.orNa
import com.revolgenx.anilib.common.ext.orZero
import com.revolgenx.anilib.common.ext.orZeroString
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ui.component.button.SmallTextButton
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.component.text.RegularText
import com.revolgenx.anilib.common.ui.component.text.shadow
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowDown
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowUp
import com.revolgenx.anilib.common.ui.icons.appicon.IcHide
import com.revolgenx.anilib.common.ui.icons.appicon.IcInfo
import com.revolgenx.anilib.common.ui.icons.appicon.IcMarkdownYoutube
import com.revolgenx.anilib.common.ui.icons.appicon.IcPlay
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.home.recommendation.ui.model.RecommendationConnectionModel
import com.revolgenx.anilib.media.ui.component.MediaItemColumnCard
import com.revolgenx.anilib.media.ui.component.MediaComponentState
import com.revolgenx.anilib.media.ui.component.rememberMediaComponentState
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
import kotlinx.coroutines.delay
import anilib.i18n.R as I18nR

@Composable
fun MediaOverviewScreen(
    viewModel: MediaViewModel,
    mediaType: MediaType,
    recommendationScreen: OnClick
) {
    ResourceScreen(viewModel = viewModel) {
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

    val mediaComponentState = rememberMediaComponentState(navigator = navigator)


    fun openLink(url: String?) {
        context.openLink(
            url = url,
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        MediaNextAiringEpisode(media)
        MediaDescription(media)
        MediaInfo(media, isAnime, context)
        MediaGenre(media.genres) {
            navigator.browseGenreScreen(it)
        }
        if (isAnime) {
            MediaTrailer(media.trailer) {
                openLink(it.url)
            }
        }
        MediaStats(media, context)
        MediaStudio(media.studios) {
            navigator.studioScreen(it.id)
        }
        MediaRelation(media.relations, mediaComponentState = mediaComponentState)
        MediaRecommendation(
            media.recommendations,
            viewAll = {
                recommendationScreen.invoke()
            },
            mediaComponentState = mediaComponentState
        )
        MediaTag(media.tags, media.tagsWithoutSpoiler ?: emptyList()) {
            navigator.browseTagScreen(it)
        }
        MediaExternalLink(media.externalLinks) {
            openLink(it.url)
        }

        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
fun MediaRecommendation(
    recommendations: RecommendationConnectionModel?,
    viewAll: OnClick,
    mediaComponentState: MediaComponentState
) {
    val medias = recommendations?.nodes ?: return
    if (medias.isEmpty()) return

    val showViewAll = recommendations.pageInfo.let {
        it?.total.orZero() > it?.perPage.orZero()
    }

    MediaHeaderWithButton(
        header = stringResource(id = I18nR.string.recommendations),
        buttonText = stringResource(id = I18nR.string.view_all),
        showButton = showViewAll,
        onClick = viewAll
    )

    LazyRow {
        items(items = medias) {
            val media = it.mediaRecommendation ?: return@items
            MediaItemColumnCard(
                width = 140.dp,
                media = media,
                mediaComponentState = mediaComponentState
            )
        }
    }
}

@Composable
fun MediaRelation(relations: MediaConnectionModel?, mediaComponentState: MediaComponentState) {
    if (relations?.edges.isNullOrEmpty()) return

    MediaHeader(text = stringResource(id = I18nR.string.relations))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(relations!!.edges!!) {
            MediaRelationCard(mediaEdgeModel = it, mediaCardState = mediaComponentState)
        }
    }
}

@Composable
fun MediaRelationCard(
    mediaEdgeModel: MediaEdgeModel,
    mediaCardState: MediaComponentState,
) {
    val media = mediaEdgeModel.node ?: return

    MediaItemColumnCard(
        media = media,
        width = 140.dp,
        footerContent = {
            val format = stringResource(id = media.format.toStringRes())
            val status = stringResource(id = media.status.toStringRes())
            val source = stringResource(id = mediaEdgeModel.relationType.toStringRes())
            val seasonYear = media.seasonYear.naText()
            val sourceYearText = if (media.isAnime) {
                stringResource(id = I18nR.string.s_dot_s).format(source, seasonYear)
            } else {
                source
            }
            LightText(
                text = sourceYearText,
                lineHeight = 11.sp
            )
            LightText(
                text = stringResource(id = I18nR.string.s_dot_s).format(format, status),
                lineHeight = 11.sp
            )
        },
        mediaComponentState = mediaCardState
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
    HeaderBox(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(top = 20.dp, bottom = 12.dp), text = text
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
        MediaHeader(text = stringResource(I18nR.string.studios))
        MediaFlowRow {
            studios!!.forEach {
                MediaInfoChip(text = it.node?.name.orEmpty()) {
                    it.node?.let(onClick)
                }
            }
        }
    }

    if (producers.isNullOrEmpty().not()) {
        MediaHeader(text = stringResource(I18nR.string.producers))
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

    MediaHeader(text = stringResource(I18nR.string.trailer))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(MaterialTheme.shapes.small)
            .clickable {
                onClick(trailer)
            }
    ) {
        ImageAsync(
            modifier = Modifier.fillMaxSize(),
            imageUrl = trailer.thumbnail
        )
        val icon = when (trailer.source) {
            TrailerSource.YOUTUBE -> AppIcons.IcMarkdownYoutube
            TrailerSource.DAILYMOTION -> AppIcons.IcPlay
            TrailerSource.UNKNOWN -> null
        }
        icon?.let {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = icon,
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun MediaStats(media: MediaModel, context: Context) {
    MediaHeader(text = stringResource(I18nR.string.stats))

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val stats = remember {
            listOf(
                MediaInfoItem(
                    title = context.getString(I18nR.string.average_score),
                    value = context.getString(I18nR.string.s_percent)
                        .format(media.averageScore.orZero())
                ),
                MediaInfoItem(
                    title = context.getString(I18nR.string.mean_score),
                    value = context.getString(I18nR.string.s_percent)
                        .format(media.meanScore.orZero())
                ),
                MediaInfoItem(
                    title = context.getString(I18nR.string.popularity),
                    value = media.popularity.orZeroString()
                ),
                MediaInfoItem(
                    title = context.getString(I18nR.string.favourites),
                    value = media.popularity.orZeroString()
                ),
            )
        }
        Grid(
            items = stats,
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
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = item.value,
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
    externalLinks ?: return
    MediaHeader(text = stringResource(I18nR.string.links))

    MediaFlowRow {
        externalLinks.forEach { link ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = link.color?.let { Color(it) }
                        ?: MaterialTheme.colorScheme.surfaceContainerLowest
                )
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
                            MaterialTheme.colorScheme.inverseOnSurface
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
    tagsWithoutSpoiler: List<MediaTagModel>,
    onClick: OnClickWithValue<String>
) {
    if (tags.isNullOrEmpty()) return

    val showSpoilerTags = remember {
        mutableStateOf(false)
    }

    val showMoreButton = remember {
        tags.size != tagsWithoutSpoiler.size
    }

    MediaHeaderWithButton(
        header = stringResource(id = I18nR.string.tags),
        showButton = showMoreButton,
        buttonText = stringResource(id = (if (!showSpoilerTags.value) I18nR.string.show_spoilers else I18nR.string.hide_spoilers))
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
            Card(
                modifier = Modifier
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            onClick(tag.name)
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (tag.isMediaSpoilerTag) {
                        Icon(
                            imageVector = AppIcons.IcHide,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = stringResource(I18nR.string.tag_name_percent)
                            .format(tag.name, tag.rank.orZero())
                    )
                    Icon(
                        modifier = Modifier.clickable {
                            tagDetail.value = tag
                            openSpoilerBottomSheet.value = true
                        },
                        imageVector = AppIcons.IcInfo,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
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
    MediaHeader(text = stringResource(I18nR.string.genre))
    MediaFlowRow {
        genres?.forEach { genre ->
            MediaInfoChip(text = genre) {
                onClick(genre)
            }
        }
    }
}

@Composable
private fun MediaInfo(media: MediaModel, isAnime: Boolean, context: Context) {
    SectionSpacer()
    val naString = naString()
    val mediaInfo = remember {
        listOf(
            MediaInfoItem(
                title = context.getString(I18nR.string.format),
                value = context.getString(media.format?.toStringRes().orNa())
            ),
            MediaInfoItem(
                title = context.getString(I18nR.string.source),
                value = context.getString(media.source?.toStringRes().orNa())
            ),
            MediaInfoItem(
                title = if (isAnime) context.getString(I18nR.string.episodes) else context.getString(
                    I18nR.string.chapters
                ),
                value = (if (isAnime) media.episodes else media.chapters)?.toString() ?: naString
            ),
            MediaInfoItem(
                title = if (isAnime) context.getString(I18nR.string.duration) else context.getString(
                    I18nR.string.volumes
                ),
                value = if (isAnime) context.getString(I18nR.string.s_min)
                    .format(media.duration?.toString() ?: naString) else media.volumes?.toString()
                    ?: naString
            ),
            MediaInfoItem(
                title = context.getString(I18nR.string.status),
                value = context.getString(media.status?.toStringRes().orNa())
            ),
            MediaInfoItem(
                title = context.getString(I18nR.string.country),
                value = media.countryOfOrigin ?: naString
            ),
            MediaInfoItem(
                title = context.getString(I18nR.string.start_date),
                value = media.startDate?.shortDate ?: naString
            ),
            MediaInfoItem(
                title = context.getString(I18nR.string.end_date),
                value = media.endDate?.shortDate ?: naString
            ),
        )
    }

    Grid(
        items = mediaInfo,
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
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = item.value,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    SectionSpacer()

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            val titles = remember {
                listOf(
                    MediaInfoItem(
                        title = context.getString(I18nR.string.romaji),
                        value = media.title?.romaji ?: naString
                    ),
                    MediaInfoItem(
                        title = context.getString(I18nR.string.english),
                        value = media.title?.english ?: naString
                    ),
                    MediaInfoItem(
                        title = context.getString(I18nR.string._native),
                        value = media.title?.native ?: naString
                    ),
                    MediaInfoItem(
                        title = context.getString(I18nR.string.hashtag),
                        value = media.hashtag ?: naString
                    ),
                    MediaInfoItem(
                        title = context.getString(I18nR.string.synonyms),
                        value = media.synonymsString ?: naString
                    )
                )
            }

            titles.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = it.title,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(width = 22.dp))
                    SelectionContainer {
                        Text(
                            text = it.value,
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
private fun MediaNextAiringEpisode(media: MediaModel) {
    media.nextAiringEpisode?.let { nextEp ->
        var timeUntilAiring by remember {
            mutableStateOf( nextEp.timeUntilAiringModel.copy())
        }
        LaunchedEffect(media) {
            nextEp.timeUntilAiringModel.renew()
            while(true){
                delay(1000)
                nextEp.timeUntilAiringModel.tick()
                timeUntilAiring = nextEp.timeUntilAiringModel.copy()
                if(nextEp.timeUntilAiringModel.alreadyAired) break
            }
        }
        Card(
            modifier = Modifier
                .padding(top = 10.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MediumText(
                        text = stringResource(id = anilib.i18n.R.string.ep_s).format(nextEp.episode),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    NextEpisodeTime(
                        dateTimeType = stringResource(id = anilib.i18n.R.string.datetime_days),
                        dateTimeValue = timeUntilAiring.day.toString()
                    )
                    NextEpisodeTime(
                        dateTimeType = stringResource(id = anilib.i18n.R.string.datetime_hour),
                        dateTimeValue = timeUntilAiring.hour.toString()
                    )
                    NextEpisodeTime(
                        dateTimeType = stringResource(id = anilib.i18n.R.string.datetime_min),
                        dateTimeValue = timeUntilAiring.min.toString()
                    )
                    NextEpisodeTime(
                        dateTimeType = stringResource(id = anilib.i18n.R.string.datetime_sec),
                        dateTimeValue = timeUntilAiring.sec.toString()
                    )
                }

            }
        }
    }
}

@Composable
private fun NextEpisodeTime(dateTimeType: String, dateTimeValue: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        RegularText(text = dateTimeValue, fontSize = 15.sp)
        MediumText(text = dateTimeType, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun MediaDescription(media: MediaModel) {
    SectionSpacer()
    var showFullDesc by remember { mutableStateOf(false) }
    var showMoreIcon by remember { mutableStateOf(false) }
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
            val noDescription =
                stringResource(id = I18nR.string.no_description).takeIf { media.description.isNullOrBlank() }
            if (showFullDesc) {
                MarkdownText(
                    text = noDescription,
                    spanned = media.descriptionSpanned,
                    onLineCount = {
                        showMoreIcon = it > 3
                    }
                ) {
                    if (showMoreIcon) {
                        showFullDesc = !showFullDesc
                    }
                }
            } else {
                MarkdownText(
                    text = noDescription,
                    spanned = media.descriptionSpanned,
                    maxLines = 3,
                    onLineCount = {
                        showMoreIcon = it > 3
                    }
                ) {
                    if (showMoreIcon) {
                        showFullDesc = !showFullDesc
                    }
                }
            }

            if (showMoreIcon) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = if (showFullDesc) AppIcons.IcArrowUp else AppIcons.IcArrowDown,
                    contentDescription = null
                )
            }
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
        val naString = naString()
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = stringResource(id = I18nR.string.tags),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MediaTagDetailItem(stringResource(I18nR.string.name), tag.name)
                MediaTagDetailItem(
                    stringResource(I18nR.string.category),
                    tag.category ?: naString
                )
                MediaTagDetailItem(
                    stringResource(I18nR.string.rank),
                    tag.rank?.toString() ?: naString
                )
                MediaTagDetailItem(
                    stringResource(I18nR.string.description),
                    tag.description ?: naString
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
    Card(onClick = onClick) {
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

@Composable
fun SectionSpacer() {
    Spacer(modifier = Modifier.size(12.dp))
}

data class MediaInfoItem(val title: String, val value: String)