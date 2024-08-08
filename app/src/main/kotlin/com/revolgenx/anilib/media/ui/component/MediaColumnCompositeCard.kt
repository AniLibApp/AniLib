package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.browseGenreScreen
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.revolgenx.anilib.common.util.OnMediaClick
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes
import kotlinx.coroutines.launch

@Composable
fun MediaItemColumnCompositeCard(
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

    MediaColumnCompositeCardContent(
        width,
        height,
        media = media,
        footerContent = footerContent,
        onMediaClick = onMediaClickHandler(mediaComponentState),
        onGenreClick = {
            navigator.browseGenreScreen(it)
        },
        onMediaLongClick = { id, _ ->
            if (userId != null) {
                navigator.mediaListEntryEditorScreen(id)
            } else {
                scope.launch {
                    snackbarHostState?.showSnackbar(
                        context.getString(R.string.please_log_in),
                        withDismissAction = true
                    )
                }
            }
        }
    )
}

@Composable
private fun MediaColumnCompositeCardContent(
    width: Dp?,
    height: Dp,
    media: MediaModel,
    onMediaClick: OnMediaClick,
    onMediaLongClick: OnMediaClick,
    onGenreClick: OnClickWithValue<String>,
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
            onMediaLongClick(media.id, media.type)
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
                    previewPlaceholder = com.revolgenx.anilib.R.drawable.bleach
                )
            }


            MediaStatsBadge(
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
                                    LightText(
                                        modifier = Modifier.clickable {
                                            onGenreClick(it)
                                        },
                                        text = it,
                                        color = MaterialTheme.colorScheme.primary,
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
                            text = stringResource(id = R.string.s_dot_s).format(format, year),
                            lineHeight = 11.sp
                        )
                    }

                }
            }
        }
    }
}