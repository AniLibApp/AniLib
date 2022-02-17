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
import com.revolgenx.anilib.common.ui.adapter.makeViewPagerAdapter2
import com.revolgenx.anilib.common.ui.adapter.setupWithViewPager2
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.databinding.UserContainerFragmentBinding
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.dialog.MessageDialog
import com.revolgenx.anilib.ui.view.makeErrorToast
import com.revolgenx.anilib.user.data.meta.UserMeta
import com.revolgenx.anilib.user.fragment.*
import com.revolgenx.anilib.user.viewmodel.UserContainerSharedVM
import com.revolgenx.anilib.util.naText
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserContainerFragment : BaseLayoutFragment<UserContainerFragmentBinding>() {

    companion object {
        const val USER_PROFILE_INFO_KEY = "USER_PROFILE_INFO_KEY"
        fun newInstance(userMeta: UserMeta) = UserContainerFragment().also {
            it.arguments = bundleOf(USER_PROFILE_INFO_KEY to userMeta)
        }
    }

    override val setHomeAsUp: Boolean get() = userMeta != null
    override val menuRes: Int = R.menu.user_profile_menu

    private val userMeta get() = arguments?.getParcelable<UserMeta?>(USER_PROFILE_INFO_KEY)

    private val viewModel by viewModel<UserContainerSharedVM>()
    private val userModel get() = viewModel.userLiveData.value?.data

    private val userProfileFragments by lazy {
        listOf(
            UserOverviewFragment(),
            UserActivityUnionFragment(),
            UserFavouriteContainerFragment(),
            AnimeUserStatsContainerFragment(),
            MangaUserStatsContainerFragment()
        )
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserContainerFragmentBinding {
        return UserContainerFragmentBinding.inflate(inflater, parent, false)
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
                requireContext().openLink(userModel?.siteUrl)
                true
            }
            else -> {
                super.onToolbarMenuSelected(item)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollapsingToolbarTheme()
        if (userMeta != null) {
            val meta = userMeta!!
            with(viewModel) {
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
        } else {
            if (requireContext().loggedIn()) {
                viewModel.userId = UserPreference.userId

                binding.animeCountHeader.setOnClickListener {
                    ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                    ChangeViewPagerPageEvent(ListContainerFragmentPage.ANIME).postEvent
                }

                binding.mangaCountHeader.setOnClickListener {
                    ChangeViewPagerPageEvent(MainActivityPage.LIST).postEvent
                    ChangeViewPagerPageEvent(ListContainerFragmentPage.MANGA).postEvent
                }
            }
        }

        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.bind()
                }
                else -> {}
            }
        }


        viewModel.toggleFollowLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.updateFollowView()
                }
                Status.ERROR -> {
                    makeErrorToast(R.string.operation_failed)
                }
                Status.LOADING -> {}
            }

        }



        binding.userInfoViewPager.adapter = makeViewPagerAdapter2(
            userProfileFragments
        )

        setupWithViewPager2(
            binding.userTabLayout,
            binding.userInfoViewPager,
            requireContext().resources.getStringArray(R.array.profile_tab_menu)
        )
        binding.userInfoViewPager.offscreenPageLimit = 4
    }

    private fun UserContainerFragmentBinding.bind() {
        val user = userModel ?: return

        userAvatarIv.hierarchy.roundingParams?.let { roundingParams ->
            roundingParams.borderColor = dynamicAccentColor
            userAvatarIv.hierarchy.roundingParams = roundingParams
        }

        userAvatarIv.setImageURI(user.avatar?.image)
        userBannerIv.setImageURI(user.bannerImage ?: user.avatar?.image)
        usernameTv.text = user.name

        binding.profileFragmentToolbar.title = user.name.naText()

        animeCountHeader.title =
            user.statistics?.anime?.count?.prettyNumberFormat()
        mangaCountHeader.title =
            user.statistics?.manga?.count?.prettyNumberFormat()

        bindFollowers()

        if (UserPreference.userId != user.id) {
            userFollowButton.visibility = View.VISIBLE
        }

        userAvatarIv.setOnClickListener {
            OpenImageEvent(user.avatar?.image).postEvent
        }

        userBannerIv.setOnClickListener {
            OpenImageEvent(user.bannerImage ?: user.avatar?.image).postEvent
        }

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

    private fun UserContainerFragmentBinding.bindFollowers() {
        val user = userModel ?: return
        followerHeader.title = user.followers.prettyNumberFormat()
        followingHeader.title = user.following.prettyNumberFormat()
        updateFollowView()

        followerHeader.setOnClickListener {
            OpenUserFriendEvent(userModel?.id, true).postEvent
        }

        followingHeader.setOnClickListener {
            OpenUserFriendEvent(userModel?.id).postEvent
        }

        userFollowButton.setOnClickListener {
            if (user.isBlocked) {
                requireContext().openLink(user.siteUrl)
                return@setOnClickListener
            }

            if (user.isFollowing) {
                with(MessageDialog.Companion.Builder()) {
                    titleRes = R.string.unfollow
                    message = getString(R.string.stop_following_s).format(
                        user.name ?: ""
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

    private fun UserContainerFragmentBinding.updateFollowView() {
        val user = userModel ?: return
        if (user.isBlocked) {
            userFollowButton.text = getString(R.string.blocked)
            return
        }

        if (user.isFollowing) {
            userFollowButton.text = getString(R.string.following)
        } else {
            userFollowButton.text = getString(R.string.follow)
        }
    }

    private fun toggleFollow() {
        userModel ?: return
        if (requireContext().loggedIn()) {
            viewModel.toggleFollow()
        } else {
            makeErrorToast(R.string.please_log_in)
        }
    }

}