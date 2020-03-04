package com.revolgenx.anilib.activity

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.se.omapi.Session
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.pranavpandey.android.dynamic.support.activity.DynamicSystemActivity
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.controller.AppController
import com.revolgenx.anilib.controller.ThemeController
import com.revolgenx.anilib.dialog.AuthDialog
import com.revolgenx.anilib.event.SessionEvent
import com.revolgenx.anilib.fragment.DiscoverFragment
import com.revolgenx.anilib.fragment.DownloadFragment
import com.revolgenx.anilib.fragment.SeasonFragment
import com.revolgenx.anilib.fragment.SettingFragment
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.preference.token
import com.revolgenx.anilib.util.makePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.openid.appauth.*
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext


typealias OnOptionItemSelected = ((menuItem: MenuItem) -> Unit)?

class MainActivity : DynamicSystemActivity(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    companion object {
        private const val DISCOVER_POS = 0
        private const val SEASON_POS = 1
        private const val COLLECTION_POS = 2
        private const val DOWNLOAD_POS = 3
        private const val PROFILE_POS = 4
    }

    private val authIntent: String = "auth_intent_key"
    private var optionItemSelectedListeners = mutableListOf<OnOptionItemSelected>()
    private val authDialogTag = "auth_dialog_tag"

    override fun getLocale(): Locale? {
        return null
    }

    override fun getThemeRes(): Int {
        return ThemeController.appStyle
    }

    override fun onCustomiseTheme() {
        ThemeController.setLocalTheme()
    }

    private var currentPage: Int = 0

    fun addOptionItemSelectedListener(listener: OnOptionItemSelected) {
        optionItemSelectedListeners.add(listener)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            navView.menu.findItem(com.revolgenx.anilib.R.id.navAuth).title =
                getString(R.string.sign_in)
            navView.menu.findItem(R.id.navFeedId).isVisible = false
            navView.menu.findItem(R.id.navNotification).isVisible = false
        } else {
            navView.menu.findItem(R.id.navAuth).title = getString(R.string.sign_out)
            navView.menu.findItem(R.id.navFeedId).isVisible = true
            navView.menu.findItem(R.id.navNotification).isVisible = true
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
                                Uri.parse("https://anilist.co/api/v2/oauth/authorize") /* auth endpoint */,
                                Uri.parse("https://anilist.co/api/v2/oauth/token") /* token endpoint */
                            )

                        val clientId = "1836"
                        val redirectUri = Uri.parse("callback://anilib.app")
                        val builder = AuthorizationRequest.Builder(
                            serviceConfiguration,
                            clientId,
                            ResponseTypeValues.CODE,
                            redirectUri
                        )
                        val request = builder.build()
                        val authorizationService = AuthorizationService(this)

                        val extra = "com.revolgenx.anilib.HANDLE_AUTHORIZATION_RESPONSE"
                        val postAuthorizationIntent = Intent(this, MainActivity::class.java)
                            .also {
                                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                it.putExtra(authIntent, extra)
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
                ClientSecretBasic("vp81MRiYkWbP7qsjeUkoVR0n3oPpc7hIvYiJFxvA")

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (currentPage == 1) {
            menuInflater.inflate(R.menu.nav_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        optionItemSelectedListeners.forEach { it?.invoke(item) }
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        optionItemSelectedListeners.clear()
    }
}
