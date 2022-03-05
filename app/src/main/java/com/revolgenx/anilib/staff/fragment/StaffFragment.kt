package com.revolgenx.anilib.staff.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.StaffFragmentLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import com.revolgenx.anilib.staff.viewmodel.StaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffFragment : BaseLayoutFragment<StaffFragmentLayoutBinding>() {
    companion object {
        private const val STAFF_ID_KEY = "STAFF_ID_KEY"
        fun newInstance(staffId: Int) = StaffFragment().also {
            it.arguments = bundleOf(STAFF_ID_KEY to staffId)
        }
    }

    private val staffId get() = arguments?.getInt(STAFF_ID_KEY)


    private val viewModel by viewModel<StaffViewModel>()

    private var staffModel: StaffModel? = null

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
        viewModel.staffField.staffId = staffId ?: return
        viewModel.staffToggleField.staffId = staffId
        val statusLayout = binding.resourceStatusLayout
        viewModel.staffInfoLiveData.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    statusLayout.resourceStatusContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    binding.updateView(res.data!!)
                }
                is Resource.Error -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    statusLayout.resourceStatusContainer.visibility = View.VISIBLE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
            }
        }

        viewModel.toggleStaffFavLiveData.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
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
                is Resource.Error -> {
                    binding.staffFavIv.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ic_error)
                }
                is Resource.Loading -> {
                    binding.staffFavIv.showLoading(true)
                }
            }
        }
        initListener()

        if (savedInstanceState == null) {
            viewModel.getStaffInfo(viewModel.staffField)
        }
    }

    private fun initListener() {
        binding.staffFavLayout.setOnClickListener {
            if (requireContext().loggedIn()) {
                viewModel.toggleCharacterFav(viewModel.staffToggleField)
            } else {
                makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
        }
    }

    private fun StaffFragmentLayoutBinding.updateView(item: StaffModel) {
        staffModel = item
        updateToolbar()
        staffNameTv.text = item.name?.full
        staffFavCountIv.text = item.favourites?.toLong()?.prettyNumberFormat()
        staffIv.setImageURI(staffModel?.image?.image)

        item.name?.native?.let {
            nativeNameTv.subtitle = it
        } ?: let {
            nativeNameTv.visibility = View.GONE
        }

        item.name?.alternative?.let {
            alternativeNameTv.subtitle = it.joinToString()
        } ?: let {
            alternativeNameTv.visibility = View.GONE
        }


        item.languageV2?.let {
            languageTv.subtitle = it
        } ?: let {
            languageTv.visibility = View.GONE
        }

        if (item.isFavourite) {
            staffFavIv.setImageResource(R.drawable.ic_favourite)
        }

        AlMarkwonFactory.getMarkwon()
            .setMarkdown(staffDescriptionTv, item.description ?: "")
    }

    override fun updateToolbar() {
        val model = staffModel ?: return
        (parentFragment as? StaffContainerFragment)?.let {
            it.updateToolbarTitle(model.name!!.full!!)
            it.updateShareableLink(model.siteUrl)
        }
    }
}