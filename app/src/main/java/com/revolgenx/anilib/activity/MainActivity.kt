package com.revolgenx.anilib.activity

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import com.facebook.drawee.view.SimpleDraweeView
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.dialog.AuthDialog
import com.revolgenx.anilib.event.ListEditorEvent
import com.revolgenx.anilib.fragment.*
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.model.field.BaseField
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.preference.token
import com.revolgenx.anilib.util.makePagerAdapter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.openid.appauth.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : DynamicSystemActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var currentPage: Int = 0


    companion object {
        private const val authIntent: String = "auth_intent_key"
        private const val authDialogTag = "auth_dialog_tag"
        private const val authorizationExtra = "com.revolgenx.anilib.HANDLE_AUTHORIZATION_RESPONSE"


    }

    override fun getLocale(): Locale? {
        return null
    }

    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    override fun setStatusBarColor(color: Int) {
        super.setStatusBarColor(color)
        setWindowStatusBarColor(statusBarColor);
    }

    override fun setNavigationBarTheme(): Boolean {
        return AppController.instance.isThemeNavigationBar
    }

    private fun themeBottomNavigation() {
        bottomNav.color = DynamicTheme.getInstance().get().primaryColor
        bottomNav.textColor = DynamicTheme.getInstance().get().accentColor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerForEvent()
        drawerLayout.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        bottomNav.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        statusBarColor = statusBarColor
        setSupportActionBar(mainToolbar)

        noSwipeViewPager.adapter = makePagerAdapter(
            BaseFragment.newInstances(
                listOf(
                    DiscoverFragment::class.java,
                    SeasonFragment::class.java,
                    DownloadFragment::class.java
                )
            )
        )
        themeBottomNavigation()
        noSwipeViewPager.offscreenPageLimit = 3
        currentPage = noSwipeViewPager.currentItem
        noSwipeViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                invalidateOptionsMenu()
            }
        })
        updateNavView()
        initListener()
    }


    private fun updateNavView() {
        if (navView == null) return

        if (!loggedIn()) {
            simpleNavView()
        } else {
            loggedInNavView()
        }
    }

    private fun loggedInNavView() {
        navView.menu.findItem(R.id.navAuth).title = getString(R.string.sign_out)
    }

    private fun simpleNavView() {
        navView.menu.findItem(R.id.navAuth).title =
            getString(R.string.sign_in)
        navView.menu.findItem(R.id.navFeedId).isVisible = false
        navView.menu.findItem(R.id.navNotification).isVisible = false
        navView.getHeaderView(0).navHeaderIcon.setImageResource(R.mipmap.ic_launcher)
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

                R.id.navAuth -> {
                    if (loggedIn()) {
                        loggedIn(false)
                        token("")
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

        bottomNav.setOnNavigationItemSelectedListener {
            val position = when (it.itemId) {
                R.id.discoverMenu -> 0
                R.id.seasonMenu -> 1
                R.id.collectionMenu -> 2
                R.id.downloadMenu -> 3
                else -> 0
            }
            if (position in 0..4) {
                noSwipeViewPager.setCurrentItem(position, false)
                true
            } else
                false
        }


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
                    loggedIn(true)
                    token(accessToken)
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
    fun onListEditEvent(event: ListEditorEvent) {

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            event.sharedElement,
            ViewCompat.getTransitionName(event.sharedElement) ?: ""
        )
        ContainerActivity.openActivity(
            this,
            ParcelableFragment(
                ListEditorFragment::class.java,
                bundleOf(
                    BaseField.MEDIA_ID_KEY to event.id,
                    BaseField.MEDIA_COVER_URL_KEY to event.coverImage,
                    BaseField.MEDIA_BANNER_URL_KEY to event.bannerImage
                )
            )
            , options
        )

    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }


    override fun onDestroy() {
        unRegisterForEvent()
        job.cancel()
        super.onDestroy()
    }
}
