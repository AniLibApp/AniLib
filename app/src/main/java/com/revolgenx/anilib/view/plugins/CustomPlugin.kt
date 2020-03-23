package com.revolgenx.anilib.view.plugins

import io.noties.markwon.AbstractMarkwonPlugin

abstract class CustomPlugin : AbstractMarkwonPlugin() {
    companion object{
        const val SPAN = "span"
        const val MARKDOWN_SPOILER = "markdown_spoiler"
        const val CLASS = "class"
    }
}
