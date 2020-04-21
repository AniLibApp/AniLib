package com.revolgenx.anilib.model.markwon

class MarkdownVideoModel {
    var url: String? = null
    var containsSpoiler = false
    var videoType: Int = 0
    override fun toString(): String {
        return "MarkdownVideoModel(url=$url, containsSpoiler=$containsSpoiler, videoType=$videoType)"
    }
}
