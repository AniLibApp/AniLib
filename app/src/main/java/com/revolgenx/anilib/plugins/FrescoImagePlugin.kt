package com.revolgenx.anilib.plugins

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
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
import com.facebook.imagepipeline.nativecode.NativeBlurFilter
import com.facebook.imagepipeline.request.ImageRequest
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.*
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
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

        override fun load(drawable: AsyncDrawable) {
            val dataSource = Fresco.getImagePipeline().fetchDecodedImage(
                ImageRequest.fromUri(drawable.destination),
                context
            )
            dataSource.subscribe(object :
                BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                override fun onNewResultImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                    if (!dataSource.isFinished) return

                    dataSource.result?.let {
                        try {
                            val resource = createDrawable(it.get()) ?: return
                            if (drawable.result is LayerDrawable && drawable.hasResult()) {
                                val layerDrawable = drawable.result as LayerDrawable
                                val spoilerDrawable = layerDrawable.getDrawable(1)
                                if (spoilerDrawable == null) {
                                    val finalDrawable =
                                        LayerDrawable(
                                            arrayOf(
                                                resource,
                                                layerDrawable.getDrawable(0)
                                            )
                                        )
                                    DrawableUtils.applyIntrinsicBounds(finalDrawable)
                                    drawable.result = finalDrawable
                                } else {
                                    val newBlurBitmap = resource.toBitmap(
                                        (resource.intrinsicWidth * 0.2).toInt(),
                                        (resource.intrinsicHeight * 0.2).toInt()
                                    )

                                    NativeBlurFilter.iterativeBoxBlur(newBlurBitmap, 5, 1)
                                    val finalDrawable =
                                        LayerDrawable(
                                            arrayOf(
                                                resource,
                                                SpoilerDrawable(
                                                    context,
                                                    newBlurBitmap
                                                ).also {
                                                    it.hasSpoiler = true
                                                },
                                                layerDrawable.getDrawable(0)
                                            )
                                        )
                                    DrawableUtils.applyIntrinsicBoundsIfEmpty(finalDrawable)
                                    drawable.result = finalDrawable
                                }
                                return
                            }else if(drawable.result is ColorDrawable){
                                val newBlurBitmap = resource.toBitmap(
                                    (resource.intrinsicWidth * 0.2).toInt(),
                                    (resource.intrinsicHeight * 0.2).toInt()
                                )

                                NativeBlurFilter.iterativeBoxBlur(newBlurBitmap, 8, 1)
                                val finalDrawable =
                                    LayerDrawable(
                                        arrayOf(
                                            resource,
                                            SpoilerDrawable(
                                                context,
                                                newBlurBitmap
                                            ).also {
                                                it.hasSpoiler = true
                                            }
                                        )
                                    )
                                DrawableUtils.applyIntrinsicBoundsIfEmpty(finalDrawable)
                                drawable.result = finalDrawable
                                return
                            }
                            DrawableUtils.applyIntrinsicBoundsIfEmpty(resource)
                            drawable.result = resource
                        } finally {
                            CloseableReference.closeSafely(it)
                        }
                    }
                }

                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                }
            }, CallerThreadExecutor.getInstance())
        }

        override fun cancel(drawable: AsyncDrawable) {
        }


        private fun createDrawable(closeableImage: CloseableImage): Drawable? {
            return when (closeableImage) {
                is CloseableAnimatedImage -> {
                    (Fresco.getImagePipelineFactory().getAnimatedDrawableFactory(context)?.createDrawable(
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