package com.revolgenx.anilib.social.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.ui.presenter.ActivityUnionPresenter
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityUnionFragment:BasePresenterFragment<ActivityUnionModel>() {
    override val basePresenter: Presenter<ActivityUnionModel>
        get() = ActivityUnionPresenter(requireContext())
    override val baseSource: Source<ActivityUnionModel>
        get() = viewModel.source?: createSource()

    override var selfAddLayoutManager: Boolean = false

    private val viewModel:ActivityUnionFragmentViewModel by viewModel()

    override fun createSource(): Source<ActivityUnionModel> {
        return viewModel.createSource()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(requireContext())
    }
}