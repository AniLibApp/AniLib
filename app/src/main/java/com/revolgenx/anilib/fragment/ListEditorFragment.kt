package com.revolgenx.anilib.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.meta.ListEditorMeta
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.viewmodel.MediaListEditorViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.list_editor_fragment_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class ListEditorFragment : BaseFragment() {

    companion object {
        const val LIST_EDITOR_META_KEY = "list_editor_meta_key"
        const val COLLAPSED = 0
        const val EXPANDED = 1
    }

    private var state = COLLAPSED //collapsed
    private var fetched = false
    private lateinit var mediaMeta: ListEditorMeta
    private val viewModel by viewModel<MediaListEditorViewModel>()

    private val offSetChangeListener by lazy {
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                state = EXPANDED
                (activity as AppCompatActivity).invalidateOptionsMenu()
            } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                state = COLLAPSED
                (activity as AppCompatActivity).invalidateOptionsMenu()
            }
        }
    }

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

        //bug or smth wont parcelize without class loader
        arguments?.classLoader = ListEditorMeta::class.java.classLoader
        mediaMeta = arguments?.getParcelable(LIST_EDITOR_META_KEY) ?: return

        setToolbarTheme()
        showMetaViews()
        initListener()

        viewModel.queryMediaList(mediaMeta.id).observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    listResourceEditorContainer.visibility = View.GONE
                    listEditorContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    fetched = true

                    if (resource.data == null) {

                    } else {

                    }
                }
                Status.ERROR -> {
                    listResourceEditorContainer.visibility = View.VISIBLE
                    listEditorContainer.visibility = View.GONE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    fetched = false
                }
                Status.LOADING -> {
                    listResourceEditorContainer.visibility = View.VISIBLE
                    listEditorContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    fetched = false
                }
            }
        })

    }

    private fun showMetaViews() {
        if (!::mediaMeta.isInitialized) return
        (activity as AppCompatActivity).also { act ->
            act.setSupportActionBar(listEditorToolbar)
            act.supportActionBar!!.title = mediaMeta.title
            act.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        listEditorCoverImage.setImageURI(mediaMeta.coverImage)
        listEditorBannerImage.setImageURI(mediaMeta.bannerImage)
        listDeletButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.red))
        listFavCardView.corner = DynamicUnitUtils.convertDpToPixels(8f).toFloat()
        listDeleteCardView.corner = DynamicUnitUtils.convertDpToPixels(8f).toFloat()
        listSaveCardView.corner = DynamicUnitUtils.convertDpToPixels(8f).toFloat()
    }

    private fun initListener() {
        appbarLayout.addOnOffsetChangedListener(offSetChangeListener)
    }

    private fun updateView() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (state == EXPANDED) return
        if (fetched)
            inflater.inflate(R.menu.list_editor_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.listSaveMenu -> {

                true
            }
            R.id.listDeleteMenu -> {

                true
            }
            R.id.listFavMenu -> {

                true
            }
            android.R.id.home -> {
                finishActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setToolbarTheme() {
        listEditorCollapsingToolbar.setStatusBarScrimColor(DynamicTheme.getInstance().get().primaryColorDark)
        listEditorCollapsingToolbar.setContentScrimColor(DynamicTheme.getInstance().get().primaryColor)
        listEditorCollapsingToolbar.setCollapsedTitleTextColor(DynamicTheme.getInstance().get().tintPrimaryColor)
    }


    override fun onDestroy() {
        appbarLayout?.removeOnOffsetChangedListener(offSetChangeListener)
        super.onDestroy()
    }
}