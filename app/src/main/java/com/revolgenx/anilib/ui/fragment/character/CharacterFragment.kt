package com.revolgenx.anilib.ui.fragment.character

import android.os.Bundle
import android.view.*
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.field.ToggleFavouriteField
import com.revolgenx.anilib.data.field.character.CharacterField
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.data.meta.CharacterMeta
import com.revolgenx.anilib.data.model.character.CharacterModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.CharacterFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status.*
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import com.revolgenx.anilib.ui.viewmodel.character.CharacterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterFragment : BaseLayoutFragment<CharacterFragmentLayoutBinding>() {
    companion object {
        const val CHARACTER_META_KEY = "character_meta_key"
    }

    private val viewModel by viewModel<CharacterViewModel>()

    private var characterModel: CharacterModel? = null
    private lateinit var characterMeta: CharacterMeta
    private val characterField by lazy {
        CharacterField().also {
            it.characterId = characterMeta.characterId
        }
    }

    private val toggleFavouriteField by lazy {
        ToggleFavouriteField().also {
            it.characterId = characterMeta.characterId
        }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CharacterFragmentLayoutBinding {
        return CharacterFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.character_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.character_share_menu -> {
                characterModel?.siteUrl?.let {
                    requireContext().openLink(it)
                } ?: makeToast(R.string.invalid)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.classLoader = CharacterMeta::class.java.classLoader
        characterMeta = arguments?.getParcelable(CHARACTER_META_KEY) ?: return

        binding.characterIv.setImageURI(characterMeta.characterUrl)
        viewModel.characterInfoLiveData.observe(viewLifecycleOwner) { res ->
            val resourceLayout = binding.resourceStatusLayout
            when (res.status) {
                SUCCESS -> {
                    resourceLayout.resourceStatusContainer.visibility = View.GONE
                    resourceLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    resourceLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                    binding.updateView(res.data!!)
                }
                ERROR -> {
                    resourceLayout.resourceStatusContainer.visibility = View.VISIBLE
                    resourceLayout.resourceProgressLayout.progressLayout.visibility = View.GONE
                    resourceLayout.resourceErrorLayout.errorLayout.visibility = View.VISIBLE
                }
                LOADING -> {
                    resourceLayout.resourceStatusContainer.visibility = View.VISIBLE
                    resourceLayout.resourceProgressLayout.progressLayout.visibility = View.VISIBLE
                    resourceLayout.resourceErrorLayout.errorLayout.visibility = View.GONE
                }
            }
        }

        viewModel.toggleFavouriteLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    binding.characterFavIv.showLoading(false)
                    if (res.data == true) {
                        characterModel?.isFavourite = characterModel?.isFavourite?.not() ?: false
                        binding.characterFavIv.setImageResource(
                            if (characterModel?.isFavourite == true) {
                                R.drawable.ic_favourite
                            } else {
                                R.drawable.ic_not_favourite
                            }
                        )
                    }
                }
                ERROR -> {
                    binding.characterFavIv.showLoading(false)
                    makeToast(R.string.failed_to_toggle, icon = R.drawable.ads_ic_error)
                }
                LOADING -> {
                    binding.characterFavIv.showLoading(true)
                }
            }
        }

        initListener()

        if (savedInstanceState == null) {
            viewModel.getCharacterInfo(characterField)
        }
    }

    private fun initListener() {
        binding.characterFavLayout.setOnClickListener {
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

    private fun CharacterFragmentLayoutBinding.updateView(item: CharacterModel) {
        characterModel = item
        characterNameTv.text = item.name?.full
        characterFavCountTv.text = item.favourites?.toLong()?.prettyNumberFormat()

        if (characterMeta.characterUrl == null) {
            characterIv.setImageURI(characterModel?.characterImageModel?.image)
        }

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

        if (item.isFavourite) {
            characterFavIv.setImageResource(R.drawable.ic_favourite)
        }

        MarkwonImpl.createHtmlInstance(requireContext()).setMarkdown(characterDescriptionTv, item.description ?: "")
    }
}