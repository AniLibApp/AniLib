package com.revolgenx.anilib.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.databinding.MediaSocialFragmentLayoutBinding

class MediaSocialContainerFragment : BaseLayoutFragment<MediaSocialFragmentLayoutBinding>() {
    private val mediaBrowserMeta
        get() = arguments?.getParcelable<MediaInfoMeta?>(
            MEDIA_INFO_META_KEY
        )


    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaSocialContainerFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    private val mediaSocialFragments by lazy {
        listOf(
            MediaSocialRecentActivityFragment.newInstance(mediaBrowserMeta!!),
            MediaSocialFollowingFragment.newInstance(mediaBrowserMeta!!)
        )
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): MediaSocialFragmentLayoutBinding {
        return MediaSocialFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            mediaSocialViewPager.adapter = makeViewPagerAdapter2(mediaSocialFragments)
            setupWithViewPager2(
                mediaSocialTabLayout,
                mediaSocialViewPager,
                resources.getStringArray(R.array.media_social_tab_menu)
            )
        }

    }

}