package com.revolgenx.anilib.fragment.studio

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.meta.StudioMeta
import com.revolgenx.anilib.field.ToggleFavouriteField
import com.revolgenx.anilib.field.studio.StudioField
import com.revolgenx.anilib.field.studio.StudioMediaField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.presenter.StudioMediaPresenter
import com.revolgenx.anilib.model.studio.StudioMediaModel
import com.revolgenx.anilib.model.studio.StudioModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.repository.util.Status.*
import com.revolgenx.anilib.util.makeSnakeBar
import com.revolgenx.anilib.util.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.viewmodel.StudioViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import kotlinx.android.synthetic.main.studio_fragment_layout.*
import kotlinx.android.synthetic.main.studio_fragment_layout.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudioFragment : BasePresenterFragment<StudioMediaModel>() {

    companion object {
        const val STUDIO_META_KEY = "studio_meta_key"
    }

    override val basePresenter: Presenter<StudioMediaModel>
        get() = StudioMediaPresenter(
            requireContext()
        )
    override val baseSource: Source<StudioMediaModel>
        get() = viewModel.source ?: createSource()

    private lateinit var studioMeta: StudioMeta

    private val studioMediaField by lazy {
        StudioMediaField().also {
            it.studioId = studioMeta.studioId
        }
    }

    private var studioModel: StudioModel? = null

    private val toggleFavouriteField: ToggleFavouriteField by lazy {
        ToggleFavouriteField().also {
            it.studioId = studioMeta.studioId
        }
    }

    private val studioField by lazy {
        StudioField().also {
            it.studioId = studioMeta.studioId
        }
    }

    private val viewModel by viewModel<StudioViewModel>()

    override fun createSource(): Source<StudioMediaModel> {
        return viewModel.createSource(studioMediaField)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        val nV = inflater.inflate(R.layout.studio_fragment_layout, container, false)

        nV.studioFragmentContainerLayout.addView(v)
        return nV
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(dynamicToolbar)
            it.supportActionBar!!.title = getString(R.string.studio)
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        layoutManager = FlexboxLayoutManager(context).also { manager ->
            manager.justifyContent = JustifyContent.SPACE_EVENLY
            manager.alignItems = AlignItems.CENTER
        }
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = StudioMeta::class.java.classLoader
        studioMeta = arguments?.getParcelable(STUDIO_META_KEY) ?: return
        super.onActivityCreated(savedInstanceState)
        viewModel.studioInfoLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    resourceStatusContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    updateView(res.data!!)
                }
                ERROR -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                }
                LOADING -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
            }
        }

        viewModel.toggleFavouriteLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    studioFavIv.showLoading(false)
                    if (res.data == true) {
                        studioModel?.isFavourite = studioModel?.isFavourite?.not() ?: false
                        studioFavIv.setImageResource(
                            if (studioModel?.isFavourite == true) {
                                R.drawable.ic_favorite
                            } else {
                                R.drawable.ic_not_favourite
                            }
                        )
                    }
                }
                ERROR -> {
                    studioFavIv.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ads_ic_error)
                }
                LOADING -> {
                    studioFavIv.showLoading(true)
                }
            }
        }

        initListener()

        if (savedInstanceState == null) {
            viewModel.getStudioInfo(studioField)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.studio_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.studio_share_menu -> {
                studioModel?.siteUrl?.let {
                    requireContext().openLink(it)
                } ?: makeToast(R.string.invalid)
                true
            }
            android.R.id.home -> {
                finishActivity()
                true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }

    private fun initListener() {
        studioFavIv.setOnClickListener {
            if (requireContext().loggedIn()) {
                viewModel.toggleStudioFav(toggleFavouriteField)
            } else {
                studioCoordinatorLayout.makeSnakeBar(R.string.please_log_in)
            }
        }
    }

    private fun updateView(item: StudioModel) {
        studioModel = item
        studioNameTv.text = item.studioName
        studioFavCountTv.text = item.favourites?.toString().naText()
        if (item.isFavourite) {
            studioFavIv.setImageResource(R.drawable.ic_favorite)
        }
    }

}