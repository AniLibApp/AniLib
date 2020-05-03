package com.revolgenx.anilib.fragment.home

import android.os.Bundle
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.presenter.notification.NotificationPresenter
import com.revolgenx.anilib.source.notification.NotificationSource
import com.revolgenx.anilib.viewmodel.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : BasePresenterFragment<NotificationModel>() {
    override val basePresenter: Presenter<NotificationModel>
        get() = NotificationPresenter()
    override val baseSource: Source<NotificationModel>
        get() = viewModel.source ?: createSource()

    private val viewModel by viewModel<NotificationViewModel>()

    override fun createSource(): Source<NotificationModel> {
        return viewModel.createSource(viewModel.field)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
