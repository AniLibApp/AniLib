package com.revolgenx.anilib.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.appbar.AppBarLayout
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.meta.ListEditorMeta
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.model.ListEditorMediaModel
import com.revolgenx.anilib.preference.userScoreFormat
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
    private var model: ListEditorMediaModel? = null

    private var circularProgressDrawable: CircularProgressDrawable? = null

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
        retainInstance = true

        //bug or smth wont parcelize without class loader
        arguments?.classLoader = ListEditorMeta::class.java.classLoader
        mediaMeta = arguments?.getParcelable(LIST_EDITOR_META_KEY) ?: return

        setToolbarTheme()
        showMetaViews()
        initListener()

        circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable!!.start()

        viewModel.mediaQueryLiveData.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    listResourceEditorContainer.visibility = View.GONE
                    listEditorContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    fetched = true
                    model = resource.data
                    updateView()
                    invalidateOptionMenu()
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

        if (savedInstanceState == null)
            viewModel.queryMediaList(mediaMeta.id)
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
        listDeleteButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.red))
        listFavCardView.corner = DynamicUnitUtils.convertDpToPixels(8f).toFloat()
        listDeleteCardView.corner = DynamicUnitUtils.convertDpToPixels(8f).toFloat()
        listSaveCardView.corner = DynamicUnitUtils.convertDpToPixels(8f).toFloat()
        listEditorScoreLayout.scoreFormatType = context!!.userScoreFormat()
        val spinnerItems = mutableListOf<DynamicSpinnerItem>()
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_watching
                ), getString(R.string.watching)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_planning
                ), getString(R.string.planning)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_completed
                ), getString(R.string.completed)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_dropped
                ), getString(R.string.dropped)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_paused_filled
                ), getString(R.string.paused)
            )
        )
        spinnerItems.add(
            DynamicSpinnerItem(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.ic_rewatching
                ), getString(R.string.rewatching)
            )
        )

        statusSpinner.adapter = DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, spinnerItems
        )
    }

    private fun initListener() {
        appbarLayout.addOnOffsetChangedListener(offSetChangeListener)

        listSaveCardView.setOnClickListener {

        }

        listDeleteCardView.setOnClickListener {

        }

        listFavCardView.setOnClickListener {

        }
    }

    private fun updateView() {
        if (model == null) return
        statusSpinner.setSelection(model!!.status)
        listEditorScoreLayout.mediaListScore = model!!.score
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (state == EXPANDED) {
            if (model == null) {
                listDeleteCardView.visibility = View.GONE
            } else {
                listDeleteCardView.visibility = View.VISIBLE
                if (model!!.isFavourite) {
                    listFavButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            context!!,
                            R.drawable.ic_favorite
                        )
                    )
                }
            }
            return
        }

        if (fetched) {
            inflater.inflate(R.menu.list_editor_menu, menu)

            if (model == null) {
                menu.findItem(R.id.listDeleteMenu).isVisible = false
                listDeleteCardView.visibility = View.GONE
            } else {
                if (model!!.isFavourite) {
                    val drawable = AppCompatDrawableManager.get()
                        .getDrawable(context!!, R.drawable.ic_favorite)
                    menu.findItem(R.id.listFavMenu).icon = drawable
                }
                listDeleteCardView.visibility = View.VISIBLE
            }
        }
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
        circularProgressDrawable?.stop()
        super.onDestroy()
    }
}