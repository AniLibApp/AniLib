package com.revolgenx.anilib.ui.fragment.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.MediaInfoMeta
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
            val adapter = makePagerAdapter(
                mediaSocialFragments,
                resources.getStringArray(R.array.media_social_tab_menu)
            )
            mediaSocialViewPager.adapter = adapter
            mediaSocialTabLayout.setupWithViewPager(mediaSocialViewPager)
        }

    }

}