package com.revolgenx.anilib.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.dialog.MediaFilterDialog
import com.revolgenx.anilib.field.home.PopularMediaField
import com.revolgenx.anilib.presenter.home.MediaPresenter
import com.revolgenx.anilib.source.MediaSource
import com.revolgenx.anilib.type.MediaSort
import com.revolgenx.anilib.viewmodel.PopularViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class DiscoverPopularFragment : DiscoverTrendingFragment() {

    private lateinit var popularRecyclerView: DynamicRecyclerView

    private val presenter
        get() = MediaPresenter(requireContext())

    private val source: MediaSource
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<PopularViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        popularRecyclerView = DynamicRecyclerView(requireContext()).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.isNestedScrollingEnabled = false
        }

        addView(
            popularRecyclerView,
            " >>> " + getString(R.string.popular) + " <<<", showSetting = true
        ) {
            handleClick(it)
        }

        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            viewModel.field = PopularMediaField.create(requireContext()).also {
                it.sort = MediaSort.POPULARITY_DESC.ordinal
            }
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            childFragmentManager.findFragmentByTag(MEDIA_POPULAR_TAG)?.let {
                (it as MediaFilterDialog).onDoneListener = {
                    renewAdapter()
                }
            }
        }
        invalidateAdapter()
    }



    private fun handleClick(which: Int) {
        if (which == 0) {

        } else if (which == 1) {
            showMediaFilterDialog(
                MediaFilterDialog.MediaFilterType.POPULAR.ordinal,
                MEDIA_POPULAR_TAG
            ) {
                renewAdapter()
            }
        }
    }


    private fun renewAdapter() {
        viewModel.updateField(requireContext())
        viewModel.createSource()
        invalidateAdapter()
    }


    /** call this method to load into recyclerview*/
    private fun invalidateAdapter() {
        viewModel.adapter = popularRecyclerView.createAdapter(source, presenter)
    }
}