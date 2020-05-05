package com.revolgenx.anilib.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R

open class DiscoverTrendingFragment : DiscoverAiringFragment() {

    private var trendingAnimeRecyclerView: DynamicRecyclerView? = null
    private var trendingMangaRecyclerView: DynamicRecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        trendingAnimeRecyclerView = DynamicRecyclerView(requireContext()).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        trendingMangaRecyclerView = DynamicRecyclerView(requireContext()).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        addView(
            trendingAnimeRecyclerView!!,
            getString(R.string.trending_anime) + ">>", showSetting = false
        ) {
            handleClick(it)
        }

        addView(
            trendingMangaRecyclerView!!,
            getString(R.string.trending_manga) + ">>", showSetting = false
        ) {
            handleClick(it, 1)
        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trendingAnimeRecyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        trendingMangaRecyclerView!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun reloadAll() {
        super.reloadAll()

    }

//    private fun invalidateAnimeAdapter() {
//        if (trendingAnimeRecyclerView == null) return
//        trendingMangaRecyclerView!!.createAdapter(source, presenter)
//        viewModel.adapter = Adapter.builder(viewLifecycleOwner, 10)
//            .setPager(PageSizePager(10))
//            .addSource(source)
//            .addPresenter(presenter)
//            .addPresenter(loadingPresenter)
//            .addPresenter(errorPresenter)
//            .addPresenter(emptyPresenter)
//            .into(discoverAiringRecyclerView!!)
//    }

    private fun invalidateMangaAdapter() {

    }


    override fun handleClick(which: Int, next: Int?) {
        if (next == null) {
            if (which == 0) {

            }
        }
    }

}