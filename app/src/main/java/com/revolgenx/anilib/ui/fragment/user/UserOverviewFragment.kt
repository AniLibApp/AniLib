package com.revolgenx.anilib.ui.fragment.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.constant.UserConstant
import com.revolgenx.anilib.data.meta.UserMeta
import com.revolgenx.anilib.databinding.UserActivityGenrePresenterBinding
import com.revolgenx.anilib.databinding.UserOverviewFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.repository.util.Status
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.ui.viewmodel.user.UserProfileViewModel
import com.revolgenx.anilib.util.getOrDefault
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserOverviewFragment : BaseLayoutFragment<UserOverviewFragmentLayoutBinding>() {

    private val userProfileViewModel by viewModel<UserProfileViewModel>()

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserOverviewFragmentLayoutBinding {
        return UserOverviewFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userMeta: UserMeta = arguments?.getParcelable(UserConstant.USER_META_KEY) ?: return

        userProfileViewModel.userProfileLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data ?: return@observe
                    binding.apply {
                        episodesWatchedTv.title = data.episodesWatched.getOrDefault().toString()
                        daysWatchedTv.title = "%.1f".format(data.daysWatched.getOrDefault())
                        animeMeanScoreTv.title = data.animeMeanScore.getOrDefault().toString()

                        volumeReadTv.title = data.volumeRead.getOrDefault().toString()
                        chaptersReadTv.title = data.chaptersRead.getOrDefault().toString()
                        mangaMeanScoreTv.title = data.mangaMeanScore.getOrDefault().toString()

                        tagGenreRecyclerView.layoutManager =
                            GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)

                        Adapter.builder(viewLifecycleOwner)
                            .addSource(Source.fromList(data.genreOverView.toList()))
                            .addPresenter(
                                Presenter.simple<Pair<String, Int>>(
                                    requireContext(),
                                    R.layout.user_activity_genre_presenter,
                                    0
                                ) { view, genres ->
                                    val genreBind = UserActivityGenrePresenterBinding.bind(view)
                                    genreBind.userGenreHeader.title = genres.first
                                    genreBind.userGenreHeader.subtitle = genres.second.toString()
                                }).into(tagGenreRecyclerView)


                        data.about?.html?.let { about ->
                            MarkwonImpl.createHtmlInstance(requireContext())
                                .setMarkdown(userAboutTv, about)

                            if (userAboutTv.text.isEmpty()) {
                                userAboutTv.setText(R.string.no_description)
                            }
                        }
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }


        if (savedInstanceState == null && isVisible) {
            with(userProfileViewModel.userField) {
                userId = userMeta.userId
                userName = userMeta.userName
            }
            userProfileViewModel.getProfile()
        }
    }

}