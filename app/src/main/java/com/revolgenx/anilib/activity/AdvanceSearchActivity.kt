package com.revolgenx.anilib.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.observe
import com.otaliastudios.elements.Adapter
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.fragment.AdvanceSearchFragment
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.model.search.filter.BaseSearchFilterModel
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.util.DataProvider
import com.revolgenx.anilib.util.makeToast
import com.revolgenx.anilib.view.navigation.AdvanceSearchFilterNavigationView
import com.revolgenx.anilib.viewmodel.AdvanceSearchActivityViewModel
import kotlinx.android.synthetic.main.advance_search_activity_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AdvanceSearchActivity : DynamicSystemActivity(),
    AdvanceSearchFilterNavigationView.AdvanceBrowseNavigationCallbackListener,
    TagChooserDialogFragment.OnDoneListener {

    companion object {
        const val GENRE_CHOOSER_DIALOG_TAG = "genre_chooser_tag"
        const val TAG_CHOOSER_DIALOG_TAG = "tag_chooser_tag"
        const val ADVANCE_SEARCH_FRAGMENT_TAG = "advance_search_fragment_tag"
        const val STREAM_CHOOSER_DIALOG_TAG = "stream_chooser_tag"
        const val ADVANCE_SEARCH_INTENT_KEY = "advance_search_intent_key"
    }


    override fun getLocale(): Locale? {
        return null
    }

    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    private val tagAdapter: Adapter.Builder
        get() {
            return com.otaliastudios.elements.Adapter.builder(this)
        }

    private val backDrawable: Drawable
        get() = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back)!!.also {
            it.setTint(DynamicTheme.getInstance().get().tintPrimaryColor)
        }

    private val filterDrawable: Drawable
        get() = ContextCompat.getDrawable(context, R.drawable.ic_button_setting)!!.also {
            it.setTint(DynamicTheme.getInstance().get().tintPrimaryColor)
        }

    private val dataProvider by lazy {
        DataProvider(this)
    }

    private val viewModel by viewModel<AdvanceSearchActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.advance_search_activity_layout)
        setUpTheme()
        setUpPersistentSearchView()
        setUpListener()
        setUpViews()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.advanceSearchFragmentContainer,
                BaseFragment.newInstance(AdvanceSearchFragment::class.java),
                ADVANCE_SEARCH_FRAGMENT_TAG
            ).commitNow()

            if (intent.hasExtra(ADVANCE_SEARCH_INTENT_KEY)) {
                //todo
                advanceSearchFilterNavView.filter =
                    intent.getParcelableExtra(ADVANCE_SEARCH_INTENT_KEY)
            }
        }
    }

    private fun getAdvanceSearchFragment() = supportFragmentManager.findFragmentByTag(
        ADVANCE_SEARCH_FRAGMENT_TAG
    ) as? AdvanceSearchFragment

    private fun setUpViews() {
        viewModel.tagTagFields?.let {
            invalidateTagFilter(it)
        }
        viewModel.genreTagFields?.let {
            invalidateGenreFilter(it)
        }
        viewModel.streamTagFields?.let {
            invalidateStreamFilter(it)
        }
    }

    private fun setUpPersistentSearchView() {
        with(persistentSearchView) {
            setLeftButtonDrawable(backDrawable)
            setRightButtonDrawable(filterDrawable)
            showRightButton()

            setOnSearchConfirmedListener { _, query ->
                if (query.isEmpty()) {
                    return@setOnSearchConfirmedListener
                }
                dataProvider.addToSearchHistory(query)
                viewModel.searchQuery = query
                viewModel.searchNow()
                persistentSearchView.collapse(false)
            }

            setOnSearchQueryChangeListener { _, oldQuery, newQuery ->
                if (newQuery.length > 3) {
                    viewModel.searchQuery = newQuery
                    viewModel.searchLate()
                } else {
                    viewModel.clearHandler()
                }
            }

            setOnSuggestionChangeListener(object : OnSuggestionChangeListener {
                override fun onSuggestionRemoved(suggestion: SuggestionItem) {
                    dataProvider.removeFromSearchHistory(suggestion.itemModel.text)
                }

                override fun onSuggestionPicked(suggestion: SuggestionItem) {
                    viewModel.searchQuery = suggestion.itemModel.text
                    dataProvider.addToSearchHistory(viewModel.searchQuery)
                    viewModel.searchNow()
                }
            })
            dataProvider.getAllHistory().takeIf { it.isNotEmpty() }?.let {
                setSuggestions(
                    SuggestionCreationUtil.asRecentSearchSuggestions(it),
                    false
                )
            }
        }
    }

    private fun setUpTheme() {
        with(persistentSearchView) {
            DynamicTheme.getInstance().get().primaryColor.let {
                setCardBackgroundColor(it)
            }

            ResourcesCompat.getFont(this@AdvanceSearchActivity, R.font.open_sans_regular)?.let {
                setQueryTextTypeface(it)
                setSuggestionTextTypeface(it)
            }

            DynamicTheme.getInstance().get().tintPrimaryColor.let {
                setSuggestionSelectedTextColor(it)
                setQueryInputHintColor(it)
                setSuggestionTextColor(it)
                setSuggestionIconColor(it)
                setSearchSuggestionIconColor(it)
            }

            DynamicTheme.getInstance().get().tintPrimaryColor.let {
                setQueryInputTextColor(it)
                setQueryInputCursorColor(it)
                setRecentSearchIconColor(it)
            }

        }


        DynamicTheme.getInstance().get().backgroundColor.let {
            rootDrawerLayout.setBackgroundColor(it)
            advanceSearchCoordinatorLayout.setBackgroundColor(it)
        }
        statusBarColor = statusBarColor

    }


    private fun setUpListener() {
        checkDialogs()
        with(persistentSearchView) {
            setOnLeftBtnClickListener {
                if (isExpanded) {
                    collapse(true)
                } else
                    finish()
            }
            setOnRightBtnClickListener {
                rootDrawerLayout.openDrawer(GravityCompat.END)
            }
        }
        with(advanceSearchFilterNavView) {
            setNavigationCallbackListener(this@AdvanceSearchActivity)
        }
        rootDrawerLayout.addDrawerListener(advanceSearchFilterNavView.drawerListener)

        dataProvider.onDataChanged {
            persistentSearchView.setSuggestions(
                SuggestionCreationUtil.asRecentSearchSuggestions(dataProvider.getAllHistory()),
                false
            )
        }


        viewModel.searchLiveData.observe(this) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let {
                        when {
                            it.searchNow -> {
                                it.searchNow = false
                                search()
                            }
                            it.searchLate -> {
                                it.searchLate = false
                                search()
                            }
                        }
                    }
                }
                Status.LOADING -> {
                }
            }
        }
    }


    private fun checkDialogs() {
        val dialog = when {
            supportFragmentManager.findFragmentByTag(TAG_CHOOSER_DIALOG_TAG) != null -> {
                supportFragmentManager.findFragmentByTag(TAG_CHOOSER_DIALOG_TAG)
            }
            supportFragmentManager.findFragmentByTag(GENRE_CHOOSER_DIALOG_TAG) != null -> {
                supportFragmentManager.findFragmentByTag(GENRE_CHOOSER_DIALOG_TAG)
            }
            supportFragmentManager.findFragmentByTag(STREAM_CHOOSER_DIALOG_TAG) != null -> {
                supportFragmentManager.findFragmentByTag(STREAM_CHOOSER_DIALOG_TAG)
            }
            else -> null
        }

        if (dialog != null) {
            (dialog as TagChooserDialogFragment).onDoneListener(this)
        }
    }


    override fun onDone(fragmentTag: String?, list: List<TagField>) {
        when (fragmentTag) {
            TAG_CHOOSER_DIALOG_TAG -> {
                invalidateTagFilter(list)
            }
            GENRE_CHOOSER_DIALOG_TAG -> {
                invalidateGenreFilter(list)
            }
            STREAM_CHOOSER_DIALOG_TAG -> {
                invalidateStreamFilter(list)
            }
        }
    }

    private fun invalidateStreamFilter(list: List<TagField>) {
        viewModel.streamTagFields = list
        advanceSearchFilterNavView.buildStreamAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateGenreFilter(list: List<TagField>) {
        viewModel.genreTagFields = list
        advanceSearchFilterNavView.buildGenreAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateTagFilter(list: List<TagField>) {
        viewModel.tagTagFields = list
        advanceSearchFilterNavView.buildTagAdapter(
            tagAdapter,
            list
        )
    }


    override fun onBackPressed() {
        if (rootDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            rootDrawerLayout.closeDrawer(GravityCompat.END)
        } else
            super.onBackPressed()
    }


    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }


    /**
     * Called by advance search filter nav view
     * */
    override fun onGenreAdd(tags: List<TagField>) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                getString(R.string.genre),
                tags
            )
        ).apply {
            onDoneListener(this@AdvanceSearchActivity)
            show(supportFragmentManager, GENRE_CHOOSER_DIALOG_TAG)
        }
    }

    /**
     * Called by advance search filter nav view
     * */
    override fun onStreamAdd(tags: List<TagField>) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                getString(R.string.streaming_on),
                tags
            )
        ).apply {
            onDoneListener(this@AdvanceSearchActivity)
            show(supportFragmentManager, STREAM_CHOOSER_DIALOG_TAG)
        }
    }

    /**
     * Called by advance search filter nav view
     * */
    override fun onTagAdd(tags: List<TagField>) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                getString(R.string.tags),
                tags
            )
        ).apply {
            onDoneListener(this@AdvanceSearchActivity)
            show(supportFragmentManager, TAG_CHOOSER_DIALOG_TAG)
        }
    }

    /**
     * Called by advance search filter nav view to fill search box
     * */
    override fun getQuery(): String {
        return viewModel.searchQuery
    }

    override fun updateGenre() {
        advanceSearchFilterNavView.invalidateGenreAdapter(tagAdapter)
    }

    override fun updateStream() {
        advanceSearchFilterNavView.invalidateStreamAdapter(tagAdapter)
    }

    override fun updateTags() {
        advanceSearchFilterNavView.invalidateTagAdapter(tagAdapter)
    }

    override fun applyFilter() {
        if (rootDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            rootDrawerLayout.closeDrawer(GravityCompat.END)
        }
        advanceSearchFilterNavView.filter?.let {
            viewModel.searchQuery = it.query!!
            persistentSearchView.inputQuery = viewModel.searchQuery
            getAdvanceSearchFragment()?.search(it)
        }
    }

    private fun search() {
        advanceSearchFilterNavView.filter?.let {
            it.query = viewModel.searchQuery
            getAdvanceSearchFragment()?.search(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}