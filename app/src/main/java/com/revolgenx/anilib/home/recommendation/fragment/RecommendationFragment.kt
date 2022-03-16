package com.revolgenx.anilib.home.recommendation.fragment

import android.os.Bundle
import android.view.*
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationModel
import com.revolgenx.anilib.home.recommendation.event.RecommendationEvent
import com.revolgenx.anilib.home.recommendation.presenter.RecommendationPresenter
import com.revolgenx.anilib.home.recommendation.viewmodel.RecommendationViewModel
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendationFragment : BasePresenterFragment<RecommendationModel>(), EventBusListener {
    override val applyInset: Boolean = false
    override val basePresenter: Presenter<RecommendationModel>
        get() = RecommendationPresenter(requireContext(), viewLifecycleOwner, viewModel)
    override val baseSource: Source<RecommendationModel>
        get() = viewModel.source ?: createSource()

    override val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(
            requireContext(), R.layout.recommendation_shimmer_loader_layout
        )
    override var gridMaxSpan: Int = 4
    override var gridMinSpan: Int = 2

    private val viewModel by viewModel<RecommendationViewModel>()

    override fun createSource(): Source<RecommendationModel> {
        return viewModel.createSource()
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel.field) {
            sort = getRecommendationSort()
            onList = getRecommendationOnList()
        }
    }

    @Subscribe()
    fun onRecommendationEvent(event: RecommendationEvent) {
        when (event) {
            is RecommendationEvent.RecommendationFilterEvent -> {
                viewModel.field.onList = event.onList
                viewModel.field.sort = event.sort
                if (!visibleToUser) return

                createSource()
                invalidateAdapter()
            }
        }
    }

}
