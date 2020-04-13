package com.revolgenx.anilib.fragment.home

import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.core.content.ContextCompat
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

    val youtube = "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>Youtube</p></div>"

    val video =
        "<video muted loop autoplay controls><source src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286' type='video/webm'>https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286</video>"

    val combined = "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
            "\n" +
            "Why do we use it?\n" +
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
            "\n<video muted loop autoplay controls><source src='https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286' type='video/webm'>video###https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286</video>\n" +
            "<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div>\n"
/*https://files.catbox.moe/0zofnv.mp4*/
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
            MarkwonImpl.instanceHtml.setMarkdown(markdownTest, combined)
        }


        val drawable1 = ContextCompat.getDrawable(requireContext(), R.drawable.ic_anilib_anilist)
        val drawable2 = ContextCompat.getDrawable(requireContext(), R.drawable.ads_ic_play)

        val layerDrawable = LayerDrawable(arrayOf(drawable1, drawable2))

        testImageView.setImageDrawable(layerDrawable)
    }


    private fun View.checkLoggedIn(): Boolean {
        val loggedIn = context.loggedIn()
        if (!loggedIn) makeSnakeBar(R.string.please_log_in)
        return loggedIn
    }

}
