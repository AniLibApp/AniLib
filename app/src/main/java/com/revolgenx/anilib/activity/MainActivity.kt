package com.revolgenx.anilib.activity

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.annotation.*
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.appwidget.ui.fragment.AiringWidgetConfigFragment
import com.revolgenx.anilib.ui.dialog.*
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.data.meta.UserMeta
import com.revolgenx.anilib.data.model.home.HomePageOrderType
import com.revolgenx.anilib.databinding.ActivityMainBinding
import com.revolgenx.anilib.radio.ui.fragments.RadioFragment
import com.revolgenx.anilib.social.ui.fragments.ActivityUnionFragment
import com.revolgenx.anilib.ui.fragment.about.AboutFragment
import com.revolgenx.anilib.ui.fragment.home.discover.DiscoverContainerFragment
import com.revolgenx.anilib.ui.fragment.home.list.ListContainerFragment
import com.revolgenx.anilib.ui.fragment.home.profile.ProfileFragment
import com.revolgenx.anilib.ui.fragment.home.profile.UserLoginFragment
import com.revolgenx.anilib.ui.fragment.notification.NotificationSettingFragment
import com.revolgenx.anilib.ui.fragment.review.AllReviewFragment
import com.revolgenx.anilib.ui.fragment.settings.*
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.*
import com.revolgenx.anilib.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import net.openid.appauth.AuthorizationRequest
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.social.ui.bottomsheet.SpoilerBottomSheet
import com.revolgenx.anilib.social.ui.bottomsheet.SpoilerBottomSheetFragment
import com.revolgenx.anilib.social.ui.fragments.composer.ActivityComposerContainerFragment
import com.revolgenx.anilib.social.ui.fragments.info.ActivityInfoFragment
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.fragment.ActivityEventListener
import com.revolgenx.anilib.ui.fragment.EntryListEditorFragment
import com.revolgenx.anilib.ui.fragment.airing.AiringFragment
import com.revolgenx.anilib.ui.fragment.character.CharacterContainerFragment
import com.revolgenx.anilib.ui.fragment.friend.UserFriendContainerFragment
import com.revolgenx.anilib.ui.fragment.list.UserMediaListContainerFragment
import com.revolgenx.anilib.ui.fragment.media.MediaInfoFragment
import com.revolgenx.anilib.ui.fragment.media.MediaListingFragment
import com.revolgenx.anilib.ui.fragment.notification.NotificationFragment
import com.revolgenx.anilib.ui.fragment.review.ReviewComposerFragment
import com.revolgenx.anilib.ui.fragment.review.ReviewFragment
import com.revolgenx.anilib.ui.fragment.search.SearchFragment
import com.revolgenx.anilib.ui.fragment.staff.StaffContainerFragment
import com.revolgenx.anilib.ui.fragment.studio.StudioFragment

class MainActivity : BaseDynamicActivity<ActivityMainBinding>(), CoroutineScope, EventBusListener {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val viewModel by viewModel<MainActivityViewModel>()

    companion object {
        private const val authIntent: String = "auth_intent_key"
        private const val authDialogTag = "auth_dialog_tag"
        private const val authorizationExtra = "com.revolgenx.anilib.HANDLE_AUTHORIZATION_RESPONSE"

        const val OPEN_AIRING_ACTION_KEY = "OPEN_AIRING_ACTION_KEY"
        const val ENTRY_LIST_ACTION_KEY = "ENTRY_LIST_ACTION_KEY"
        const val ENTRY_LIST_DATA_KEY = "ENTRY_LIST_DATA_KEY"
        const val MEDIA_INFO_DATA_KEY = "MEDIA_INFO_DATA_KEY"
        const val MEDIA_INFO_ACTION_KEY = "MEDIA_INFO_ACTION_KEY"
    }

    private var pressedTwice = false

    private val discoverContainerFragment by lazy {
        DiscoverContainerFragment()
    }

    private val listContainerFragment by lazy {
        ListContainerFragment()
    }

    private val activityUnionFragment by lazy {
        ActivityUnionFragment()
    }

    private val radioFragment by lazy {
        RadioFragment()
    }

    private val profileFragment by lazy {
        ProfileFragment()
    }

    private val loginFragment by lazy {
        UserLoginFragment()
    }

    override fun bindView(inflater: LayoutInflater, parent: ViewGroup?): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //update before layout inflate
        updateSharedPreference()
        super.onCreate(savedInstanceState)
        checkReleaseInfo()
        binding.updateView()
        silentFetchUserInfo()
        checkIntent(intent)
    }

    private fun checkReleaseInfo() {
        if (getVersion(this) != DynamicPackageUtils.getAppVersion(this)) {
            ReleaseInfoDialog().show(supportFragmentManager, ReleaseInfoDialog.tag)
        }
    }

    override fun onResume() {
        super.onResume()
        statusBarColor = dynamicBackgroundColor
    }

    private fun checkIsFromShortcut(newIntent: Intent?) {
        val intent = newIntent ?: return
        if (intent.action == Intent.ACTION_VIEW) {
            if (intent.hasExtra(LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY)) {
                val currentShortcut = intent.getIntExtra(
                    LauncherShortcutKeys.LAUNCHER_SHORTCUT_EXTRA_KEY,
                    LauncherShortcuts.HOME.ordinal
                )
                when (LauncherShortcuts.values()[currentShortcut]) {
                    LauncherShortcuts.HOME -> {
                        binding.mainBottomNavView.selectedItemId = R.id.home_navigation_menu
                    }
                    LauncherShortcuts.ANIME -> {
                        binding.mainBottomNavView.selectedItemId = R.id.list_navigation_menu
                        ChangeViewPagerPageEvent(ListContainerFragmentPage.ANIME).postSticky
                    }
                    LauncherShortcuts.MANGA -> {
                        binding.mainBottomNavView.selectedItemId = R.id.list_navigation_menu
                        ChangeViewPagerPageEvent(ListContainerFragmentPage.MANGA).postSticky
                    }
                    LauncherShortcuts.RADIO -> {
                        binding.mainBottomNavView.selectedItemId = R.id.music_navigation_menu
                    }
                    LauncherShortcuts.NOTIFICATION -> {
                        openNotificationCenter()
                    }
                }
            }
        }
    }

    private fun ActivityMainBinding.updateView() {
        val menuList = mutableListOf<HomeMenuItem>()

        menuList.add(
            HomeMenuItem(
                R.id.home_navigation_menu,
                R.string.home,
                R.drawable.ic_home,
                getHomePageOrderFromType(this@MainActivity, HomePageOrderType.HOME),
                discoverContainerFragment
            )
        )
        if (loggedIn()) {
            menuList.add(
                HomeMenuItem(
                    R.id.list_navigation_menu,
                    R.string.list,
                    R.drawable.ic_media_list,
                    getHomePageOrderFromType(this@MainActivity, HomePageOrderType.LIST),
                    listContainerFragment
                )
            )
            menuList.add(
                HomeMenuItem(
                    R.id.activity_navigation_menu,
                    R.string.social,
                    R.drawable.ic_activity_union,
                    getHomePageOrderFromType(this@MainActivity, HomePageOrderType.ACTIVITY),
                    activityUnionFragment
                )
            )
        }
        menuList.add(
            HomeMenuItem(
                R.id.music_navigation_menu,
                R.string.radio,
                R.drawable.ic_radio,
                getHomePageOrderFromType(this@MainActivity, HomePageOrderType.RADIO),
                radioFragment
            )
        )

        menuList.add(
            HomeMenuItem(
                R.id.profile_navigation_menu,
                R.string.profile,
                R.drawable.ic_person,
                HomePageOrderType.values().size,
                if (loggedIn()) profileFragment else loginFragment
            )
        )

        menuList.sortBy { it.order }
        menuList.forEach {
            mainBottomNavView.menu.add(Menu.NONE, it.id, Menu.NONE, it.text).setIcon(it.drawRes)
        }

        val pagerAdapter = makePagerAdapter(menuList.map { it.fragment })
        mainViewPager.adapter = pagerAdapter
        mainViewPager.offscreenPageLimit = pagerAdapter.count - 1


        mainBottomNavView.setOnNavigationItemSelectedListener {
            mainBottomNavView.menu.forEachIndexed { index, item ->
                if (it == item) {
                    mainViewPager.setCurrentItem(index, true)
                }
            }
            false
        }


        mainViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mainBottomNavView.menu.iterator().forEach {
                    it.isChecked = false
                }
                mainBottomNavView.menu.getItem(position).isChecked = true
            }
        })

    }

    private fun updateSharedPreference() {
        if (!isSharedPreferenceSynced(context)) {
            TagPrefUtil.reloadTagPref(context)
            TagPrefUtil.reloadGenrePref(context)
            TagPrefUtil.reloadStreamingPref(context)
            isSharedPreferenceSynced(context, true)
        }
    }


    private fun silentFetchUserInfo() {
        if (loggedIn()) {
            viewModel.getUserLiveData()
        }
    }

    @Subscribe
    fun goToPageEvent(event: ChangeViewPagerPageEvent) {
        if (event.data is MainActivityPage) {
            val homePageOrder = when (event.data) {
                MainActivityPage.HOME -> {
                    getHomePageOrderFromType(this, HomePageOrderType.HOME)
                }
                MainActivityPage.LIST -> {
                    getHomePageOrderFromType(this, HomePageOrderType.LIST)
                }
                MainActivityPage.RADIO -> {
                    getHomePageOrderFromType(this, HomePageOrderType.RADIO)
                }
            }

            binding.mainViewPager.setCurrentItem(homePageOrder, false)
        }
    }

    override fun onNewIntent(intent: Intent) {
        checkIntent(intent)
        super.onNewIntent(intent)
    }


    private fun checkIntent(@Nullable intent: Intent?) {
        if (intent != null) {
            if (intent.hasExtra(authIntent)) {
                handleAuthorizationResponse(intent)
                intent.removeExtra(authIntent)
            }

            checkIsFromShortcut(intent)

            when (intent.action) {
                Intent.ACTION_VIEW -> {
                    val data = intent.data ?: return
                    val paths = data.pathSegments

                    when (paths[0]) {
                        "user" -> {
                            val userId = paths[1].toIntOrNull()
                            val username = paths[1].toString()
                            OpenUserProfileEvent(userId, username).postEvent
                        }
                        "anime", "manga" -> {
                            val type = if (paths[0].compareTo("anime") == 0) {
                                MediaType.ANIME.ordinal
                            } else {
                                MediaType.MANGA.ordinal
                            }
                            val mediaId = paths[1].toIntOrNull() ?: return

                            openMediaInfoCenter(
                                MediaInfoMeta(
                                    mediaId = mediaId,
                                    type,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            )
                        }
                        "character" -> {
                            val characterId = paths[1].toIntOrNull() ?: return
                            openCharacterCenter(characterId)
                        }
                        "staff" -> {
                            val staffId = paths[1].toIntOrNull() ?: return
                            openStaffCenter(staffId)
                        }
                        "activity"->{
                            val activityId = paths[1].toIntOrNull() ?: return
                            openActivityInfoCenter(activityId)
                        }
                    }
                }
                OPEN_AIRING_ACTION_KEY -> {
                    openAiringScheduleCenter()
                }
                MEDIA_INFO_ACTION_KEY -> {
                    val meta =
                        intent.getParcelableExtra<MediaInfoMeta?>(MEDIA_INFO_DATA_KEY) ?: return
                    openMediaInfoCenter(meta)
                }
                ENTRY_LIST_ACTION_KEY -> {
                    val meta =
                        intent.getParcelableExtra<ListEditorMeta?>(ENTRY_LIST_DATA_KEY) ?: return
                    openMediaListEditorCenter(meta)
                }
            }

            intent.action = ""
        }
    }


    private fun handleAuthorizationResponse(@NonNull intent: Intent) {
        val response = AuthorizationResponse.fromIntent(intent)
        if (response != null) {
            val service = AuthorizationService(this)
            val clientAuth: ClientAuthentication =
                ClientSecretBasic(BuildConfig.anilistclientSecret)

            service.performTokenRequest(
                response.createTokenExchangeRequest(),
                clientAuth
            ) { tokenResponse, exception ->
                if (exception != null) {
                    Timber.w(exception, "Token Exchange failed")
                } else {
                    val accessToken = tokenResponse?.accessToken!!
                    logIn(accessToken)
                    SessionEvent(true).postEvent
                    startActivity(Intent(this.context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    (supportFragmentManager.findFragmentByTag(authDialogTag) as? DynamicDialogFragment)?.dismiss()
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            binding.drawerLayout.isDrawerOpen(GravityCompat.END) -> {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
            else -> {
                if ((supportFragmentManager.backStackEntryCount >= 1)) {

                    val topFragment = supportFragmentManager.fragments.lastOrNull()
                    if ((topFragment is ActivityEventListener)) {
                        if (!topFragment.onBackPressed()) {
                            super.onBackPressed()
                        }
                    } else {
                        super.onBackPressed()
                    }
                    return
                }

                if (pressedTwice) {
                    finish()
                } else {
                    makeToast(R.string.press_again_to_exit, icon = R.drawable.ic_exit)
                    Handler().postDelayed({
                        pressedTwice = false
                    }, 1000)
                }
                pressedTwice = true
            }
        }
    }


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }


    @Subscribe
    fun signInEvent(event: AuthenticateEvent) {
        if (loggedIn()) {
            context.logOut()
            SessionEvent(false).postEvent
            startActivity(Intent(this.context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        } else {
            AuthenticationDialog.newInstance()
                .show(supportFragmentManager, authDialogTag)
            val serviceConfiguration =
                AuthorizationServiceConfiguration(
                    Uri.parse(BuildConfig.anilistAuthEndPoint) /* auth endpoint */,
                    Uri.parse(BuildConfig.anilistTokenEndPoint) /* token endpoint */
                )

            val clientId = BuildConfig.anilistclientId
            val redirectUri = Uri.parse(BuildConfig.anilistRedirectUri)
            val builder = AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                ResponseTypeValues.CODE,
                redirectUri
            )
            val request = builder.build()
            val authorizationService = AuthorizationService(this)

            val postAuthorizationIntent = Intent(this, MainActivity::class.java)
                .also {
                    it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    it.putExtra(authIntent, authorizationExtra)
                }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                postAuthorizationIntent,
                0
            )
            launch(Dispatchers.IO) {
                authorizationService.performAuthorizationRequest(request, pendingIntent)
            }
        }
    }

    @Subscribe
    fun onBaseEvent(event: BaseEvent) {
        when (event) {
            is OpenUserProfileEvent -> {
                addFragmentToMain(ProfileFragment().also {
                    it.arguments = bundleOf(
                        ProfileFragment.USER_PROFILE_INFO_KEY to UserMeta(
                            event.userId,
                            event.username
                        )
                    )
                })
            }

            is OpenSpoilerContentEvent ->{
                addFragmentToMain(SpoilerBottomSheetFragment.newInstance(event.spanned))
            }

            is OpenUserFriendEvent -> {
                event.userId ?: return
                addFragmentToMain(
                    UserFriendContainerFragment.newInstance(
                        event.userId,
                        event.isFollower
                    )
                )
            }

            is OpenMediaListEditorEvent -> {
                openMediaListEditorCenter(event.meta)
            }

            is OpenMediaInfoEvent -> {
                openMediaInfoCenter(event.meta)
            }

            is OpenMediaListingEvent -> {
                addFragmentToMain(MediaListingFragment.newInstance(event.mediaIdsIn))
            }

            is OpenUserMediaListEvent -> {
                addFragmentToMain(UserMediaListContainerFragment.newInstance(event.meta))
            }

            is OpenSearchEvent -> {
                addFragmentToMain(SearchFragment.newInstance(event.data))
            }

            is OpenReviewEvent -> {
                addFragmentToMain(ReviewFragment.newInstance(event.reviewId))
            }

            is OpenAllReviewEvent -> {
                addFragmentToMain(AllReviewFragment())
            }

            is OpenNotificationCenterEvent -> {
                openNotificationCenter()
            }

            is OpenCharacterEvent -> {
                openCharacterCenter(event.characterId)
            }

            is OpenStaffEvent -> {
                openStaffCenter(event.staffId)
            }

            is OpenStudioEvent->{
                openStudioCenter(event)
            }

            is OpenAiringScheduleEvent -> {
                openAiringScheduleCenter()
            }

            is OpenReviewComposerEvent -> {
                addFragmentToMain(ReviewComposerFragment.newInstance(event.mediaId))
            }

            is OpenActivityComposer ->{
                addFragmentToMain(ActivityComposerContainerFragment())
            }

            is OpenActivityInfoEvent->{
                openActivityInfoCenter(event.activityId)
            }

            is OpenSettingEvent -> {
                when (event.settingEventType) {
                    SettingEventTypes.ABOUT -> {
                        addFragmentToMain(AboutFragment())
                    }
                    SettingEventTypes.APPLICATION -> {
                        addFragmentToMain(ApplicationSettingFragment())
                    }
                    SettingEventTypes.SETTING -> {
                        addFragmentToMain(SettingFragment())
                    }
                    SettingEventTypes.MEDIA_LIST -> {
                        addFragmentToMain(MediaListSettingFragment())
                    }
                    SettingEventTypes.MEDIA_SETTING -> {
                        addFragmentToMain(MediaSettingFragment())
                    }
                    SettingEventTypes.THEME -> {
                        addFragmentToMain(ThemeControllerFragment())
                    }
                    SettingEventTypes.CUSTOMIZE_FILTER -> {
                        addFragmentToMain(CustomizeFilterFragment())
                    }
                    SettingEventTypes.AIRING_WIDGET -> {
                        addFragmentToMain(AiringWidgetConfigFragment())
                    }
                    SettingEventTypes.TRANSLATION -> {
                        addFragmentToMain(TranslationSettingFragment())
                    }
                    SettingEventTypes.NOTIFICATION -> {
                        addFragmentToMain(NotificationSettingFragment())
                    }
                    SettingEventTypes.LANGUAGE_CHOOSER -> {
                        addFragmentToMain(MlLanguageChooserFragment())
                    }
                }
            }
        }
    }

    private fun openActivityInfoCenter(activityId: Int) {
        addFragmentToMain(ActivityInfoFragment.newInstance(activityId))
    }

    private fun openStudioCenter(event: OpenStudioEvent) {
        addFragmentToMain(StudioFragment.newInstance(event.studioId))
    }

    private fun openAiringScheduleCenter() {
        addFragmentToMain(AiringFragment())
    }

    private fun openStaffCenter(staffId: Int) {
        addFragmentToMain(StaffContainerFragment.newInstance(staffId))
    }

    private fun openCharacterCenter(characterId: Int) {
        addFragmentToMain(CharacterContainerFragment.newInstance(characterId))
    }

    private fun openMediaListEditorCenter(meta: ListEditorMeta) {
        addFragmentToMain(EntryListEditorFragment.newInstance(meta))
    }

    private fun openMediaInfoCenter(meta: MediaInfoMeta) {
        addFragmentToMain(MediaInfoFragment.newInstance(meta))
    }

    private fun openNotificationCenter() {
        addFragmentToMain(NotificationFragment())
    }

    private fun addFragmentToMain(
        baseFragment: BaseFragment,
        transactionAnimation: FragmentAnimationType = FragmentAnimationType.FADE
    ) {
        getTransactionWithAnimation(transactionAnimation)
            .add(R.id.main_fragment_container, baseFragment)
            .addToBackStack(null).commit()
    }


    internal data class HomeMenuItem(
        @IdRes val id: Int,
        @StringRes val text: Int,
        @DrawableRes val drawRes: Int,
        val order: Int,
        val fragment: BaseFragment
    )

}
