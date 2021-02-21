package com.revolgenx.anilib.ui.fragment.home.discover

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.PageSizePager
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.dialog.DiscoverMediaListFilterDialog
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.model.home.OrderedViewModel
import com.revolgenx.anilib.databinding.DiscoverFragmentLayoutBinding
import com.revolgenx.anilib.ui.bottomsheet.discover.MediaFilterBottomSheetFragment
import com.revolgenx.anilib.ui.view.widgets.DynamicDrawableTextView
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

        val garlandLinearLayout = DynamicLinearLayout(requireContext()).also {
            it.id = R.id.garlandLinearLayout
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.orientation = LinearLayout.VERTICAL
        }


        val topGarlandLayout = ConstraintLayout(requireContext()).also {
            it.id = R.id.garlandConstraintLayout
            it.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(52f)
            ).also { params->
                params.topMargin = dp(6f)
                params.bottomMargin = dp(6f)
            }
        }

        val garlandTextView = DynamicDrawableTextView(requireContext()).also {
            it.id = R.id.garlandTitleTv
            it.layoutParams =
                ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            it.textSize = 16f
            it.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            it.setDrawables(
                icon,
                R.drawable.ic_angle_right
            )
            it.compoundDrawablePadding = dp(6f)
            it.gravity = Gravity.CENTER_VERTICAL
        }


        val garlandSettingIv = DynamicImageView(requireContext()).also {
            it.id = R.id.garlandSettingIv
            it.layoutParams = ViewGroup.LayoutParams(dp(48f), dp(48f))
            it.setImageResource(R.drawable.ic_filter)
            it.setPadding(dp(12f))
            it.colorType = Theme.ColorType.TEXT_PRIMARY
            it.visibility = View.GONE
            it.setBackgroundResource(R.drawable.ads_button_remote)
        }

        topGarlandLayout.addView(garlandTextView)
        topGarlandLayout.addView(garlandSettingIv)


        val topGarlandConstraintSet = ConstraintSet()
        topGarlandConstraintSet.clone(topGarlandLayout)


        topGarlandConstraintSet.connect(
            garlandTextView.id,
            ConstraintSet.START,
            topGarlandLayout.id,
            ConstraintSet.START,
            dp(10f)
        )

        topGarlandConstraintSet.connect(
            garlandTextView.id,
            ConstraintSet.TOP,
            topGarlandLayout.id,
            ConstraintSet.TOP,
            0
        )
        topGarlandConstraintSet.connect(
            garlandTextView.id,
            ConstraintSet.BOTTOM,
            topGarlandLayout.id,
            ConstraintSet.BOTTOM,
            0
        )

        topGarlandConstraintSet.connect(
            garlandTextView.id,
            ConstraintSet.END,
            garlandSettingIv.id,
            ConstraintSet.START,
            0
        )

        topGarlandConstraintSet.connect(
            garlandSettingIv.id,
            ConstraintSet.END,
            topGarlandLayout.id,
            ConstraintSet.END,
            0
        )
        topGarlandConstraintSet.connect(
            garlandSettingIv.id,
            ConstraintSet.BOTTOM,
            topGarlandLayout.id,
            ConstraintSet.BOTTOM,
            0
        )
        topGarlandConstraintSet.connect(
            garlandSettingIv.id,
            ConstraintSet.BOTTOM,
            garlandTextView.id,
            ConstraintSet.BOTTOM,
            0
        )
        topGarlandConstraintSet.connect(
            garlandSettingIv.id,
            ConstraintSet.TOP,
            garlandTextView.id,
            ConstraintSet.TOP,
            0
        )


        topGarlandConstraintSet.setHorizontalBias(garlandTextView.id, 0f)
        topGarlandConstraintSet.applyTo(topGarlandLayout)


        garlandLinearLayout.addView(topGarlandLayout)
        garlandLinearLayout.addView(discoverChildView)


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

        discoverLayout.addView(garlandLinearLayout)
    }


    protected fun showMediaFilterDialog(type: Int, callback: (() -> Unit)) {
        MediaFilterBottomSheetFragment().show(requireContext()){
            arguments = bundleOf(MediaFilterBottomSheetFragment.mediaFilterTypeKey to type)
            onDoneListener = callback
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