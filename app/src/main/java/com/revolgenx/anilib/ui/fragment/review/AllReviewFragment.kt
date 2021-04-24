package com.revolgenx.anilib.ui.fragment.review

import android.os.Bundle
import android.view.*
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.dialog.ReviewsFilterDialog
import com.revolgenx.anilib.common.ui.fragment.BasePresenterToolbarFragment
import com.revolgenx.anilib.data.model.review.ReviewModel
import com.revolgenx.anilib.ui.presenter.review.ReviewPresenter
import com.revolgenx.anilib.ui.viewmodel.review.AllReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllReviewFragment : BasePresenterToolbarFragment<ReviewModel>() {
    override val basePresenter: Presenter<ReviewModel>
        get() = ReviewPresenter(requireContext())
    override val baseSource: Source<ReviewModel>
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<AllReviewViewModel>()

    override val setHomeAsUp: Boolean = true
    override val titleRes: Int = R.string.reviews


    override val loadingPresenter: Presenter<Unit>
        get() = Presenter.forLoadingIndicator(
            requireContext(), R.layout.review_shimmer_loader_layout
        )

    override fun createSource(): Source<ReviewModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            childFragmentManager.findFragmentByTag(ReviewsFilterDialog::class.java.simpleName)
                ?.let {
                    (it as ReviewsFilterDialog).positiveCallback = {
                        it?.let {
                            viewModel.field.reviewSort = it
                            createSource()
                            invalidateAdapter()
                        }
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reviews_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reviewsFilterMenu -> {
                ReviewsFilterDialog.newInstance(viewModel.field.reviewSort).also {
                    it.positiveCallback = {
                        it?.let {
                            viewModel.field.reviewSort = it
                            createSource()
                            invalidateAdapter()
                        }
                    }
                    it.show(childFragmentManager, ReviewsFilterDialog::class.java.simpleName)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}