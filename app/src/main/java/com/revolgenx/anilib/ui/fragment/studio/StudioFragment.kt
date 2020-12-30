package com.revolgenx.anilib.ui.fragment.studio

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.StudioMeta
import com.revolgenx.anilib.data.model.studio.StudioMediaModel
import com.revolgenx.anilib.data.model.studio.StudioModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.StudioFragmentLayoutBinding
import com.revolgenx.anilib.ui.presenter.StudioMediaPresenter
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.ui.viewmodel.StudioViewModel
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

    private var studioModel: StudioModel? = null

    private val toggleFavouriteField: ToggleFavouriteField by lazy {
        ToggleFavouriteField().also {
            it.studioId = studioMeta.studioId
        }
    }

    private lateinit var studioBinding:StudioFragmentLayoutBinding

    private val viewModel by viewModel<StudioViewModel>()

    override fun createSource(): Source<StudioMediaModel> {
        return viewModel.createSource()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        studioBinding = StudioFragmentLayoutBinding.inflate(inflater, container, false)
        studioBinding.studioFragmentContainerLayout.addView(v)

        return studioBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(studioBinding.studioToolbar.dynamicToolbar)
            it.supportActionBar!!.title = getString(R.string.studio)
            it.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
        layoutManager = GridLayoutManager(
            this.context,
            span
        ).also {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter?.getItemViewType(position) == 0) {
                        1
                    } else {
                        span
                    }
                }
            }
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
        viewModel.field.studioId = studioMeta.studioId
        viewModel.studioField.studioId = studioMeta.studioId

        val statusLayout = studioBinding.resourceStatusLayout
        viewModel.studioInfoLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    statusLayout.resourceStatusContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    updateView(res.data!!)
                }
                ERROR -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                LOADING -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
            }
        }

        viewModel.toggleFavouriteLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    studioBinding.studioFavIv.showLoading(false)
                    if (res.data == true) {
                        studioModel?.isFavourite = studioModel?.isFavourite?.not() ?: false
                        studioBinding.studioFavIv.setImageResource(
                            if (studioModel?.isFavourite == true) {
                                R.drawable.ic_favourite
                            } else {
                                R.drawable.ic_not_favourite
                            }
                        )
                    }
                }
                ERROR -> {
                    studioBinding.studioFavIv.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ads_ic_error)
                }
                LOADING -> {
                    studioBinding.studioFavIv.showLoading(true)
                }
            }
        }

        initListener()

        if (savedInstanceState == null) {
            viewModel.getStudioInfo()
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
        studioBinding.studioFavIv.setOnClickListener {
            if (requireContext().loggedIn()) {
                viewModel.toggleStudioFav(toggleFavouriteField)
            } else {
                makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }
    }

    private fun updateView(item: StudioModel) {
        studioModel = item
        studioBinding.studioNameTv.text = item.studioName
        studioBinding.studioFavCountTv.text = item.favourites?.toString().naText()
        if (item.isFavourite) {
            studioBinding.studioFavIv.setImageResource(R.drawable.ic_favourite)
        }
    }

}