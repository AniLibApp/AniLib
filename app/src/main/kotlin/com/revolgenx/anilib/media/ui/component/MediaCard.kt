package com.revolgenx.anilib.media.ui.component

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.maybeLocalSnackbarHostState
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.composition.localUser
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.common.util.OnMediaListEntryClick
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import anilib.i18n.R as I18nR

data class MediaComponentState(
    val navigator: Navigator,
    val scope: CoroutineScope,
    val context: Context,
    val snackbarHostState: SnackbarHostState?,
    val userId: Int?
)

@Composable
fun rememberMediaComponentState(
    navigator: Navigator,
    context: Context = localContext(),
    userId: Int? = localUser().userId,
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState? = maybeLocalSnackbarHostState()
): MediaComponentState {
    return remember(
        navigator, context, userId, scope, snackbarHostState
    ) {
        MediaComponentState(
            navigator = navigator,
            context = context,
            userId = userId,
            scope = scope,
            snackbarHostState = snackbarHostState
        )
    }
}


@Composable
fun MediaCard(
    media: MediaModel,
    width: Dp? = null,
    height: Dp = 248.dp,
    footerContent: @Composable (ColumnScope.() -> Unit)? = null,
    mediaComponentState: MediaComponentState
) {
    val (
        navigator,
        scope,
        context,
        snackbarHostState,
        userId
    ) = mediaComponentState

    MediaCardContent(
        media = media,
        width = width,
        height = height,
        footerContent = footerContent,
        onMediaClick = { id, type ->
            navigator.mediaScreen(id, type)
        },
        onMediaEntryClick = { id, _ ->
            if (userId != null) {
                navigator.mediaListEntryEditorScreen(id, userId)
            } else {
                scope.launch {
                    snackbarHostState?.showSnackbar(
                        context.getString(I18nR.string.please_log_in),
                        withDismissAction = true
                    )
                }
            }
        })
}


@Composable
fun CoverMediaCard(
    media: MediaModel,
    width: Dp? = null,
    height: Dp = 248.dp,
    mediaComponentState: MediaComponentState,
    footerContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    val (
        navigator,
        scope,
        context,
        snackbarHostState,
        userId
    ) = mediaComponentState

    CoverMediaCardContent(
        width,
        height,
        media = media,
        footerContent = footerContent,
        onMediaClick = { id, type ->
            navigator.mediaScreen(id, type)
        },
        onMediaEntryClick = { id, _ ->
            if (userId != null) {
                navigator.mediaListEntryEditorScreen(id, userId)
            } else {
                scope.launch {
                    snackbarHostState?.showSnackbar(
                        context.getString(I18nR.string.please_log_in),
                        withDismissAction = true
                    )
                }
            }
        }
    )
}


@Composable
private fun MediaCardContent(
    media: MediaModel,
    width: Dp? = null,
    height: Dp = 248.dp,
    footerContent: @Composable (ColumnScope.() -> Unit)? = null,
    onMediaClick: OnMediaClick,
    onMediaEntryClick: OnMediaListEntryClick
) {
    Card(
        modifier = Modifier
            .let {
                if (width != null) {
                    it.width(width)
                } else {
                    it.fillMaxWidth()
                }
            }
            .height(height)
            .padding(4.dp),
        onClick = {
            onMediaClick(media.id, media.type)
        },
        onLongClick = {
            onMediaEntryClick(media.id, media.type)
        },
    ) {
        Box {

            MediaCoverImageType { type ->
                ImageAsync(
                    modifier = Modifier
                        .height(165.dp)
                        .fillMaxWidth(),
                    imageUrl = media.coverImage?.image(type),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }

            MediaInfoBadge(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp),
                media = media
            )

        }

        Column(
            modifier = Modifier.padding(
                horizontal = MediaInfoHorizontalPadding,
                vertical = MediaInfoVerticalPadding
            )
        ) {
            MediaTitleType { type ->
                MediumText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MediaCardTitleBottomPadding),
                    text = media.title?.title(type).naText()
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            if (footerContent != null) {
                footerContent()
            } else {
                val format = stringResource(id = media.format.toStringRes())
                val status = stringResource(id = media.status.toStringRes())
                val statusColor = media.status.toColor()
                val year = media.seasonYear.naText()


                LightText(
                    text = status,
                    color = statusColor,
                    lineHeight = 11.sp
                )

                LightText(
                    text = stringResource(id = I18nR.string.s_dot_s).format(format, year),
                    lineHeight = 11.sp
                )
            }
        }
    }
}


@Composable
private fun CoverMediaCardContent(
    width: Dp?,
    height: Dp,
    media: MediaModel,
    onMediaClick: OnMediaClick,
    onMediaEntryClick: OnMediaListEntryClick,
    footerContent: @Composable() (ColumnScope.() -> Unit)?,
) {
    Card(
        modifier = Modifier
            .let {
                if (width != null) {
                    it.width(width)
                } else {
                    it.fillMaxWidth()
                }
            }
            .height(height)
            .padding(4.dp),
        onClick = {
            onMediaClick(media.id, media.type)
        },
        onLongClick = {
            onMediaEntryClick(media.id, media.type)
        },
    ) {
        Box {
            MediaCoverImageType { type ->
                ImageAsync(
                    modifier = Modifier.fillMaxSize(),
                    imageUrl = media.coverImage?.image(type),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }


            MediaInfoBadge(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp),
                media = media
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = MediaInfoHorizontalPadding,
                            vertical = MediaInfoVerticalPadding
                        )
                ) {
                    MediaTitleType { type ->
                        MediumText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MediaCardTitleBottomPadding),
                            text = media.title?.title(type).naText()
                        )
                    }


                    if (footerContent != null) {
                        footerContent()
                    } else {
                        media.genres?.let {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                it.take(3).forEach {
                                    MediumText(
                                        text = it,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 10.sp,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        val format = stringResource(id = media.format.toStringRes())
                        val status = stringResource(id = media.status.toStringRes())
                        val statusColor = media.status.toColor()
                        val year = media.seasonYear.naText()


                        LightText(
                            text = status,
                            color = statusColor,
                            lineHeight = 11.sp
                        )

                        LightText(
                            text = stringResource(id = I18nR.string.s_dot_s).format(format, year),
                            lineHeight = 11.sp
                        )
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaItemRowContent(
    media: MediaModel,
    content: @Composable () -> Unit = {},
    mediaComponentState: MediaComponentState
) {
    val (
        navigator,
        scope,
        context,
        snackbarHostState,
        userId
    ) = mediaComponentState

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = {
                    if (userId != null) {
                        navigator.mediaListEntryEditorScreen(media.id, userId)
                    } else {
                        scope.launch {
                            snackbarHostState?.showSnackbar(
                                context.getString(I18nR.string.please_log_in),
                                withDismissAction = true
                            )
                        }
                    }
                },
                onClick = {
                    navigator.mediaScreen(media.id, media.type)
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(6.dp, alignment = Alignment.Start)
    ) {

        Box {
            MediaCoverImageType { type ->
                ImageAsync(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(74.dp),
                    imageUrl = media.coverImage?.image(type),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }

            MediaInfoBadge(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp),
                media = media
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = MediaInfoVerticalPadding)
        ) {

            MediaTitleType { type ->
                MediumText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = media.title?.title(type).naText(),
                )
            }


            Text(
                stringResource(id = media.status.toStringRes()),
                color = media.status.toColor(),
                fontSize = 12.sp
            )

            val format = stringResource(id = media.format.toStringRes())
            val year = media.seasonYear.naText()

            LightText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = I18nR.string.s_dot_s).format(format, year),
            )

            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaRowItemContentEnd(
    media: MediaModel,
    content: @Composable () -> Unit = {},
    mediaComponentState: MediaComponentState
) {
    val (
        navigator,
        scope,
        context,
        snackbarHostState,
        userId
    ) = mediaComponentState

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = {
                    if (userId != null) {
                        navigator.mediaListEntryEditorScreen(media.id, userId)
                    } else {
                        scope.launch {
                            snackbarHostState?.showSnackbar(
                                context.getString(I18nR.string.please_log_in),
                                withDismissAction = true
                            )
                        }
                    }
                },
                onClick = {
                    navigator.mediaScreen(media.id, media.type)
                }
            ),
        horizontalArrangement = Arrangement.spacedBy(
            6.dp,
            alignment = Alignment.End
        )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = MediaInfoVerticalPadding),
            horizontalAlignment = Alignment.End
        ) {

            MediaTitleType { type ->
                MediumText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = media.title?.title(type).naText(),
                    textAlign = TextAlign.End
                )
            }


            Text(
                stringResource(id = media.status.toStringRes()),
                color = media.status.toColor(),
                fontSize = 12.sp,
                textAlign = TextAlign.End,
            )

            val format = stringResource(id = media.format.toStringRes())

            val year = media.seasonYear.naText()

            LightText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = I18nR.string.s_dot_s).format(format, year),
                textAlign = TextAlign.End,
            )

            content()
        }

        Box {
            MediaCoverImageType { type ->
                ImageAsync(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(74.dp),
                    imageUrl = media.coverImage?.image(type),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }



            MediaInfoBadge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                media = media
            )

        }
    }
}


private val MediaCardTitleBottomPadding = 4.dp
private val MediaInfoHorizontalPadding = 5.dp
private val MediaInfoVerticalPadding = 5.dp