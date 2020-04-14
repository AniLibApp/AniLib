package com.revolgenx.anilib

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
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
    fun addElement(){
        val docs = Jsoup.parse(youtube)
        docs.select("div.youtube").forEach {element->
            element.appendElement("p").append("Youtube")
        }

        println(docs.body().html())
    }

    @Test
    fun extract() {
        if(video___.contains("video###")){
            println(video___.substring("video###".length))
        }
        if(youtube__.contains("youtube###")){
            println(youtube__.substring("youtube###".length))
        }
    }

    val youtubediv = "<span class='markdown_spoiler'><div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div>" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div</span>"
    @Test
    fun joinspan(){
        println(System.currentTimeMillis())
        youtubediv.contains("markdown_spoiler")
        println(System.currentTimeMillis())
        val docs = Jsoup.parse(youtubediv)
        println(System.currentTimeMillis())
        val element = docs.select("span.markdown_spoiler").firstOrNull()
        element?.select("div.youtube")?.attr("alt", "markdown_spoiler")?.forEach {
            println(it.outerHtml())
        }
        println(System.currentTimeMillis())

    }
}