package com.revolgenx.anilib.notification.fragment

import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BasePresenterToolbarFragment
import com.revolgenx.anilib.notification.data.model.NotificationModel
import com.revolgenx.anilib.notification.presenter.NotificationPresenter
import com.revolgenx.anilib.notification.viewmodel.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : BasePresenterToolbarFragment<NotificationModel>() {
    override val basePresenter: Presenter<NotificationModel>
        get() = NotificationPresenter(requireContext())
    override val baseSource: Source<NotificationModel>
        get() = viewModel.source ?: createSource()

    private val viewModel by viewModel<NotificationViewModel>()

    override val titleRes: Int = R.string.notification
    override val setHomeAsUp: Boolean = true

    override fun createSource(): Source<NotificationModel> {
        return viewModel.createSource()
    }
}
