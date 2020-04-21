package com.revolgenx.anilib.markwon.plugins
//
//import android.content.Context
//import android.text.Spanned
//import android.widget.TextView
//import com.revolgenx.anilib.markwon.MarkwonImpl
//import io.noties.markwon.AbstractMarkwonPlugin
//import io.noties.markwon.MarkwonVisitor
//import org.commonmark.node.Text
//
//class TextVisitorPlugin(private val context: Context): AbstractMarkwonPlugin() {
//    override fun configureVisitor(builder: MarkwonVisitor.Builder) {
//        builder.on(Text::class.java) { markwonVisitor: MarkwonVisitor, text: Text ->
//        }
//        super.configureVisitor(builder)
//    }
//
//    override fun beforeSetText(textView: TextView, markdown: Spanned) {
//        MarkwonImpl.createHtmlInstance(context).
//        super.beforeSetText(textView, markdown)
//    }
//}