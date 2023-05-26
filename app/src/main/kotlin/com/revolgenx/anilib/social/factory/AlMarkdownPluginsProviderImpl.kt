package com.revolgenx.anilib.social.factory

import androidx.compose.ui.graphics.toArgb
import com.revolgenx.anilib.common.ui.theme.LightColorScheme
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.core.MarkwonTheme

class AlMarkdownPluginsProviderImpl : AlMarkdownPluginsProvider {
    override fun getPlugins(): List<AbstractMarkwonPlugin> {
        return listOf(Plugin())
    }
}
internal class Plugin() : AbstractMarkwonPlugin() {
    override fun configureTheme(builder: MarkwonTheme.Builder) {
        val primaryColor = LightColorScheme.primary.toArgb()
        builder
            .blockQuoteColor(primaryColor)
            .linkColor(primaryColor)
    }
}
