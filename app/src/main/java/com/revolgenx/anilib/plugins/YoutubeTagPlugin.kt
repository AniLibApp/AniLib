package com.revolgenx.anilib.plugins

import android.content.Context
import android.graphics.drawable.BitmapDrawable
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
import com.revolgenx.anilib.constant.YOUTUBE_START_INDEX
import com.revolgenx.anilib.event.YoutubeClickedEvent
import com.revolgenx.anilib.meta.YoutubeMeta
import com.revolgenx.anilib.util.dp
import io.noties.markwon.*
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import org.commonmark.node.Image
import java.util.*

class YoutubeTagPlugin(private val context: Context) : CustomPlugin() {
    private val playBitMapDrawable: BitmapDrawable?
        get() {
            return ContextCompat.getDrawable(context, R.drawable.ic_play_cirle)
                ?.toBitmap(dp(48f), dp(48f))
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

                    youtubeSpan.drawable.result = LayerDrawable(arrayOf(playBitMapDrawable))

                    return arrayOf(youtubeSpan, object : ClickableSpan() {
                        override fun onClick(widget: View) {
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