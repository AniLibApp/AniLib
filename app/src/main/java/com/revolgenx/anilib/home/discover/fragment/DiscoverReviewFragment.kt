package com.revolgenx.anilib.home.discover.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.OpenAllReviewEvent
import com.revolgenx.anilib.review.presenter.ReviewPresenter
import com.revolgenx.anilib.infrastructure.source.home.discover.AllReviewSource
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.home.discover.viewmodel.DiscoverReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverReviewFragment : DiscoverNewFragment() {
    private lateinit var discoverReviewRecyclerView: RecyclerView
    private val viewModel by viewModel<DiscoverReviewViewModel>()
    private val presenter
        get() = ReviewPresenter(requireContext())

    private val source: AllReviewSource
        get() = viewModel.source ?: viewModel.createSource()


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
        discoverReviewRecyclerView = RecyclerView(requireContext()).also {
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
            OpenAllReviewEvent().postEvent
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        invalidateAdapter()
    }

    /** call this method to load into recyclerview*/
    private fun invalidateAdapter() {
        discoverReviewRecyclerView.createAdapter(source, presenter)
    }
}