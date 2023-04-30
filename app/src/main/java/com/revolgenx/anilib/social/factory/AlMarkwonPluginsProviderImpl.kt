package com.revolgenx.anilib.social.factory

import androidx.compose.ui.graphics.toArgb
import com.revolgenx.anilib.app.preference.colorScheme
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.core.MarkwonTheme

class AlMarkwonPluginsProviderImpl : AlMarkwonPluginsProvider {
    override fun getPlugins(): List<AbstractMarkwonPlugin> {
        return listOf(Plugin())
    }
}
internal class Plugin() : AbstractMarkwonPlugin() {
    override fun configureTheme(builder: MarkwonTheme.Builder) {
        val primaryColor = colorScheme.primary.toArgb()
        builder
            .blockQuoteColor(primaryColor)
            .linkColor(primaryColor)
    }
}
