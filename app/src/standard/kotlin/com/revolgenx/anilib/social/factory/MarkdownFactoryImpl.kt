package com.revolgenx.anilib.social.factory

import android.content.Context
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import java.lang.reflect.Method

object MarkdownFactoryImpl : MarkdownFactory {
    private var _markwon: Markwon? = null
    val markwon: Markwon get() = _markwon!!

    private var alMarkdownClass: Class<*>? = null
    private var alMarkdownCreateMethod: Method? = null
    private var anilifyMethod: Method? = null

    private fun createClass(): Class<*> {
        return Class.forName("com.revolgenx.anilib.social.markdown.AlMarkdown")
    }

    private fun createMarkdown(vararg params: Any): Any? {
        return try {
            alMarkdownClass =
                alMarkdownClass ?: createClass()
            alMarkdownCreateMethod = alMarkdownCreateMethod ?: alMarkdownClass!!.getMethod(
                "create",
                Context::class.java,
                Int::class.java,
                Boolean::class.java,
                MarkdownCallback::class.java
            )
            val objectInstance = alMarkdownClass!!.getField("INSTANCE")
                .get(null)
            return alMarkdownCreateMethod!!.invoke(objectInstance, *params)
        } catch (e: Exception) {
            Markwon.builder(params[0] as Context)
                .usePlugins(
                    listOf(
                        HtmlPlugin.create(),
                        StrikethroughPlugin.create(),
                        LinkifyPlugin.create()
                    )
                ).build()
        }
    }

    override fun initialize(
        context: Context,
        primaryColor: Int,
        autoPlayGif: Boolean,
        markdownCallback: MarkdownCallback
    ): Markwon {
        _markwon = _markwon ?: createMarkdown(
            context,
            primaryColor,
            autoPlayGif,
            markdownCallback
        ) as Markwon
        return _markwon!!
    }

    override fun anilify(text: String?): String {
        return try {
            alMarkdownClass = alMarkdownClass ?: createClass()
            anilifyMethod =
                anilifyMethod ?: alMarkdownClass!!.getMethod("anilify", String::class.java)
            val objectInstance = alMarkdownClass!!.getField("INSTANCE")
                .get(null)
            anilifyMethod!!.invoke(objectInstance, text) as String
        } catch (e: Exception) {
            return text ?: ""
        }

    }

    override fun destroy() {
        _markwon = null
    }
}