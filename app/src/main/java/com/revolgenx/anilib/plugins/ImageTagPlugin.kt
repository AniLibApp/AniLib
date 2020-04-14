package com.revolgenx.anilib.plugins

import android.text.style.ClickableSpan
import android.view.View
import com.revolgenx.anilib.event.ImageClickedEvent
import com.revolgenx.anilib.meta.ImageMeta
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import org.commonmark.node.Image
import timber.log.Timber
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
                renderProps.set(ImageProps.DESTINATION, source)
                renderProps.set(
                    ImageProps.IMAGE_SIZE,
                    ImageSize(ImageSize.Dimension(100f, "%"), null)
                )
                return arrayOf(configuration.spansFactory().get(Image::class.java)
                    ?.getSpans(configuration, renderProps), object : ClickableSpan() {
                    override fun onClick(widget: View) {
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