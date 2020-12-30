package com.revolgenx.anilib.ui.view.util

import android.widget.EditText
import com.revolgenx.anilib.databinding.MarkwonHelperLayoutBinding
import com.revolgenx.anilib.util.getClipBoardText


fun attachHelperToView(binding: MarkwonHelperLayoutBinding, editText: EditText) {
    binding.apply {
        pasteIv.setOnClickListener {
            editText.addMarkdown(binding.root.context.getClipBoardText(), 0)
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