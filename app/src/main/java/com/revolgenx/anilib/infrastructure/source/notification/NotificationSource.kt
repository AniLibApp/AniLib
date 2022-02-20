package com.revolgenx.anilib.infrastructure.source.notification

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.notification.service.NotificationService
import com.revolgenx.anilib.common.source.BaseRecyclerSource
import com.revolgenx.anilib.notification.data.field.NotificationField
import com.revolgenx.anilib.notification.data.model.NotificationModel
import io.reactivex.disposables.CompositeDisposable

class NotificationSource(
    field: NotificationField,
    private val notificationService: NotificationService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<NotificationModel, NotificationField>(field) {
    override fun areItemsTheSame(first: NotificationModel, second: NotificationModel): Boolean {
        return first.id == second.id
    }


    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        notificationService.getNotifications(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
