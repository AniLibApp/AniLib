package com.revolgenx.anilib.markwon.plugins

import android.text.Layout
import android.text.style.AlignmentSpan
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler
import java.util.*

class CenterPlugin : CustomPlugin() {

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(HtmlPlugin::class.java) { plugin ->
            plugin.addHandler(object : SimpleTagHandler() {
                override fun getSpans(
                    configuration: MarkwonConfiguration,
                    renderProps: RenderProps,
                    tag: HtmlTag
                ): Any? {
                    return arrayOf(AlignmentSpan { Layout.Alignment.ALIGN_CENTER })
                }

                override fun supportedTags(): MutableCollection<String> {
                    return Collections.singleton(CENTER)
                }

            })
        }
        super.configure(registry)
    }
}