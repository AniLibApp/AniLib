package com.revolgenx.anilib.common.ui.component.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.imageLoader
import com.revolgenx.anilib.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.coil.CoilImageState
import com.skydoves.landscapist.coil.LocalCoilImageLoader
import com.skydoves.landscapist.components.rememberImageComponent

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    imageLoader: @Composable () -> ImageLoader = { getCoilImageLoader() },
    imageOptions: ImageOptions = ImageOptions(),
    @DrawableRes previewPlaceholder: Int = 0,
    failure: (@Composable BoxScope.(CoilImageState.Failure) -> Unit)? = null
) {
    CoilImage(
        modifier = modifier,
        imageModel = { imageUrl },
        imageOptions = imageOptions,
        imageLoader = imageLoader,
        previewPlaceholder = previewPlaceholder,
        failure = failure ?: {
            Image(
                painter = painterResource(id = R.drawable.ic_error_anilib),
                contentDescription = null
            )
        }
    )
}

@Composable
fun getCoilImageLoader(): ImageLoader {
    return LocalCoilImageLoader.current ?: LocalContext.current.imageLoader
}