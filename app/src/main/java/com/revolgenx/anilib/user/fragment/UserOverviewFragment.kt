package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.loadBioByDefault
import com.revolgenx.anilib.common.repository.util.Resource
import com.revolgenx.anilib.databinding.ResourceStatusContainerLayoutBinding
import com.revolgenx.anilib.databinding.UserActivityGenrePresenterBinding
import com.revolgenx.anilib.databinding.UserOverviewFragmentLayoutBinding
import com.revolgenx.anilib.social.factory.AlMarkwonFactory
import com.revolgenx.anilib.social.markwon.AlStringUtil.anilify
import com.revolgenx.anilib.user.viewmodel.UserOverViewFragmentVM
import com.revolgenx.anilib.util.getOrDefault
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserOverviewFragment : BaseUserFragment<UserOverviewFragmentLayoutBinding>() {
    private val viewModel by viewModel<UserOverViewFragmentVM>()
    private val userModel get() = viewModel.overviewLiveData.value?.data

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): UserOverviewFragmentLayoutBinding {
        return UserOverviewFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!sharedViewModel.hasUserData) return

        with(viewModel.field) {
            userId = sharedViewModel.userId
            userName = sharedViewModel.userName
        }

        viewModel.overviewLiveData.observe(viewLifecycleOwner) {
            sharedViewModel.userLiveData.value = it
            when (it) {
                is Resource.Success -> {
                    binding.resourceStatusLayout.statusSuccess()
                    it.data?.let {
                        binding.bind()
                    }
                }
                is Resource.Error -> {
                    binding.resourceStatusLayout.statusError()
                }
                is Resource.Loading -> {
                    binding.resourceStatusLayout.statusLoading()
                }
            }
        }

        if (savedInstanceState != null) {
            viewModel.getUserOverView()
            visibleToUser = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (!visibleToUser) {
            viewModel.getUserOverView()
        }
        visibleToUser = true
    }


    private fun UserOverviewFragmentLayoutBinding.bind() {
        val user = userModel ?: return

        val animeStats = user.statistics?.anime
        val mangaStats = user.statistics?.manga
        episodesWatchedTv.title = animeStats?.episodesWatched.getOrDefault().toString()
        daysWatchedTv.title = "%.1f".format(animeStats?.daysWatched.getOrDefault())
        animeMeanScoreTv.title = animeStats?.meanScore.getOrDefault().toString()

        volumeReadTv.title = mangaStats?.volumesRead.getOrDefault().toString()
        chaptersReadTv.title = mangaStats?.chaptersRead.getOrDefault().toString()
        mangaMeanScoreTv.title = mangaStats?.meanScore.getOrDefault().toString()

        tagGenreRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)

        Adapter.builder(viewLifecycleOwner)
            .addSource(Source.fromList(animeStats?.genres?.map { it.genre to it.count }
                ?: emptyList()))
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


        if (loadBioByDefault()) {
            loadBioCardVew.visibility = View.GONE
            aboutContainerLayout.visibility = View.VISIBLE
            if (user.about.isNullOrBlank()) {
                userAboutTv.setText(R.string.no_description)
            } else {
                AlMarkwonFactory.getMarkwon()
                    .setMarkdown(userAboutTv, anilify(user.about!!))
            }
        } else {
            aboutContainerLayout.visibility = View.GONE
            loadBioCardVew.visibility = View.VISIBLE
            loadBioCardVew.setOnClickListener {
                aboutContainerLayout.visibility = View.VISIBLE
                if (user.about.isNullOrBlank()) {
                    userAboutTv.setText(R.string.no_description)
                } else {
                    AlMarkwonFactory.getMarkwon()
                        .setMarkdown(userAboutTv, anilify(user.about!!))
                }
                loadBioCardVew.visibility = View.GONE
            }
        }
    }

    private fun ResourceStatusContainerLayoutBinding.statusSuccess() {
        resourceStatusContainer.visibility = View.GONE
        resourceProgressLayout.progressLayout.visibility = View.GONE
        resourceErrorLayout.errorLayout.visibility = View.GONE
    }

    private fun ResourceStatusContainerLayoutBinding.statusError() {
        resourceStatusContainer.visibility = View.VISIBLE
        resourceProgressLayout.progressLayout.visibility = View.GONE
        resourceErrorLayout.errorLayout.visibility = View.VISIBLE
    }

    private fun ResourceStatusContainerLayoutBinding.statusLoading() {
        resourceStatusContainer.visibility = View.VISIBLE
        resourceProgressLayout.progressLayout.visibility = View.VISIBLE
        resourceErrorLayout.errorLayout.visibility = View.GONE
    }
}