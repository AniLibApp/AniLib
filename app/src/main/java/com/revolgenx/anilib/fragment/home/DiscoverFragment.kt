package com.revolgenx.anilib.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.BrowseActivity
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.makeSnakeBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.discover_fragment_layout.*
import kotlinx.android.synthetic.main.overview_recommendation_presnter_layout.*

class DiscoverFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment_layout, container, false)
    }


    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_editor_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listFavMenu -> {
                activity?.drawerLayout?.openDrawer(activity?.mainBrowseFilterNavView!!, true)
                true
            }
            R.id.listDeleteMenu -> {
                BrowseActivity.openActivity(requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loading.color = DynamicTheme.getInstance().get().accentColor

        mediaRecommendationDislikeTv.setOnClickListener {
            it.checkLoggedIn()
        }

        listPrivateButton.setOnClickListener {
            TagChooserDialogFragment.newInstance(
                TagChooserField(
                    "header",
                    listOf(TagField("genre", true), TagField("help", false))
                )
            ).show(childFragmentManager, "tag")
        }
        testplusminus.visibility = View.VISIBLE
    }


    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}
