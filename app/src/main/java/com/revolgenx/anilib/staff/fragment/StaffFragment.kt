package com.revolgenx.anilib.staff.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.common.event.OpenImageEvent
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.common.viewmodel.getViewModelOwner
import com.revolgenx.anilib.databinding.StaffFragmentLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.staff.viewmodel.StaffContainerViewModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.staff.viewmodel.StaffViewModel
import com.revolgenx.anilib.util.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class StaffFragment : BaseLayoutFragment<StaffFragmentLayoutBinding>() {
    companion object {
        private const val STAFF_ID_KEY = "STAFF_ID_KEY"
        fun newInstance(staffId: Int) = StaffFragment().also {
            it.arguments = bundleOf(STAFF_ID_KEY to staffId)
        }
    }

    private val staffId get() = arguments?.getInt(STAFF_ID_KEY)
    private val viewModel by viewModel<StaffViewModel>()
    private val staffShareVM by viewModel<StaffContainerViewModel>(owner = getViewModelOwner())
    private val staffModel get() = viewModel.staffModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.staff_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.staff_open_in_browser_menu -> {
                openLink(staffModel?.siteUrl)
                true
            }
            R.id.staff_share_menu -> {
                shareText(staffModel?.siteUrl)
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.staffField.staffId = staffId ?: return
        viewModel.staffToggleField.staffId = staffId
        val statusLayout = binding.resourceStatusLayout
        viewModel.staffInfoLiveData.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    statusLayout.resourceStatusContainer.visibility = View.GONE
                    statusLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    statusLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    staffShareVM.staffLink = staffModel?.siteUrl
                    binding.updateView()
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
                    binding.updateFavourite()
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
        binding.initListener()
        viewModel.getStaffInfo(viewModel.staffField)
    }

    private fun StaffFragmentLayoutBinding.initListener() {
        staffNameTv.setOnLongClickListener {
            requireContext().copyToClipBoard(staffNameTv.text?.toString())
            true
        }

        staffAlternateNameTv.setOnLongClickListener {
            requireContext().copyToClipBoard(staffAlternateNameTv.text?.toString())
            true
        }
        staffFavIv.setOnClickListener {
            loginContinue {
                viewModel.toggleCharacterFav(viewModel.staffToggleField)
            }
        }
    }

    private fun StaffFragmentLayoutBinding.updateView() {
        val item = staffModel ?: return
        item.name?.let {
            staffNameTv.text = it.full
            val alternatives = it.alternative?.joinToString(", ") ?: ""
            val hasAlternatives = alternatives.isNotBlank()
            val alternativeNames = (it.native?.plus(if (hasAlternatives) ", " else "") ?: "") + alternatives
            staffAlternateNameTv.text = alternativeNames
        }

        staffFavCountTv.text = item.favourites?.toLong()?.prettyNumberFormat()
        staffIv.setImageURI(staffModel?.image?.image)
        staffIv.setOnClickListener {
            staffModel?.image?.image?.let {
                OpenImageEvent(it).postEvent
            }
        }

        updateFavourite()

        val staffDescription = generateStaffInfo(item) + item.description
        AlMarkwonFactory.getMarkwon()
            .setMarkdown(staffDescriptionTv, staffDescription)
    }

    private fun StaffFragmentLayoutBinding.updateFavourite() {
        val favResource = if (staffModel?.isFavourite == true) R.drawable.ic_favourite else R.drawable.ic_not_favourite
        staffFavIv.setImageResource(favResource)
    }


    private fun generateStaffInfo(model: StaffModel): String {
        var generalInfo = ""
        model.dateOfBirth?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>${getString(R.string.birthday)}:</b> $month $day, $year \n"
        }
        model.dateOfDeath?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>${getString(R.string.death)}:</b> $month $day, $year \n"
        }
        model.age?.let {
            generalInfo += "<b>${getString(R.string.age)}:</b> $it \n"
        }
        model.gender?.let {
            generalInfo += "<b>${getString(R.string.gender)}:</b> $it \n"
        }
        model.bloodType?.let {
            generalInfo += "<b>${getString(R.string.blood_type)}:</b> $it \n"
        }
        model.yearsActive?.let {
            val firstDate = it.getOrNull(0)
            val lastDate = it.getOrNull(1) ?: getString(R.string.present)
            generalInfo += "<b>${getString(R.string.years_active)}:</b> $firstDate-$lastDate \n"
        }

        model.homeTown?.let {
            generalInfo += "<b>${getString(R.string.hometown)}:</b> $it \n \n"
        }

        return generalInfo
    }
}