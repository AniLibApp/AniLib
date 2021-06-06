package com.revolgenx.anilib.ui.fragment.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.forEachIndexed
import androidx.core.view.iterator
import androidx.viewpager.widget.ViewPager
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.CharacterContainerFragmentLayoutBinding
import com.revolgenx.anilib.ui.viewmodel.character.CharacterContainerViewModel
import com.revolgenx.anilib.util.openLink
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterContainerFragment : BaseLayoutFragment<CharacterContainerFragmentLayoutBinding>() {
    override val setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.character_fragment_menu

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        characterId ?: return
        updateToolbarTitle(viewModel.title ?: requireContext().getString(R.string.character))
        binding.characterViewPager.adapter = makePagerAdapter(characterFragments)
        binding.characterViewPager.offscreenPageLimit = 2
        binding.characterBottomNav.inflateMenu(R.menu.character_nav_menu)

        binding.characterBottomNav.setOnNavigationItemSelectedListener {
            binding.characterBottomNav.menu.forEachIndexed { index, item ->
                if (it == item) {
                    binding.characterViewPager.setCurrentItem(index, true)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        binding.characterViewPager.addOnPageChangeListener(object :
            ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                binding.characterBottomNav.menu.iterator().forEach {
                    it.isChecked = false
                }
                binding.characterBottomNav.menu.getItem(position).isChecked = true
            }
        })
    }

    override fun getBaseToolbar(): Toolbar {
        return binding.characterToolbar.dynamicToolbar
    }


    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.character_share_menu -> {
                requireContext().openLink(viewModel.characterLink)
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