package com.revolgenx.anilib.ui.fragment.home.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MainActivity
import com.revolgenx.anilib.activity.MediaListActivity
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.data.model.user.UserFollowerCountModel
import com.revolgenx.anilib.data.model.user.UserProfileModel
import com.revolgenx.anilib.databinding.ProfileFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.AuthenticateEvent
import com.revolgenx.anilib.infrastructure.event.ImageClickedEvent
import com.revolgenx.anilib.infrastructure.event.SettingEvent
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.dialog.MessageDialog
import com.revolgenx.anilib.ui.dialog.UserFollowerDialog
import com.revolgenx.anilib.ui.fragment.stats.StatsOverviewFragment
import com.revolgenx.anilib.ui.fragment.stats.UserStatsContainerFragment
import com.revolgenx.anilib.ui.fragment.user.UserFavouriteContainerFragment
import com.revolgenx.anilib.ui.fragment.user.UserOverviewFragment
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.viewmodel.user.UserProfileViewModel
import com.revolgenx.anilib.util.getOrDefault
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseLayoutFragment<ProfileFragmentLayoutBinding>() {

    companion object {
        const val USER_PROFILE_INFO_KEY = "USER_PROFILE_INFO_KEY"
    }


    private val viewModel by viewModel<UserProfileViewModel>()
    private var userProfileModel: UserProfileModel? = null


    private val userProfileFragments by lazy {
        listOf(
            UserOverviewFragment(),
            UserFavouriteContainerFragment(),
            UserStatsContainerFragment(), //anime
            UserStatsContainerFragment(), //manga
        )
    }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ProfileFragmentLayoutBinding {
        return ProfileFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userMeta: UserMeta? = arguments?.getParcelable(USER_PROFILE_INFO_KEY)

        val showUserInfo = if (userMeta != null) {
            with(viewModel.userField) {
                userId = userMeta.userId
                userName = userMeta.userName
            }
            binding.userProfileBackIv.visibility = View.VISIBLE
            binding.userProfileBackIv.setOnClickListener {
                finishActivity()
            }

            binding.animeCountHeader.setOnClickListener {
                MediaListActivity.openActivity(
                    requireContext(),
                    MediaListMeta(userMeta.userId, null, MediaType.ANIME.ordinal)
                )
            }

            binding.mangaCountHeader.setOnClickListener {
                MediaListActivity.openActivity(
                    requireContext(),
                    MediaListMeta(userMeta.userId, null, MediaType.MANGA.ordinal)
                )
            }

            binding.userProfileMoreIv.setOnClickListener {
                makeArrayPopupMenu(it, arrayOf(getString(R.string.share))) { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            requireContext().openLink(userProfileModel?.siteUrl)
                        }
                    }
                }
            }

            true
        } else {
            if (requireContext().loggedIn()) {
                viewModel.userField.userId = requireContext().userId()

                binding.animeCountHeader.setOnClickListener {
                    val mainActivity = requireActivity()
                    if (mainActivity is MainActivity) {
                        mainActivity.goToList(MediaType.ANIME.ordinal)
                    }
                }

                binding.mangaCountHeader.setOnClickListener {
                    val mainActivity = requireActivity()
                    if (mainActivity is MainActivity) {
                        mainActivity.goToList(MediaType.MANGA.ordinal)
                    }
                }


                binding.userProfileMoreIv.setOnClickListener {
                    makeArrayPopupMenu(
                        it,
                        resources.getStringArray(R.array.user_profile_more_actions)
                    ) { _, _, position, _ ->
                        when (position) {
                            0 -> {
                                SettingEvent().postEvent
                            }
                            1 -> {
                                requireContext().openLink(getString(R.string.discord_invite_link))
                            }
                            2 -> {
                                requireContext().openLink(getString(R.string.translate_link))
                            }
                            3 -> {
                                requireContext().openLink(userProfileModel?.siteUrl)
                            }
                            4 -> {
                                AuthenticateEvent().postEvent
                            }
                        }
                    }
                }
                true
            } else {
                false
            }
        }


        if (showUserInfo) {
            binding.bindUserView(savedInstanceState)
        } else {
            binding.bindLogInView()
        }
    }

    private fun ProfileFragmentLayoutBinding.bindUserView(savedInstanceState: Bundle?) {
        viewModel.userProfileLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { model ->
                        userProfileModel = model
                        userAvatarIv.setImageURI(model.avatar?.image)
                        userBannerIv.setImageURI(model.bannerImage ?: model.avatar?.image)
                        usernameTv.text = model.userName

                        animeCountHeader.title =
                            model.totalAnime.getOrDefault().prettyNumberFormat()
                        mangaCountHeader.title =
                            model.totalManga.getOrDefault().prettyNumberFormat()

                        if (requireContext().userId() != model.userId) {
                            userFollowButton.visibility = View.VISIBLE
                        }


                        userAvatarIv.setOnClickListener {
                            ImageClickedEvent(ImageMeta(model.avatar?.image)).postEvent
                        }

                        userBannerIv.setOnClickListener {
                            ImageClickedEvent(
                                ImageMeta(
                                    model.bannerImage ?: model.avatar?.image
                                )
                            ).postEvent
                        }

                    }
                }
                Status.ERROR -> {
                    makeToast(R.string.user_detail_fetch_failed)
                }
                Status.LOADING -> {
                }
            }
        }

        viewModel.followerLiveData.observe(viewLifecycleOwner) { res ->
            if (res.status == Status.SUCCESS) {
                bindFollowers(res.data!!)
            }
        }


        if (savedInstanceState == null) {
            viewModel.getProfile()
            viewModel.getFollower()
        }

        val userMeta = UserMeta(
            viewModel.userField.userId,
            viewModel.userField.userName,
            false
        )

        (userProfileFragments[0] as UserOverviewFragment).userProfileViewModel =
            viewModel
        (userProfileFragments[1] as UserFavouriteContainerFragment).userProfileViewModel =
            viewModel

        (userProfileFragments[2] as UserStatsContainerFragment).arguments = bundleOf(
            StatsOverviewFragment.USER_STATS_PARCEL_KEY
                    to UserStatsMeta(userMeta, MediaType.ANIME.ordinal)
        )
        (userProfileFragments[3] as UserStatsContainerFragment).arguments = bundleOf(
            StatsOverviewFragment.USER_STATS_PARCEL_KEY
                    to UserStatsMeta(userMeta, MediaType.MANGA.ordinal)
        )

        val adapter = makePagerAdapter(
            userProfileFragments,
            requireContext().resources.getStringArray(R.array.profile_tab_menu)
        )

        userInfoViewPager.adapter = adapter
        userInfoViewPager.offscreenPageLimit = 3
        userTabLayout.setupWithViewPager(userInfoViewPager)

    }

    private fun ProfileFragmentLayoutBinding.bindFollowers(followers: UserFollowerCountModel) {
        followerHeader.title = followers.followers.getOrDefault().prettyNumberFormat()
        followingHeader.title = followers.following.getOrDefault().prettyNumberFormat()

        updateFollowView()


        followerHeader.setOnClickListener {
            UserFollowerDialog.newInstance(FollowerMeta(viewModel.userField.userId))
                .show(childFragmentManager, "follower_dialog")
        }

        followingHeader.setOnClickListener {
            UserFollowerDialog.newInstance(
                FollowerMeta(
                    viewModel.userField.userId,
                    true
                )
            )
                .show(childFragmentManager, "following_dialog")
        }

        userFollowButton.setOnClickListener {
            if (userProfileModel == null) return@setOnClickListener

            if (userProfileModel!!.isBlocked == true) {
                requireContext().openLink(userProfileModel!!.siteUrl)
                return@setOnClickListener
            }

            if (userProfileModel!!.isFollowing == true) {
                with(MessageDialog.Companion.Builder()) {
                    titleRes = R.string.unfollow
                    message = getString(R.string.stop_following_s).format(
                        userProfileModel?.userName ?: ""
                    )
                    positiveTextRes = R.string.yes
                    negativeTextRes = R.string.no
                    build().let {
                        it.onButtonClickedListener = { _: DialogInterface, which: Int ->
                            when (which) {
                                AlertDialog.BUTTON_POSITIVE -> {
                                    toggleFollow()
                                }
                            }
                        }
                        it.show(childFragmentManager, MessageDialog.messageDialogTag)
                    }
                    return@setOnClickListener
                }
            }

            toggleFollow()
        }

    }

    private fun ProfileFragmentLayoutBinding.updateFollowView() {
        if (userProfileModel == null) return

        if (userProfileModel?.isFollowing == true) {
            userFollowButton.text = getString(R.string.following)
        } else {
            userFollowButton.text = getString(R.string.follow)
        }

        if (userProfileModel?.isBlocked == true) {
            userFollowButton.text = getString(R.string.blocked)
        }
    }

    private fun ProfileFragmentLayoutBinding.bindLogInView() {

    }

    private fun toggleFollow() {
        with(viewModel.userField) {
            if (userId != null) {
                viewModel.toggleFollowField.userId = userId
                if (requireContext().loggedIn()) {
                    viewModel.toggleFollow {
                        if (it.status == Status.SUCCESS) {
                            binding.updateFollowView()
                        } else if (it.status == Status.ERROR) {
                            makeToast(R.string.operation_failed, icon = R.drawable.ic_error)
                        }
                    }
                } else {
                    makeToast(R.string.please_log_in, icon = R.drawable.ic_error)
                }
            }
        }
    }

}