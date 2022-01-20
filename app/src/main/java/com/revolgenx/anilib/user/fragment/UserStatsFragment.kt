package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.View
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.UserConstant
import com.revolgenx.anilib.user.data.meta.UserStatsMeta
import com.revolgenx.anilib.user.data.model.stats.BaseStatisticModel
import com.revolgenx.anilib.user.presenter.UserStatsPresenter
import com.revolgenx.anilib.user.viewmodel.UserStatsViewModel

abstract class UserStatsFragment : BasePresenterFragment<BaseStatisticModel>() {

    abstract val viewModel: UserStatsViewModel
    abstract val statsType: UserStatsField.UserStatsType
    override val basePresenter: Presenter<BaseStatisticModel>
        get() = UserStatsPresenter(requireContext())

    override val baseSource: Source<BaseStatisticModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseStatisticModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userStatsMeta =
            arguments?.getParcelable<UserStatsMeta>(UserConstant.USER_STATS_META_KEY)
        if(userStatsMeta != null){
            with(viewModel.field) {
                userId = userStatsMeta.userMeta.userId
                userName = userStatsMeta.userMeta.userName
                type = userStatsMeta.type
                userStatsType = statsType
            }
        }
    }

}
