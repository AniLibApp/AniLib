package com.revolgenx.anilib.fragment.base

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
import com.pranavpandey.android.dynamic.support.widget.DynamicSwipeRefreshLayout
import com.revolgenx.anilib.R
import kotlinx.android.synthetic.main.base_presenter_fragment_layout.view.*
import timber.log.Timber


/**
 * BasePresenter class contains @property baseRecyclerView @property layoutManager
 *
 * */
abstract class BasePresenterFragment<S : Any> : BaseFragment() {
    abstract val basePresenter: Presenter<S>
    abstract val baseSource: Source<S>

    var visibleToUser = false

    var loadingPresenter: Presenter<Void>? = null
        get() {
            return if (field == null) Presenter.forLoadingIndicator(
                context!!,
                R.layout.loading_layout
            )
            else field
        }

    var errorPresenter: Presenter<Void>? = null
        get() {
            return if (field == null) Presenter.forErrorIndicator(context!!, R.layout.error_layout)
            else field
        }

    var emptyPresenter: Presenter<Void>? = null
        get() {
            return if (field == null) Presenter.forEmptyIndicator(context!!, R.layout.empty_layout)
            else field
        }

    lateinit var baseRecyclerView: RecyclerView
    lateinit var baseSwipeRefreshLayout: DynamicSwipeRefreshLayout
    lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.base_presenter_fragment_layout, container, false)
        baseRecyclerView = v.base_presenter_recycler_view
        baseSwipeRefreshLayout = v.base_swipe_to_refresh
        return v;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager =
            GridLayoutManager(
                this.context,
                if (context!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
            )
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        baseRecyclerView.layoutManager = layoutManager

        if (savedInstanceState == null)
            createSource()

        baseSwipeRefreshLayout.setOnRefreshListener {
            createSource()
            baseSwipeRefreshLayout.isRefreshing = false
            invalidateAdapter()
        }
    }

    override fun onResume() {
        super.onResume()
        visibleToUser = true
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        visibleToUser = false
    }

    abstract fun createSource(): Source<S>

    /** call this method to load into recyclerview*/
    protected fun invalidateAdapter() {
        Adapter.builder(this, 10)
            .setPager(PageSizePager(10))
            .addSource(baseSource)
            .addPresenter(basePresenter)
            .addPresenter(loadingPresenter!!)
            .addPresenter(errorPresenter!!)
            .addPresenter(emptyPresenter!!)
            .into(baseRecyclerView)
    }

}