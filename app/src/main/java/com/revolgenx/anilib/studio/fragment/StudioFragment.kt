package com.revolgenx.anilib.studio.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.studio.data.model.StudioMediaModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.StudioFragmentLayoutBinding
import com.revolgenx.anilib.studio.presenter.StudioMediaPresenter
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.studio.viewmodel.StudioViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StudioFragment : BasePresenterFragment<StudioMediaModel>() {

    companion object {
        private const val STUDIO_ID_KEY = "STUDIO_ID_KEY"
        fun newInstance(studioId: Int) = StudioFragment().also {
            it.arguments = bundleOf(STUDIO_ID_KEY to studioId)
        }
    }

    private val studioId get() = arguments?.getInt(STUDIO_ID_KEY)

    override val basePresenter: Presenter<StudioMediaModel>
        get() = StudioMediaPresenter(
            requireContext()
        )

    override val baseSource: Source<StudioMediaModel>
        get() = viewModel.source ?: createSource()

    private var studioModel: StudioModel? = null

    private lateinit var studioBinding: StudioFragmentLayoutBinding

    private val viewModel by viewModel<StudioViewModel>()


    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.studio_fragment_menu
    override val titleRes: Int = R.string.studio

    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3


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
        studioBinding.root.setBackgroundColor(dynamicBackgroundColor)
        studioBinding.studioFragmentContainerLayout.addView(v)
        return studioBinding.root
    }


    override fun getBaseToolbar(): Toolbar {
        return studioBinding.studioToolbar.dynamicToolbar
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.field.studioId = studioId ?: return
        viewModel.studioField.studioId = studioId
        viewModel.toggleFavouriteField.studioId = studioId

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

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
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
                viewModel.toggleStudioFav(viewModel.toggleFavouriteField)
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