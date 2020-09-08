package com.revolgenx.anilib.dialog

import android.os.Bundle
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.markwon.MarkwonImpl
import kotlinx.android.synthetic.main.release_info_dialog_layout.*

class ReleaseInfoDialog : BaseDialogFragment() {
    companion object {
        val tag = ReleaseInfoDialog::class.java.name
    }

    override var viewRes: Int? = R.layout.release_info_dialog_layout
    override var positiveText: Int? = R.string.close

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        with(alertDialog) {
            MarkwonImpl.createInstance(requireContext())
                .setMarkdown(this.releaseInfo, requireContext().getString(R.string.release_info))

            MarkwonImpl.createHtmlInstance(requireContext())
                .setMarkdown(this.studioNoteTv, requireContext().getString(R.string.studio_note))
        }
    }

}