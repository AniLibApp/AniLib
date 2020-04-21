package com.revolgenx.anilib.markwon.plugins

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.text.style.ClickableSpan
import android.view.View
import com.revolgenx.anilib.event.ImageClickedEvent
import com.revolgenx.anilib.meta.ImageMeta
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.image.DrawableUtils
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import org.commonmark.node.Image
import java.util.*

class ImageTagPlugin : CustomPlugin() {
    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(HtmlPlugin::class.java).addHandler(object : SimpleTagHandler() {
            override fun getSpans(
                configuration: MarkwonConfiguration,
                renderProps: RenderProps,
                tag: HtmlTag
            ): Any? {

                val source = tag.attributes()[SRC] ?: return null
                val widthStr = tag.attributes()[WIDTH]?.trim()


                val widthPercent = if (widthStr?.contains("%") == true) {
                    widthStr.replace("%", "").toFloatOrNull() ?: 100f
                } else {
                    widthStr?.toFloatOrNull()?.let { width ->
                        if (width > 500) 100f else width.div(500).times(100)
                    } ?: 50f
                }
                val containsSpoiler = tag.attributes()[ALT] == MARKDOWN_SPOILER

                renderProps.set(ImageProps.DESTINATION, source)
                renderProps.set(
                    ImageProps.IMAGE_SIZE,
                    ImageSize(ImageSize.Dimension(widthPercent, "%"), null)
                )

                val imageSpan = configuration.spansFactory().get(Image::class.java)?.getSpans(
                    configuration,
                    renderProps
                ) as AsyncDrawableSpan

                if (containsSpoiler)
                    imageSpan.drawable.result = ColorDrawable()

                return arrayOf(imageSpan, object : ClickableSpan() {
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
            }

            override fun supportedTags(): MutableCollection<String> {
                return Collections.singleton(IMG)
            }

        })
    }
}