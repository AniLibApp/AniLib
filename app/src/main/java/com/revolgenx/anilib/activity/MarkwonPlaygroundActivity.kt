package com.revolgenx.anilib.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.databinding.MarkwonPlaygroundActivityLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.markwon.AlStringUtil.anilify

class MarkwonPlaygroundActivity:BaseDynamicActivity<MarkwonPlaygroundActivityLayoutBinding>() {

    companion object{
        const val SPANNED_DATA_KEY = "SPANNED_DATA_KEY"
    }

    private var mdText = ""
    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MarkwonPlaygroundActivityLayoutBinding {
        return MarkwonPlaygroundActivityLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mdText = intent.getStringExtra(SPANNED_DATA_KEY)?: ""

        binding.spannedEditorMd.setText(mdText)
        AlMarkwonFactory.getMarkwon().setMarkdown(binding.spannedMd, anilify(mdText))
        binding.markwonProceed.setOnClickListener {
            AlMarkwonFactory.getMarkwon().setMarkdown(binding.spannedMd, anilify(binding.spannedEditorMd.text.toString()))
        }



    }


}