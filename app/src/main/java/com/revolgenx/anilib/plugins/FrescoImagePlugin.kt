package com.revolgenx.anilib.plugins

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.text.Spanned
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.fresco.animation.drawable.AnimatedDrawable2
import com.facebook.imagepipeline.image.CloseableAnimatedImage
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.CloseableStaticBitmap
import com.facebook.imagepipeline.request.ImageRequest
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.*
import com.revolgenx.anilib.util.dp
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.*
import org.commonmark.node.Image
import timber.log.Timber
import kotlin.math.absoluteValue


class FrescoImagePlugin(private val frescoAsyncDrawableLoader: FrescoAsyncDrawableLoader) :
    AbstractMarkwonPlugin() {

    companion object {
        fun create(context: Context): FrescoImagePlugin {
            return FrescoImagePlugin(FrescoAsyncDrawableLoader(FrescoStoreImpl(context)))
        }
    }

    interface FrescoStore {
        fun load(drawable: AsyncDrawable)
        fun cancel(drawable: AsyncDrawable)
    }

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(Image::class.java, ImageSpanFactory())
    }

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.asyncDrawableLoader(frescoAsyncDrawableLoader)
    }

    override fun beforeSetText(textView: TextView, markdown: Spanned) {
        AsyncDrawableScheduler.unschedule(textView);
    }

    override fun afterSetText(textView: TextView) {
        AsyncDrawableScheduler.schedule(textView);
    }

    class FrescoAsyncDrawableLoader(private val frescoStore: FrescoStore) : AsyncDrawableLoader() {
        override fun placeholder(drawable: AsyncDrawable): Drawable? {
            return null
        }

        override fun cancel(drawable: AsyncDrawable) {
            frescoStore.cancel(drawable)
        }

        override fun load(drawable: AsyncDrawable) {
            frescoStore.load(drawable)
        }
    }

    class FrescoStoreImpl(private val context: Context) : FrescoStore {
        private val closeableRefs = mutableMapOf<String, CloseableReference<CloseableImage>>()
        private val frescoPipeline
            get() = Fresco.getImagePipeline()

        private val frescoImagePipelineFactory
            get() = Fresco.getImagePipelineFactory()

        private var imageSource: ImageSourceType = ImageSourceType.NORMAL

        private val playBitMapDrawable: BitmapDrawable?
            get() {
                return ContextCompat.getDrawable(context, R.drawable.ic_play_cirle)
                    ?.toBitmap(dp(48f), dp(48f))
                    ?.toDrawable(context.resources)?.also {
                        it.setTint(DynamicTheme.getInstance().get().accentColor)
                        it.gravity = Gravity.CENTER
                    }
            }

        companion object {
            enum class ImageSourceType {
                NORMAL, VIDEO, YOUTUBE
            }
        }

        override fun load(drawable: AsyncDrawable) {
            val source = filterSource(drawable.destination)

            val dataSource = frescoPipeline.fetchDecodedImage(
                ImageRequest.fromUri(source),
                context
            )
            dataSource.subscribe(object :
                BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                override fun onNewResultImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                    if (dataSource.isFinished.not()) return
                    dataSource.result?.let {
                        closeableRefs[drawable.destination] = it
                        try {
                            val resource = createDrawable(it.get()) ?: return
                            DrawableUtils.applyIntrinsicBounds(resource)

                            if (imageSource == ImageSourceType.NORMAL) {
                                drawable.result = resource
                            } else {
                                val finalDrawable =
                                    LayerDrawable(arrayOf(resource, playBitMapDrawable))
                                DrawableUtils.applyIntrinsicBounds(finalDrawable)
                                drawable.result = finalDrawable
                            }
                        } finally {
                            closeableRefs.remove(drawable.destination)
                            CloseableReference.closeSafely(it)
                        }
                    }
                }

                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                    val d = dataSource.failureCause
                }
            }, CallerThreadExecutor.getInstance())
        }

        private fun filterSource(source: String): String {
            return when {
                source.contains(VIDEO_TAG) -> {
                    imageSource = ImageSourceType.VIDEO
                    source.substring(VIDEO_START_INDEX)
                }
                source.contains(YOUTUBE_TAG) -> {
                    imageSource = ImageSourceType.YOUTUBE
                    try {
                        String.format(YOUTUBE_IMG_URL, source.substring(source.indexOf("?v=") + 3))
                    } catch (e: Exception) {
                        ""
                    }
                }
                else -> {
                    imageSource = ImageSourceType.NORMAL
                    source
                }
            }
        }

        override fun cancel(drawable: AsyncDrawable) {
            closeableRefs[drawable.destination]?.let {
                CloseableReference.closeSafely(it)
                closeableRefs.remove(drawable.destination)
            }
        }


        private fun createDrawable(closeableImage: CloseableImage): Drawable? {
            return when (closeableImage) {
                is CloseableAnimatedImage -> {
                    (frescoImagePipelineFactory.getAnimatedDrawableFactory(context)?.createDrawable(
                        closeableImage
                    ) as? AnimatedDrawable2)?.also {
                        it.start()
                    }
                }
                is CloseableStaticBitmap -> {
                    BitmapDrawable(context.resources, closeableImage.underlyingBitmap)
                }
                else -> {
                    null
                }
            }
        }
    }

}