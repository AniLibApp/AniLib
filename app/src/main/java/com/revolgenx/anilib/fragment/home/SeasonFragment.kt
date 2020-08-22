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
import com.revolgenx.anilib.activity.SearchActivity
import com.revolgenx.anilib.activity.MainActivity
import com.revolgenx.anilib.dialog.MediaFilterDialog
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.event.ListEditorResultEvent
import com.revolgenx.anilib.event.TagEvent
import com.revolgenx.anilib.event.TagOperationType
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
import com.revolgenx.anilib.viewmodel.SeasonViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonFragment : BasePresenterFragment<CommonMediaModel>(),
    TagChooserDialogFragment.TagChooserDialogCallback {

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
                TagChooserDialogFragment.newInstance(
                    TagChooserField(
                        requireContext().getString(R.string.genre),
                        viewModel.field.genreTagFields.values.toList()
                    )
                ).also {
                    it.onDoneListener(this)
                    it.show(childFragmentManager, SearchActivity.GENRE_CHOOSER_DIALOG_TAG)
                }
                true
            }
            R.id.filterSeasonTagId -> {
                TagChooserDialogFragment.newInstance(
                    TagChooserField(
                        requireContext().getString(R.string.tags),
                        viewModel.field.tagTagFields.values.toList()
                    )
                ).also {
                    it.onDoneListener(this)
                    it.show(childFragmentManager, SearchActivity.TAG_CHOOSER_DIALOG_TAG)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSeasonDialogListener() {
        childFragmentManager.findFragmentByTag(SEASON_FILTER_DIALOG_TAG)?.let {
            (it as MediaFilterDialog).onDoneListener = {
                renewAdapter()
            }
        }
    }


    private fun updateGenreDialogListener() {
        childFragmentManager.findFragmentByTag(SearchActivity.GENRE_CHOOSER_DIALOG_TAG)?.let {
            (it as TagChooserDialogFragment).onDoneListener(this)
        }
    }

    private fun updateTagDialogListener() {
        childFragmentManager.findFragmentByTag(SearchActivity.TAG_CHOOSER_DIALOG_TAG)?.let {
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

    override fun onTagChooserDone(fragmentTag: String?, list: List<TagField>) {
        when (fragmentTag) {
            SearchActivity.TAG_CHOOSER_DIALOG_TAG -> {
                list.forEach {
                    viewModel.field.tagTagFields[it.tag]?.isTagged = it.isTagged
                }
                viewModel.field.tags = list.filter { it.isTagged }.map { it.tag }.toList()
                viewModel.field.saveTags(requireContext())
            }
            SearchActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                list.forEach {
                    viewModel.field.genreTagFields[it.tag]?.isTagged = it.isTagged
                }
                viewModel.field.genres = list.filter { it.isTagged }.map { it.tag }.toList()
                viewModel.field.saveGenre(requireContext())
            }
        }

        renewAdapter()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTagEvent(event: TagEvent) {
        val fragmentTag = event.tag
        val tagFields = event.tagFields;
        when (event.tag) {
            SearchActivity.TAG_CHOOSER_DIALOG_TAG -> {
                when (event.operationType) {
                    TagOperationType.ADD_TAG -> {
                        tagFields.forEach {
                            viewModel.field.tagTagFields[it.tag] = it
                        }
                        addTagToNavView(fragmentTag, tagFields)

                    }
                    TagOperationType.DELETE_TAG -> {
                        tagFields.forEach {
                            viewModel.field.tagTagFields.remove(it.tag)
                        }
                        removeTagToNavView(fragmentTag, tagFields)
                    }
                    else -> {

                    }
                }
            }
            SearchActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                when (event.operationType) {
                    TagOperationType.ADD_GENRE -> {
                        tagFields.forEach {
                        viewModel.field.genreTagFields[it.tag] = it
                    }
                    }
                    TagOperationType.DELETE_GENRE -> {
                        tagFields.forEach {
                            viewModel.field.genreTagFields.remove(it.tag)
                        }
                        removeTagToNavView(fragmentTag, tagFields)
                    }
                    else -> {

                    }
                }
            }
            else -> {

            }
        }
    }


    private fun addTagToNavView(fragmentTag: String?, tags: List<TagField>) {
        when (fragmentTag) {
            SearchActivity.TAG_CHOOSER_DIALOG_TAG -> {
                (activity as? MainActivity)?.mainBrowseFilterNavView?.addTagField(tags)
            }
            SearchActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                (activity as? MainActivity)?.mainBrowseFilterNavView?.addGenreField(tags)
            }
            else -> {

            }
        }
    }

    private fun removeTagToNavView(fragmentTag: String?, tags: List<TagField>) {
        when (fragmentTag) {
            SearchActivity.TAG_CHOOSER_DIALOG_TAG -> {
                (activity as? MainActivity)?.mainBrowseFilterNavView?.removeTagField(tags)
            }
            SearchActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                (activity as? MainActivity)?.mainBrowseFilterNavView?.removeGenreField(tags)
            }
            else -> {

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
