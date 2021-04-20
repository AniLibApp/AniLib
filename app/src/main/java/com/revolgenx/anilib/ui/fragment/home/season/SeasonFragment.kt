package com.revolgenx.anilib.ui.fragment.home.season

import android.os.Bundle
import android.widget.TextView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.otaliastudios.elements.extensions.SimplePresenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.infrastructure.event.ListEditorResultEvent
import com.revolgenx.anilib.infrastructure.event.TagEvent
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.data.field.TagState
import com.revolgenx.anilib.data.field.home.SeasonField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.common.preference.getUserGenre
import com.revolgenx.anilib.common.preference.getUserTag
import com.revolgenx.anilib.infrastructure.event.SeasonEvent
import com.revolgenx.anilib.infrastructure.source.home.discover.MediaFormatHeaderSource
import com.revolgenx.anilib.ui.presenter.season.SeasonPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.ui.viewmodel.home.season.SeasonViewModel
import com.revolgenx.anilib.util.EventBusListener
import okhttp3.internal.notify
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonFragment : BasePresenterFragment<CommonMediaModel>(), EventBusListener {

    private val viewModel by viewModel<SeasonViewModel>()

    override val basePresenter: Presenter<CommonMediaModel> by lazy {
        SeasonPresenter(requireContext())
    }

    override val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(
            requireContext(), R.layout.season_shimmer_loader_layout
        )

    override val baseSource: Source<CommonMediaModel>
        get() {
            return viewModel.source ?: createSource()
        }


    override fun createSource(): Source<CommonMediaModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.field = SeasonField.create(requireContext())
            getUserTag(requireContext()).forEach {
                viewModel.field.tagTagFields[it] = TagField(it, TagState.EMPTY)
            }
            getUserGenre(requireContext()).forEach {
                viewModel.field.genreTagFields[it] = TagField(it, TagState.EMPTY)
            }
            viewModel.field.genres?.forEach {
                viewModel.field.genreTagFields[it]?.tagState = TagState.TAGGED
            }
            viewModel.field.tags?.forEach {
                viewModel.field.tagTagFields[it]?.tagState = TagState.TAGGED
            }
        }
        super.onActivityCreated(savedInstanceState)
        initListener()
    }


    private fun initListener() {
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }



    private fun renewAdapter() {
        viewModel.field.updateFields(requireContext())
        createSource()
        invalidateAdapter()
    }


    override fun adapterBuilder(): Adapter.Builder {
        val builder = super.adapterBuilder()

        if (viewModel.isHeaderEnabled()) {
            builder.addSource(MediaFormatHeaderSource(requireContext()))
            builder.addPresenter(
                SimplePresenter<HeaderSource.Data<CommonMediaModel, String>>(
                    requireContext(),
                    R.layout.header_presenter_layout,
                    HeaderSource.ELEMENT_TYPE
                ) { v, header ->
                    (v as TextView).text = header.header
                })
        }

        return builder
    }


    @Subscribe
    fun onTagEvent(event: TagEvent) {
        when (event.tagType) {
            MediaTagFilterTypes.SEASON_TAG -> {
                event.tagFields.forEach {
                    viewModel.field.tagTagFields[it.tag]?.tagState = it.tagState
                }
                viewModel.field.tags =
                    event.tagFields.filter { it.tagState == TagState.TAGGED }.map { it.tag }
                        .toList()
                viewModel.field.saveTags(requireContext())
                renewAdapter()
            }
            MediaTagFilterTypes.SEASON_GENRE -> {
                event.tagFields.forEach {
                    viewModel.field.genreTagFields[it.tag]?.tagState = it.tagState
                }
                viewModel.field.genres =
                    event.tagFields.filter { it.tagState == TagState.TAGGED }.map { it.tag }
                        .toList()
                viewModel.field.saveGenre(requireContext())
                renewAdapter()
            }
            else -> {}
        }

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListEditorEvent(event: ListEditorResultEvent) {
        event.listEditorResultMeta.let {
            viewModel.updateMediaProgress(it.mediaId, it.progress)
        }
        notifyDataSetChanged()
        EventBus.getDefault().removeStickyEvent(event)
    }

    @Subscribe()
    fun seasonEvent(event:SeasonEvent){
        when(event){
            SeasonEvent.SeasonFilterEvent -> {
                renewAdapter()
            }
            is SeasonEvent.SeasonChangeEvent -> {
                viewModel.dispose()
                renewAdapter()
            }
            is SeasonEvent.SeasonHeaderEvent -> {
                viewModel.isHeaderEnabled(event.showHeader)
                renewAdapter()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

}
