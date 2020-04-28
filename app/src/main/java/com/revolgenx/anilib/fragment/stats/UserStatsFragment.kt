package com.revolgenx.anilib.fragment.stats

import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.user.stats.BaseStatsModel
import com.revolgenx.anilib.presenter.stats.UserStatsPresenter
import com.revolgenx.anilib.viewmodel.UserStatsViewModel

abstract class UserStatsFragment : BasePresenterFragment<BaseStatsModel>() {

    abstract val viewModel: UserStatsViewModel

    override val basePresenter: Presenter<BaseStatsModel>
        get() = UserStatsPresenter(requireContext())

    override val baseSource: Source<BaseStatsModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseStatsModel> {
        return viewModel.createSource(viewModel.field)
    }

}
