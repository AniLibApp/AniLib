package com.revolgenx.anilib.ui.fragment.home.recommendation

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.recommendation.RecommendationModel
import com.revolgenx.anilib.infrastructure.event.RecommendationEvent
import com.revolgenx.anilib.ui.presenter.recommendation.RecommendationPresenter
import com.revolgenx.anilib.ui.viewmodel.home.recommendation.RecommendationViewModel
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendationFragment : BasePresenterFragment<RecommendationModel>() , EventBusListener {
    override val basePresenter: Presenter<RecommendationModel>
        get() = RecommendationPresenter(requireContext(), viewLifecycleOwner, viewModel)
    override val baseSource: Source<RecommendationModel>
        get() = viewModel.source ?: createSource()

    override val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(
            requireContext(), R.layout.recommendation_shimmer_loader_layout
        )

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
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItemViewType(position) == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            with(viewModel.field) {
                sort = getRecommendationSort(requireContext())
                onList = getRecommendationOnList(requireContext())
            }
        }

    }

    @Subscribe()
    fun onRecommendationEvent(event:RecommendationEvent){
        when(event){
            is RecommendationEvent.RecommendationFilterEvent->{
                viewModel.field.onList = event.onList
                viewModel.field.sort = event.sort
                if (!visibleToUser) return

                createSource()
                invalidateAdapter()
            }
        }
    }

}
