package com.revolgenx.anilib.dialog

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.support.widget.DynamicButton
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.event.RecommendationFilterEvent
import com.revolgenx.anilib.field.recommendation.RecommendationFilterField
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.dp
import kotlinx.android.synthetic.main.recommendation_filter_dialog.*

//todo handle rotation
class RecommendationFilterDialog : DynamicDialogFragment() {

    private val recommendationFilterField: RecommendationFilterField
            by lazy {
                RecommendationFilterField.field(requireContext())
            }

    companion object {
        fun newInstance() = RecommendationFilterDialog()
    }


    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            setTitle(R.string.recommendation_filter)
            setPositiveButton(R.string.done) { dialogInterface, _ ->
                if(dialogInterface is DynamicDialog ){
                    RecommendationFilterEvent(recommendationFilterField.apply {
                        onList = dialogInterface.onListToggleSwitch.getCheckedPosition() == 1
                        sorting = dialogInterface.recommendationSortToggleSwitch.getCheckedPosition()
                    }).postEvent
                }
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }
            setView(R.layout.recommendation_filter_dialog)
            isAutoDismiss = false
        }

        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }

    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {
        alertDialog.apply {
            setOnShowListener {

                if (context.loggedIn()) {
                    onListToggleSwitch?.let {
                        it.uncheckedBackgroundColor = ThemeController.lightSurfaceColor()
                        it.checkedBackgroundColor = DynamicTheme.getInstance().get().tintAccentColor
                        it.borderRadius = dp(6f).toFloat()
                        it.elevation = dp(1f).toFloat()
                        it.checkedTextColor = DynamicTheme.getInstance().get().accentColor
                        it.uncheckedTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
                        it.reDraw()
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
                    it.uncheckedBackgroundColor = ThemeController.lightSurfaceColor()
                    it.checkedBackgroundColor = DynamicTheme.getInstance().get().tintAccentColor
                    it.borderRadius = dp(6f).toFloat()
                    it.elevation = dp(1f).toFloat()
                    it.checkedTextColor = DynamicTheme.getInstance().get().accentColor
                    it.uncheckedTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
                    it.reDraw()
                    it.setEntries(R.array.recommendation_sort)
                    it.setCheckedPosition(
                        recommendationFilterField.sorting
                    )
                }

                getButton(AlertDialog.BUTTON_POSITIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }
                getButton(AlertDialog.BUTTON_NEGATIVE)?.let {
                    (it as DynamicButton).isAllCaps = false
                }
            }
        }

        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }
}