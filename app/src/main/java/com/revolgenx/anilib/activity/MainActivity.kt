package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.iterator
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Adapter
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.dialog.AuthDialog
import com.revolgenx.anilib.dialog.TagChooserDialogFragment
import com.revolgenx.anilib.event.*
import com.revolgenx.anilib.meta.MediaListMeta
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.fragment.*
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.fragment.home.DiscoverFragment
import com.revolgenx.anilib.fragment.home.DownloadFragment
import com.revolgenx.anilib.fragment.home.RecommendationFragment
import com.revolgenx.anilib.fragment.home.SeasonFragment
import com.revolgenx.anilib.meta.DraweeViewerMeta
import com.revolgenx.anilib.meta.UserMeta
import com.revolgenx.anilib.preference.*
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.*
import com.revolgenx.anilib.view.navigation.BrowseFilterNavigationView
import com.revolgenx.anilib.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.openid.appauth.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseDynamicActivity(), CoroutineScope,
    BrowseFilterNavigationView.AdvanceBrowseNavigationCallbackListener
    , TagChooserDialogFragment.OnDoneListener {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val viewModel by viewModel<MainActivityViewModel>()

    companion object {
        private const val authIntent: String = "auth_intent_key"
        private const val authDialogTag = "auth_dialog_tag"
        private const val authorizationExtra = "com.revolgenx.anilib.HANDLE_AUTHORIZATION_RESPONSE"
    }

    private val tagAdapter: Adapter.Builder
        get() {
            return Adapter.builder(this)
        }

    private fun themeBottomNavigation() {
        bottomNav.color = DynamicTheme.getInstance().get().primaryColor
        bottomNav.textColor = DynamicTheme.getInstance().get().accentColor
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override val layoutRes: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNav.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        statusBarColor = statusBarColor
        setSupportActionBar(mainToolbar)
        themeBottomNavigation()

        mainViewPager.adapter = makePagerAdapter(
            BaseFragment.newInstances(
                listOf(
                    DiscoverFragment::class.java,
                    SeasonFragment::class.java,
                    RecommendationFragment::class.java,
                    DownloadFragment::class.java
                )
            )
        )

        mainViewPager.offscreenPageLimit = 3
        initListener()
        updateNavView()
        updateRightNavView()
        silentFetchUserInfo()
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
            navView.menu.findItem(R.id.navAuth).title = getString(R.string.sign_out)
        }

        navView.getHeaderView(0).let { headerView ->
            val userAvatar = userAvatar()!!
            val userBanner = userBannerImage()!!
            val userName = userName()

            if (userAvatar.isEmpty()) {
                headerView.navHeaderIcon.setImageDrawable(
                    AppCompatDrawableManager.get().getDrawable(
                        context,
                        R.mipmap.ic_launcher
                    )
                )

            } else {
                headerView.navHeaderIcon.setImageURI(userAvatar)
            }

            headerView.navHeaderIcon.setOnClickListener {
                if (checkLoggedIn())
                    UserProfileActivity.openActivity(this, UserMeta(context.userId(), null, true))
            }

            headerView.navHeaderIcon.hierarchy.let {
                it.roundingParams = it.roundingParams?.setBorderColor(DynamicTheme.getInstance().get().accentColorDark)
            }

            DynamicTheme.getInstance().get().primaryColorDark

            headerView.navHeaderTitle.text = userName
            headerView.navHeaderTitle.setTextColor(ContextCompat.getColor(context, R.color.white))

            if (userBanner.isNotEmpty()) {
                headerView.navHeaderBackground.setImageURI(userAvatar)
            }

        }
    }

    private fun checkLoggedIn(): Boolean {
        return if(context.loggedIn()){
            true
        }else{
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
            findItem(R.id.navNotification).isVisible = false
            findItem(R.id.navAnimeListId).isVisible = false
            findItem(R.id.navMangaListId).isVisible = false
        }
    }


    private fun initListener() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            mainToolbar,
            R.string.nav_open,
            R.string.nav_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navSetting -> {
                    ContainerActivity.openActivity(
                        this,
                        ParcelableFragment(SettingFragment::class.java, bundleOf())
                    )
                    true
                }

                R.id.navAnimeListId -> {
                    MediaListActivity.openActivity(
                        this,
                        MediaListMeta(context.userId(), null, MediaType.ANIME.ordinal)
                    )
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
//                        SessionEvent(false).postEvent
                        startActivity(Intent(this.context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                        finish()
                    } else {
                        AuthDialog.newInstance().show(supportFragmentManager, authDialogTag)
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
                    startActivity(Intent(this.context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                    (supportFragmentManager.findFragmentByTag(authDialogTag) as? DynamicDialogFragment)?.dismiss()
                }
            }
        }
    }


    /*Events*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBaseEvent(event: BaseEvent) {
        when (event) {
            is BrowseMediaEvent -> {
                startActivity(Intent(this, MediaBrowseActivity::class.java).apply {
                    this.putExtra(MediaBrowseActivity.MEDIA_BROWSER_META, event.mediaBrowserMeta)
                })
            }
            is ListEditorEvent -> {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    event.sharedElement,
                    ViewCompat.getTransitionName(event.sharedElement) ?: ""
                )
                ContainerActivity.openActivity(
                    this,
                    ParcelableFragment(
                        EntryListEditorFragment::class.java,
                        bundleOf(
                            EntryListEditorFragment.LIST_EDITOR_META_KEY to event.meta
                        )
                    )
                    , options
                )
            }
            is BrowseGenreEvent -> {
                BrowseActivity.openActivity(
                    this, event.genre
                )
            }
            is BrowseEvent -> {
                BrowseActivity.openActivity(this)
            }

            is ImageClickedEvent -> {
                SimpleDraweeViewerActivity.openActivity(this, DraweeViewerMeta(event.meta.url))
            }

            is YoutubeClickedEvent -> {
                openLink(event.meta.url)
            }
            is VideoClickedEvent -> {
                openLink(event.videoMeta.url)
            }
        }
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }

    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }


    override fun onDone(fragmentTag: String?, list: List<TagField>) {
        when (fragmentTag) {
            BrowseActivity.TAG_CHOOSER_DIALOG_TAG -> {
                invalidateTagFilter(list)
            }
            BrowseActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                invalidateGenreFilter(list)
            }
            BrowseActivity.STREAM_CHOOSER_DIALOG_TAG -> {
                invalidateStreamFilter(list)
            }
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


    /**
     * Called by advance search filter nav view
     * */
    override fun onGenreChoose(tags: List<TagField>) {
        openTagChooserDialog(tags, BrowseActivity.GENRE_CHOOSER_DIALOG_TAG)
    }

    /**
     * Called by advance search filter nav view
     * */
    override fun onStreamChoose(tags: List<TagField>) {
        openTagChooserDialog(tags, BrowseActivity.STREAM_CHOOSER_DIALOG_TAG)
    }

    /**
     * Called by advance search filter nav view
     * */
    override fun onTagChoose(tags: List<TagField>) {
        openTagChooserDialog(tags, BrowseActivity.TAG_CHOOSER_DIALOG_TAG)
    }

    override fun onGenreAdd(tags: List<TagField>) {
        viewModel.genreTagFields = tags.toMutableList()
    }

    override fun onTagAdd(tags: List<TagField>) {
        viewModel.tagTagFields = tags.toMutableList()
    }

    override fun onStreamAdd(tags: List<TagField>) {
        viewModel.streamTagFields = tags.toMutableList()
    }

    override fun onTagRemoved(tag: String) {
        viewModel.tagTagFields?.find { it.tag == tag }?.let {
            viewModel.tagTagFields?.remove(it)
        }
    }

    override fun onGenreRemoved(tag: String) {
        viewModel.genreTagFields?.find { it.tag == tag }?.let {
            viewModel.genreTagFields?.remove(it)
        }
    }

    override fun onStreamRemoved(tag: String) {
        viewModel.streamTagFields?.find { it.tag == tag }?.let {
            viewModel.streamTagFields?.remove(it)
        }
    }

    override fun updateGenre() {
        mainBrowseFilterNavView.invalidateGenreAdapter(tagAdapter)
    }

    override fun updateStream() {
        mainBrowseFilterNavView.invalidateStreamAdapter(tagAdapter)
    }

    override fun updateTags() {
        mainBrowseFilterNavView.invalidateTagAdapter(tagAdapter)
    }


    override fun getQuery(): String {
        return ""
    }

    override fun applyFilter() {
        mainBrowseFilterNavView.getFilter()?.let {
            BrowseFilterDataProvider.setBrowseFilterData(context, it)
            BrowseActivity.openActivity(
                this, it
            )
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    private fun openTagChooserDialog(tags: List<TagField>, dialogTag: String) {
        TagChooserDialogFragment.newInstance(
            TagChooserField(
                getString(R.string.tags),
                tags
            )
        ).apply {
            onDoneListener(this@MainActivity)
            show(supportFragmentManager, dialogTag)
        }
    }

}
