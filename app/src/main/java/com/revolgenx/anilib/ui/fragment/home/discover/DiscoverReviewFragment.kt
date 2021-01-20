package com.revolgenx.anilib.ui.fragment.home.discover

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.isHomeOrderEnabled
import com.revolgenx.anilib.data.model.home.HomeOrderType
import com.revolgenx.anilib.infrastructure.event.BrowseAllReviewsEvent
import com.revolgenx.anilib.ui.presenter.review.ReviewPresenter
import com.revolgenx.anilib.infrastructure.source.home.discover.AllReviewSource
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.ui.viewmodel.home.discover.DiscoverReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverReviewFragment : DiscoverNewFragment() {
    private lateinit var discoverReviewRecyclerView: DynamicRecyclerView
    private val viewModel by viewModel<DiscoverReviewViewModel>()
    private val presenter
        get() = ReviewPresenter(requireContext())

    private val source: AllReviewSource
        get() = viewModel.source ?: viewModel.createSource()


    private var sectionVisibleToUser = false

    override fun onResume() {
        super.onResume()
        if(!sectionVisibleToUser){
            invalidateAdapter()
        }
        sectionVisibleToUser = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        val discoverRecyclerContainer = FrameLayout(requireContext()).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        discoverReviewRecyclerView = DynamicRecyclerView(requireContext()).also {
            it.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(600f)
            ).also {
                it.setMargins(0, dp(10f), 0, 0)
            }
        }

        discoverRecyclerContainer.addView(discoverReviewRecyclerView)


        addView(
            discoverRecyclerContainer,
            getString(R.string.recent_reviews), icon = R.drawable.ic_review, showSetting = false
        ) {
            handleClick(it)
        }

        return v
    }

    private fun handleClick(which: Int) {
        if (which == 0) {
            BrowseAllReviewsEvent().postEvent
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discoverReviewRecyclerView.layoutManager =
            GridLayoutManager(
                this.context,
                if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
            )
    }

    /** call this method to load into recyclerview*/
    private fun invalidateAdapter() {
        discoverReviewRecyclerView.createAdapter(source, presenter)
    }
}