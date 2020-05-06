package com.revolgenx.anilib.fragment.stats

import android.os.Bundle
import android.view.View
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.field.stats.UserStatsField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.meta.UserStatsMeta
import com.revolgenx.anilib.model.user.stats.BaseStatsModel
import com.revolgenx.anilib.presenter.stats.UserStatsPresenter
import com.revolgenx.anilib.viewmodel.UserStatsViewModel

abstract class UserStatsFragment : BasePresenterFragment<BaseStatsModel>() {

    abstract val viewModel: UserStatsViewModel
    abstract val statsType: UserStatsField.UserStatsType
    override val basePresenter: Presenter<BaseStatsModel>
        get() = UserStatsPresenter(requireContext())

    override val baseSource: Source<BaseStatsModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseStatsModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userStatsMeta =
            arguments?.getParcelable<UserStatsMeta>(StatsOverviewFragment.USER_STATS_PARCEL_KEY)
        with(viewModel.field) {
            userId = userStatsMeta?.userMeta?.userId
            userName = userStatsMeta?.userMeta?.userName
            type = userStatsMeta?.type
            userStatsType = statsType
        }
    }

}
