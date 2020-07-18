package com.revolgenx.anilib.markwon.plugins

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.text.style.ClickableSpan
import android.view.View
import com.revolgenx.anilib.constant.YOUTUBE
import com.revolgenx.anilib.constant.YOUTUBE_IMG_URL
import com.revolgenx.anilib.event.YoutubeClickedEvent
import com.revolgenx.anilib.meta.YoutubeMeta
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
import com.revolgenx.anilib.view.drawable.YoutubePlayBitmapDrawable
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.*
import org.commonmark.node.Image
import java.util.*

class YoutubeTagPlugin(private val context: Context) : CustomPlugin() {
    private val playBitMapDrawable: YoutubePlayBitmapDrawable?
        get() = YoutubePlayBitmapDrawable(context)

    companion object {
        val youtubeRegex =
            Regex("(?:youtube(?:-nocookie)?\\.com/(?:[^/]+/.+/|(?:v|e(?:mbed)?)/|.*[?&]v=)|youtu\\.be/)([^\"&?/ ]{11})")
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
                    val source = tag.attributes()[ID] ?: return null
                    val containsSpoiler = tag.attributes()[ALT] == MARKDOWN_SPOILER

                    renderProps.set(
                        ImageProps.DESTINATION,
                        String.format(
                            YOUTUBE_IMG_URL,
                            youtubeRegex.find(source)?.groupValues?.get(1) ?: ""
                        )
                    )
                    renderProps.set(
                        ImageProps.IMAGE_SIZE,
                        ImageSize(ImageSize.Dimension(100f, "%"), null)
                    )

                    var youtubeSpan = configuration.spansFactory().get(Image::class.java)?.getSpans(
                        configuration,
                        renderProps
                    ) as? AsyncDrawableSpan

                    if (youtubeSpan == null){
                        youtubeSpan = ImageSpanFactory().getSpans(configuration, renderProps) as AsyncDrawableSpan
                    }


                    youtubeSpan.drawable.result = if (!containsSpoiler) LayerDrawable(
                        arrayOf(playBitMapDrawable, null)
                    ) else LayerDrawable(arrayOf(playBitMapDrawable, ColorDrawable()))

                    return arrayOf(youtubeSpan, object : ClickableSpan() {
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
                }


                override fun supportedTags(): MutableCollection<String> {
                    return Collections.singleton(DIV)
                }
            })
        }
    }
}