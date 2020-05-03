package com.revolgenx.anilib.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
        return viewModel.createSource(viewModel.field)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val notView = inflater.inflate(R.layout.notification_fragment_layout, container, false)
        with(activity as ContainerActivity) {
            setSupportActionBar(notView.dynamicToolbar)
            this.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
            supportActionBar!!.title = getString(R.string.notification)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        notView.notificationContainerFrameLayout.addView(view)
        return notView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finishActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
