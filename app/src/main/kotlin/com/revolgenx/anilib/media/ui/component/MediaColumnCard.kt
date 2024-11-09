package com.revolgenx.anilib.media.ui.component

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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

fun onMediaClickHandler(
    mediaComponentState: MediaComponentState,
    openMediaEntryList: Boolean = false
): OnMediaClick {
    val (
        navigator,
        scope,
        context,
        snackbarHostState,
        userId
    ) = mediaComponentState

    return if (openMediaEntryList) {
        { id, _ ->
            if (userId != null) {
                navigator.mediaListEntryEditorScreen(id)
            } else {
                scope.launch {
                    snackbarHostState?.showSnackbar(
                        context.getString(anilib.i18n.R.string.please_log_in),
                        withDismissAction = true
                    )
                }
            }
        }
    } else {
        { id, type ->
            mediaComponentState.navigator.mediaScreen(id, type)
        }
    }
}

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
fun MediaItemColumnCard(
    media: MediaModel,
    width: Dp? = null,
    height: Dp = 254.dp,
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

    MediaColumnCardContent(
        media = media,
        width = width,
        height = height,
        footerContent = footerContent,
        onMediaClick = { id, type ->
            navigator.mediaScreen(id, type)
        },
        onMediaLongClick = { id, _ ->
            if (userId != null) {
                navigator.mediaListEntryEditorScreen(id)
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
private fun MediaColumnCardContent(
    media: MediaModel,
    width: Dp? = null,
    height: Dp = 248.dp,
    footerContent: @Composable (ColumnScope.() -> Unit)? = null,
    onMediaClick: OnMediaClick,
    onMediaLongClick: OnMediaClick
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
            onMediaLongClick(media.id, media.type)
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

            MediaStatsBadge(
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
                val year = media.seasonYear

                LightText(
                    text = year?.let {
                        stringResource(id = I18nR.string.s_dot_s).format(
                            status,
                            year
                        )
                    } ?: status,
                    color = media.status.toColor(),
                    lineHeight = 11.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                val isAnime = media.isAnime
                val episodesOrChapters  = if (isAnime) media.episodes else media.chapters
                val epOrChStr = if(isAnime) anilib.i18n.R.string.ep_s_s else anilib.i18n.R.string.ch_s_s

                LightText(
                    modifier = Modifier.fillMaxWidth(),
                    text = episodesOrChapters?.let { stringResource(id = epOrChStr).format(episodesOrChapters, format) } ?: format,
                )
            }
        }
    }
}


val MediaCardTitleBottomPadding = 4.dp
val MediaInfoHorizontalPadding = 5.dp
val MediaInfoVerticalPadding = 5.dp