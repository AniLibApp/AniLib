package com.revolgenx.anilib.review.fragment

import android.os.Bundle
import android.view.*
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.review.dialog.ReviewFilterDialog
import com.revolgenx.anilib.common.ui.fragment.BasePresenterToolbarFragment
import com.revolgenx.anilib.review.data.model.ReviewModel
import com.revolgenx.anilib.review.presenter.ReviewPresenter
import com.revolgenx.anilib.review.viewmodel.AllReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllReviewFragment : BasePresenterToolbarFragment<ReviewModel>() {
    override val basePresenter: Presenter<ReviewModel>
        get() = ReviewPresenter(requireContext())
    override val baseSource: Source<ReviewModel>
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<AllReviewViewModel>()

    override val setHomeAsUp: Boolean = true
    override val titleRes: Int = R.string.reviews
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND

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
            childFragmentManager.findFragmentByTag(ReviewFilterDialog::class.java.simpleName)
                ?.let {
                    (it as ReviewFilterDialog).positiveCallback = {
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
                ReviewFilterDialog.newInstance(viewModel.field.reviewSort).also {
                    it.positiveCallback = {
                        it?.let {
                            viewModel.field.reviewSort = it
                            createSource()
                            invalidateAdapter()
                        }
                    }
                    it.show(childFragmentManager, ReviewFilterDialog::class.java.simpleName)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}