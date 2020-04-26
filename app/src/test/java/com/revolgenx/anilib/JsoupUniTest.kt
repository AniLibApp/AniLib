package com.revolgenx.anilib

import com.revolgenx.anilib.model.markwon.MarkdownImageModel
import com.revolgenx.anilib.model.markwon.MarkdownVideoModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Entities
import org.jsoup.nodes.TextNode
import org.junit.Test

class JsoupUniTest {

    val html =
        "<img width='1080'  src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286'>"
    val youtube = "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div>"
    val video =
        "<video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video>"

    val videoImge = "http://img.youtube.com/vi/<insert-youtube-video-id-here>/hqdefault.jpg"

    val complete =
        "<img width='1080'  src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286'>\"\n" +
                "    <div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div>" +
                "    <video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video>"

    /*            it.attr("width", "100%")
*/

    val youtube__ = "youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU"
    val video___ = "video###https://files.catbox.moe/0zofnv.mp4"
    @Test
    fun parse() {
        val docs = Jsoup.parse(youtube)
        val element = docs.select("div.youtube").first()
        val img = Element("img").attr("src", "youtube###${element.attr("id")}")
        element.replaceWith(img)
        println(docs.html())
    }

    @Test
    fun video() {
        val docs = Jsoup.parse(video)
        val imgElement = Element("img")
        docs.select("video").forEach { element ->
            element.replaceWith(
                imgElement.attr(
                    "src",
                    "video###${element.select("source[src]").first().attr("src")}"
                )
            )
        }
        println(docs.body().html())
    }

    @Test
    fun width() {
        val docs = Jsoup.parse(html)
        docs.select("img").forEach {
            it.attr("width", "100%")
        }
        println(docs.html())
    }

    @Test
    fun complete() {
        val docs = Jsoup.parse(complete)

        docs.select("img").forEach {
            it.attr("width", "100%")
        }

        docs.select("div.youtube").forEach { element ->
            element.replaceWith(
                Element("img").attr("width", "100%").attr("src", "youtube###${element.attr("id")}")
            )
        }

        docs.select("video").forEach { element ->
            element.replaceWith(
                Element("img").attr("width", "100%").attr(
                    "src",
                    "video###${element.select("source[src]").first().attr("src")}"
                )
            )
        }

        println(docs.body().html())
    }


    @Test
    fun addElement() {
        val docs = Jsoup.parse(youtube)
        docs.select("div.youtube").forEach { element ->
            element.appendElement("p").append("Youtube")
        }

        println(docs.body().html())
    }

    @Test
    fun extract() {
        if (video___.contains("video###")) {
            println(video___.substring("video###".length))
        }
        if (youtube__.contains("youtube###")) {
            println(youtube__.substring("youtube###".length))
        }
    }

    val youtubediv = "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'>\n\n<p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>" +
            "<span class='markdown_spoiler'><span><video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video></div></span></span>"

    val videodiv =
        "<span class='markdown_spoiler'><video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video></div>" +
                "<span class='markdown_spoiler'><video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video></div>" +
                "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>"

    val newline = "<p>ehello \n wor</p><p>world</p>"

    @Test
    fun joinspan() {
        val replace = newline.replace("\n", "<br>")
        val docs = Jsoup.parse(replace)
        docs.select("span.markdown_spoiler").forEach {
            it.select("div.youtube").attr("alt", "markdown_spoiler")
            it.select("video").attr("alt", "markdown_spoiler")
        }

        println(docs.body().html())
    }


    val youtubediv1 =
        "<span class='markdown_spoiler'><span><div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>"
    val image =
        "<span class='markdown_spoiler'><span><img  src='https://www.youtube.com/watch?v=XoyLbuX8EXU'></div></span></span>"

    @Test
    fun imageTest() {
        val docs = Jsoup.parse(youtubediv)
        val videos = mutableListOf<MarkdownVideoModel>()
        val images = mutableListOf<MarkdownImageModel>()
        docs.select("div.youtube").forEachIndexed { index, element ->
            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
            if (index < 8) {
                if (containsSpoiler)
                    element.attr("alt", "markdown_spoiler")
            } else {
                videos.add(MarkdownVideoModel().also {
                    it.containsSpoiler = containsSpoiler
                    it.url = element.attr("id")
                    element.remove()
                })
            }
        }

        docs.select("video").forEachIndexed { index, element ->
            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
            if (index < 8) {
                if (containsSpoiler)
                    element.attr("alt", "markdown_spoiler")
            } else {
                videos.add(MarkdownVideoModel().also {
                    it.url = element.select("source[src]").firstOrNull()?.attr("src")
                    it.videoType = 1
                    it.containsSpoiler = containsSpoiler
                    element.remove()
                })
            }
        }

        docs.select("img").forEachIndexed { index, element ->
            val imageSrc = element.attr("src")
            val containsSpoiler = element.parents().hasClass("markdown_spoiler")
            if (index <= 8) {
                if (containsSpoiler)
                    element.attr("alt", "markdown_spoiler")
            } else {
                images.add(MarkdownImageModel().also {
                    it.url = imageSrc
                    it.containsSpoiler = containsSpoiler
                    element.remove()
                })
            }
        }

        println(docs.body().html())
        videos.forEach { println(it.toString()) }
        images.forEach { println(it.toString()) }
    }


    @Test
    fun insertElement() {
        val t = "<video><source></video>"
        val docs = Jsoup.parse(t)
        docs.select("video").forEach {
            //            it.text("video")
            it.appendText("video")
        }
        println(docs.body().html().replace("\n", ""))
    }

    @Test
    fun newLineCenter() {
        val t = "<center>hello \n hello</center><span>hello</span>"
        println(t.replace("\n", "<br>"))

        Jsoup.parse(t)?.let {
            it.outputSettings().prettyPrint(false)
            it.select("center").forEach {
                println(it.html(it.html().replace("<br>", "</center><center>")))
            }

            println(it.body().html())
        }
    }
}