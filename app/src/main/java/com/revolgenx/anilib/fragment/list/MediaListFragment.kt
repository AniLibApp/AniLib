package com.revolgenx.anilib.fragment.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaListActivity
import com.revolgenx.anilib.event.DisplayModeChangedEvent
import com.revolgenx.anilib.event.DisplayTypes
import com.revolgenx.anilib.event.ListEditorResultEvent
import com.revolgenx.anilib.field.MediaListCollectionFilterField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.meta.MediaListMeta
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.preference.getMediaListGridPresenter
import com.revolgenx.anilib.presenter.list.MediaListCollectionPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.viewmodel.media_list.MediaListCollectionViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class MediaListFragment : BasePresenterFragment<MediaListModel>() {

    override val basePresenter: Presenter<MediaListModel>
        get() = MediaListCollectionPresenter(requireContext(), mediaListMeta!!, viewModel)
    override val baseSource: Source<MediaListModel>
        get() = viewModel.source ?: createSource()

    abstract val viewModel: MediaListCollectionViewModel
    abstract val mediaListStatus: Int

    protected var mediaListMeta: MediaListMeta? = null


    override fun createSource(): Source<MediaListModel> {
        return viewModel.createSource()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = MediaListMeta::class.java.classLoader
        mediaListMeta = arguments?.getParcelable(MediaListActivity.MEDIA_LIST_META_KEY) ?: return
        viewModel.field.also { field ->
            field.userId = mediaListMeta?.userId
            field.userName = mediaListMeta?.userName
            field.mediaListStatus = mediaListStatus
            field.type = mediaListMeta?.type
        }
        super.onActivityCreated(savedInstanceState)

        baseSwipeRefreshLayout.setOnRefreshListener {
            viewModel.renewSource()
            createSource()
            baseSwipeRefreshLayout.isRefreshing = false
            invalidateAdapter()
        }
    }

    fun filter(mediaListFilterField: MediaListCollectionFilterField) {
        viewModel.filter = mediaListFilterField
        if (visibleToUser) {
            createSource()
            invalidateAdapter()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
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


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListEditorEvent(event: ListEditorResultEvent) {
        if (event.listEditorResultMeta.status != mediaListStatus) return
        event.listEditorResultMeta.let {
            if (it.progress == null) {
                viewModel.filteredList?.remove(viewModel.listMap[it.mediaId])
                viewModel.listMap.remove(it.mediaId)
                createSource()
                invalidateAdapter()
            } else {
                viewModel.listMap[it.mediaId]?.apply {
                    progress = it.progress
                }
                adapter?.notifyDataSetChanged()
            }
        }
        EventBus.getDefault().removeStickyEvent(event)
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }
}
