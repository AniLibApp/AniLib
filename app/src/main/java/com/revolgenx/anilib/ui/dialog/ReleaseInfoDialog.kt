package com.revolgenx.anilib.ui.dialog

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.ReleaseInfoDialogLayoutBinding
import com.revolgenx.anilib.markwon.MarkwonImpl

class ReleaseInfoDialog : BaseDialogFragment() {
    companion object {
        val tag = ReleaseInfoDialog::class.java.name
    }

    override var viewRes: Int? = R.layout.release_info_dialog_layout
    override var positiveText: Int? = R.string.close

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        val binding = ReleaseInfoDialogLayoutBinding.bind(dialogView)
        MarkwonImpl.createInstance(requireContext())
            .setMarkdown(binding.releaseInfo, requireContext().getString(R.string.release_info))

        MarkwonImpl.createHtmlInstance(requireContext())
            .setMarkdown(binding.studioNoteTv, requireContext().getString(R.string.studio_note))
    }

}