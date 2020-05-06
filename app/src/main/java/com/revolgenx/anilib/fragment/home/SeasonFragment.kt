package com.revolgenx.anilib.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.BrowseActivity
import com.revolgenx.anilib.dialog.SeasonFilterDialog
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.event.ListEditorResultEvent
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.field.SeasonField
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.presenter.SeasonPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.viewmodel.SeasonViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonFragment : BasePresenterFragment<CommonMediaModel>(),
    TagChooserDialogFragment.OnDoneListener {

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

    private val genreTagField by lazy {
        val genreTags = mutableMapOf<String, TagField>()
        requireContext().resources.getStringArray(R.array.media_genre).forEach {
            genreTags[it] = TagField(it, false)
        }
        genreTags
    }

    private val tagTagField by lazy {
        val genreTags = mutableMapOf<String, TagField>()
        requireContext().resources.getStringArray(R.array.media_tags).forEach {
            genreTags[it] = TagField(it, false)
        }
        genreTags
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.season_menu, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            viewModel.field = SeasonField.create(requireContext())
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            updateSeasonDialogListener()
            updateGenreDialogListener()
            updateTagDialogListener()
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
                updateToolbarTitle()
                invalidateAdapter()
                true
            }
            R.id.nextSeasonId -> {
                viewModel.nextSeason(requireContext())
                updateToolbarTitle()
                invalidateAdapter()
                true
            }
            R.id.filterSeasonId -> {
                SeasonFilterDialog().also {
                    it.onDoneListener = {
                        renewAdapter()
                    }
                    it.show(childFragmentManager, SEASON_FILTER_DIALOG_TAG)
                }
                true
            }
            R.id.filterSeasonGenreId -> {
                TagChooserDialogFragment.newInstance(
                    TagChooserField(
                        requireContext().getString(R.string.genre),
                        genreTagField.values.toList()
                    )
                ).also {
                    it.onDoneListener(this)
                    it.show(childFragmentManager, BrowseActivity.GENRE_CHOOSER_DIALOG_TAG)
                }
                true
            }
            R.id.filterSeasonTagId -> {
                TagChooserDialogFragment.newInstance(
                    TagChooserField(
                        requireContext().getString(R.string.genre),
                        tagTagField.values.toList()
                    )
                ).also {
                    it.onDoneListener(this)
                    it.show(childFragmentManager, BrowseActivity.TAG_CHOOSER_DIALOG_TAG)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSeasonDialogListener() {
        childFragmentManager.findFragmentByTag(SEASON_FILTER_DIALOG_TAG)?.let {
            (it as SeasonFilterDialog).onDoneListener = {
                renewAdapter()
            }
        }
    }


    private fun updateGenreDialogListener() {
        childFragmentManager.findFragmentByTag(BrowseActivity.GENRE_CHOOSER_DIALOG_TAG)?.let {
            (it as TagChooserDialogFragment).onDoneListener(this)
        }
    }

    private fun updateTagDialogListener() {
        childFragmentManager.findFragmentByTag(BrowseActivity.TAG_CHOOSER_DIALOG_TAG)?.let {
            (it as TagChooserDialogFragment).onDoneListener(this)
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

    override fun onDone(fragmentTag: String?, list: List<TagField>) {
        when (fragmentTag) {
            BrowseActivity.TAG_CHOOSER_DIALOG_TAG -> {
                list.forEach {
                    tagTagField[it.tag]?.isTagged = it.isTagged
                }
                viewModel.field.tags = list.filter { it.isTagged }.map { it.tag }.toList()
                viewModel.field.saveSeasonField(requireContext())
            }
            BrowseActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                list.forEach {
                    genreTagField[it.tag]?.isTagged = it.isTagged
                }
                viewModel.field.genres = list.filter { it.isTagged }.map { it.tag }.toList()
                viewModel.field.saveSeasonField(requireContext())
            }
        }

        renewAdapter()
    }

    private fun renewAdapter() {
        viewModel.field = SeasonField.create(requireContext())
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
