package com.revolgenx.anilib.ui.fragment.notification

import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.common.ui.fragment.BasePresenterToolbarFragment
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.ui.presenter.notification.NotificationPresenter
import com.revolgenx.anilib.ui.viewmodel.notification.NotificationViewModel
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
