package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.iterator
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Adapter
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicPackageUtils
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.dialog.AuthenticationDialog
import com.revolgenx.anilib.dialog.ReleaseInfoDialog
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.event.*
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.fragment.settings.SettingFragment
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.fragment.home.RecommendationFragment
import com.revolgenx.anilib.fragment.home.SeasonFragment
import com.revolgenx.anilib.fragment.home.discover.DiscoverFragment
import com.revolgenx.anilib.meta.MediaListMeta
import com.revolgenx.anilib.meta.UserMeta
import com.revolgenx.anilib.preference.*
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.*
import com.revolgenx.anilib.view.navigation.BrowseFilterNavigationView
import com.revolgenx.anilib.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_layout.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.openid.appauth.*
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseDynamicActivity(), CoroutineScope,
    BrowseFilterNavigationView.AdvanceBrowseNavigationCallbackListener {
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

    private val discoverFragments by lazy {
        listOf(
            DiscoverFragment::class.java,
            SeasonFragment::class.java,
            RecommendationFragment::class.java
        )
    }

    private fun themeBottomNavigation() {
        bottomNav.color = DynamicTheme.getInstance().get().primaryColor
        bottomNav.textColor = DynamicTheme.getInstance().get().accentColor
    }


    override val layoutRes: Int = R.layout.activity_main


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

        bottomNav.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        setSupportActionBar(dynamicToolbar)
        themeBottomNavigation()

        mainViewPager.adapter = makePagerAdapter(
            BaseFragment.newInstances(
                discoverFragments
            )
        )

        mainViewPager.offscreenPageLimit = 3
        initListener()
        updateNavView()
        updateRightNavView()
        silentFetchUserInfo()
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
        mainBrowseFilterNavView.setFilter(filter, false)
    }

    private fun silentFetchUserInfo() {
        if (loggedIn()) {
            viewModel.getUserLiveData().observe(this, Observer {
                updateNavView()
            })
        }
    }


    @SuppressLint("RestrictedApi")
    private fun updateNavView() {
        if (navView == null) return

        if (!loggedIn()) {
            simpleNavView()
        } else {
            navView.menu.findItem(R.id.navFeedId).isVisible = false
            navView.menu.findItem(R.id.navAuth).title = getString(R.string.sign_out)
        }

        navView.getHeaderView(0).let { headerView ->
            val userAvatar = userAvatar()
            val userBanner = userBannerImage()
            val userName = userName()

            headerView.headerIconCardView.corner = dp(16f).toFloat()
            if (userAvatar.isNullOrEmpty()) {
                headerView.navHeaderIcon.setImageDrawable(
                    AppCompatDrawableManager.get().getDrawable(
                        context,
                        R.drawable.ic_main_with_background
                    )
                )

            } else {
                headerView.navHeaderIcon.setImageURI(userAvatar)
            }

            headerView.navHeaderIcon.setOnClickListener {
                if (loggedIn()) {
                    UserProfileActivity.openActivity(this, UserMeta(context.userId(), null, true))
                } else {
                    makeToast(R.string.please_log_in)
                }
            }

            headerView.navHeaderIcon.hierarchy.let {
                it.roundingParams =
                    it.roundingParams?.setBorderColor(
                        DynamicTheme.getInstance().get().tintAccentColor
                    )
            }

            DynamicTheme.getInstance().get().primaryColorDark

            headerView.navHeaderTitle.text = userName
            headerView.navHeaderTitle.setTextColor(ContextCompat.getColor(context, R.color.white))

            if (!userBanner.isNullOrEmpty()) {
                headerView.navHeaderBackground.setImageURI(userBanner)
            }

        }
    }

    private fun checkLoggedIn(): Boolean {
        return if (context.loggedIn()) {
            true
        } else {
            makeLogInSnackBar(mainViewPager)
            false
        }
    }


    @SuppressLint("RestrictedApi")
    private fun simpleNavView() {
        navView.menu.findItem(R.id.navAuth).title =
            getString(R.string.sign_in)
        with(navView.menu) {
            findItem(R.id.navFeedId).isVisible = false
            findItem(R.id.navAnimeListId).isVisible = false
            findItem(R.id.navMangaListId).isVisible = false
        }
    }


    private fun initListener() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            dynamicToolbar,
            R.string.nav_open,
            R.string.nav_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.navActivityId -> {
                    makeToast(R.string.in_progress, icon = R.drawable.ic_planning)
                    true
                }
                R.id.navSetting -> {
                    ContainerActivity.openActivity(
                        this,
                        ParcelableFragment(SettingFragment::class.java, bundleOf())
                    )
                    true
                }

                R.id.contribute -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                R.id.stageVersion -> {
                    BrowseSiteEvent().postEvent
                    true
                }
                R.id.navAnimeListId -> {
                    BrowseMediaListEvent(
                        MediaListMeta(
                            context.userId(),
                            null,
                            MediaType.ANIME.ordinal
                        )
                    ).postEvent

                    true
                }

                R.id.navMangaListId -> {
                    MediaListActivity.openActivity(
                        this,
                        MediaListMeta(context.userId(), null, MediaType.MANGA.ordinal)
                    )
                    true
                }

                R.id.navAuth -> {
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
                    true
                }
                else -> false
            }
        }

        mainViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                bottomNav.menu.iterator().forEach {
                    it.isChecked = false
                }
                bottomNav.menu.getItem(position).isChecked = true
            }
        })

        bottomNav.setOnNavigationItemSelectedListener {
            bottomNav.menu.forEachIndexed { index, item ->
                if (it == item) {
                    mainViewPager.setCurrentItem(index, true)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        mainBrowseFilterNavView.setNavigationCallbackListener(this)

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
            drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            drawerLayout.isDrawerOpen(GravityCompat.END) -> {
                drawerLayout.closeDrawer(GravityCompat.END)
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
    fun onTagEvent(event: TagEvent) {
        when (event.tagType) {
            MediaTagFilterTypes.TAGS -> invalidateTagFilter(event.tagFields)
            MediaTagFilterTypes.GENRES -> invalidateGenreFilter(event.tagFields)
            MediaTagFilterTypes.STREAMING_ON -> invalidateStreamFilter(event.tagFields)
        }
    }


    private fun invalidateStreamFilter(list: List<TagField>) {
        viewModel.streamTagFields = list.toMutableList()
        mainBrowseFilterNavView.buildStreamAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateGenreFilter(list: List<TagField>) {
        viewModel.genreTagFields = list.toMutableList()
        mainBrowseFilterNavView.buildGenreAdapter(
            tagAdapter,
            list
        )
    }

    private fun invalidateTagFilter(list: List<TagField>) {
        viewModel.tagTagFields = list.toMutableList()
        mainBrowseFilterNavView.buildTagAdapter(
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
        }
    }

    override fun updateTags(tagType: MediaTagFilterTypes) {
        when (tagType) {
            MediaTagFilterTypes.TAGS -> {
                mainBrowseFilterNavView.invalidateTagAdapter(tagAdapter)
            }
            MediaTagFilterTypes.GENRES -> {
                mainBrowseFilterNavView.invalidateGenreAdapter(tagAdapter)
            }
            MediaTagFilterTypes.STREAMING_ON -> {
                mainBrowseFilterNavView.invalidateStreamAdapter(tagAdapter)
            }
        }
    }

    override fun getQuery(): String {
        return ""
    }

    override fun applyFilter() {
        mainBrowseFilterNavView.getFilter().let {
            BrowseFilterDataProvider.setBrowseFilterData(context, it)
            SearchActivity.openActivity(
                this, it
            )
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

}
