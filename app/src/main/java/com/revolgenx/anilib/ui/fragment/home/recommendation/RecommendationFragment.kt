package com.revolgenx.anilib.ui.fragment.home.recommendation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.recommendation.RecommendationModel
import com.revolgenx.anilib.databinding.RecommendationFragmentLayoutBinding
import com.revolgenx.anilib.ui.presenter.recommendation.RecommendationPresenter
import com.revolgenx.anilib.ui.viewmodel.home.recommendation.RecommendationViewModel
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecommendationFragment : BasePresenterFragment<RecommendationModel>() {
    override val basePresenter: Presenter<RecommendationModel>
        get() = RecommendationPresenter(requireContext(), viewLifecycleOwner, viewModel)
    override val baseSource: Source<RecommendationModel>
        get() = viewModel.source ?: createSource()

    override val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(
            requireContext(), R.layout.recommendation_shimmer_loader_layout
        )

    private val viewModel by viewModel<RecommendationViewModel>()

    private var _recommendationBinding: RecommendationFragmentLayoutBinding? = null
    private val recommendationBinding: RecommendationFragmentLayoutBinding get() = _recommendationBinding!!

    override fun createSource(): Source<RecommendationModel> {
        return viewModel.createSource()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _recommendationBinding =
            RecommendationFragmentLayoutBinding.inflate(inflater, container, false)
        recommendationBinding.recommendationLinearLayout.addView(v)
        return recommendationBinding.root
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

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        inflater.inflate(R.menu.recommendation_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recommendationBinding.initListener()
        recommendationBinding.recommendationOnListCheckBox.visibility =
            if (requireContext().loggedIn()) View.VISIBLE else View.GONE

        if (savedInstanceState == null) {
            with(viewModel.field) {
                sort = getRecommendationSort(requireContext())
                onList = getRecommendationOnList(requireContext())
            }
        }

    }

    private fun RecommendationFragmentLayoutBinding.initListener() {
        recommendationOnListCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (requireContext().loggedIn()) {
                viewModel.field.onList = isChecked
            } else {
                viewModel.field.onList = null
            }
            createSource()
            invalidateAdapter()
        }

        recommendationSortSpinner.onItemSelectedListener = null
        recommendationSortSpinner.onItemSelected { position ->
            viewModel.field.sort = position
            if (!visibleToUser) return@onItemSelected
            setRecommendationSort(requireContext(), position)
            createSource()
            invalidateAdapter()
        }

    }

}
