package com.revolgenx.anilib.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.*
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.appwidget.ui.fragment.AiringWidgetConfigFragment
import com.revolgenx.anilib.ui.dialog.*
import com.revolgenx.anilib.common.event.*
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.user.data.meta.UserMeta
import com.revolgenx.anilib.home.data.meta.HomePageOrderType
import com.revolgenx.anilib.databinding.ActivityMainBinding
import com.revolgenx.anilib.radio.ui.fragments.RadioFragment
import com.revolgenx.anilib.social.ui.fragments.ActivityUnionFragment
import com.revolgenx.anilib.app.about.fragment.AboutFragment
import com.revolgenx.anilib.home.discover.fragment.DiscoverContainerFragment
import com.revolgenx.anilib.home.profile.fragment.UserContainerFragment
import com.revolgenx.anilib.home.profile.fragment.UserLoginFragment
import com.revolgenx.anilib.app.setting.fragment.NotificationSettingFragment
import com.revolgenx.anilib.review.fragment.AllReviewFragment
import com.revolgenx.anilib.app.setting.fragment.*
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.*
import com.revolgenx.anilib.activity.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.social.ui.bottomsheet.SpoilerBottomSheetFragment
import com.revolgenx.anilib.social.ui.fragments.activity_composer.message.ActivityMessageComposerContainerFragment
import com.revolgenx.anilib.social.ui.fragments.activity_composer.reply.ActivityReplyComposerContainerFragment
import com.revolgenx.anilib.social.ui.fragments.activity_composer.text.ActivityTextComposerContainerFragment
import com.revolgenx.anilib.social.ui.fragments.info.ActivityInfoFragment
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.activity.event.ActivityEventListener
import com.revolgenx.anilib.activity.viewmodel.MainSharedVM
import com.revolgenx.anilib.airing.fragment.AiringFragment
import com.revolgenx.anilib.character.fragment.CharacterContainerFragment
import com.revolgenx.anilib.friend.fragment.UserFriendContainerFragment
import com.revolgenx.anilib.home.list.fragment.MediaListCollectionContainerFragment
import com.revolgenx.anilib.media.fragment.MediaInfoContainerFragment
import com.revolgenx.anilib.user.fragment.MediaListingFragment
import com.revolgenx.anilib.notification.fragment.NotificationFragment
import com.revolgenx.anilib.review.fragment.ReviewComposerFragment
import com.revolgenx.anilib.review.fragment.ReviewFragment
import com.revolgenx.anilib.search.fragment.SearchFragment
import com.revolgenx.anilib.app.setting.fragment.EditTagFilterFragment
import com.revolgenx.anilib.common.event.AuthenticateEvent
import com.revolgenx.anilib.common.event.BaseEvent
import com.revolgenx.anilib.common.event.SessionEvent
import com.revolgenx.anilib.entry.fragment.MediaListEntryFragment
import com.revolgenx.anilib.staff.fragment.StaffContainerFragment
import com.revolgenx.anilib.studio.fragment.StudioFragment
import com.revolgenx.anilib.notification.viewmodel.NotificationStoreViewModel
import com.revolgenx.anilib.user.fragment.UserMediaListCollectionContainerFragment
import com.revolgenx.anilib.home.event.ChangeViewPagerPageEvent
import com.revolgenx.anilib.home.event.MainActivityPage
import kotlinx.coroutines.launch
import net.openid.appauth.*

class MainActivity : BaseDynamicActivity<ActivityMainBinding>(), CoroutineScope, EventBusListener {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val viewModel by viewModel<MainActivityViewModel>()

    private val mainSharedVM by viewModel<MainSharedVM>()
    private val notificationStoreVM by viewModel<NotificationStoreViewModel>()
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var authorizationService: AuthorizationService? = null


    private val fragmentLifeCycleCallback = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, f.javaClass.simpleName)
                param(FirebaseAnalytics.Param.SCREEN_CLASS, f.javaClass.simpleName)
            }
        }
    }

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

    private val alListContainerFragment by lazy {
        MediaListCollectionContainerFragment()
    }

    private val activityUnionFragment by lazy {
        ActivityUnionFragment()
    }

    private val radioFragment by lazy {
        RadioFragment()
    }

    private val profileFragment by lazy {
        UserContainerFragment()
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
        doIfNotDevFlavor {
            supportFragmentManager.registerFragmentLifecycleCallbacks(
                fragmentLifeCycleCallback,
                true
            )
        }
    }


    override fun onStop() {
        super.onStop()
        disposeAuthorizationService()
        doIfNotDevFlavor {
            supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifeCycleCallback)
        }
        unRegisterForEvent()
    }

    private fun disposeAuthorizationService() {
        authorizationService?.dispose()
        authorizationService = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //update before layout inflate
        updateSharedPreference()
        super.onCreate(savedInstanceState)
        checkReleaseInfo()
        binding.updateView()
        silentFetchUserInfo()
        checkIntent(intent)
        registerForResult()
    }

    private fun registerForResult() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val intent = result.data ?: return@registerForActivityResult
                        handleAuthorizationResponse(intent)
                    }
                    Activity.RESULT_CANCELED -> {
                        makeToast(R.string.cancelled)
                        dismissAuthLoadingDialog()
                    }
                }
            }
    }

    private fun checkReleaseInfo() {
        if (getVersion(this) != DynamicPackageUtils.getVersionName(this)) {
            ReleaseInfoDialog().show(supportFragmentManager, ReleaseInfoDialog.tag)
        }
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
                        mainSharedVM.mediaListCurrentTab.value = MediaType.ANIME.ordinal
                    }
                    LauncherShortcuts.MANGA -> {
                        binding.mainBottomNavView.selectedItemId = R.id.list_navigation_menu
                        mainSharedVM.mediaListCurrentTab.value = MediaType.MANGA.ordinal
                    }
//                    LauncherShortcuts.RADIO -> {
//                        binding.mainBottomNavView.selectedItemId = R.id.music_navigation_menu
//                    }
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
                getHomePageOrderFromType(HomePageOrderType.HOME),
                discoverContainerFragment
            )
        )

        loginContinue(false) {
            menuList.add(
                HomeMenuItem(
                    R.id.list_navigation_menu,
                    R.string.list,
                    R.drawable.ic_media_list,
                    getHomePageOrderFromType(HomePageOrderType.LIST),
                    alListContainerFragment
                )
            )
            menuList.add(
                HomeMenuItem(
                    R.id.activity_navigation_menu,
                    R.string.social,
                    R.drawable.ic_activity_union,
                    getHomePageOrderFromType(HomePageOrderType.ACTIVITY),
                    activityUnionFragment
                )
            )
        }
//        menuList.add(
//            HomeMenuItem(
//                R.id.music_navigation_menu,
//                R.string.radio,
//                R.drawable.ic_radio,
//                getHomePageOrderFromType(HomePageOrderType.RADIO),
//                radioFragment
//            )
//        )

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


        mainBottomNavView.setOnItemSelectedListener {
            mainBottomNavView.menu.forEachIndexed { index, item ->
                if (it == item) {
                    mainViewPager.setCurrentItem(index, true)
                }
            }
            false
        }

        mainViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                mainBottomNavView.menu.getItem(position).isChecked = true
            }
        })

    }

    private fun updateSharedPreference() {
        if (!isSharedPreferenceSynced()) {
            TagPrefUtil.reloadTagPref(context)
            TagPrefUtil.reloadGenrePref(context)
            TagPrefUtil.reloadStreamingPref(context)
            isSharedPreferenceSynced(true)
        }
    }


    private fun silentFetchUserInfo() {
        if (loggedIn()) {
            viewModel.getUserLiveData().observe(this) {
                notificationStoreVM.setUnreadNotificationCount(it.unreadNotificationCount ?: 0)
            }
        }
    }

    @Subscribe
    fun goToPageEvent(event: ChangeViewPagerPageEvent) {
        val homePageOrder = when (event.data) {
            MainActivityPage.HOME -> {
                getHomePageOrderFromType(HomePageOrderType.HOME)
            }
            MainActivityPage.LIST -> {
                getHomePageOrderFromType(HomePageOrderType.LIST)
            }
//            MainActivityPage.RADIO -> {
//                getHomePageOrderFromType(HomePageOrderType.RADIO)
//            }
        }

        binding.mainViewPager.setCurrentItem(homePageOrder, false)
    }

    override fun onNewIntent(intent: Intent) {
        checkIntent(intent)
        super.onNewIntent(intent)
    }


    private fun checkIntent(@Nullable intent: Intent?) {
        if (intent != null) {

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
                        "activity" -> {
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
                    intent.getIntExtra(ENTRY_LIST_DATA_KEY, -1).takeIf { it != -1 }?.let {
                        openMediaListEditorCenter(it)
                    }
                }
            }

            intent.action = ""
        }
    }


    private fun handleAuthorizationResponse(@NonNull intent: Intent) {
        val response = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        if (ex != null) {
            Timber.w(ex)
            makeToast(msg = getString(R.string.failed_to_sign_in, ex.message))
        }
        if (response != null) {
            authorizationService = authorizationService ?: AuthorizationService(this)
            val clientAuth: ClientAuthentication =
                ClientSecretBasic(BuildConfig.anilistclientSecret)

            authorizationService?.performTokenRequest(
                response.createTokenExchangeRequest(),
                clientAuth
            ) { tokenResponse, exception ->
                if (exception != null) {
                    Timber.e(exception, "Token Exchange failed")
                } else {
                    val accessToken = tokenResponse?.accessToken!!
                    logIn(accessToken)
                    SessionEvent(true).postEvent
                    startActivity(Intent(this.context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    dismissAuthLoadingDialog()
                    finish()
                }
            }
        }
    }

    private fun dismissAuthLoadingDialog() {
        (supportFragmentManager.findFragmentByTag(authDialogTag) as? DynamicDialogFragment)?.dismiss()
    }


    override fun onBackPressed() {
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
            Handler(Looper.myLooper()!!).postDelayed({
                pressedTwice = false
            }, 1000)
        }
        pressedTwice = true
    }


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }


    @Subscribe
    fun signInEvent(event: AuthenticateEvent) {
        if (loggedIn()) {
            logOut()
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
            launch(Dispatchers.IO) {
                authorizationService = AuthorizationService(this@MainActivity)
                val requestIntent = authorizationService!!.getAuthorizationRequestIntent(request)
                resultLauncher?.launch(requestIntent)
            }
        }
    }

    @Subscribe
    fun onBaseEvent(event: BaseEvent) {
        when (event) {
            is OpenUserProfileEvent -> {
                addFragmentToMain(
                    UserContainerFragment.newInstance(
                        UserMeta(
                            event.userId,
                            event.username
                        )
                    )
                )
            }

            is OpenSpoilerContentEvent -> {
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
                openMediaListEditorCenter(event.mediaId)
            }

            is OpenMediaInfoEvent -> {
                openMediaInfoCenter(event.meta)
            }

            is OpenMediaListingEvent -> {
                addFragmentToMain(MediaListingFragment.newInstance(event.mediaIdsIn))
            }

            is OpenUserMediaListEvent -> {
                addFragmentToMain(UserMediaListCollectionContainerFragment.newInstance(event.meta))
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

            is OpenStudioEvent -> {
                openStudioCenter(event)
            }

            is OpenAiringScheduleEvent -> {
                openAiringScheduleCenter()
            }

            is OpenReviewComposerEvent -> {
                addFragmentToMain(ReviewComposerFragment.newInstance(event.mediaId))
            }

            is OpenActivityTextComposer -> {
                addFragmentToMain(ActivityTextComposerContainerFragment())
            }

            is OpenActivityMessageComposer -> {
                addFragmentToMain(ActivityMessageComposerContainerFragment.newInstance(event.recipientId))
            }

            is OpenActivityReplyComposer -> {
                addFragmentToMain(ActivityReplyComposerContainerFragment.newInstance(event.activityId))
            }

            is OpenActivityInfoEvent -> {
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
                    SettingEventTypes.ADD_REMOVE_TAG_FILTER -> {
                        addFragmentToMain(EditTagFilterFragment.newInstance((event.data as TagSettingEventMeta).meta))
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

    private fun openMediaListEditorCenter(mediaId: Int) {
        loginContinue {
            addFragmentToMain(MediaListEntryFragment.newInstance(mediaId))
        }
    }

    private fun openMediaInfoCenter(meta: MediaInfoMeta) {
        addFragmentToMain(MediaInfoContainerFragment.newInstance(meta))
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
