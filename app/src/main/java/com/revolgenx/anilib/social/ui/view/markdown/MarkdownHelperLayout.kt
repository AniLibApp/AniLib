package com.revolgenx.anilib.social.ui.view.markdown

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import com.pranavpandey.android.dynamic.support.widget.DynamicHorizontalScrollView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.MarkdownHelperLayoutBinding
import com.revolgenx.anilib.ui.dialog.InputDialog
import com.revolgenx.anilib.util.getClipBoardText

class MarkdownHelperLayout:DynamicHorizontalScrollView {
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        contrastWithColorType = Theme.ColorType.NONE
        colorType = Theme.ColorType.BACKGROUND
        val binding = MarkdownHelperLayoutBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        binding.bind()
    }

    private var editText:EditText? = null

    fun setTextEditView(editText:EditText){
        this.editText = editText
    }

    private fun MarkdownHelperLayoutBinding.bind(){
        markdownPasteIv.setOnClickListener {
            editText?.append(context.getClipBoardText())
        }

        markdownBoldIv.setOnClickListener {
            editText?.addMarkdown("____", 2)
        }

        markdownItalicsIv.setOnClickListener {
            editText?.addMarkdown("__", 1)
        }

        markdownStrikeThroughIv.setOnClickListener {
            editText?.addMarkdown("~~~~", 2)
        }

        markdownSpoilerIv.setOnClickListener {
            editText?.addMarkdown("~!!~", 2)
        }

        markdownLinkIv.setOnClickListener {
            showInputDialog(R.string.add_url, hint = R.string.please_input_a_url, "[link]()", 1)
        }
        markdownImageIv.setOnClickListener {
            showInputDialog(R.string.add_image, hint = R.string.please_input_a_url, "img220()", 1)
        }
        markdownYoutubeIv.setOnClickListener {
            showInputDialog(R.string.add_youtube_video, hint = R.string.please_input_a_url, "youtube()", 1)
        }
        markdownVideoIv.setOnClickListener {
            showInputDialog(R.string.add_youtube_video, hint = R.string.please_input_a_url, "webm()", 1)
        }
        markdownOrderedListIv.setOnClickListener {
            editText?.addMarkdown("1. ")
        }
        markdownOrderedListIv.setOnClickListener {
            editText?.addMarkdown("- ")
        }
        markdownHeaderIv.setOnClickListener {
            editText?.addMarkdown("# ")
        }
        markdownCenterIv.setOnClickListener {
            editText?.addMarkdown("~~~~~~", 3)
        }
        markdownQuoteIv.setOnClickListener {
            editText?.addMarkdown(">")
        }
        markdownCodeIv.setOnClickListener {
            editText?.addMarkdown("``", 1)
        }
    }


    private fun showInputDialog(title:Int, hint:Int, markdown:String, selection:Int = 0){
        InputDialog.newInstance(title, hint = hint).apply {
            onInputDoneListener  = {
                if(context != null){
                    editText?.addMarkdown(markdown, selection)
                    editText?.text?.insert(editText?.selectionStart ?: 0, it)
                }
            }
            show(this@MarkdownHelperLayout.context)
        }
    }

    private fun EditText.addMarkdown(markdown: String, selection: Int = 0) {
        append(markdown)
        setSelection(length() - selection)
    }
}