package com.revolgenx.anilib.markwon.plugins

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.YOUTUBE
import com.revolgenx.anilib.constant.YOUTUBE_IMG_URL
import com.revolgenx.anilib.event.YoutubeClickedEvent
import com.revolgenx.anilib.meta.YoutubeMeta
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
import io.noties.markwon.*
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.image.DrawableUtils
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import org.commonmark.node.Image
import java.util.*

class YoutubeTagPlugin(private val context: Context) : CustomPlugin() {
    private val playBitMapDrawable: BitmapDrawable?
        get() {
            return ContextCompat.getDrawable(context, R.drawable.ic_play_cirle)
                ?.toBitmap(dp(56f), dp(56f))
                ?.toDrawable(context.resources)?.also {
                    it.setTint(DynamicTheme.getInstance().get().accentColor)
                    it.gravity = Gravity.CENTER
                }
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
                        String.format(YOUTUBE_IMG_URL, source.substring(source.indexOf("?v=") + 3))
                    )
                    renderProps.set(ImageProps.REPLACEMENT_TEXT_IS_LINK, true)
                    renderProps.set(
                        ImageProps.IMAGE_SIZE,
                        ImageSize(ImageSize.Dimension(100f, "%"), null)
                    )

                    val youtubeSpan = configuration.spansFactory().get(Image::class.java)?.getSpans(
                        configuration,
                        renderProps
                    ) as AsyncDrawableSpan


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
                                    youtubeSpan.drawable.result = LayerDrawable(arrayOf(layerDrawable.getDrawable(0), layerDrawable.getDrawable(2))).also {
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