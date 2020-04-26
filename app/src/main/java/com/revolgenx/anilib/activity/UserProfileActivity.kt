package com.revolgenx.anilib.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.revolgenx.anilib.dialog.UserFollowerDialog
import com.revolgenx.anilib.event.*
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.meta.*
import com.revolgenx.anilib.model.user.UserFollowerCountModel
import com.revolgenx.anilib.model.user.UserProfileModel
import com.revolgenx.anilib.preference.userId
import com.revolgenx.anilib.repository.util.Status
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.*
import com.revolgenx.anilib.video.ExoVideoInstance
import com.revolgenx.anilib.viewmodel.UserProfileViewModel
import io.noties.markwon.recycler.MarkwonAdapter
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import kotlinx.android.synthetic.main.user_activity_genre_presenter.view.*
import kotlinx.android.synthetic.main.user_activity_layout.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

//todo://handle intent, handle review //stats
class UserProfileActivity : BasePopupVideoActivity() {
    override val layoutRes: Int = R.layout.user_activity_layout
    private lateinit var userMeta: UserMeta

    companion object {
        fun openActivity(context: Context, userMeta: UserMeta) {
            context.startActivity(Intent(context, UserProfileActivity::class.java).also {
                it.putExtra(USER_ACTIVITY_META_KEY, userMeta)
            })
        }

        const val USER_ACTIVITY_META_KEY = "USER_ACTIVITY_META_KEY"
        const val combined =
            "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                    "\n" +
                    "Why do we use it?\n" +
                    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
                    "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                    "\n" +
                    "Why do we use it?\n" +
                    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
                    "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                    "\n" +
                    "Why do we use it?\n" +
                    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
                    "\norem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                    "\n" +
                    "Why do we use it? <img width='440'  src='https://dream-wonderland.com/blog/wp-content/uploads/2017/08/Ohys-Raws-Aikatsu-Stars-69-TX-1280x720-x264-AAC.mp4_20170820_005650.721.jpg'>" +
                    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
                    "\n<video muted loop autoplay alt='markdown_spoiler' controls><source src='https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4' type='video/webm'>video###https://media1.tenor.com/images/fcdbd7e6438f73799ba0c0704b44daa6/tenor.gif?itemid=3558286</video>\n" +
                    "<span class='markdown_spoiler'><span><div class='youtube' alt='markdown_spoiler' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div></span></span>\n"

    }

    private var userProfileModel: UserProfileModel? = null


    private val viewModel by viewModel<UserProfileViewModel>()


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userMeta = intent.getParcelableExtra(USER_ACTIVITY_META_KEY) ?: return
        userMeta.userId = 122706
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
        }
    }

    private fun updateFollowerView(followers: UserFollowerCountModel?) {
        followers?.followers?.let {
            followerTv.text =
                getString(R.string.s_followers).format("${it.prettyNumberFormat()} ")
        }

        followers?.following?.let {
            followingTv.text = getString(R.string.s_following).format("${it.prettyNumberFormat()} ")
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
        genreRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        Adapter.builder(this).addSource(Source.fromList(data.genreOverView.toList())).addPresenter(
            Presenter.simple<Pair<String, Int>>(
                this,
                R.layout.user_activity_genre_presenter,
                0
            ) { view, genres ->
                view.userGenreHeader.title = genres.first
                view.userGenreHeader.subtitle = genres.second.toString()
            }).into(genreRecyclerView)

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
            }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: BaseEvent) {
        when (event) {
            is UserBrowseEvent -> {
                openActivity(this, UserMeta(event.userId, null))
            }
            is ImageClickedEvent -> {
                SimpleDraweeViewerActivity.openActivity(this, DraweeViewerMeta(event.meta.url))
            }
            is YoutubeClickedEvent -> {
                openLink(event.meta.url)
            }
            is VideoClickedEvent -> {
                ExoVideoInstance.getInstance().url = event.videoMeta.url
                prepare()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setToolbarTheme() {
        userCollapsingToolbar.setStatusBarScrimColor(DynamicTheme.getInstance().get().primaryColorDark)
        userCollapsingToolbar.setContentScrimColor(DynamicTheme.getInstance().get().primaryColor)
        userCollapsingToolbar.setCollapsedTitleTextColor(DynamicTheme.getInstance().get().tintPrimaryColor)
        userCollapsingToolbar.setBackgroundColor(DynamicTheme.getInstance().get().backgroundColor)
        userCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT)
    }

}