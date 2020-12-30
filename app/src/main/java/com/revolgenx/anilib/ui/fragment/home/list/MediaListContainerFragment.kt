package com.revolgenx.anilib.ui.fragment.home.list

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.data.meta.MediaListFilterMeta
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.databinding.MediaListContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.DisplayModeChangedEvent
import com.revolgenx.anilib.infrastructure.event.DisplayTypes
import com.revolgenx.anilib.ui.dialog.MediaListCollectionFilterDialog
import com.revolgenx.anilib.ui.presenter.list.MediaListCollectionPresenter
import com.revolgenx.anilib.ui.viewmodel.home.list.MediaListContainerViewModel
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class MediaListContainerFragment : BasePresenterFragment<MediaListModel>(),
    MediaListCallbackInterface, EventBusListener {


    override val basePresenter: Presenter<MediaListModel>
        get() = MediaListCollectionPresenter(
            requireContext(),
            mediaListMeta,
            viewModel.mediaListViewModel
        )
    override val baseSource: Source<MediaListModel>
        get() = viewModel.mediaListViewModel.source ?: createSource()

    private val viewModel by viewModel<MediaListContainerViewModel>()
    private var _mediaListBinding: MediaListContainerFragmentBinding? = null
    private val mediaListBinding get() = _mediaListBinding!!

    override var gridMaxSpan: Int = 4
    override var gridMinSpan: Int = 2

    private val mediaListMeta: MediaListMeta by lazy {
        mediaListMetaArgs()
    }


    override fun createSource(): Source<MediaListModel> {
        return viewModel.mediaListViewModel.createSource()
    }

    private fun renewSource() {
        viewModel.mediaListViewModel.renewSource()
    }

    protected abstract fun mediaListMetaArgs(): MediaListMeta

    protected fun getCurrentStatus(): Int {
        return viewModel.currentListStatus
    }

    override fun setCurrentStatus(status: Int) {
        viewModel.mediaListViewModel.filter.search = null
        showSearchET(false)
        viewModel.currentListStatus = status
        invalidateAdapter()
    }

    fun showSearchET(b: Boolean) {
        mediaListBinding.mediaListSearchEt.let {
            if(b){
                it.visibility = View.VISIBLE
                mediaListBinding.mediaListSearchEt.setText("")
                mediaListBinding.mediaListSearchEt.requestFocus()
                (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                    mediaListBinding.mediaListSearchEt,
                    0
                )
            }else{
                it.visibility = View.GONE
            }
        }
    }

    override fun reloadLayoutManager() {
        var span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

        when (getMediaListGridPresenter()) {
            MediaListDisplayMode.COMPACT -> {
            }
            MediaListDisplayMode.NORMAL , MediaListDisplayMode.MINIMAL_LIST-> {
                span /= 2
            }
            MediaListDisplayMode.CARD -> {
            }
            MediaListDisplayMode.CLASSIC, MediaListDisplayMode.MINIMAL -> {
                span += 1
            }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _mediaListBinding = MediaListContainerFragmentBinding.inflate(inflater, container, false)
        mediaListBinding.mediaListLinearLayout.addView(v)
        return mediaListBinding.root
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

        mediaListBinding.mediaListSearchEt.doOnTextChanged { text, _, _, _ ->
            viewModel.mediaListViewModel.filter.search = text.toString()
            createSource()
            invalidateAdapter()
        }


        if (savedInstanceState == null) {
            with(viewModel.mediaListField) {
                userId = mediaListMeta.userId
                type = mediaListMeta.type
                viewModel.mediaListField = this
            }
        }

    }

    override fun onDestroyView() {
        _mediaListBinding = null
        super.onDestroyView()
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

