package com.revolgenx.anilib.markwon.plugins

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.text.style.ClickableSpan
import android.view.View
import com.revolgenx.anilib.event.VideoClickedEvent
import com.revolgenx.anilib.meta.VideoMeta
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
import com.revolgenx.anilib.view.drawable.VideoPlayBitmapDrawable
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

class VideoTagPlugin(private val context: Context) : CustomPlugin() {

    private val playBitmapDrawable: VideoPlayBitmapDrawable?
        get() = VideoPlayBitmapDrawable(context)

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(HtmlPlugin::class.java) { plugin ->
            plugin.addHandler(object : SimpleTagHandler() {
                override fun getSpans(
                    configuration: MarkwonConfiguration,
                    renderProps: RenderProps,
                    tag: HtmlTag
                ): Any? {
                    if (!tag.isBlock) return null

                    val source =
                        tag.asBlock.children().firstOrNull()?.attributes()?.get(SRC) ?: return null
                    val containsSpoiler = tag.attributes()[ALT] == MARKDOWN_SPOILER

                    renderProps.set(ImageProps.DESTINATION, source)
                    renderProps.set(
                        ImageProps.IMAGE_SIZE,
                        ImageSize(ImageSize.Dimension(100f, "%"), null)
                    )


                    val videoSpan = configuration.spansFactory().get(Image::class.java)?.getSpans(
                        configuration,
                        renderProps
                    ) as AsyncDrawableSpan

                    videoSpan.drawable.result = (if (!containsSpoiler) LayerDrawable(
                        arrayOf(playBitmapDrawable, null)
                    ) else LayerDrawable(arrayOf(playBitmapDrawable, ColorDrawable()))).also {
                        DrawableUtils.applyIntrinsicBounds(it)
                    }

                    return arrayOf(videoSpan, object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            if ((videoSpan.drawable.result is LayerDrawable)) {
                                val layerDrawable = videoSpan.drawable.result as LayerDrawable
                                val spoilerDrawable = layerDrawable.getDrawable(1)
                                if (spoilerDrawable is SpoilerDrawable) {
                                    spoilerDrawable.hasSpoiler = false
                                    videoSpan.drawable.result = LayerDrawable(
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
                            VideoClickedEvent(VideoMeta(source)).postEvent
                        }
                    })
                }

                override fun supportedTags(): MutableCollection<String> {
                    return Collections.singleton(VIDEO)
                }
            })
        }
    }
}