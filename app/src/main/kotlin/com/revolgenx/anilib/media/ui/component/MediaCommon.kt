package com.revolgenx.anilib.media.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.composition.LocalMediaState
import com.revolgenx.anilib.media.ui.model.MediaModel
import com.revolgenx.anilib.media.ui.model.toColor
import com.revolgenx.anilib.media.ui.model.toStringRes
import kotlinx.coroutines.launch

@Composable
fun MediaTitleType(content: @Composable (type: Int) -> Unit) {
    content(LocalMediaState.current.titleType)
}

@Composable
fun MediaCoverImageType(content: @Composable (type: Int) -> Unit) {
    content(LocalMediaState.current.coverImageType)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaRowCommonContent(
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
                        navigator.mediaListEntryEditorScreen(media.id)
                    } else {
                        scope.launch {
                            snackbarHostState?.showSnackbar(
                                context.getString(R.string.please_log_in),
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
                    previewPlaceholder = com.revolgenx.anilib.R.drawable.bleach
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
                text = stringResource(id = R.string.s_dot_s).format(format, year),
            )

            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaRowCommonContentEnd(
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
                        navigator.mediaListEntryEditorScreen(media.id)
                    } else {
                        scope.launch {
                            snackbarHostState?.showSnackbar(
                                context.getString(R.string.please_log_in),
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
                text = stringResource(id = R.string.s_dot_s).format(format, year),
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
                    previewPlaceholder = com.revolgenx.anilib.R.drawable.bleach
                )
            }



            MediaStatsBadge(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
                media = media
            )

        }
    }
}