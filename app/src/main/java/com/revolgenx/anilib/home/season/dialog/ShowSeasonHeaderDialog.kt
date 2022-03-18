package com.revolgenx.anilib.home.season.dialog

import android.content.DialogInterface
import android.os.Bundle
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.showSeasonHeader
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.databinding.ShowSeasonHeaderDialogLayoutBinding

class ShowSeasonHeaderDialog: BaseDialogFragment<ShowSeasonHeaderDialogLayoutBinding>() {

    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel
    override var titleRes: Int? = R.string.show_headers

    var onDoneListener: ((isChecked:Boolean, isChanged:Boolean)->Unit)? = null

    override fun bindView(): ShowSeasonHeaderDialogLayoutBinding {
        return ShowSeasonHeaderDialogLayoutBinding.inflate(provideLayoutInflater, null, false)
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        binding.showSeasonHeaderSwitch.isChecked = showSeasonHeader()
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        super.onPositiveClicked(dialogInterface, which)
        val showSeasonHeader = showSeasonHeader()
        val isChecked = binding.showSeasonHeaderSwitch.isChecked
        val isChanged = showSeasonHeader != isChecked

        showSeasonHeader(isChecked)
        onDoneListener?.invoke(isChecked, isChanged)
    }
}