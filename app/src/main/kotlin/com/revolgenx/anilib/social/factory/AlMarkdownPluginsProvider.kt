package com.revolgenx.anilib.social.factory

import io.noties.markwon.AbstractMarkwonPlugin

interface AlMarkdownPluginsProvider {
    fun getPlugins(): List<AbstractMarkwonPlugin>
}