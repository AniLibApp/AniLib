package com.revolgenx.anilib.ui.fragment.about

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.fragment.DynamicViewPagerFragment
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment


class AboutFragment : DynamicViewPagerFragment() {

    companion object {

        /**
         * Returns the new instance of this fragment.
         *
         * @param page The default selected page.
         *
         * @return The new instance of [AboutFragment].
         */
        fun newInstance(page: Int): androidx.fragment.app.Fragment {
            val fragment = AboutFragment()
            fragment.arguments = bundleOf(ADS_ARGS_VIEW_PAGER_PAGE to page)
            return fragment
        }
    }

    override fun getSubtitle(): CharSequence? {
        // Set subtitle for the app compat activity.
        return DynamicPackageUtils.getAppVersion(requireContext())
    }


    override fun getTitles(): List<String> {
        // Initialize an empty string array for tab titles.
        val titles = ArrayList<String>()

        titles.add(getString(R.string.ads_menu_info))
        titles.add(getString(R.string.ads_licenses))
        titles.add(getString(R.string.ads_translators))

        // Return all the added tab titles.
        return titles
    }

    override fun getPages(): List<androidx.fragment.app.Fragment> {
        // Initialize an empty fragment array for view pages pages.
        val pages = ArrayList<androidx.fragment.app.Fragment>()

        pages.add(AppInfoFragment())
        pages.add(LicensesFragment())
        pages.add(TranslatorsFragment())

        // Return all the added fragments.
        return pages
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Scroll toolbar for this fragment.
        dynamicActivity.setToolbarLayoutFlags(
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        )

        // Select current page from the bundle arguments.
        if (arguments != null && requireArguments().containsKey(
                ADS_ARGS_VIEW_PAGER_PAGE
            )
        ) {
            setPage(requireArguments().getInt(ADS_ARGS_VIEW_PAGER_PAGE))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Remove tab layout from the header.
        dynamicActivity.addHeader(null, true)
    }
}
