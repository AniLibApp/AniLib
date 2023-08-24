package com.revolgenx.anilib.common.ui.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.composition.localNavigator


@Composable
fun ImageAsync(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    imageOptions: ImageOptions = ImageOptions(),
    @DrawableRes previewPlaceholder: Int = 0,
    viewable: Boolean = false,
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    imageLoader: ImageLoader? = null
) {
    if (LocalInspectionMode.current && previewPlaceholder != 0) {
        with(imageOptions) {
            Image(
                modifier = modifier,
                painter = painterResource(id = previewPlaceholder),
                alignment = alignment,
                contentScale = contentScale,
                alpha = alpha,
                colorFilter = colorFilter,
                contentDescription = contentDescription,
            )
            return
        }
    }

    with(imageOptions) {
        AsyncImage(
            modifier = modifier.let {
                if (viewable) {
                    val navigator = localNavigator()
                    it.clickable {
                        imageUrl?.let {
                            navigator.imageViewerScreen(imageUrl)
                        }
                    }
                } else {
                    modifier
                }
            },
            model = imageUrl,
            contentDescription = contentDescription,
            error = imageUrl?.let { painterResource(id = R.drawable.ic_error_anilib) },
            contentScale = contentScale,
            alignment = alignment,
            alpha = alpha,
            colorFilter = colorFilter,
            onLoading = {
                onLoading?.invoke()
            },
            onError = {
                onError?.invoke()
            },
            onSuccess = {
                onSuccess?.invoke()
            },
            imageLoader = imageLoader ?: localContext().imageLoader
        )
    }
}


@Immutable
data class ImageOptions(
    val alignment: Alignment = Alignment.Center,
    val contentDescription: String? = null,
    val contentScale: ContentScale = ContentScale.Crop,
    val colorFilter: ColorFilter? = null,
    val alpha: Float = DefaultAlpha,
)
