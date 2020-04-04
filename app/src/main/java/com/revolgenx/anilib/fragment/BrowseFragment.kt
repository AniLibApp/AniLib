package com.revolgenx.anilib.fragment

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.presenter.search.BrowsePresenter
import com.revolgenx.anilib.viewmodel.BrowseFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowseFragment : BasePresenterFragment<BaseModel>() {

    private val viewModel by viewModel<BrowseFragmentViewModel>()

    override val basePresenter: Presenter<BaseModel>
        get() = BrowsePresenter(requireContext(), viewLifecycleOwner)

    override val baseSource: Source<BaseModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseModel> {
        return viewModel.createSource(viewModel.field)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = FlexboxLayoutManager(context).also { manager ->
            manager.justifyContent = JustifyContent.SPACE_EVENLY
            manager.alignItems = AlignItems.CENTER
        }
    }

    fun search(filter: BaseSearchFilterModel?) {
        filter?.let {
            viewModel.field = it.toField()
            createSource()
            invalidateAdapter()
        }
    }

}