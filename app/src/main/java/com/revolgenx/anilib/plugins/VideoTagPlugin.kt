package com.revolgenx.anilib.plugins

import com.revolgenx.anilib.constant.VIDEO_TAG
import io.noties.markwon.*
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import io.noties.markwon.image.ImageSpanFactory
import org.commonmark.node.Image
import java.util.*

class VideoTagPlugin : CustomPlugin() {

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
                    var source = tag.asBlock.children().firstOrNull()?.attributes()?.get(SRC) ?: return null
                    source = VIDEO_TAG + source
                    renderProps.set(ImageProps.DESTINATION, source)
                    renderProps.set(ImageProps.IMAGE_SIZE, ImageSize(ImageSize.Dimension(100f,"%"), null))
                    return imageSpanFactory?.getSpans(configuration, renderProps)
                }

                override fun supportedTags(): MutableCollection<String> {
                    return Collections.singleton(VIDEO)
                }
            })
        }
    }
}