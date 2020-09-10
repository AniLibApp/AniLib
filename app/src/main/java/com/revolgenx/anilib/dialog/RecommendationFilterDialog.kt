package com.revolgenx.anilib.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicButton
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.controller.themeIt
import com.revolgenx.anilib.event.RecommendationFilterEvent
import com.revolgenx.anilib.field.recommendation.RecommendationFilterField
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.dp
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