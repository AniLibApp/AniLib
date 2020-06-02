package com.revolgenx.anilib.fragment.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.notification.NotificationModel
import com.revolgenx.anilib.presenter.notification.NotificationPresenter
import com.revolgenx.anilib.viewmodel.NotificationViewModel
import kotlinx.android.synthetic.main.notification_fragment_layout.view.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
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
            it.title = getString(R.string.notification)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }
}
