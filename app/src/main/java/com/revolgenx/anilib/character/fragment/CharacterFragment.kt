package com.revolgenx.anilib.character.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.character.viewmodel.CharacterContainerViewModel
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.CharacterFragmentLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.prettyNumberFormat
import com.revolgenx.anilib.character.viewmodel.CharacterViewModel
import com.revolgenx.anilib.common.event.OpenImageEvent
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.common.viewmodel.getViewModelOwner
import com.revolgenx.anilib.util.copyToClipBoard
import com.revolgenx.anilib.util.loginContinue
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CharacterFragment : BaseLayoutFragment<CharacterFragmentLayoutBinding>() {
    private val viewModel by viewModel<CharacterViewModel>()
    private val containerSharedVM by viewModel<CharacterContainerViewModel>(owner = getViewModelOwner())
    private val characterModel get() = viewModel.characterModel
    private val characterId: Int? get() = arguments?.getInt(CHARACTER_ID_KEY)

    companion object {
        private const val CHARACTER_ID_KEY = "CHARACTER_ID_KEY"
        fun newInstance(characterId: Int) = CharacterFragment().also {
            it.arguments = bundleOf(CHARACTER_ID_KEY to characterId)
        }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CharacterFragmentLayoutBinding {
        return CharacterFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field.characterId = characterId ?: return
        viewModel.toggleField.characterId = characterId

        viewModel.characterInfoLiveData.observe(viewLifecycleOwner) { res ->
            val resourceLayout = binding.resourceStatusLayout
            when (res) {
                is Resource.Success -> {
                    resourceLayout.resourceStatusContainer.visibility = View.GONE
                    resourceLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    resourceLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    containerSharedVM.siteUrl = characterModel?.siteUrl
                    binding.updateView()
                }
                is Resource.Error -> {
                    resourceLayout.resourceStatusContainer.visibility = View.VISIBLE
                    resourceLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    resourceLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    resourceLayout.resourceStatusContainer.visibility = View.VISIBLE
                    resourceLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    resourceLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
            }
        }

        viewModel.toggleFavouriteLiveData.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    binding.characterFavIv.showLoading(false)
                    binding.updateFavourite()
                }
                is Resource.Error -> {
                    binding.characterFavIv.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ic_error)
                }
                is Resource.Loading -> {
                    binding.characterFavIv.showLoading(true)
                }
            }
        }

        binding.initListener()
        viewModel.getCharacterInfo(viewModel.field)
    }

    private fun CharacterFragmentLayoutBinding.initListener() {
        characterNameTv.setOnLongClickListener {
            requireContext().copyToClipBoard(characterNameTv.text?.toString())
            true
        }

        characterAlternateNameTv.setOnLongClickListener {
            requireContext().copyToClipBoard(characterAlternateNameTv.text?.toString())
            true
        }
        binding.characterFavIv.setOnClickListener {
            loginContinue {
                viewModel.toggleCharacterFav(viewModel.toggleField)
            }
        }
    }

    private fun CharacterFragmentLayoutBinding.updateView() {
        val item = characterModel ?: return
        item.name?.let {
            characterNameTv.text = it.full
            val alternatives = it.alternative?.joinToString(", ") ?: ""
            val hasAlternatives = alternatives.isNotBlank()
            val alternativeNames = (it.native?.plus(if (hasAlternatives) ", " else "") ?: "") + alternatives
            characterAlternateNameTv.text = alternativeNames
        }
        characterFavCountTv.text = item.favourites?.toLong()?.prettyNumberFormat()
        characterIv.setImageURI(characterModel?.image?.image)
        characterIv.setOnClickListener {
            characterModel?.image?.image?.let {
                OpenImageEvent(it).postEvent
            }
        }
        updateFavourite()

        val characterInfo = generateCharacterInfo(item) + item.description
        AlMarkwonFactory.getMarkwon()
            .setMarkdown(characterDescriptionTv, characterInfo)
    }

    private fun CharacterFragmentLayoutBinding.updateFavourite() {
        val favouriteRes = if (characterModel?.isFavourite == true) R.drawable.ic_favourite else R.drawable.ic_not_favourite
        characterFavIv.setImageResource(favouriteRes)
    }


    private fun generateCharacterInfo(model: CharacterModel): String {
        var generalInfo = ""
        model.dateOfBirth?.let {
            val month = it.month?.let { m ->
                Month.of(m).getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } ?: ""
            val day = it.day ?: ""
            val year = it.year ?: ""
            generalInfo += "<b>${getString(R.string.birthday)}:</b> $month $day, $year \n"
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

        if (generalInfo.isNotBlank()) {
            generalInfo += " \n"
        }
        return generalInfo
    }

}