package com.revolgenx.anilib.plugins

import android.graphics.drawable.LayerDrawable
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.revolgenx.anilib.constant.VIDEO_START_INDEX
import com.revolgenx.anilib.constant.VIDEO_TAG
import com.revolgenx.anilib.constant.YOUTUBE_START_INDEX
import com.revolgenx.anilib.constant.YOUTUBE_TAG
import com.revolgenx.anilib.event.ImageClickedEvent
import com.revolgenx.anilib.event.VideoClickedEvent
import com.revolgenx.anilib.event.YoutubeClickedEvent
import com.revolgenx.anilib.meta.ImageMeta
import com.revolgenx.anilib.meta.VideoMeta
import com.revolgenx.anilib.meta.YoutubeMeta
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.image.ImageProps
import org.commonmark.node.Image
import timber.log.Timber

class ImageClickHandlerPlugin : AbstractMarkwonPlugin() {

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        val origin = builder.getFactory(Image::class.java)
        if (origin != null) {
            builder.setFactory(
                Image::class.java
            ) { configuration, props ->
                val span = origin.getSpans(
                    configuration,
                    props
                )
                arrayOf( // use origin image span
                    span,
                    object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            val tv = widget as TextView
                            val text = tv.text
                            val spanned = text as Spanned
                            val start = spanned.getSpanStart(this)
                            val end = spanned.getSpanEnd(this)
                            var source = text.substring(start, end)

                            val asyncDrawableSpan = span as AsyncDrawableSpan
                            asyncDrawableSpan.drawable.result  = (asyncDrawableSpan.drawable.result as LayerDrawable).getDrawable(0)

                            when {
                                source.contains(VIDEO_TAG) -> {
                                    source = source.substring(VIDEO_START_INDEX)
                                    VideoClickedEvent(VideoMeta(source)).postEvent
                                }
                                source.contains(YOUTUBE_TAG) -> {
                                    source = source.substring(YOUTUBE_START_INDEX)
                                    YoutubeClickedEvent(YoutubeMeta(source)).postEvent
                                }
                                else -> {
                                    ImageClickedEvent(ImageMeta(source)).postEvent
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}