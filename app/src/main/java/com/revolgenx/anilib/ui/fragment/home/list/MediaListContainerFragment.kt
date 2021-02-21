package com.revolgenx.anilib.ui.fragment.home.list

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.popup.DynamicArrayPopup
import com.pranavpandey.android.dynamic.support.popup.DynamicPopup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.loadMediaListFilter
import com.revolgenx.anilib.common.preference.storeMediaListFilterField
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.data.meta.MediaListFilterMeta
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.databinding.BasePresenterFragmentLayoutBinding
import com.revolgenx.anilib.databinding.MediaListContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.DisplayModeChangedEvent
import com.revolgenx.anilib.infrastructure.event.DisplayTypes
import com.revolgenx.anilib.ui.presenter.home.discover.MediaListCollectionPresenter
import com.revolgenx.anilib.ui.viewmodel.home.list.MediaListContainerViewModel
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class MediaListContainerFragment : BasePresenterFragment<MediaListModel>(), EventBusListener {

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
    protected val mediaListBinding get() = _mediaListBinding!!

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

    fun setCurrentStatus(status: Int) {
        viewModel.mediaListViewModel.field.filter.search = null
        showSearchET(false)
        viewModel.currentListStatus = status
        invalidateAdapter()
    }

    fun showSearchET(b: Boolean? = null) {
        mediaListBinding.mediaListSearchEt.let {
            if (b == null) {
                val searchLayoutIsVisible = mediaListBinding.mediaListSearchLayout.isVisible
                mediaListBinding.mediaListSearchLayout.visibility =
                    if (searchLayoutIsVisible) {
                        mediaListBinding.mediaListSearchEt.setText("")
                        mediaListBinding.mediaListSearchEt.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
                        View.GONE
                    } else {
                        mediaListBinding.mediaListSearchEt.setText("")
                        mediaListBinding.mediaListSearchEt.requestFocus()
                        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                            mediaListBinding.mediaListSearchEt,
                            0
                        )
                        View.VISIBLE
                    }
            } else {
                if (b == true) {
                    mediaListBinding.mediaListSearchLayout.visibility = View.VISIBLE
                    mediaListBinding.mediaListSearchEt.setText("")
                    mediaListBinding.mediaListSearchEt.requestFocus()
                    (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                        mediaListBinding.mediaListSearchEt,
                        0
                    )
                } else {
                    mediaListBinding.mediaListSearchEt.setText("")
                    mediaListBinding.mediaListSearchEt.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
                    mediaListBinding.mediaListSearchLayout.visibility = View.GONE
                    return
                }
            }
        }
    }

    override fun reloadLayoutManager() {
        var span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

        when (getMediaListGridPresenter()) {
            MediaListDisplayMode.COMPACT -> {
            }
            MediaListDisplayMode.NORMAL, MediaListDisplayMode.MINIMAL_LIST -> {
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
        mediaListBinding.mediaListSearchEt.typeface =
            ResourcesCompat.getFont(requireContext(), R.font.cabin_regular)
        mediaListBinding.clearText.setOnClickListener {
            val mediaSearchEt = mediaListBinding.mediaListSearchEt
            if (mediaSearchEt.text.isNullOrEmpty()) {
                showSearchET(false)
            } else {
                mediaSearchEt.setText("")
                mediaSearchEt.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
            }
        }
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

        mediaListBinding.mediaListSearchEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_SEARCH) return@setOnEditorActionListener false

            viewModel.mediaListViewModel.field.filter.search =
                mediaListBinding.mediaListSearchEt.text?.toString() ?: ""
            createSource()
            invalidateAdapter()
            true
        }

        if (savedInstanceState == null) {
            with(viewModel.mediaListField) {
                userId = mediaListMeta.userId
                type = mediaListMeta.type
                viewModel.mediaListField = this
            }

            val mediaListFilterField = MediaListCollectionFilterField().apply {
                loadMediaListFilter(requireContext(), mediaListMeta.type).let {
                    formatsIn = it.formatsIn
                    status = it.status
                    genre = it.genre
                    listSort = it.listSort
                }
            }

            viewModel.listStatusViewModel.forEach {
                it.value.updateFilter(mediaListFilterField)
            }
        }

    }

    override fun onDestroyView() {
        _mediaListBinding = null
        super.onDestroyView()
    }

    fun filterList(mediaListFilterMeta: MediaListFilterMeta) {
        val mediaListFilterField = MediaListCollectionFilterField(
            formatsIn = mediaListFilterMeta.formatsIn,
            status = mediaListFilterMeta.status,
            genre = mediaListFilterMeta.genres,
            listSort = mediaListFilterMeta.mediaListSort
        )
        storeMediaListFilterField(requireContext(), mediaListFilterField, mediaListMeta.type)
        viewModel.listStatusViewModel.forEach {
            it.value.updateFilter(mediaListFilterField)
        }

        if (visibleToUser) {
            createSource()
            invalidateAdapter()
        }
    }

}

//interface MediaListCallbackInterface {
//    fun getStatusName(): String
//    fun getStatus(): Array<out String>
//    fun setCurrentStatus(status: Int)
//    fun filterList(mediaListFilterMeta: MediaListFilterMeta)
//}

