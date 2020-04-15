package com.revolgenx.anilib.plugins

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
import com.revolgenx.anilib.constant.VIDEO_START_INDEX
import com.revolgenx.anilib.constant.VIDEO_TAG
import com.revolgenx.anilib.event.VideoClickedEvent
import com.revolgenx.anilib.meta.VideoMeta
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.view.drawable.SpoilerDrawable
import io.noties.markwon.*
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import io.noties.markwon.image.*
import org.commonmark.node.Image
import java.util.*

class VideoTagPlugin(private val context: Context) : CustomPlugin() {

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
                    if (!tag.isBlock) return null

                    val source = tag.asBlock.children().firstOrNull()?.attributes()?.get(SRC) ?: return null
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

                    videoSpan.drawable.result =  if (!containsSpoiler) LayerDrawable(
                        arrayOf(playBitMapDrawable, null)
                    ) else LayerDrawable(arrayOf(playBitMapDrawable, ColorDrawable()))

                    return arrayOf(videoSpan, object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            if ((videoSpan.drawable.result is LayerDrawable)) {
                                val layerDrawable = videoSpan.drawable.result as LayerDrawable
                                val spoilerDrawable = layerDrawable.getDrawable(1)
                                if (spoilerDrawable is SpoilerDrawable) {
                                    spoilerDrawable.hasSpoiler = false
                                    videoSpan.drawable.result = LayerDrawable(arrayOf(layerDrawable.getDrawable(0), layerDrawable.getDrawable(2))).also {
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