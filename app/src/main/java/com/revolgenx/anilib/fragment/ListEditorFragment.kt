package com.revolgenx.anilib.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.meta.ListEditorMeta
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.util.makeToast
import kotlinx.android.synthetic.main.list_editor_fragment_layout.*
import timber.log.Timber
import kotlin.math.abs

class ListEditorFragment : BaseFragment() {

    companion object {
        const val LIST_EDITOR_META_KEY = "list_editor_meta_key"
        const val COLLAPSED = 0
        const val EXPANDED = 1
    }

    private var state = COLLAPSED //collapsed


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_editor_fragment_layout, container, false)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).invalidateOptionsMenu()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.classLoader = ListEditorMeta::class.java.classLoader
        val mediaMeta = arguments?.getParcelable<ListEditorMeta>(LIST_EDITOR_META_KEY) ?: return
        val mediaId = mediaMeta.id
        val mediaCoverUrl = mediaMeta.coverImage
        val mediaBannerUrl = mediaMeta.bannerImage
        val mediaTitle = mediaMeta.title


        listEditorCollapsingToolbar.setStatusBarScrimColor(DynamicTheme.getInstance().get().primaryColorDark)
        listEditorCollapsingToolbar.setContentScrimColor(
            DynamicTheme.getInstance().get().primaryColor
        )
        listEditorCollapsingToolbar.setCollapsedTitleTextColor(DynamicTheme.getInstance().get().tintPrimaryColor)
        appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                state = EXPANDED
                (activity as AppCompatActivity).invalidateOptionsMenu()
            } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                state = COLLAPSED
                (activity as AppCompatActivity).invalidateOptionsMenu()
            }
        })


        (activity as AppCompatActivity).also { act ->
            act.setSupportActionBar(listEditorToolbar)
            act.supportActionBar!!.title = mediaTitle
            act.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        listEditorCoverImage.setImageURI(mediaCoverUrl)
        listEditorBannerImage.setImageURI(mediaBannerUrl)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (state == EXPANDED) return
        inflater.inflate(R.menu.season_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finishActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}