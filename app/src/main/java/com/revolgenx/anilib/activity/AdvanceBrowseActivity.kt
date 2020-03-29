package com.revolgenx.anilib.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.util.makeToast
import com.revolgenx.anilib.view.navigation.AdvanceBrowseNavigationView
import kotlinx.android.synthetic.main.advance_browse_activity_layout.*
import java.util.*

class AdvanceBrowseActivity : DynamicSystemActivity(),
    AdvanceBrowseNavigationView.AdvanceBrowseNavigationCallbackListener {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.advance_browse_activity_layout)
        setUpTheme()
        setUpSearchView()
        setUpListener()
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

            ResourcesCompat.getFont(this@AdvanceBrowseActivity, R.font.open_sans_regular)?.let {
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
        with(navigationSearchView) {
            setOnLeftBtnClickListener {
                finish()
            }
            setOnRightBtnClickListener {
                rootDrawerLayout.openDrawer(GravityCompat.END)
            }
        }
        rootDrawerLayout.addDrawerListener(browseFilterNavigationView.drawerListener)
        with(browseFilterNavigationView) {
            setNavigationCallbackListener(this@AdvanceBrowseActivity)
        }
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

    override fun onGenreAdd() {
        makeToast(msg = "addes")
    }

    override fun onStreamAdd() {
        makeToast(msg = "addes")

    }

    override fun onTagAdd() {
        makeToast(msg = "addes")
    }

    override fun getQuery(): String {
        return query
    }

}