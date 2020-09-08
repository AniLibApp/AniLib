package com.revolgenx.anilib.fragment.review

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.dialog.ReviewsFilterDialog
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.review.ReviewModel
import com.revolgenx.anilib.presenter.review.ReviewPresenter
import com.revolgenx.anilib.viewmodel.review.AllReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllReviewFragment : BasePresenterFragment<ReviewModel>() {
    override val basePresenter: Presenter<ReviewModel>
        get() = ReviewPresenter(requireContext())
    override val baseSource: Source<ReviewModel>
        get() = viewModel.source ?: viewModel.createSource()

    private val viewModel by viewModel<AllReviewViewModel>()

    override fun createSource(): Source<ReviewModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.let {
            it.title = getString(R.string.reviews)
            it.setHomeAsUpIndicator(R.drawable.ic_close)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        if (savedInstanceState != null) {
            childFragmentManager.findFragmentByTag(ReviewsFilterDialog::class.java.simpleName)
                ?.let {
                    (it as ReviewsFilterDialog).positiveCallback = {
                        it?.let { viewModel.field.reviewSort = it }
                        createSource()
                        invalidateAdapter()
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
            android.R.id.home -> {
                finishActivity()
                true
            }
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