package com.revolgenx.anilib.common.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.pagers.PageSizePager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.BasePresenterFragmentLayoutBinding


/**
 * BasePresenter class contains @property baseRecyclerView @property layoutManager
 *
 * */
abstract class BasePresenterFragment<M : Any>() : BaseLayoutFragment<BasePresenterFragmentLayoutBinding>() {
    abstract val basePresenter: Presenter<M>
    abstract val baseSource: Source<M>


    var adapter: Adapter? = null

    protected open val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(requireContext(), R.layout.loading_layout)

    private val errorPresenter: Presenter<Unit> by lazy {
        Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
    }

    private val emptyPresenter: Presenter<Unit> by lazy {
        Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
    }

    val baseRecyclerView get() = binding.basePresenterRecyclerView
    val baseSwipeRefreshLayout get() = binding.baseSwipeToRefresh

    lateinit var layoutManager: RecyclerView.LayoutManager

    open protected var gridMinSpan = 1
    open protected var gridMaxSpan = 2

    var span: Int = 0

    override fun bindView(inflater: LayoutInflater, parent: ViewGroup?): BasePresenterFragmentLayoutBinding {
        return BasePresenterFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        span = if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) gridMaxSpan else gridMinSpan
        layoutManager = GridLayoutManager(this.context, span).also {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter?.getItemViewType(position) == 0) {
                        1
                    } else {
                        span
                    }
                }
            }
        }

    }

    protected open fun reloadLayoutManager() {
        baseRecyclerView.layoutManager = layoutManager
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reloadLayoutManager()

        baseSwipeRefreshLayout.setOnRefreshListener {
            createSource()
            baseSwipeRefreshLayout.isRefreshing = false
            invalidateAdapter()
        }
    }


    override fun onResume() {
        super.onResume()
        if (!visibleToUser) invalidateAdapter()
        visibleToUser = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        visibleToUser = false
    }

    abstract fun createSource(): Source<M>

    /** call this method to load into recyclerview*/
    protected fun invalidateAdapter() {
        adapter = adapterBuilder().into(baseRecyclerView)
    }

    protected open fun adapterBuilder(): Adapter.Builder {
        return Adapter.builder(this, 10).setPager(PageSizePager(10)).addSource(baseSource).addPresenter(basePresenter).addPresenter(loadingPresenter).addPresenter(errorPresenter).addPresenter(emptyPresenter)
    }

}