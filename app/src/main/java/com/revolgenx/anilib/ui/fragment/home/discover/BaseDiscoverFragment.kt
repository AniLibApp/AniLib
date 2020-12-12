package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
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
import com.revolgenx.anilib.ui.dialog.MediaFilterDialog
import com.revolgenx.anilib.ui.dialog.DiscoverMediaListFilterDialog
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.databinding.DiscoverFragmentLayoutBinding
import com.revolgenx.anilib.util.dp

abstract class BaseDiscoverFragment : BaseLayoutFragment<DiscoverFragmentLayoutBinding>(),
    BaseDiscoverHelper {

    protected val discoverLayout: ViewGroup get() = binding.discoverLinearLayout
    protected lateinit var garlandLayout: View

    protected val loadingPresenter: Presenter<Unit>
        get() {
            return Presenter.forLoadingIndicator(
                requireContext(), R.layout.loading_layout
            )
        }

    protected val shimmerLoader: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(requireContext(), R.layout.discover_shimmer_layout)


    protected val errorPresenter: Presenter<Unit>
        get() {
            return Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
        }

    protected val emptyPresenter: Presenter<Unit>
        get() {
            return Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
        }

    companion object {
        const val MEDIA_TRENDING_TAG = "MEDIA_TRENDING_TAG"
        const val MEDIA_POPULAR_TAG = "MEDIA_POPULAR_TAG"
        const val NEWLY_ADDED_TAG = "NEWLY_ADDED_TAG"
        const val MEDIA_LIST_WATCHING_TAG = "MEDIA_LIST_WATCHING_TAG"
        const val MEDIA_LIST_READING_TAG = "MEDIA_LIST_READING_TAG"
    }

    protected val orderedViewList = mutableListOf<OrderedViewModel>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): DiscoverFragmentLayoutBinding {
        return DiscoverFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        orderedViewList.sortBy { it.order }
        orderedViewList.forEach {
            addView(it.oView, it.title, it.showSetting, it.icon, onClick = it.onClick)
        }
        orderedViewList.clear()
        return v
    }


    override fun addView(
        discoverChildView: View,
        title: String,
        showSetting: Boolean,
        @DrawableRes icon: Int?,
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
            it.typeface = ResourcesCompat.getFont(requireContext(), R.font.cabin_semi_bold)
            it.setPadding(dp(4f), dp(10f), dp(4f), dp(10f))
            it.setCompoundDrawablesRelativeWithIntrinsicBounds(
                icon?.let { ContextCompat.getDrawable(requireContext(), it) },
                null,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_angle_right),
                null
            )
            it.compoundDrawablePadding = dp(6f)
            it.gravity = Gravity.CENTER_VERTICAL
        }

        val garlandSettingIv = DynamicImageView(requireContext()).also {
            it.id = R.id.garlandSettingIv
            it.layoutParams = ViewGroup.LayoutParams(0, 0)
            it.setImageResource(R.drawable.ic_filter)
            it.colorType = Theme.ColorType.TEXT_PRIMARY
            it.setPadding(dp(10f))
            it.visibility = View.GONE
            it.setBackgroundResource(R.drawable.ads_button_remote)
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
        garlandContainer.addView(discoverChildView)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)


        constraintSet.connect(
            garlandTextView.id,
            ConstraintSet.START,
            constraintLayout.id,
            ConstraintSet.START,
            dp(10f)
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
            garlandSettingIv.id,
            ConstraintSet.START,
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


        constraintSet.setHorizontalBias(garlandTextView.id, 0f)
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


    protected fun showMediaListFilterDialog(type: Int, tag: String, callback: (() -> Unit)) {
        DiscoverMediaListFilterDialog.newInstance(type).also {
            it.onDoneListener = callback
            it.show(childFragmentManager, tag)
        }
    }

    protected fun RecyclerView.createAdapter(
        source: Source<*>,
        presenter: Presenter<*>,
        useShimmerLoader: Boolean = false,
        loader: Presenter<*>? = null
    ): Adapter {
        return Adapter.builder(viewLifecycleOwner, 10)
            .setPager(PageSizePager(10))
            .addSource(source)
            .addPresenter(presenter)
            .addPresenter(loader ?: if (useShimmerLoader) shimmerLoader else loadingPresenter)
            .addPresenter(errorPresenter)
            .addPresenter(emptyPresenter)
            .into(this)
    }

}