package com.revolgenx.anilib.markwon.plugins.markwon

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.text.style.ClickableSpan
import android.view.View
import com.revolgenx.anilib.constant.YOUTUBE_IMG_URL
import com.revolgenx.anilib.event.YoutubeClickedEvent
import com.revolgenx.anilib.meta.YoutubeMeta
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
import com.revolgenx.anilib.view.drawable.YoutubePlayBitmapDrawable
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

class YoutubeMarkwonPlugin(private val context: Context) : AbstractMarkwonPlugin() {
    companion object {
        fun create(context: Context) =
            YoutubeMarkwonPlugin(
                context
            )
    }

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(CorePlugin::class.java) { corePlugin ->
            corePlugin.addOnTextAddedListener(
                YoutubeMarkwonListener(
                    context
                )
            )
        }
    }

    internal class YoutubeMarkwonListener(val context: Context) : CorePlugin.OnTextAddedListener {
        companion object {
            val pattern: Pattern = Pattern.compile("youtube\\(([^)]+)\\)")
            val youtubeRegex =
                Regex("(?:youtube(?:-nocookie)?\\.com/(?:[^/]+/.+/|(?:v|e(?:mbed)?)/|.*[?&]v=)|youtu\\.be/)([^\"&?/ ]{11})")
        }

        private val playBitMapDrawable: YoutubePlayBitmapDrawable?
            get() = YoutubePlayBitmapDrawable(context)

        override fun onTextAdded(visitor: MarkwonVisitor, text: String, start: Int) {
            val matcher = pattern.matcher(text)
            while (matcher.find()) {

                val configuration = visitor.configuration();
                val renderProps = visitor.renderProps();
                val source = matcher.group(1) ?: ""

                renderProps.set(
                    ImageProps.DESTINATION,
                    String.format(YOUTUBE_IMG_URL, youtubeRegex.find(source)?.groupValues?.get(1) ?: "")
                )

                renderProps.set(
                    ImageProps.IMAGE_SIZE,
                    ImageSize(ImageSize.Dimension(100f, "%"), null)
                )

                val youtubeSpan = configuration.spansFactory().get(Image::class.java)?.getSpans(
                    configuration,
                    renderProps
                ) as AsyncDrawableSpan

                youtubeSpan.drawable.result = LayerDrawable(
                    arrayOf(playBitMapDrawable, null)
                )

                val index = start + matcher.start()
                SpannableBuilder.setSpans(
                    visitor.builder(),
                    arrayOf(youtubeSpan, object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            if ((youtubeSpan.drawable.result is LayerDrawable)) {
                                val layerDrawable = youtubeSpan.drawable.result as LayerDrawable
                                val spoilerDrawable = layerDrawable.getDrawable(1)
                                if (spoilerDrawable is SpoilerDrawable) {
                                    spoilerDrawable.hasSpoiler = false
                                    youtubeSpan.drawable.result = LayerDrawable(
                                        arrayOf(
                                            layerDrawable.getDrawable(0),
                                            layerDrawable.getDrawable(2)
                                        )
                                    ).also {
                                        DrawableUtils.applyIntrinsicBoundsIfEmpty(it)
                                    }
                                    return
                                }
                            }
                            YoutubeClickedEvent(YoutubeMeta(source)).postEvent
                        }
                    })
                    , index
                    , index + source.length + 1
                )
            }
        }
    }
}