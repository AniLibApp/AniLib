package com.revolgenx.anilib.fragment.staff

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.meta.StaffMeta
import com.revolgenx.anilib.field.staff.StaffMediaRoleField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.StaffMediaRoleModel
import com.revolgenx.anilib.presenter.StaffMediaRolePresenter
import com.revolgenx.anilib.viewmodel.StaffMediaRoleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//staff roles
class StaffMediaRoleFragment : BasePresenterFragment<StaffMediaRoleModel>() {
    override val basePresenter: Presenter<StaffMediaRoleModel>
        get() = StaffMediaRolePresenter(requireContext())
    override val baseSource: Source<StaffMediaRoleModel>
        get() = viewModel.source ?: createSource()

    private lateinit var staffmeta: StaffMeta

    override fun createSource(): Source<StaffMediaRoleModel> {
        return viewModel.createSource()
    }

    private val viewModel by viewModel<StaffMediaRoleViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.elementAt(position)?.element?.type == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = StaffMeta::class.java.classLoader
        staffmeta = arguments?.getParcelable(StaffFragment.STAFF_META_KEY) ?: return
        viewModel.field.staffId = staffmeta.staffId
        super.onActivityCreated(savedInstanceState)
    }
}
