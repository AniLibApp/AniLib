package com.revolgenx.anilib.plugins

import com.revolgenx.anilib.constant.YOUTUBE
import com.revolgenx.anilib.constant.YOUTUBE_TAG
import io.noties.markwon.*
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import io.noties.markwon.image.ImageSpanFactory
import org.commonmark.node.Image
import java.util.*

class YoutubeTagPlugin : CustomPlugin() {

    private var imageSpanFactory:SpanFactory? = null
    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        imageSpanFactory = builder.getFactory(Image::class.java)
    }
    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(HtmlPlugin::class.java) { plugin ->
            plugin.addHandler(object : SimpleTagHandler() {
                override fun getSpans(
                    configuration: MarkwonConfiguration,
                    renderProps: RenderProps,
                    tag: HtmlTag
                ): Any? {
                    if (tag.attributes()[CLASS] != YOUTUBE) return null
                    var source = tag.attributes()[ID] ?: return null
                    source = YOUTUBE_TAG + source
                    renderProps.set(ImageProps.DESTINATION, source)
                    renderProps.set(ImageProps.REPLACEMENT_TEXT_IS_LINK, true)
                    renderProps.set(
                        ImageProps.IMAGE_SIZE,
                        ImageSize(ImageSize.Dimension(100f, "%"), null)
                    )

                    return imageSpanFactory?.getSpans(configuration, renderProps)
                }


                override fun supportedTags(): MutableCollection<String> {
                    return Collections.singleton(DIV)
                }
            })
        }
    }
}