package com.revolgenx.anilib.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.PageSizePager
import com.pranavpandey.android.dynamic.support.widget.DynamicFrameLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.dialog.MediaFilterDialog
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.util.dp
import kotlinx.android.synthetic.main.discover_fragment_layout.view.*

abstract class BaseDiscoverFragment : BaseFragment(), BaseDiscoverHelper {

    protected lateinit var discoverLayout: ViewGroup
    protected lateinit var garlandLayout: View

    protected val loadingPresenter: Presenter<Void>
        get() {
            return Presenter.forLoadingIndicator(
                requireContext(), R.layout.loading_layout
            )
        }

    protected val errorPresenter: Presenter<Void>
        get() {
            return Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
        }

    protected val emptyPresenter: Presenter<Void>
        get() {
            return Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.discover_fragment_layout, container, false)
        discoverLayout = v.discoverLinearLayout
        return v
    }

    override fun addView(
        view: View,
        title: String,
        showSetting: Boolean,
        onClick: ((which: Int) -> Unit)?
    ) {
        val constraintLayout = ConstraintLayout(requireContext()).also {
            it.id = R.id.garlandConstraintLayout
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val garlandTextView = DynamicTextView(requireContext()).also {
            it.id = R.id.garlandTitleTv
            it.layoutParams =
                ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            it.textSize = 16f
            it.typeface = ResourcesCompat.getFont(requireContext(), R.font.berlinrounded_extra_bold)
            it.setPadding(dp(10f))
        }

        val garlandSettingIv = DynamicImageView(requireContext()).also {
            it.id = R.id.garlandSettingIv
            it.layoutParams = ViewGroup.LayoutParams(0, 0)
            it.setImageResource(R.drawable.ic_button_setting)
            it.colorType = Theme.ColorType.TINT_SURFACE
            it.setPadding(dp(8f))
            it.visibility = View.GONE
        }

        val garlandContainer = DynamicFrameLayout(requireContext()).also {
            it.id = R.id.gralandFrameContainer
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        garlandTextView.text = title
        if (showSetting) {
            garlandSettingIv.visibility = View.VISIBLE
            garlandSettingIv.setOnClickListener {
                onClick?.invoke(1)
            }
        }

        garlandTextView.setOnClickListener {
            onClick?.invoke(0)
        }

        constraintLayout.addView(garlandTextView)
        constraintLayout.addView(garlandSettingIv)
        constraintLayout.addView(garlandContainer)
        garlandContainer.addView(view)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            garlandTextView.id,
            ConstraintSet.START,
            constraintLayout.id,
            ConstraintSet.START,
            0
        )
        constraintSet.connect(
            garlandTextView.id,
            ConstraintSet.TOP,
            constraintLayout.id,
            ConstraintSet.TOP,
            0
        )
        constraintSet.connect(
            garlandTextView.id,
            ConstraintSet.END,
            constraintLayout.id,
            ConstraintSet.END,
            0
        )
        constraintSet.connect(
            garlandSettingIv.id,
            ConstraintSet.END,
            constraintLayout.id,
            ConstraintSet.END,
            0
        )
        constraintSet.connect(
            garlandSettingIv.id,
            ConstraintSet.BOTTOM,
            garlandTextView.id,
            ConstraintSet.BOTTOM,
            0
        )
        constraintSet.connect(
            garlandSettingIv.id,
            ConstraintSet.TOP,
            garlandTextView.id,
            ConstraintSet.TOP,
            0
        )

        constraintSet.connect(
            garlandContainer.id,
            ConstraintSet.START,
            constraintLayout.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            garlandContainer.id,
            ConstraintSet.TOP,
            garlandTextView.id,
            ConstraintSet.BOTTOM
        )

//        constraintSet.setMargin(garlandTextView.id, ConstraintSet.TOP, dp(10f))
        constraintSet.setDimensionRatio(garlandSettingIv.id, "1:1")
        constraintSet.applyTo(constraintLayout)

        discoverLayout.addView(constraintLayout)
    }


    protected fun showMediaFilterDialog(type: Int, tag: String, callback: (() -> Unit)) {
        MediaFilterDialog.newInstance(type).also {
            it.onDoneListener = callback
            it.show(childFragmentManager, tag)
        }
    }

    protected fun RecyclerView.createAdapter(
        source: Any,
        presenter: Any
    ): Adapter {
        return Adapter.builder(viewLifecycleOwner, 10)
            .setPager(PageSizePager(10))
            .addSource(source as Source<*>)
            .addPresenter(presenter as Presenter<*>)
            .addPresenter(loadingPresenter)
            .addPresenter(errorPresenter)
            .addPresenter(emptyPresenter)
            .into(this)
    }

}