package com.revolgenx.anilib.fragment.staff

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.event.meta.StaffMeta
import com.revolgenx.anilib.field.staff.StaffMediaRoleField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.StaffMediaRoleModel
import com.revolgenx.anilib.presenter.StaffMediaRolePresenter
import com.revolgenx.anilib.viewmodel.StaffMediaRoleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffMediaRoleFragment : BasePresenterFragment<StaffMediaRoleModel>() {
    override val basePresenter: Presenter<StaffMediaRoleModel>
        get() = StaffMediaRolePresenter(requireContext())
    override val baseSource: Source<StaffMediaRoleModel>
        get() = viewModel.source ?: createSource()

    private lateinit var staffmeta: StaffMeta
    private val field by lazy {
        StaffMediaRoleField().also {
            it.staffId = staffmeta.staffId
        }
    }

    override fun createSource(): Source<StaffMediaRoleModel> {
        return viewModel.createSource(field)
    }

    private val viewModel by viewModel<StaffMediaRoleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = FlexboxLayoutManager(context).also { manager ->
            manager.justifyContent = JustifyContent.SPACE_EVENLY
            manager.alignItems = AlignItems.CENTER
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = StaffMeta::class.java.classLoader
        staffmeta = arguments?.getParcelable(StaffFragment.STAFF_META_KEY) ?: return
        super.onActivityCreated(savedInstanceState)
    }
}
