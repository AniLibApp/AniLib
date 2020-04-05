package com.revolgenx.anilib.fragment.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.recommendation.RecommendationField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.recommendation.RecommendationModel
import com.revolgenx.anilib.presenter.recommendation.RecommendationPresenter
import com.revolgenx.anilib.viewmodel.RecommendationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendationFragment : BasePresenterFragment<RecommendationModel>() {
    override val basePresenter: Presenter<RecommendationModel>
        get() = RecommendationPresenter(requireContext())
    override val baseSource: Source<RecommendationModel>
        get() = viewModel.source ?: createSource()

    private val viewModel by viewModel<RecommendationViewModel>()

    private val field: RecommendationField by lazy {
        RecommendationField()
    }

    override fun createSource(): Source<RecommendationModel> {
        return viewModel.createSource(field)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        layoutManager = FlexboxLayoutManager(context).also { manager ->
//            manager.justifyContent = JustifyContent.SPACE_EVENLY
//            manager.alignItems = AlignItems.CENTER
//        }
        layoutManager =
            GridLayoutManager(
                this.context,
                if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
            )
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recommendation_menu, menu)
    }
}
