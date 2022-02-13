package com.revolgenx.anilib.review.fragment

import android.view.*
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.review.dialog.ReviewFilterBottomSheet
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

    override val menuRes: Int = R.menu.all_review_fragment_menu
    override val setHomeAsUp: Boolean = true
    override val titleRes: Int = R.string.reviews
    override val toolbarColorType: Int = Theme.ColorType.BACKGROUND

    override fun createSource(): Source<ReviewModel> {
        return viewModel.createSource()
    }

    override fun onToolbarMenuSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reviewsFilterMenu -> {
                ReviewFilterBottomSheet.newInstance(viewModel.field.reviewSort).also {
                    it.positiveCallback = {
                        it?.let {
                            viewModel.field.reviewSort = it
                            createSource()
                            invalidateAdapter()
                        }
                    }
                    it.show(requireContext())
                }
                true
            }
            else -> {
                super.onToolbarMenuSelected(item)
            }
        }
    }

}