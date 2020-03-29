package com.revolgenx.anilib.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.view.navigation.AdvanceSearchFilterNavigationView
import com.revolgenx.anilib.viewmodel.AdvanceSearchViewModel
import kotlinx.android.synthetic.main.advance_search_activity_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class AdvanceSearchActivity : DynamicSystemActivity(),
    AdvanceSearchFilterNavigationView.AdvanceBrowseNavigationCallbackListener,
    TagChooserDialogFragment.OnDoneListener {

    companion object {
        const val GENRE_CHOOSER_DIALOG_TAG = "genre_chooser_tag"
        const val TAG_CHOOSER_DIALOG_TAG = "tag_chooser_tag"
        const val STREAM_CHOOSER_DIALOG_TAG = "stream_chooser_tag"
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

    private val backDrawable: Drawable
        get() = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back)!!.also {
            it.setTint(DynamicTheme.getInstance().get().tintPrimaryColor)
        }

    private val filterDrawable: Drawable
        get() = ContextCompat.getDrawable(context, R.drawable.ic_button_setting)!!.also {
            it.setTint(DynamicTheme.getInstance().get().tintPrimaryColor)
        }

    private var query: String = ""


    private val viewModel by viewModel<AdvanceSearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.advance_search_activity_layout)
        setUpTheme()
        setUpSearchView()
        setUpListener()
        setUpViews()
    }

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

    private fun setUpSearchView() {
        with(navigationSearchView) {
            setLeftButtonDrawable(backDrawable)
            setRightButtonDrawable(filterDrawable)
            showRightButton()
        }
    }

    private fun setUpTheme() {
        with(navigationSearchView) {
            DynamicTheme.getInstance().get().primaryColor.let {
                setCardBackgroundColor(it)
            }

            ResourcesCompat.getFont(this@AdvanceSearchActivity, R.font.open_sans_regular)?.let {
                setQueryTextTypeface(it)
                setSuggestionTextTypeface(it)
            }

            DynamicTheme.getInstance().get().tintSurfaceColor.let {
                setSuggestionSelectedTextColor(it)
                setQueryInputHintColor(it)
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
        with(navigationSearchView) {
            setOnLeftBtnClickListener {
                finish()
            }
            setOnRightBtnClickListener {
                rootDrawerLayout.openDrawer(GravityCompat.END)
            }
        }
        rootDrawerLayout.addDrawerListener(advanceSearchFilterNavView.drawerListener)
        with(advanceSearchFilterNavView) {
            setNavigationCallbackListener(this@AdvanceSearchActivity)
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
            com.otaliastudios.elements.Adapter.builder(this)
                .addSource(Source.fromList(list.filter { it.isTagged }.map { it.tag })),
            list
        )
    }

    private fun invalidateGenreFilter(list: List<TagField>) {
        viewModel.genreTagFields = list
        advanceSearchFilterNavView.buildGenreAdapter(
            com.otaliastudios.elements.Adapter.builder(this)
                .addSource(Source.fromList(list.filter { it.isTagged }.map { it.tag })),
            list
        )
    }

    private fun invalidateTagFilter(list: List<TagField>) {
        viewModel.tagTagFields = list
        advanceSearchFilterNavView.buildTagAdapter(
            com.otaliastudios.elements.Adapter.builder(this)
                .addSource(Source.fromList(list.filter { it.isTagged }.map { it.tag })),
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
        return query
    }

}