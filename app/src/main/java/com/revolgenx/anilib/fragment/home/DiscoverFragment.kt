package com.revolgenx.anilib.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ExoVideoPlayerActivity
import com.revolgenx.anilib.activity.TestActivity
import com.revolgenx.anilib.dialog.MediaListFilterDialog
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.event.BrowseEvent
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.util.makeSnakeBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.discover_fragment_layout.*
import kotlinx.android.synthetic.main.overview_recommendation_presnter_layout.*
import timber.log.Timber

class DiscoverFragment : BaseFragment() {
    val makrdowntest =
        "<img width='100%'  src='https://dream-wonderland.com/blog/wp-content/uploads/2017/08/Ohys-Raws-Aikatsu-Stars-69-TX-1280x720-x264-AAC.mp4_20170820_005650.721.jpg'>"

    val test =
        "<img width='1080'  src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286'>"

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
                BrowseEvent().postEvent
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loading.color = DynamicTheme.getInstance().get().accentColor

        mediaRecommendationDislikeIv.setOnClickListener {
            //            MediaListFilterDialog.newInstance(mediaListFilterField).show(childFragmentManager, "media_filter_dialog")

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
        mediaDescriptionTv.setOnClickListener {
            startActivity(Intent(requireContext(), TestActivity::class.java))
        }
        openVideoPlayer.setOnClickListener {
            startActivity(Intent(requireContext(), ExoVideoPlayerActivity::class.java))
        }

        markdownTest.post {
            MarkwonImpl.createHtmlInstance(requireContext()).setMarkdown(markdownTest, test)
        }
    }


    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}
