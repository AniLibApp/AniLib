package com.revolgenx.anilib.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.dialog.MediaFilterDialog
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.event.ListEditorResultEvent
import com.revolgenx.anilib.event.TagEvent
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.field.home.SeasonField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.preference.getUserGenre
import com.revolgenx.anilib.preference.getUserTag
import com.revolgenx.anilib.presenter.SeasonPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.viewmodel.home.SeasonViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonFragment : BasePresenterFragment<CommonMediaModel>() {

    private val viewModel by viewModel<SeasonViewModel>()

    companion object {
        const val SEASON_FILTER_DIALOG_TAG = "season_filter_dialog"
    }

    override val basePresenter: Presenter<CommonMediaModel> by lazy {
        SeasonPresenter(requireContext())
    }

    override val baseSource: Source<CommonMediaModel>
        get() {
            return viewModel.source ?: createSource()
        }

    private val seasons by lazy {
        requireContext().resources.getStringArray(R.array.media_season)
    }

    override fun createSource(): Source<CommonMediaModel> {
        return viewModel.createSource()
    }


    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.season_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.field = SeasonField.create(requireContext())
            getUserTag(requireContext()).forEach {
                viewModel.field.tagTagFields[it] = TagField(it, false)
            }
            getUserGenre(requireContext()).forEach {
                viewModel.field.genreTagFields[it] = TagField(it, false)
            }
            viewModel.field.genres?.forEach {
                viewModel.field.genreTagFields[it]?.isTagged = true
            }
            viewModel.field.tags?.forEach {
                viewModel.field.tagTagFields[it]?.isTagged = true
            }
        }
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            updateSeasonDialogListener()
        }
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
        updateToolbarTitle()
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.prevSeasonId -> {
                viewModel.previousSeason(requireContext())
                renewAdapter()
                true
            }
            R.id.nextSeasonId -> {
                viewModel.nextSeason(requireContext())
                renewAdapter()
                true
            }
            R.id.filterSeasonId -> {
                MediaFilterDialog.newInstance(MediaFilterDialog.MediaFilterType.SEASON.ordinal)
                    .also {
                        it.onDoneListener = {
                            renewAdapter()
                        }
                        it.show(childFragmentManager, SEASON_FILTER_DIALOG_TAG)
                    }
                true
            }
            R.id.filterSeasonGenreId -> {
                openTagChooserDialog(viewModel.field.genreTagFields.values.toList(), MediaTagFilterTypes.SEASON_GENRE)
                true
            }
            R.id.filterSeasonTagId -> {
                openTagChooserDialog(viewModel.field.tagTagFields.values.toList(), MediaTagFilterTypes.SEASON_TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openTagChooserDialog(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                tagType,
                tags
            )
        ).show(childFragmentManager, TagChooserDialogFragment::class.java.simpleName)
    }

    private fun updateSeasonDialogListener() {
        childFragmentManager.findFragmentByTag(SEASON_FILTER_DIALOG_TAG)?.let {
            (it as MediaFilterDialog).onDoneListener = {
                renewAdapter()
            }
        }
    }


    private fun updateToolbarTitle() {
        (activity as? AppCompatActivity)?.let {
            viewModel.field.let { field ->
                field.season?.let { season ->
                    it.supportActionBar?.title = seasons[season]
                }
                field.seasonYear?.let { year ->
                    it.supportActionBar?.subtitle = year.toString()
                }
            }
        }
    }

    @Subscribe
    fun onTagEvent(event:TagEvent){
        when(event.tagType){
            MediaTagFilterTypes.SEASON_TAG->{
                event.tagFields.forEach {
                    viewModel.field.tagTagFields[it.tag]?.isTagged = it.isTagged
                }
                viewModel.field.tags = event.tagFields.filter { it.isTagged }.map { it.tag }.toList()
                viewModel.field.saveTags(requireContext())
                renewAdapter()
            }
            MediaTagFilterTypes.SEASON_GENRE->{
                event.tagFields.forEach {
                    viewModel.field.genreTagFields[it.tag]?.isTagged = it.isTagged
                }
                viewModel.field.genres = event.tagFields.filter { it.isTagged }.map { it.tag }.toList()
                viewModel.field.saveGenre(requireContext())
                renewAdapter()
            }
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
