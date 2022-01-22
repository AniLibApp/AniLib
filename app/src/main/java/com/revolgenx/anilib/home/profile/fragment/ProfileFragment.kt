package com.revolgenx.anilib.home.profile.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.user.data.model.UserFollowerCountModel
import com.revolgenx.anilib.user.data.model.UserProfileModel
import com.revolgenx.anilib.databinding.ProfileFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.dialog.MessageDialog
import com.revolgenx.anilib.user.fragment.UserStatsContainerFragment
import com.revolgenx.anilib.user.fragment.UserActivityUnionFragment
import com.revolgenx.anilib.user.fragment.UserFavouriteContainerFragment
import com.revolgenx.anilib.user.fragment.UserOverviewFragment
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.user.data.meta.UserMeta
import com.revolgenx.anilib.user.data.meta.UserStatsMeta
import com.revolgenx.anilib.user.viewmodel.UserProfileViewModel
import com.revolgenx.anilib.util.getOrDefault
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseLayoutFragment<ProfileFragmentLayoutBinding>() {

    companion object {
        const val USER_PROFILE_INFO_KEY = "USER_PROFILE_INFO_KEY"
        fun newInstance(userMeta: UserMeta) = ProfileFragment().also {
            it.arguments = bundleOf(USER_PROFILE_INFO_KEY to userMeta)
        }
    }

    var showUserInfo: Boolean = false

    private val viewModel by viewModel<UserProfileViewModel>()
    private var userProfileModel: UserProfileModel? = null

    override val setHomeAsUp: Boolean get() = userMeta != null
    override val menuRes: Int = R.menu.user_profile_menu

    private val userMeta get() = arguments?.getParcelable<UserMeta?>(USER_PROFILE_INFO_KEY)

    private val _userMeta get() = userMeta ?:  UserMeta(
        viewModel.userField.userId,
        viewModel.userField.userName,
        false
    )

    private val userProfileFragments by lazy {
        listOf(
            UserOverviewFragment.newInstance(_userMeta),
            UserActivityUnionFragment.newInstance(_userMeta),
            UserFavouriteContainerFragment.newInstance(_userMeta),
            UserStatsContainerFragment.newInstance(UserStatsMeta(_userMeta, MediaType.ANIME.ordinal)), //anime
            UserStatsContainerFragment.newInstance(UserStatsMeta(_userMeta, MediaType.MANGA.ordinal)), //manga
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

    override fun onToolbarInflated() {
        val menu = getBaseToolbar().menu
        if (userMeta != null) {
            menu.findItem(R.id.setting_menu).isVisible = false
            menu.findItem(R.id.sign_out_menu).isVisible = false
        }
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting_menu -> {
                OpenSettingEvent(SettingEventTypes.SETTING).postEvent
                true
            }
            R.id.sign_out_menu -> {
                AuthenticateEvent().postEvent
                true
            }
            R.id.user_share_menu -> {
                requireContext().openLink(userProfileModel?.siteUrl)
                true
            }
            else -> {
                super.onToolbarMenuSelected(item)
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showUserInfo = if (userMeta != null) {
            val meta = userMeta!!
            with(viewModel.userField) {
                userId = meta.userId
                userName = meta.userName
            }

            binding.animeCountHeader.setOnClickListener {
                OpenUserMediaListEvent(
                    MediaListMeta(meta.userId, meta.userName, MediaType.ANIME.ordinal)
                ).postEvent
            }

            binding.mangaCountHeader.setOnClickListener {
                OpenUserMediaListEvent(
                    MediaListMeta(meta.userId, meta.userName, MediaType.MANGA.ordinal)
                ).postEvent
            }
            true
        } else {
            if (requireContext().loggedIn()) {
                viewModel.userField.userId = UserPreference.userId

                binding.animeCountHeader.setOnClickListener {
                    ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                    ChangeViewPagerPageEvent(ListContainerFragmentPage.ANIME).postEvent
                }

                binding.mangaCountHeader.setOnClickListener {
                    ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                    ChangeViewPagerPageEvent(ListContainerFragmentPage.MANGA).postEvent
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


    override fun getBaseToolbar(): Toolbar {
        return binding.profileFragmentToolbar
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

                        viewModel.userField.userId = model.id

                        if (savedInstanceState == null) {
                            viewModel.getFollower()
                        }

                        userAvatarIv.hierarchy.roundingParams?.let { roundingParams ->
                            roundingParams.borderColor = dynamicAccentColor
                            userAvatarIv.hierarchy.roundingParams = roundingParams
                        }

                        userAvatarIv.setImageURI(model.avatar?.image)
                        userBannerIv.setImageURI(model.bannerImage ?: model.avatar?.image)
                        usernameTv.text = model.name

                        binding.profileFragmentToolbar.title = userProfileModel?.name.naText()


                        animeCountHeader.title =
                            model.totalAnime.getOrDefault().prettyNumberFormat()
                        mangaCountHeader.title =
                            model.totalManga.getOrDefault().prettyNumberFormat()

                        if (UserPreference.userId != model.id) {
                            userFollowButton.visibility = View.VISIBLE
                        }


                        userAvatarIv.setOnClickListener {
                            OpenImageEvent(model.avatar?.image).postEvent
                        }

                        userBannerIv.setOnClickListener {
                            OpenImageEvent(model.bannerImage ?: model.avatar?.image).postEvent
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


        val adapter = makePagerAdapter(
            userProfileFragments,
            requireContext().resources.getStringArray(R.array.profile_tab_menu)
        )

        userInfoViewPager.adapter = adapter
        userInfoViewPager.offscreenPageLimit = 4
        userTabLayout.setupWithViewPager(userInfoViewPager)

    }

    private fun ProfileFragmentLayoutBinding.bindFollowers(followers: UserFollowerCountModel) {
        followerHeader.title = followers.followers.getOrDefault().prettyNumberFormat()
        followingHeader.title = followers.following.getOrDefault().prettyNumberFormat()

        updateFollowView()


        followerHeader.setOnClickListener {
            OpenUserFriendEvent(viewModel.userField.userId, true).postEvent
        }

        followingHeader.setOnClickListener {
            OpenUserFriendEvent(viewModel.userField.userId).postEvent

        }

        userFollowButton.setOnClickListener {
            if (userProfileModel == null) return@setOnClickListener

            if (userProfileModel!!.isBlocked) {
                requireContext().openLink(userProfileModel!!.siteUrl)
                return@setOnClickListener
            }

            if (userProfileModel!!.isFollowing) {
                with(MessageDialog.Companion.Builder()) {
                    titleRes = R.string.unfollow
                    message = getString(R.string.stop_following_s).format(
                        userProfileModel?.name ?: ""
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