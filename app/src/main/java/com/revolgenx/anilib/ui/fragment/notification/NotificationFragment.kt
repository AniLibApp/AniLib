package com.revolgenx.anilib.ui.fragment.notification

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.ui.presenter.notification.NotificationPresenter
import com.revolgenx.anilib.ui.viewmodel.notification.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationFragment : BasePresenterFragment<NotificationModel>() {
    override val basePresenter: Presenter<NotificationModel>
        get() = NotificationPresenter(requireContext())
    override val baseSource: Source<NotificationModel>
        get() = viewModel.source ?: createSource()

    private val viewModel by viewModel<NotificationViewModel>()

    override fun createSource(): Source<NotificationModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_close)
            it.setTitle(R.string.notification)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }
}
