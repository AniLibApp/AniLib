package com.revolgenx.anilib.ui.fragment.home.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaListActivity
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.UserConstant
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.data.model.user.UserFollowerCountModel
import com.revolgenx.anilib.data.model.user.UserProfileModel
import com.revolgenx.anilib.databinding.ProfileFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.dialog.MessageDialog
import com.revolgenx.anilib.ui.dialog.UserFollowerDialog
import com.revolgenx.anilib.ui.fragment.stats.UserStatsContainerFragment
import com.revolgenx.anilib.ui.fragment.user.UserFavouriteContainerFragment
import com.revolgenx.anilib.ui.fragment.user.UserOverviewFragment
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.viewmodel.user.UserProfileViewModel
import com.revolgenx.anilib.util.getOrDefault
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseLayoutFragment<ProfileFragmentLayoutBinding>() {

    companion object {
        const val USER_PROFILE_INFO_KEY = "USER_PROFILE_INFO_KEY"
    }

    var showUserInfo: Boolean = false

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollapsingToolbarTheme()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userMeta: UserMeta? = arguments?.getParcelable(USER_PROFILE_INFO_KEY)


        showUserInfo = if (userMeta != null) {
            binding.profileFragmentToolbar.inflateMenu(R.menu.user_profile_menu)

            with(viewModel.userField) {
                userId = userMeta.userId
                userName = userMeta.userName
            }
            with(binding.profileFragmentToolbar) {
                setNavigationIcon(R.drawable.ads_ic_back)
                setNavigationOnClickListener {
                    finishActivity()
                }

                menu.findItem(R.id.setting_menu).isVisible = false
                menu.findItem(R.id.sign_out_menu).isVisible = false

                setOnMenuItemClickListener {
                    if (it.itemId == R.id.user_share_menu) {
                        requireContext().openLink(userProfileModel?.siteUrl)
                        true
                    } else {
                        false
                    }
                }
            }

            binding.animeCountHeader.setOnClickListener {
                MediaListActivity.openActivity(
                    requireContext(),
                    MediaListMeta(userMeta.userId, userMeta.userName, MediaType.ANIME.ordinal)
                )
            }

            binding.mangaCountHeader.setOnClickListener {
                MediaListActivity.openActivity(
                    requireContext(),
                    MediaListMeta(userMeta.userId, userMeta.userName, MediaType.MANGA.ordinal)
                )
            }

            true
        } else {
            if (requireContext().loggedIn()) {
                binding.profileFragmentToolbar.inflateMenu(R.menu.user_profile_menu)

                viewModel.userField.userId = requireContext().userId()

                binding.animeCountHeader.setOnClickListener {
                    ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                    ChangeViewPagerPageEvent(ListContainerFragmentPage.ANIME).postEvent
                }

                binding.mangaCountHeader.setOnClickListener {
                    ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                    ChangeViewPagerPageEvent(ListContainerFragmentPage.MANGA).postEvent
                }

                binding.profileFragmentToolbar.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.setting_menu -> {
                            SettingEvent(SettingEventTypes.SETTING).postEvent
                            true
                        }
                        R.id.user_share_menu -> {
                            requireContext().openLink(userProfileModel?.siteUrl)
                            true
                        }
                        R.id.sign_out_menu -> {
                            AuthenticateEvent().postEvent
                            true
                        }
                        else -> false
                    }
                }
                true
            } else {
                false
            }
        }


        if (showUserInfo) {
            binding.bindUserView(savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!visibleToUser && showUserInfo) {
            viewModel.getProfile()
        }
        visibleToUser = true
    }


    private fun setCollapsingToolbarTheme() {
        binding.userCollapsingToolbar.setCollapsedTitleTextColor(
            DynamicTheme.getInstance().get().textPrimaryColor
        )
        binding.userCollapsingToolbar.setBackgroundColor(
            DynamicTheme.getInstance().get().backgroundColor
        )

        binding.userCollapsingToolbar.setStatusBarScrimColor(
            DynamicTheme.getInstance().get().backgroundColor
        )
        binding.userCollapsingToolbar.setContentScrimColor(
            DynamicTheme.getInstance().get().backgroundColor
        )

        binding.profileFragmentToolbar.colorType = Theme.ColorType.BACKGROUND
        binding.profileFragmentToolbar.textColorType = Theme.ColorType.TEXT_PRIMARY

    }


    private fun ProfileFragmentLayoutBinding.bindUserView(savedInstanceState: Bundle?) {
        viewModel.userProfileLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { model ->
                        userProfileModel = model

                        viewModel.userField.userId = model.userId

                        if (savedInstanceState == null) {
                            viewModel.getFollower()
                        }

                        userAvatarIv.hierarchy.roundingParams?.let { roundingParams ->
                            roundingParams.borderColor = dynamicAccentColor
                            userAvatarIv.hierarchy.roundingParams = roundingParams
                        }

                        userAvatarIv.setImageURI(model.avatar?.image)
                        userBannerIv.setImageURI(model.bannerImage ?: model.avatar?.image)
                        usernameTv.text = model.userName

                        binding.profileFragmentToolbar.title = userProfileModel?.userName.naText()


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


        val userMeta = UserMeta(
            viewModel.userField.userId,
            viewModel.userField.userName,
            false
        )


        userProfileFragments[0].arguments = bundleOf(UserConstant.USER_META_KEY to userMeta)
        userProfileFragments[1].arguments = bundleOf(UserConstant.USER_META_KEY to userMeta)
        userProfileFragments[2].arguments = bundleOf(
            UserConstant.USER_STATS_META_KEY
                    to UserStatsMeta(userMeta, MediaType.ANIME.ordinal)
        )
        userProfileFragments[3].arguments = bundleOf(
            UserConstant.USER_STATS_META_KEY
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