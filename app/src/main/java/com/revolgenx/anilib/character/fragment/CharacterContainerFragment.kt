package com.revolgenx.anilib.character.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.CharacterContainerFragmentLayoutBinding
import com.revolgenx.anilib.character.viewmodel.CharacterContainerViewModel
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.shareText
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterContainerFragment : BaseLayoutFragment<CharacterContainerFragmentLayoutBinding>() {
    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.character_fragment_menu
    override val showMenuIcon: Boolean = true
    private val characterFragments by lazy {
        listOf(
            CharacterFragment.newInstance(characterId!!),
            CharacterMediaFragment.newInstance(characterId!!),
            CharacterActorFragment.newInstance(characterId!!)
        )
    }

    private val characterId get() = arguments?.getInt(CHARACTER_ID_KEY)
    private val viewModel by viewModel<CharacterContainerViewModel>()

    companion object {
        private const val CHARACTER_ID_KEY = "CHARACTER_ID_KEY"
        fun newInstance(characterId: Int) = CharacterContainerFragment().also {
            it.arguments = bundleOf(CHARACTER_ID_KEY to characterId)
        }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): CharacterContainerFragmentLayoutBinding {
        return CharacterContainerFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterId ?: return
        updateToolbarTitle(viewModel.title ?: requireContext().getString(R.string.character))
        binding.characterViewPager.adapter = makeViewPagerAdapter2(characterFragments)
        val tabItems = listOf(R.string.about to R.drawable.ic_role, R.string.media to R.drawable.ic_media, R.string.voice_role to R.drawable.ic_voice_role)
        setupWithViewPager2(binding.characterTabLayout, binding.characterViewPager, tabItems)
        binding.characterViewPager.offscreenPageLimit = 2
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.dynamicToolbar
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.character_share_menu -> {
                shareText(viewModel.characterLink)
                true
            }
            R.id.character_open_in_browser_menu->{
                openLink(viewModel.characterLink)
                true
            }
            else -> {
                false
            }
        }
    }

    fun updateToolbarTitle(title: String) {
        viewModel.title = title
        getBaseToolbar().title = title
    }

    fun updateShareableLink(link: String?) {
        viewModel.characterLink = link
    }
}