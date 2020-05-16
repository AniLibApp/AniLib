package com.revolgenx.anilib.markwon.plugins.markwon

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.text.style.ClickableSpan
import android.view.View
import com.revolgenx.anilib.event.ImageClickedEvent
import com.revolgenx.anilib.meta.ImageMeta
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.SpannableBuilder
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.image.DrawableUtils
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import org.commonmark.node.Image
import java.util.regex.Pattern

class ImageMarkwonPlugin(private val context: Context) : AbstractMarkwonPlugin() {

    companion object {
        fun create(context: Context) = ImageMarkwonPlugin(context)
    }

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(CorePlugin::class.java) { corePlugin ->
            corePlugin.addOnTextAddedListener(ImageMarkwonListener(context))
        }
    }

    internal class ImageMarkwonListener(context: Context) : CorePlugin.OnTextAddedListener {
        companion object {
            val pattern: Pattern = Pattern.compile("img(\\d+)?\\(([^)]+)\\)")
        }

        override fun onTextAdded(visitor: MarkwonVisitor, text: String, start: Int) {
            val matcher = pattern.matcher(text)
            while (matcher.find()) {
                val widthStr = matcher.group(1)
                val source = matcher.group(2) ?: ""
                val configuration = visitor.configuration();
                val renderProps = visitor.renderProps();


                val widthPercent = widthStr?.toFloatOrNull()?.let { width ->
                    if (width > 500) 100f else width.div(500).times(100)
                } ?: 80f

                renderProps.set(ImageProps.DESTINATION, source)
                renderProps.set(
                    ImageProps.IMAGE_SIZE,
                    ImageSize(ImageSize.Dimension(widthPercent, "%"), null)
                )

                val imageSpan = configuration.spansFactory().get(Image::class.java)?.getSpans(
                    configuration,
                    renderProps
                ) as AsyncDrawableSpan

                val index = start + matcher.start()

                SpannableBuilder.setSpans(
                    visitor.builder()
                    , arrayOf(imageSpan, object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            if ((imageSpan.drawable.result is LayerDrawable)) {
                                val layerDrawable = imageSpan.drawable.result as LayerDrawable
                                val spoilerDrawable = layerDrawable.getDrawable(1)
                                if (spoilerDrawable is SpoilerDrawable) {
                                    spoilerDrawable.hasSpoiler = false
                                    imageSpan.drawable.result = layerDrawable.getDrawable(0).also {
                                        DrawableUtils.applyIntrinsicBoundsIfEmpty(it)
                                    }
                                    return
                                }
                            }
                            ImageClickedEvent(ImageMeta(source)).postEvent
                        }
                    })
                    , index
                    , index + source.length + 1
                )
            }
        }
    }
}