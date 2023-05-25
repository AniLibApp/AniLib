package com.revolgenx.anilib.common.ui.component.image

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.imageLoader
import com.revolgenx.anilib.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.coil.LocalCoilImageLoader
import com.skydoves.landscapist.components.rememberImageComponent

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    imageLoader: @Composable () -> ImageLoader = { getCoilImageLoader() },
    imageOptions: ImageOptions = ImageOptions(),
    @DrawableRes previewPlaceholder: Int = 0,
) {
    CoilImage(
        modifier = modifier,
        imageModel = { imageUrl },
        imageOptions = imageOptions,
        imageLoader = imageLoader,
        previewPlaceholder = previewPlaceholder,
        component = rememberImageComponent {
            +CrossfadePlugin(
                duration = 300
            )
        },
        failure = {
            Icon(
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