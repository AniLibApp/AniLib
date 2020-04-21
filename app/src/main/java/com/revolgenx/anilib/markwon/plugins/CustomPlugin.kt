package com.revolgenx.anilib.markwon.plugins

import io.noties.markwon.AbstractMarkwonPlugin

abstract class CustomPlugin : AbstractMarkwonPlugin() {
    companion object{
        const val SPAN = "span"
        const val MARKDOWN_SPOILER = "markdown_spoiler"
        const val CLASS = "class"
        const val VIDEO = "video"
        const val DIV = "div"
        const val ID = "id"
        const val SRC = "src"
        const val WIDTH = "width"
        const val IMG = "img"
        const val ALT = "alt"
        const val CENTER = "center"
    }
}
