package com.revolgenx.anilib.plugins

import android.text.style.ClickableSpan
import android.view.View
import com.revolgenx.anilib.event.ImageClickedEvent
import com.revolgenx.anilib.meta.ImageMeta
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.ImageProps
import org.commonmark.node.Image

class ImageClickHandlerPlugin : AbstractMarkwonPlugin() {
    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        val origin = builder.getFactory(Image::class.java)
        if (origin != null) {
            builder.setFactory(
                Image::class.java
            ) { configuration, props ->
                arrayOf( // use origin image span
                    origin.getSpans(
                        configuration,
                        props
                    ),
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            ImageClickedEvent(ImageMeta(ImageProps.DESTINATION.require(props))).postEvent
                        }
                    }
                )
            }
        }
    }
}