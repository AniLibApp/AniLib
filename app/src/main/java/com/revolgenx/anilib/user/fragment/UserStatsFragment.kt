package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.View
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.user.data.field.UserStatsField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.common.viewmodel.getViewModelOwner
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.data.model.stats.BaseStatisticModel
import com.revolgenx.anilib.user.presenter.UserStatsPresenter
import com.revolgenx.anilib.user.viewmodel.UserStatsContainerSharedVM
import com.revolgenx.anilib.user.viewmodel.UserStatsViewModel
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    protected val sharedViewModel by viewModel<UserStatsContainerSharedVM>(owner = getViewModelOwner())

    protected open val type = MediaType.ANIME.ordinal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel.field) {
            userId = sharedViewModel.userId
            userName = sharedViewModel.userName
            type = this@UserStatsFragment.type
            userStatsType = statsType
        }
    }

}
