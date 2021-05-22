package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Adapter
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.ui.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.infrastructure.event.BrowseFilterAppliedEvent
import com.revolgenx.anilib.infrastructure.event.TagEvent
import com.revolgenx.anilib.data.field.TagChooserField
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.ui.fragment.search.SearchFragment
import com.revolgenx.anilib.data.model.search.filter.SearchFilterModel
import com.revolgenx.anilib.databinding.SearchActivityLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.util.DataProvider
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.ui.view.navigation.BrowseFilterNavigationView
import com.revolgenx.anilib.ui.viewmodel.browse.BrowseActivityViewModel
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseDynamicActivity<SearchActivityLayoutBinding>(),
    BrowseFilterNavigationView.AdvanceBrowseNavigationCallbackListener {

    companion object {
        const val TAG_CHOOSER_DIALOG_TAG = "tag_chooser_tag"
        const val ADVANCE_SEARCH_FRAGMENT_TAG = "advance_search_fragment_tag"
        const val ADVANCE_SEARCH_INTENT_KEY = "advance_search_intent_key"


        fun openActivity(context: Context, searchFilterModel: SearchFilterModel? = null) {
            context.startActivity(Intent(context, SearchActivity::class.java).also { intent ->
                searchFilterModel?.let {
                    intent.putExtra(ADVANCE_SEARCH_INTENT_KEY, it)
                }
            })
        }
    }

    private val tagAdapter: Adapter.Builder
        get() {
            return Adapter.builder(this)
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

    private val viewModel by viewModel<BrowseActivityViewModel>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SearchActivityLayoutBinding {
        return SearchActivityLayoutBinding.inflate(inflater)
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpTheme()
        setUpPersistentSearchView()
        setUpListener()
        setUpViews()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.browseFragmentContainer,
                SearchFragment(),
                ADVANCE_SEARCH_FRAGMENT_TAG
            ).commitNow()

            if (intent.hasExtra(ADVANCE_SEARCH_INTENT_KEY)) {
                intent.getParcelableExtra<SearchFilterModel>(ADVANCE_SEARCH_INTENT_KEY)?.let {
                    binding.browseFilterNavView.setFilter(it)
                }
            }
        }
    }

    private fun getAdvanceSearchFragment() = supportFragmentManager.findFragmentByTag(
        ADVANCE_SEARCH_FRAGMENT_TAG
    ) as? SearchFragment

    private fun setUpViews() {
        viewModel.tagTagFields.takeIf { it.isNotEmpty() }?.let {
            invalidateTagFilter(it)
        }
        viewModel.genreTagFields.takeIf { it.isNotEmpty() }?.let {
            invalidateGenreFilter(it)
        }
        viewModel.streamTagFields.takeIf { it.isNotEmpty() }?.let {
            invalidateStreamFilter(it)
        }
    }

    private fun setUpPersistentSearchView() {
        with(binding.persistentSearchView) {
            setLeftButtonDrawable(backDrawable)
            setRightButtonDrawable(filterDrawable)
            showRightButton()

            setOnSearchConfirmedListener { _, query ->
                if (query.isEmpty()) {
                    binding.persistentSearchView.collapse(false)
                    return@setOnSearchConfirmedListener
                }
                dataProvider.addToSearchHistory(query)
                viewModel.searchQuery = query
                viewModel.searchNow()
                binding.persistentSearchView.collapse(false)
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
        with(binding.persistentSearchView) {
            DynamicTheme.getInstance().get().primaryColor.let {
                setCardBackgroundColor(it)
            }

            ResourcesCompat.getFont(this@SearchActivity, R.font.cabin_regular)?.let {
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
            binding.rootDrawerLayout.setBackgroundColor(it)
            binding.advanceSearchCoordinatorLayout.setBackgroundColor(it)
        }
        statusBarColor = statusBarColor

    }


    private fun setUpListener() {
        with(binding.persistentSearchView) {
            setOnLeftBtnClickListener {
                if (isExpanded) {
                    collapse(true)
                } else
                    finish()
            }
            setOnRightBtnClickListener {
                binding.rootDrawerLayout.openDrawer(GravityCompat.END)
            }
        }
        with(binding.browseFilterNavView) {
            setNavigationCallbackListener(this@SearchActivity)
        }
        binding.rootDrawerLayout.addDrawerListener(binding.browseFilterNavView.drawerListener)

        dataProvider.onDataChanged {
            binding.persistentSearchView.setSuggestions(
                SuggestionCreationUtil.asRecentSearchSuggestions(dataProvider.getAllHistory()),
                false
            )
        }


        viewModel.searchLiveData.observe(this) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let {
                        if (it.searchNow) {
                            it.searchNow = false
                            search()
                        }
                    }
                }
                else -> {}
            }
        }

        /**problem with transition
         * {@link https://github.com/facebook/fresco/issues/1445}*/
        ActivityCompat.setExitSharedElementCallback(this, object : SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: List<String?>?,
                sharedElements: List<View>,
                sharedElementSnapshots: List<View?>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                if (sharedElements.isEmpty()) {
                    return
                }
                for (view in sharedElements) {
                    if (view is SimpleDraweeView) {
                        view.drawable.setVisible(true, true)
                    }
                }
            }
        })
    }

    @Subscribe
    fun onTagEvent(event: TagEvent){
        when(event.tagType){
            MediaTagFilterTypes.TAGS -> invalidateTagFilter(event.tagFields)
            MediaTagFilterTypes.GENRES -> invalidateGenreFilter(event.tagFields)
            MediaTagFilterTypes.STREAMING_ON -> invalidateStreamFilter(event.tagFields)
            else->{}
        }
    }

    private fun invalidateStreamFilter(list: List<TagField>) {
        viewModel.streamTagFields = list.toMutableList()
        binding.browseFilterNavView.buildStreamAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateGenreFilter(list: List<TagField>) {
        viewModel.genreTagFields = list.toMutableList()
        binding.browseFilterNavView.buildGenreAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateTagFilter(list: List<TagField>) {
        viewModel.tagTagFields = list.toMutableList()
        binding.browseFilterNavView.buildTagAdapter(
            tagAdapter,
            list
        )
    }

    override fun onBackPressed() {
        when {
            binding.rootDrawerLayout.isDrawerOpen(GravityCompat.END) -> {
                binding.rootDrawerLayout.closeDrawer(GravityCompat.END)
            }
            binding.persistentSearchView.isExpanded -> {
                binding.persistentSearchView.collapse()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }


    override fun openTagChooserDialog(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                tagType,
                tags
            )
        ).show(supportFragmentManager, TagChooserDialogFragment::class.java.simpleName)
    }

    override fun onTagAdd(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        when(tagType){
            MediaTagFilterTypes.TAGS -> {
                viewModel.tagTagFields = tags.toMutableList()
            }
            MediaTagFilterTypes.GENRES -> {
                viewModel.genreTagFields = tags.toMutableList()
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                viewModel.streamTagFields = tags.toMutableList()
            }
            else->{}

        }

    }

    override fun onTagRemoved(tag: String, tagType: MediaTagFilterTypes) {
        when(tagType){
            MediaTagFilterTypes.TAGS -> {
                viewModel.tagTagFields.removeAll { it.tag == tag }
            }
            MediaTagFilterTypes.GENRES -> {
                viewModel.genreTagFields.removeAll { it.tag == tag }
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                viewModel.streamTagFields.removeAll { it.tag == tag }
            }
            else->{}

        }
    }

    override fun updateTags(tagType: MediaTagFilterTypes) {
        when(tagType){
            MediaTagFilterTypes.TAGS -> {
                binding.browseFilterNavView.invalidateTagAdapter(tagAdapter)
            }
            MediaTagFilterTypes.GENRES -> {
                binding. browseFilterNavView.invalidateGenreAdapter(tagAdapter)
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                binding.browseFilterNavView.invalidateStreamAdapter(tagAdapter)
            }
            else->{}

        }
    }


    /**
     * Called by advance search filter nav view to fill search box
     * */
    override fun getQuery(): String {
        return viewModel.searchQuery
    }


    override fun applyFilter() {
        if (binding.rootDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.rootDrawerLayout.closeDrawer(GravityCompat.END)
        }
        binding.browseFilterNavView.getFilter().let {
            viewModel.searchQuery = it.query!!
            binding.persistentSearchView.inputQuery = viewModel.searchQuery
            BrowseFilterAppliedEvent(it).postSticky
        }
    }

    private fun search() {
        binding.browseFilterNavView.getFilter().let {
            it.query = viewModel.searchQuery
            BrowseFilterAppliedEvent(it).postSticky
        }
    }

}