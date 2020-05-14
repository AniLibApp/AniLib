package com.revolgenx.anilib.view.util

import android.view.View
import android.widget.EditText
import com.revolgenx.anilib.util.getClipBoardText
import kotlinx.android.synthetic.main.markwon_helper_layout.view.*


fun attachHelperToView(view: View, editText: EditText) {
    view.apply {
        pasteIv.setOnClickListener {
            editText.addMarkdown(context.getClipBoardText(), 0)
        }
        boldIv.setOnClickListener {
            editText.addMarkdown("____", 2)
        }
        italicsIv.setOnClickListener {
            editText.addMarkdown("__", 1)
        }
        spoilerIv.setOnClickListener {
            editText.addMarkdown("~!!~", 2)
        }
        linkIv.setOnClickListener {
            editText.addMarkdown("[link]()", 1)
        }
        imageIv.setOnClickListener {
            editText.addMarkdown("img()", 1)
        }
        youtubeIv.setOnClickListener {
            editText.addMarkdown("youtube()", 1)
        }
        videoIv.setOnClickListener {
            editText.addMarkdown("webm()", 1)
        }
    }

}

private fun EditText.addMarkdown(markdown: String, selection: Int) {
    append(markdown)
    setSelection(length() - selection)
}