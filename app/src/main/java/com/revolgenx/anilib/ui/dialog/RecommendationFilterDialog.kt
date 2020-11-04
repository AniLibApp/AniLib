package com.revolgenx.anilib.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.app.theme.themeIt
import com.revolgenx.anilib.infrastructure.event.RecommendationFilterEvent
import com.revolgenx.anilib.data.field.recommendation.RecommendationFilterField
import com.revolgenx.anilib.common.preference.loggedIn
import kotlinx.android.synthetic.main.recommendation_filter_dialog.*

//todo handle rotation
class RecommendationFilterDialog : BaseDialogFragment() {

    override var titleRes: Int? = R.string.recommendation_filter
    override var viewRes: Int? = R.layout.recommendation_filter_dialog
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

    private val recommendationFilterField: RecommendationFilterField
            by lazy {
                RecommendationFilterField.field(requireContext())
            }

    companion object {
        fun newInstance() = RecommendationFilterDialog()
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        if (dialogInterface is DynamicDialog) {
            RecommendationFilterEvent(recommendationFilterField.apply {
                onList = dialogInterface.onListToggleSwitch.getCheckedPosition() == 1
                sorting = dialogInterface.recommendationSortToggleSwitch.getCheckedPosition()
            }).postEvent
        }
    }

    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        with(alertDialog) {
            if (context.loggedIn()) {
                onListToggleSwitch?.let {
                    it.themeIt()
                    it.setEntries(R.array.on_list)
                    it.setCheckedPosition(
                        if (recommendationFilterField.onList)
                            1
                        else 0
                    )
                }
            } else {
                onListToggleSwitch?.visibility = View.GONE
            }

            recommendationSortToggleSwitch?.let {
                it.themeIt()
                it.setEntries(R.array.recommendation_sort)
                it.setCheckedPosition(
                    recommendationFilterField.sorting
                )
            }
            getButton(AlertDialog.BUTTON_POSITIVE)?.isAllCaps = false
            getButton(AlertDialog.BUTTON_NEGATIVE)?.isAllCaps = false
        }
    }
}