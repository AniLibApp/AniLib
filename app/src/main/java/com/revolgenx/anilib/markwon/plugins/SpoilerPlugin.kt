package com.revolgenx.anilib.markwon.plugins

import com.revolgenx.anilib.ui.view.span.SpoilerSpan
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler

class SpoilerPlugin : CustomPlugin() {
    companion object {
        fun create(): SpoilerPlugin {
            return SpoilerPlugin()
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
                    if (tag.attributes().containsKey(CLASS)) {
                        if (tag.attributes()[CLASS].equals(MARKDOWN_SPOILER, true)) {
                            return SpoilerSpan()
                        }
                    }

                    return null
                }

                override fun supportedTags(): MutableCollection<String> {
                    return mutableListOf(SPAN)
                }
            })
        }
        super.configure(registry)
    }
}