package com.revolgenx.anilib.infrastructure.source.notification

import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.data.field.notification.NotificationField
import com.revolgenx.anilib.data.model.notification.NotificationModel
import com.revolgenx.anilib.infrastructure.service.notification.NotificationService
import com.revolgenx.anilib.common.infrastruture.source.BaseRecyclerSource
import io.reactivex.disposables.CompositeDisposable

class NotificationSource(
    field: NotificationField,
    private val notificationService: NotificationService,
    private val compositeDisposable: CompositeDisposable
) : BaseRecyclerSource<NotificationModel, NotificationField>(field) {
    override fun areItemsTheSame(first: NotificationModel, second: NotificationModel): Boolean {
        return first.baseId == second.baseId
    }


    override fun onPageOpened(page: Page, dependencies: List<Element<*>>) {
        super.onPageOpened(page, dependencies)
        field.page = pageNo
        notificationService.getNotifications(field, compositeDisposable) {
            postResult(page, it)
        }
    }
}
