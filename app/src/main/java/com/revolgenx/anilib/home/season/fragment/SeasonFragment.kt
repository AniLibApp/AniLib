package com.revolgenx.anilib.home.season.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.otaliastudios.elements.extensions.SimplePresenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.home.season.data.field.SeasonField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.common.preference.getUserGenre
import com.revolgenx.anilib.common.preference.getUserTag
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.ui.selector.data.meta.SelectableMeta
import com.revolgenx.anilib.home.discover.event.SeasonEvent
import com.revolgenx.anilib.infrastructure.source.home.discover.MediaFormatHeaderSource
import com.revolgenx.anilib.ui.selector.dialog.SelectableDialogFragment
import com.revolgenx.anilib.home.season.presenter.SeasonPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.home.season.viewmodel.SeasonViewModel
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.ui.selector.constant.SelectedState
import com.revolgenx.anilib.util.EventBusListener
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonFragment : BasePresenterFragment<MediaModel>(), EventBusListener {

    override val applyInset: Boolean = false
    private val viewModel by viewModel<SeasonViewModel>()
    private val field get() = viewModel.field
    override val basePresenter: Presenter<MediaModel> by lazy {
        SeasonPresenter(requireContext())
    }

    override val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(
            requireContext(), R.layout.season_shimmer_loader_layout
        )

    override val baseSource: Source<MediaModel>
        get() {
            return viewModel.source ?: createSource()
        }

    private val tagList by lazy {
        getUserTag()
    }

    private val genreList by lazy {
        getUserGenre()
    }

    override fun createSource(): Source<MediaModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field = SeasonField.create()
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    private fun renewAdapter() {
        viewModel.field.updateFields()
        createSource()
        invalidateAdapter()
    }


    override fun adapterBuilder(): Adapter.Builder {
        val builder = super.adapterBuilder()

        if (viewModel.isHeaderEnabled()) {
            builder.addSource(MediaFormatHeaderSource(requireContext()))
            builder.addPresenter(
                SimplePresenter<HeaderSource.Data<MediaModel, String>>(
                    requireContext(),
                    R.layout.header_presenter_layout,
                    HeaderSource.ELEMENT_TYPE
                ) { v, header ->
                    (v as TextView).text = header.header
                })
        }

        return builder
    }


//
//    @Subscribe
//    fun onTagEvent(event: TagEvent) {
//        when (event.tagType) {
//            SelectableTypes.SEASON_TAG -> {
//                event.tagFields.forEach {
//                    viewModel.field.tagTagFields[it.tag]?.tagState = it.tagState
//                }
//                viewModel.field.tags =
//                    event.tagFields.filter { it.tagState == TagState.TAGGED }.map { it.tag }
//                        .toList()
//                viewModel.field.saveTags(requireContext())
//                renewAdapter()
//            }
//            SelectableTypes.SEASON_GENRE -> {
//                event.tagFields.forEach {
//                    viewModel.field.genreTagFields[it.tag]?.tagState = it.tagState
//                }
//                viewModel.field.genres =
//                    event.tagFields.filter { it.tagState == TagState.TAGGED }.map { it.tag }
//                        .toList()
//                viewModel.field.saveGenre(requireContext())
//                renewAdapter()
//            }
//            else -> {}
//        }
//
//    }


    @Subscribe()
    fun seasonEvent(event: SeasonEvent) {
        when (event) {
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
            SeasonEvent.SeasonTagEvent -> {
                val selectableList = genreList.map {
                    val state = when {
                        field.genres?.contains(it) == true -> {
                            SelectedState.SELECTED
                        }
                        else -> {
                            SelectedState.NONE
                        }
                    }
                    MutablePair(it, state)
                }
                openSelectorDialog(R.string.genre, selectableList)
            }
            SeasonEvent.SeasonGenreEvent -> {
                val selectableList = tagList.map {
                    val state = when {
                        field.tags?.contains(it) == true -> {
                            SelectedState.SELECTED
                        }
                        else -> {
                            SelectedState.NONE
                        }
                    }
                    MutablePair(it, state)
                }
                openSelectorDialog(R.string.tags, selectableList)
            }
        }
    }

    private fun openSelectorDialog(
        title: Int,
        selectableItem: List<MutablePair<String, SelectedState>>,
        hasIntermediateMode: Boolean = false
    ) {
        SelectableDialogFragment.newInstance(
            SelectableMeta(
                title = title,
                hasIntermediateMode = hasIntermediateMode,
                selectableItems = selectableItem
            )
        ).show(childFragmentManager, SelectableDialogFragment::class.java.simpleName)
    }


    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

}
