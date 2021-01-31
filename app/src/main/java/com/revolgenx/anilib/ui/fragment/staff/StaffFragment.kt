package com.revolgenx.anilib.ui.fragment.staff

import android.os.Bundle
import android.view.*
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.field.staff.StaffField
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.data.meta.StaffMeta
import com.revolgenx.anilib.data.model.StaffModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.StaffFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import com.revolgenx.anilib.ui.viewmodel.staff.StaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffFragment : BaseLayoutFragment<StaffFragmentLayoutBinding>() {
    companion object {
        const val STAFF_META_KEY = "staff_meta_key"
    }

    private val viewModel by viewModel<StaffViewModel>()


    private var staffModel: StaffModel? = null
    private lateinit var staffMeta: StaffMeta
    private val staffField by lazy {
        StaffField().also {
            it.staffId = staffMeta.staffId
        }
    }

    private val toggleFavouriteField by lazy {
        ToggleFavouriteField().also {
            it.staffId = staffMeta.staffId
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.staff_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.staff_share_menu -> {
                staffModel?.siteUrl?.let {
                    requireContext().openLink(it)
                } ?: makeToast(R.string.invalid)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): StaffFragmentLayoutBinding {
        return StaffFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.classLoader = StaffMeta::class.java.classLoader
        staffMeta = arguments?.getParcelable(STAFF_META_KEY) ?: return
        binding.staffIv.setImageURI(staffMeta.staffUrl)
        val statusLayout = binding.resourceStatusLayout
        viewModel.staffInfoLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    statusLayout.resourceStatusContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    binding.updateView(res.data!!)
                }
                Status.ERROR -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
            }
        }

        viewModel.toggleStaffFavLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    binding.staffFavIv.showLoading(false)
                    if (res.data == true) {
                        staffModel?.isFavourite = staffModel?.isFavourite?.not() ?: false
                        binding.staffFavIv.setImageResource(
                            if (staffModel?.isFavourite == true) {
                                R.drawable.ic_favourite
                            } else {
                                R.drawable.ic_not_favourite
                            }
                        )
                    }
                }
                Status.ERROR -> {
                    binding.staffFavIv.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ads_ic_error)
                }
                Status.LOADING -> {
                    binding.staffFavIv.showLoading(true)
                }
            }
        }
        initListener()

        if (savedInstanceState == null) {
            viewModel.getStaffInfo(staffField)
        }
    }

    private fun initListener() {
        binding.staffFavLayout.setOnClickListener {
            if (requireContext().loggedIn()) {
                viewModel.toggleCharacterFav(toggleFavouriteField)
            } else {
                makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }

    private fun StaffFragmentLayoutBinding.updateView(item: StaffModel) {
        staffModel = item
        staffNameTv.text = item.staffName?.full
        staffFavCountIv.text = item.favourites?.toLong()?.prettyNumberFormat()

        if (staffMeta.staffUrl == null) {
            staffIv.setImageURI(staffModel?.staffImage?.image)
        }

        item.staffName?.native?.let {
            nativeNameTv.subtitle = it
        } ?: let {
            nativeNameTv.visibility = View.GONE
        }

        item.staffName?.alternative?.let {
            alternativeNameTv.subtitle = it.joinToString()
        } ?: let {
            alternativeNameTv.visibility = View.GONE
        }

        item.language?.let {
            languageTv.subtitle =
                requireContext().resources.getStringArray(R.array.staff_language)[it]
        } ?: let {
            languageTv.visibility = View.GONE
        }

        if (item.isFavourite) {
            staffFavIv.setImageResource(R.drawable.ic_favourite)
        }

        MarkwonImpl.createHtmlInstance(requireContext()).setMarkdown(staffDescriptionTv, item.description ?: "")
    }
}