package com.revolgenx.anilib.ui.fragment.home.season

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.ui.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.infrastructure.event.ListEditorResultEvent
import com.revolgenx.anilib.infrastructure.event.TagEvent
import com.revolgenx.anilib.data.field.TagChooserField
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.data.field.TagState
import com.revolgenx.anilib.data.field.home.SeasonField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.CommonMediaModel
import com.revolgenx.anilib.common.preference.getUserGenre
import com.revolgenx.anilib.common.preference.getUserTag
import com.revolgenx.anilib.databinding.SeasonFragmentBinding
import com.revolgenx.anilib.ui.bottomsheet.discover.MediaFilterBottomSheetFragment
import com.revolgenx.anilib.ui.presenter.season.SeasonPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.ui.viewmodel.home.season.SeasonViewModel
import com.revolgenx.anilib.util.EventBusListener
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

    private val seasons by lazy {
        requireContext().resources.getStringArray(R.array.media_season)
    }

    private lateinit var seasonBinding: SeasonFragmentBinding

    override fun createSource(): Source<CommonMediaModel> {
        return viewModel.createSource()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        seasonBinding = SeasonFragmentBinding.inflate(inflater, container, false)
        val v = super.onCreateView(inflater, container, savedInstanceState)
        seasonBinding.seasonFrameLayout.addView(v)
        return seasonBinding.root
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
        seasonBinding.apply {
            seasonPreviousIv.setOnClickListener {
                viewModel.previousSeason(requireContext())
                renewAdapter()
            }

            seasonNextIv.setOnClickListener {
                viewModel.nextSeason(requireContext())
                renewAdapter()
            }

            seasonFilterIv.onPopupMenuClickListener = { _, position ->
                when(position){
                    0 -> {
                        MediaFilterBottomSheetFragment().show(requireContext()) {
                            arguments = bundleOf(MediaFilterBottomSheetFragment.mediaFilterTypeKey to MediaFilterBottomSheetFragment.MediaFilterType.SEASON.ordinal)
                            onDoneListener = {
                                renewAdapter()
                            }
                        }
                    }
                    1->{
                        openTagChooserDialog(
                            viewModel.field.genreTagFields.values.toList(),
                            MediaTagFilterTypes.SEASON_GENRE
                        )
                    }
                    2->{
                        openTagChooserDialog(
                            viewModel.field.tagTagFields.values.toList(),
                            MediaTagFilterTypes.SEASON_TAG
                        )
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        updateToolbarTitle()
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    private fun openTagChooserDialog(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                tagType,
                tags
            )
        ).show(childFragmentManager, TagChooserDialogFragment::class.java.simpleName)
    }

    private fun updateToolbarTitle() {
        seasonBinding.apply {
            viewModel.field.let { field ->
                var toolbarTitle = ""
                field.season?.let { season ->
                    toolbarTitle += seasons[season]
                }
                field.seasonYear?.let {
                    toolbarTitle += " $it"
                }
                seasonNameTv.text = toolbarTitle
            }
        }
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


    private fun renewAdapter() {
        viewModel.field.updateFields(requireContext())
        updateToolbarTitle()
        createSource()
        invalidateAdapter()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListEditorEvent(event: ListEditorResultEvent) {
        event.listEditorResultMeta.let {
            viewModel.updateMediaProgress(it.mediaId, it.progress)
        }
        adapter?.notifyDataSetChanged()
        EventBus.getDefault().removeStickyEvent(event)
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

}
