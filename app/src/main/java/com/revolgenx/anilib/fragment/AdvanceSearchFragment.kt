package com.revolgenx.anilib.fragment

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.field.search.MediaSearchField
import com.revolgenx.anilib.field.search.BaseAdvanceSearchField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.BaseModel
import com.revolgenx.anilib.model.search.filter.*
import com.revolgenx.anilib.presenter.search.AdvanceSearchPresenter
import com.revolgenx.anilib.viewmodel.AdvanceSearchFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdvanceSearchFragment : BasePresenterFragment<BaseModel>() {

    private val viewModel by viewModel<AdvanceSearchFragmentViewModel>()

    override val basePresenter: Presenter<BaseModel>
        get() = AdvanceSearchPresenter(requireContext())

    override val baseSource: Source<BaseModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseModel> {
        return viewModel.createSource(viewModel.field)
    }

    override fun onResume() {
        super.onResume()
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