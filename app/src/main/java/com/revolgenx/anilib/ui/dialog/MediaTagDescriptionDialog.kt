package com.revolgenx.anilib.ui.dialog

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.media.data.model.MediaTagModel
import com.revolgenx.anilib.databinding.MediaTagDescriptionDialogBinding
import com.revolgenx.anilib.media.viewmodel.MediaTagDescriptionViewModel
import com.revolgenx.anilib.util.naText
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaTagDescriptionDialog : BaseDialogFragment<MediaTagDescriptionDialogBinding>() {

    private val viewModel by viewModel<MediaTagDescriptionViewModel>()

    override var positiveText: Int? = R.string.close

    companion object {
        const val MEDIA_TAG_DIALOG_KEY = "MEDIA_TAG_DIALOG_KEY"
        fun newInstance(model: MediaTagModel) = MediaTagDescriptionDialog().also {
            it.arguments = bundleOf(MEDIA_TAG_DIALOG_KEY to model)
        }
    }

    override fun bindView(): MediaTagDescriptionDialogBinding {
        return MediaTagDescriptionDialogBinding.inflate(provideLayoutInflater)
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        positiveButton?.textSize = 14f

        binding.apply {
            (arguments?.getParcelable(MEDIA_TAG_DIALOG_KEY) as? MediaTagModel)?.let {
                tagNameTv.text = it.name
                tagCategoryTv.text = it.category.naText()
                tagDescriptionTv.text = it.description.naText()
                tagRankTv.text = it.rank?.toString().naText()
            }
        }
    }

    fun show(ctx: Context, func: MediaTagDescriptionDialog.() -> Unit): MediaTagDescriptionDialog {
        func()
        show(ctx)
        return this
    }
}