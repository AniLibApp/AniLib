package com.revolgenx.anilib.markwon

import android.content.Context
import android.text.Layout
import android.text.style.AlignmentSpan
import com.revolgenx.anilib.markwon.plugins.*
import com.revolgenx.anilib.markwon.plugins.markwon.ImageMarkwonPlugin
import com.revolgenx.anilib.markwon.plugins.markwon.MarkwonInlineParserPlugin
import com.revolgenx.anilib.markwon.plugins.markwon.YoutubeMarkwonPlugin
import com.revolgenx.anilib.data.model.markwon.MarkdownImageModel
import com.revolgenx.anilib.data.model.markwon.MarkdownModel
import com.revolgenx.anilib.data.model.markwon.MarkdownVideoModel
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParser
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.simple.ext.SimpleExtPlugin
import org.commonmark.node.SoftLineBreak
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.jsoup.Jsoup
import timber.log.Timber


object MarkwonImpl {
    fun createHtmlInstance(context: Context): Markwon {
        return Markwon.builder(context)
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(HtmlPlugin.create())
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(CenterPlugin())
            .usePlugin(ImageTagPlugin())
            .usePlugin(SpoilerPlugin.create())
            .usePlugin(FrescoImagePlugin.create(context))
            .usePlugin(VideoTagPlugin(context))
            .usePlugin(YoutubeTagPlugin(context))
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureVisitor(builder: MarkwonVisitor.Builder) {
                    builder.on(SoftLineBreak::class.java) { visitor, _ ->
                        visitor.forceNewLine()
                    }
                }
            })
            .build()
    }


    fun createInstance(context: Context) = Markwon.builder(context)
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(LinkifyPlugin.create())
        .usePlugin(FrescoImagePlugin.create(context))
        .usePlugin(ImageMarkwonPlugin(context))
        .usePlugin(YoutubeMarkwonPlugin(context))
        .usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureParser(builder: Parser.Builder) {
                builder.inlineParserFactory(MarkwonInlineParser.factoryBuilder().build())
            }
        })
        .usePlugin(SimpleExtPlugin.create { plugin ->
            plugin.addExtension(
                3, '~'
            ) { _, _ -> AlignmentSpan { Layout.Alignment.ALIGN_CENTER } }
        })
        .usePlugin(HtmlPlugin.create())
        .usePlugin(CenterPlugin())
        .usePlugin(ImageTagPlugin())
        .usePlugin(SpoilerPlugin.create())
        .usePlugin(VideoTagPlugin(context))
        .usePlugin(YoutubeTagPlugin(context))
        .build()

    const val BR = "<br>"

    lateinit var parserInstance: Parser
    lateinit var rendererInstance: HtmlRenderer

    fun createMarkwonCompatible(html1: String): MarkdownModel {
        var html2 = html1.replace("\n",BR)
        val docs = Jsoup.parse(html2)
        val videos = mutableListOf<MarkdownVideoModel>()
        val images = mutableListOf<MarkdownImageModel>()

        docs.select("div.youtube").forEach { element ->
            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
            element.appendText("youtube")
            if (containsSpoiler)
                element.attr("alt", "markdown_spoiler")
        }

        docs.select("video").forEachIndexed { index, element ->
            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
            element.appendText("video")
            if (containsSpoiler)
                element.attr("alt", "markdown_spoiler")
        }


        docs.select("img").forEachIndexed { index, element ->
            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
            if (containsSpoiler)
                element.attr("alt", "markdown_spoiler")
        }


//        docs.select("div.youtube").forEachIndexed { index, element ->
//            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
//            if (index < 8) {
//                if (containsSpoiler)
//                    element.attr("alt", "markdown_spoiler")
//            } else {
//                videos.add(MarkdownVideoModel().also {
//                    it.containsSpoiler = containsSpoiler
//                    it.url = element.attr("id")
//                    element.remove()
//                })
//            }
//        }
//
//        docs.select("video").forEachIndexed { index, element ->
//            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
//            if (index < 8) {
//                if (containsSpoiler)
//                    element.attr("alt", "markdown_spoiler")
//            } else {
//                videos.add(MarkdownVideoModel().also {
//                    it.url = element.select("source[src]").firstOrNull()?.attr("src")
//                    it.videoType = 1
//                    it.containsSpoiler = containsSpoiler
//                    element.remove()
//                })
//            }
//        }
//
//        docs.select("img").forEachIndexed { index, element ->
//            val imageSrc = element.attr("src")
//            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
//            if (index <= 20) {
//                if (containsSpoiler)
//                    element.attr("alt", "markdown_spoiler")
//            } else {
//                images.add(MarkdownImageModel().also {
//                    it.url = imageSrc
//                    it.containsSpoiler = containsSpoiler
//                    element.remove()
//                })
//            }
//        }

        return MarkdownModel().also {
            it.html = "<span></span>" +
                docs.body().html()
                    .replace("\n", "")
                    .replace("<br>", "\n")
//                .replace("</center>", "</center><br>")
//                .replace("<center>", "\n<center>")
//                .replace("<br>", "<span></span>\n\n<span></span>")
            it.images = images
            it.videos = videos
            Timber.d(it.html)
        }
    }

    fun createMarkwonEditor(context: Context) = MarkwonEditor.create(createInstance(context))
}

