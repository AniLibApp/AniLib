package com.revolgenx.anilib.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment
import com.revolgenx.anilib.model.review.ReviewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewComposerFragment : BaseToolbarFragment() {
    override val title: Int = R.string.review_composer
    override val contentRes: Int = R.layout.review_composer_fragment_layout

    private val viewModel by viewModel<ReviewComposerViewModel>()

    private val reviewModel: ReviewModel? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.review_composer_menu, menu)
    }
}