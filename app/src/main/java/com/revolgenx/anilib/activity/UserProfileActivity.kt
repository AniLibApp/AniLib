package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.dialog.MessageDialog
import com.revolgenx.anilib.ui.dialog.UserFollowerDialog
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.data.model.user.UserFollowerCountModel
import com.revolgenx.anilib.data.model.user.UserProfileModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.preference.userName
import com.revolgenx.anilib.databinding.UserProfileAcitivtyLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.openLink
import com.revolgenx.anilib.util.prettyNumberFormat
import com.revolgenx.anilib.ui.viewmodel.user.UserProfileViewModel
import io.noties.markwon.recycler.MarkwonAdapter
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import kotlinx.android.synthetic.main.user_activity_genre_presenter.view.*
import kotlinx.android.synthetic.main.user_profile_acitivty_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

//todo://, handle review //stats
class UserProfileActivity : BaseDynamicActivity<UserProfileAcitivtyLayoutBinding>() {
    private lateinit var userMeta: UserMeta

    companion object {
        fun openActivity(context: Context, userMeta: UserMeta) {
            context.startActivity(Intent(context, UserProfileActivity::class.java).also {
                it.putExtra(USER_ACTIVITY_META_KEY, userMeta)
            })
        }

        const val USER_ACTIVITY_META_KEY = "USER_ACTIVITY_META_KEY"
    }


    private var userProfileModel: UserProfileModel? = null


    private val viewModel by viewModel<UserProfileViewModel>()
    private val toggleFollowCallback = { _: DialogInterface, which: Int ->
        when (which) {
            AlertDialog.BUTTON_POSITIVE -> {
                toggleFollow()
            }
        }
    }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserProfileAcitivtyLayoutBinding {
        return UserProfileAcitivtyLayoutBinding.inflate(inflater)
    }

    private fun toggleFollow() {
        if (::userMeta.isInitialized) {
            viewModel.toggleFollowField.userId = userMeta.userId
            if (loggedIn()) {
                viewModel.toggleFollow {
                    if (it.status == Status.SUCCESS) {
                        updateFollowView()
                    } else if (it.status == Status.ERROR) {
                        makeToast(R.string.operation_failed, icon = R.drawable.ic_error)
                    }
                }
            } else {
                makeToast(R.string.please_log_in, icon = R.drawable.ic_error)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent == null) return

        userMeta = if (intent.action == Intent.ACTION_VIEW) {
            val data = intent.data ?: return
            val paths = data.pathSegments
            val userId = paths[1].toIntOrNull()
            val username = paths[1].toString()
            UserMeta(
                userId,
                if (userId == null) username else null,
                userId == userId() || username == userName()
            )
        } else {
            intent.getParcelableExtra(USER_ACTIVITY_META_KEY) ?: return
        }

        setSupportActionBar(userToolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setToolbarTheme()

        userAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if ((abs(verticalOffset) >= (appBarLayout.totalScrollRange - userToolbar.height))) {
                return@OnOffsetChangedListener
            }

            userAvatar.pivotY = userAvatar.height.toFloat()
            userAvatar.pivotX = userAvatar.width.toFloat() / 2

            userAvatar.scaleX = 1 + (verticalOffset.toFloat() / (appBarLayout.totalScrollRange))
            userAvatar.scaleY = 1 + (verticalOffset.toFloat() / (appBarLayout.totalScrollRange))
        })

        userAvatar.hierarchy.apply {
            roundingParams =
                roundingParams?.setBorderColor(DynamicTheme.getInstance().get().backgroundColor)
        }

        viewModel.userProfileLiveData.observe(this) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    resourceStatusContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    updateView(res.data)
                }
                Status.ERROR -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    makeToast(R.string.error, icon = R.drawable.ic_error)
                }
                Status.LOADING -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
            }
        }

        viewModel.followerLiveData.observe(this) { res ->
            if (res.status == Status.SUCCESS) {
                updateFollowerView(res.data)
            }
        }


        if (savedInstanceState == null) {
            viewModel.userField.also {
                it.userId = userMeta.userId
                it.userName = userMeta.userName
            }
            viewModel.getProfile()
            viewModel.getFollower()
        } else {
            resolveChange()
        }
    }

    private fun resolveChange() {
        (supportFragmentManager.findFragmentByTag(MessageDialog.messageDialogTag) as? MessageDialog)?.let {
            it.onButtonClickedListener = toggleFollowCallback
        }
    }

    private fun updateFollowerView(followers: UserFollowerCountModel?) {
        followers?.followers?.let {
            followerTv.text =
                getString(R.string.s_followers).format("${it.prettyNumberFormat()} ")
        }

        followers?.following?.let {
            followingTv.text =
                getString(R.string.s_following).format("${it.prettyNumberFormat()} ")
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        menuInflater.inflate(R.menu.user_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.userShareMenu -> {
                userProfileModel?.siteUrl?.let {
                    openLink(it)
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateFollowView() {
        if (userProfileModel == null) return

        if (userProfileModel?.isFollowing == true) {
            userFollowTv.text = getString(R.string.following)
        } else {
            userFollowTv.text = getString(R.string.follow)
        }

        if (userProfileModel?.isBlocked == true) {
            userFollowTv.text = getString(R.string.blocked)
        }

    }

    private fun updateView(data: UserProfileModel?) {
        if (data == null) return
        userProfileModel = data

        userAvatar.setImageURI(data.avatar?.image)
        userBannerImage.setImageURI(data.bannerImage ?: data.avatar?.image)
        userNameTv.text = data.userName
        if (userId() != userMeta.userId) {
            userFollowTv.visibility = View.VISIBLE
        }

        supportActionBar?.title = data.userName

        if (data.isFollowing == true) {
            userFollowTv.text = getString(R.string.following)
        } else {
            userFollowTv.text = getString(R.string.follow)
        }

        if (data.isBlocked == true) {
            userFollowTv.text = getString(R.string.blocked)
        }


        data.about?.let {

            val adapter =
                MarkwonAdapter.create(R.layout.markwon_textview_layout, R.id.markdown_holder_tv)
            userAboutRecyclerView.adapter = adapter
            userAboutRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter.setMarkdown(
                MarkwonImpl.createHtmlInstance(this),
                it.html!!
//                combined
            )
            adapter.notifyDataSetChanged()
        }
        data.totalAnime?.let {
            totalCountTv.title = it.toString()
        }

        data.daysWatched?.let {
            daysWatchedTv.title = "%.1f".format(it)
        }

        data.animeMeanScore?.let {
            animeMeanScoreTv.title = it.toString()
        }

        data.totalManga?.let {
            totalMangaTv.title = it.toString()
        }

        data.chaptersRead?.let {
            chaptersReadTv.title = it.toString()
        }

        data.mangaMeanScore?.let {
            mangaMeanScoreTv.title = it.toString()
        }
        tagGenreRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        Adapter.builder(this).addSource(Source.fromList(data.genreOverView.toList()))
            .addPresenter(
                Presenter.simple<Pair<String, Int>>(
                    this,
                    R.layout.user_activity_genre_presenter,
                    0
                ) { view, genres ->
                    view.userGenreHeader.title = genres.first
                    view.userGenreHeader.subtitle = genres.second.toString()
                }).into(tagGenreRecyclerView)

        initListener()
    }

    @SuppressLint("RestrictedApi")
    private fun initListener() {
        userAnimeListFrameLayout.setOnClickListener {
            MediaListActivity.openActivity(
                this,
                MediaListMeta(userMeta.userId, null, MediaType.ANIME.ordinal)
            )
        }

        userMangaListFrameLayout.setOnClickListener {
            MediaListActivity.openActivity(
                this,
                MediaListMeta(userMeta.userId, null, MediaType.MANGA.ordinal)
            )
        }

        userFavouriteFrameLayout.setOnClickListener {
            ViewPagerContainerActivity.openActivity(
                this,
                ViewPagerContainerMeta(
                    ViewPagerContainerType.FAVOURITE,
                    UserMeta(userMeta.userId, userMeta.userName)
                )
            )
        }

        userStatsFrameLayout.setOnClickListener {
            PopupMenu(this, it).let {
                it.inflate(R.menu.user_stats_menu)
                it.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.userAnimeStats -> {
                            NavViewPagerContainerActivity.openActivity(
                                this,
                                NavViewPagerContainerMeta(
                                    NavViewPagerContainerType.ANIME_STATS,
                                    UserStatsMeta(userMeta, MediaType.ANIME.ordinal)
                                )
                            )
                            true
                        }
                        R.id.userMangaStats -> {
                            NavViewPagerContainerActivity.openActivity(
                                this,
                                NavViewPagerContainerMeta(
                                    NavViewPagerContainerType.MANGA_STATS,
                                    UserStatsMeta(userMeta, MediaType.MANGA.ordinal)
                                )
                            )
                            true
                        }
                        else -> false
                    }
                }
                it.show()
            }
        }

        followerFrameLayout.setOnClickListener {
            UserFollowerDialog.newInstance(FollowerMeta(userMeta.userId))
                .show(supportFragmentManager, "follower_dialog")
        }

        followingFrameLayout.setOnClickListener {
            UserFollowerDialog.newInstance(FollowerMeta(userMeta.userId, true))
                .show(supportFragmentManager, "following_dialog")
        }

        userFollowTv.setOnClickListener {
            if (userProfileModel!!.isBlocked == true) {
                openLink(userProfileModel!!.siteUrl)
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
                        it.onButtonClickedListener = toggleFollowCallback
                        it.show(supportFragmentManager, MessageDialog.messageDialogTag)
                    }
                }
                return@setOnClickListener
            }

            toggleFollow()
        }

        userAvatar.setOnClickListener {
            SimpleDraweeViewerActivity.openActivity(
                this,
                DraweeViewerMeta(userProfileModel!!.avatar!!.image)
            )
        }
        userBannerImage.setOnClickListener {
            SimpleDraweeViewerActivity.openActivity(
                this,
                DraweeViewerMeta(userProfileModel!!.bannerImage)
            )
        }
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: BaseEvent) {
//        when (event) {
//            is UserBrowseEvent -> {
//                openActivity(this, UserMeta(event.userId, null))
//            }
//            is ImageClickedEvent -> {
//                SimpleDraweeViewerActivity.openActivity(this, DraweeViewerMeta(event.meta.url))
//            }
//            is YoutubeClickedEvent -> {
//                openLink(event.meta.url)
//            }
//            is VideoClickedEvent -> {
//                ExoVideoInstance.getInstance().url = event.videoMeta.url
//                prepare()
//            }
//        }
//    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setToolbarTheme() {
        userCollapsingToolbar.setStatusBarScrimColor(
            DynamicTheme.getInstance().get().primaryColorDark
        )
        userCollapsingToolbar.setContentScrimColor(
            DynamicTheme.getInstance().get().primaryColor
        )
        userCollapsingToolbar.setCollapsedTitleTextColor(
            DynamicTheme.getInstance().get().tintPrimaryColor
        )
        userCollapsingToolbar.setBackgroundColor(
            DynamicTheme.getInstance().get().backgroundColor
        )
        userCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
    }

}