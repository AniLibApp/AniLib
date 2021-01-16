package com.revolgenx.anilib.activity

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.iterator
import androidx.viewpager.widget.ViewPager
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Adapter
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.ui.dialog.*
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.data.field.TagChooserField
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.ui.fragment.notification.NotificationFragment
import com.revolgenx.anilib.data.meta.UserMeta
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.adapter.makePagerAdapter
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.data.model.home.HomePageOrderType
import com.revolgenx.anilib.databinding.ActivityMainBinding
import com.revolgenx.anilib.radio.ui.fragments.RadioFragment
import com.revolgenx.anilib.ui.fragment.home.discover.DiscoverContainerFragment
import com.revolgenx.anilib.ui.fragment.home.list.ListContainerFragment
import com.revolgenx.anilib.ui.fragment.home.profile.ProfileFragment
import com.revolgenx.anilib.ui.fragment.home.profile.UserLoginFragment
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.*
import com.revolgenx.anilib.ui.view.navigation.BrowseFilterNavigationView
import com.revolgenx.anilib.ui.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.openid.appauth.*
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseDynamicActivity<ActivityMainBinding>(), CoroutineScope,
    BrowseFilterNavigationView.AdvanceBrowseNavigationCallbackListener, EventBusListener {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val viewModel by viewModel<MainActivityViewModel>()

    companion object {
        private const val authIntent: String = "auth_intent_key"
        private const val authDialogTag = "auth_dialog_tag"
        private const val authorizationExtra = "com.revolgenx.anilib.HANDLE_AUTHORIZATION_RESPONSE"
    }

    private var pressedTwice = false

    private val tagAdapter: Adapter.Builder
        get() {
            return Adapter.builder(this)
        }

    private val discoverContainerFragment by lazy {
        DiscoverContainerFragment()
    }

    private val listContainerFragment by lazy {
        ListContainerFragment()
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

        if (getVersion(this) != DynamicPackageUtils.getAppVersion(this)) {
            ReleaseInfoDialog().show(supportFragmentManager, ReleaseInfoDialog.tag)
        }

        initListener()
        updateRightNavView()
        binding.updateView()
        silentFetchUserInfo()

        if (savedInstanceState == null) {
            checkForUpdate()
        }
    }

    private fun ActivityMainBinding.updateView() {
        val menuList = mutableListOf<HomeMenuItem>()

        menuList.add(HomeMenuItem(R.id.home_navigation_menu, R.string.home, R.drawable.ic_home, getHomePageOrderFromType(this@MainActivity, HomePageOrderType.HOME), discoverContainerFragment))
        if(loggedIn()){
            menuList.add(HomeMenuItem(R.id.list_navigation_menu, R.string.list, R.drawable.ic_media_list, getHomePageOrderFromType(this@MainActivity, HomePageOrderType.LIST), listContainerFragment))
        }
        menuList.add(HomeMenuItem(R.id.music_navigation_menu, R.string.radio, R.drawable.ic_radio, getHomePageOrderFromType(this@MainActivity, HomePageOrderType.RADIO),radioFragment))

        menuList.add(HomeMenuItem(R.id.profile_navigation_menu, R.string.profile, R.drawable.ic_person, HomePageOrderType.values().size, if(loggedIn()) profileFragment else loginFragment))

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
//                    val newIndex = if (loggedIn()) {
//                        index
//                    } else {
//                        if (index != 0) {
//                            index - 1
//                        } else {
//                            0
//                        }
//                    }
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
//                val newPosition = if (loggedIn()) {
//                    position
//                } else {
//                    if (position != 0) {
//                        position + 1
//                    } else {
//                        0
//                    }
//                }
                mainBottomNavView.menu.getItem(position).isChecked = true
            }
        })

    }

    private fun checkForUpdate(manualCheck: Boolean = false) {
        AppUpdater.startAppUpdater(this, supportFragmentManager, manualCheck)
    }


    private fun updateSharedPreference() {
        if (!isSharedPreferenceSynced(context)) {
            TagPrefUtil.reloadTagPref(context)
            TagPrefUtil.reloadGenrePref(context)
            TagPrefUtil.reloadStreamingPref(context)
            isSharedPreferenceSynced(context, true)
        }
    }


    private fun updateRightNavView() {
        val filter = BrowseFilterDataProvider.getBrowseFilterData(this) ?: return
        binding.mainBrowseFilterNavView.setFilter(filter, false)
    }

    private fun silentFetchUserInfo() {
        if (loggedIn()) {
            viewModel.getUserLiveData()
        }
    }

    fun goToList(type: Int) {
        binding.mainViewPager.setCurrentItem(1, false)

        (getViewPagerFragment(1) as? ListContainerFragment)?.goToListType(type)
    }


    private fun getViewPagerFragment(pos: Int) =
        supportFragmentManager.findFragmentByTag("android:switcher:${R.id.main_view_pager}:$pos")


    private fun initListener() {
        binding.mainBrowseFilterNavView.setNavigationCallbackListener(this)

        /**problem with transition
         * {@link https://github.com/facebook/fresco/issues/1445}*/
        ActivityCompat.setExitSharedElementCallback(this, object : SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: List<String?>?,
                sharedElements: List<View>,
                sharedElementSnapshots: List<View?>?
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                if (sharedElements.isEmpty()) {
                    return
                }
                for (view in sharedElements) {
                    if (view is SimpleDraweeView) {
                        view.drawable.setVisible(true, true)
                    }
                }
            }
        })

    }

    override fun onNewIntent(intent: Intent?) {
        checkIntent(intent)
        super.onNewIntent(intent)
    }


    private fun checkIntent(@Nullable intent: Intent?) {
        if (intent != null) {
            if (intent.hasExtra(authIntent)) {
                handleAuthorizationResponse(intent)
            }
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
    fun onTagEvent(event: TagEvent) {
        when (event.tagType) {
            MediaTagFilterTypes.TAGS -> invalidateTagFilter(event.tagFields)
            MediaTagFilterTypes.GENRES -> invalidateGenreFilter(event.tagFields)
            MediaTagFilterTypes.STREAMING_ON -> invalidateStreamFilter(event.tagFields)
            else -> {
            }
        }
    }

    @Subscribe
    fun onNotificationEvent(event: BrowseNotificationEvent) {
        ToolbarContainerActivity.openActivity(
            this,
            ParcelableFragment(
                NotificationFragment::class.java,
                bundleOf(
                    UserMeta.userMetaKey to UserMeta(
                        userId(),
                        null,
                        true
                    )
                )
            )
        )
    }


    private fun invalidateStreamFilter(list: List<TagField>) {
        viewModel.streamTagFields = list.toMutableList()
        binding.mainBrowseFilterNavView.buildStreamAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateGenreFilter(list: List<TagField>) {
        viewModel.genreTagFields = list.toMutableList()
        binding.mainBrowseFilterNavView.buildGenreAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateTagFilter(list: List<TagField>) {
        viewModel.tagTagFields = list.toMutableList()
        binding.mainBrowseFilterNavView.buildTagAdapter(
            tagAdapter,
            list
        )
    }

    override fun openTagChooserDialog(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                tagType,
                tags
            )
        ).show(supportFragmentManager, TagChooserDialogFragment::class.java.simpleName)
    }

    override fun onTagAdd(tags: List<TagField>, tagType: MediaTagFilterTypes) {
        when (tagType) {
            MediaTagFilterTypes.TAGS -> {
                viewModel.tagTagFields = tags.toMutableList()
            }
            MediaTagFilterTypes.GENRES -> {
                viewModel.genreTagFields = tags.toMutableList()
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                viewModel.streamTagFields = tags.toMutableList()
            }
            else -> {
            }
        }

    }

    override fun onTagRemoved(tag: String, tagType: MediaTagFilterTypes) {
        when (tagType) {
            MediaTagFilterTypes.TAGS -> {
                viewModel.tagTagFields.removeAll { it.tag == tag }
            }
            MediaTagFilterTypes.GENRES -> {
                viewModel.genreTagFields.removeAll { it.tag == tag }
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                viewModel.streamTagFields.removeAll { it.tag == tag }
            }
            else -> {
            }
        }
    }

    override fun updateTags(tagType: MediaTagFilterTypes) {
        when (tagType) {
            MediaTagFilterTypes.TAGS -> {
                binding.mainBrowseFilterNavView.invalidateTagAdapter(tagAdapter)
            }
            MediaTagFilterTypes.GENRES -> {
                binding.mainBrowseFilterNavView.invalidateGenreAdapter(tagAdapter)
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                binding.mainBrowseFilterNavView.invalidateStreamAdapter(tagAdapter)
            }
            else -> {
            }
        }
    }


    override fun getQuery(): String {
        return ""
    }

    override fun applyFilter() {
        binding.mainBrowseFilterNavView.getFilter().let {
            BrowseFilterDataProvider.setBrowseFilterData(context, it)
            SearchActivity.openActivity(
                this, it
            )
        }

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        }
    }


    internal data class HomeMenuItem(@IdRes val id:Int, @StringRes val text:Int, @DrawableRes val drawRes:Int, val order:Int, val fragment:BaseFragment)

}
