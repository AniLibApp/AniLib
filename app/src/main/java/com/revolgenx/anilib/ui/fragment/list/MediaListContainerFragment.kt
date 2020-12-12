package com.revolgenx.anilib.ui.fragment.list

import android.content.res.Configuration
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.data.meta.MediaListFilterMeta
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.infrastructure.event.DisplayModeChangedEvent
import com.revolgenx.anilib.infrastructure.event.DisplayTypes
import com.revolgenx.anilib.ui.dialog.MediaListCollectionFilterDialog
import com.revolgenx.anilib.ui.presenter.list.MediaListCollectionPresenter
import com.revolgenx.anilib.ui.viewmodel.media_list.MediaListContainerViewModel
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class MediaListContainerFragment : BasePresenterFragment<MediaListModel>(),
    MediaListCallbackInterface {


    override val basePresenter: Presenter<MediaListModel>
        get() = MediaListCollectionPresenter(
            requireContext(),
            mediaListMeta,
            viewModel.mediaListViewModel
        )
    override val baseSource: Source<MediaListModel>
        get() = viewModel.mediaListViewModel.source ?: createSource()

    private val viewModel by viewModel<MediaListContainerViewModel>()

    override var portraitMaxSpan: Int = 4
    override var portraitMinSpan: Int = 2

    private val mediaListMeta: MediaListMeta by lazy {
        mediaListMetaArgs()
    }


    override fun createSource(): Source<MediaListModel> {
        return viewModel.mediaListViewModel.createSource()
    }

    private fun renewSource(){
        viewModel.mediaListViewModel.renewSource()
    }

    protected abstract fun mediaListMetaArgs(): MediaListMeta

    protected fun getCurrentStatus(): Int {
        return viewModel.currentListStatus
    }

    override fun setCurrentStatus(status: Int) {
        viewModel.currentListStatus = status
        invalidateAdapter()
    }

    override fun reloadLayoutManager() {
        var span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

        if (getMediaListGridPresenter() == 1) {
            span /= 2
        }

        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
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

        super.reloadLayoutManager()
    }


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }


    @Subscribe()
    fun onDisplayModeEvent(event: DisplayModeChangedEvent) {
        when (event.whichDisplay) {
            DisplayTypes.MEDIA_LIST -> {
                changePresenterLayout()
            }
        }
    }

    private fun changePresenterLayout() {
        reloadLayoutManager()
        if (visibleToUser) {
            invalidateAdapter()
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        if ((mediaListMeta.userId == null && mediaListMeta.userName == null)) {
            return
        }

        baseSwipeRefreshLayout.setOnRefreshListener {
            renewSource()
            baseSwipeRefreshLayout.isRefreshing = false
            invalidateAdapter()
        }


        if (savedInstanceState == null) {
            with(viewModel.mediaListField) {
                userId = mediaListMeta.userId
                type = mediaListMeta.type
                viewModel.mediaListField = this
            }
        }

//        (savedInstanceState?.getParcelable(MediaListCollectionFilterDialog.LIST_FILTER_PARCEL_KEY) as? MediaListCollectionFilterField)?.let { field ->
//            mediaListFilterField = field
//            if (field.search.isNullOrEmpty().not()) {
//                menuItem?.expandActionView()
//                (menuItem?.actionView as? SearchView)?.let {
//                    it.setQuery(field.search!!, true)
//                }
//            }
//        }
    }

    override fun openListFilterDialog() {
        MediaListCollectionFilterDialog.newInstance(viewModel.mediaListViewModel.filter)
            .show(childFragmentManager, "media_filter_dialog")
    }

    override fun filterList(mediaListFilterMeta: MediaListFilterMeta) {
        viewModel.mediaListViewModel.filter.apply {
            mediaListFilterMeta.let {
                format = it.format
                status = it.status
                genre = it.genres
                listSort = it.mediaListSort
            }
        }

        if (visibleToUser) {
            createSource()
            invalidateAdapter()
        }
    }
}

interface MediaListCallbackInterface {
    fun getStatusName(): String
    fun getStatus(): Array<out String>
    fun setCurrentStatus(status: Int)
    fun openListFilterDialog()
    fun filterList(mediaListFilterMeta: MediaListFilterMeta)
}

