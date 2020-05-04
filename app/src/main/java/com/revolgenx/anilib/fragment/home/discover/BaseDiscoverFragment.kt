package com.revolgenx.anilib.fragment.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.discover_fragment_layout.view.*
import kotlinx.android.synthetic.main.discover_garland_layout.view.*

abstract class BaseDiscoverFragment : BaseFragment(), BaseDiscoverHelper {

    protected lateinit var discoverLayout: ViewGroup
    protected lateinit var garlandLayout: View


    protected val loadingPresenter: Presenter<Void>
        get() {
            return Presenter.forLoadingIndicator(
                requireContext(), R.layout.loading_layout
            )
        }

    protected val errorPresenter: Presenter<Void>
        get() {
            return Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
        }

    protected val emptyPresenter: Presenter<Void>
        get() {
            return Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.discover_fragment_layout, container, false)
        discoverLayout = v.discoverLinearLayout
        return v
    }

    override fun addView(
        view: View,
        title: String,
        onClick: ((which: Int) -> Unit)
    ) {
        garlandLayout =
            LayoutInflater.from(view.context).inflate(R.layout.discover_garland_layout, null)

        garlandLayout.garlandTitleTv.setOnClickListener {
            onClick.invoke(0)
        }
        garlandLayout.garlandSettingIv.setOnClickListener {
            onClick.invoke(1)
        }

        garlandLayout.garlandTitleTv.text = title
        garlandLayout.garlandContainer.addView(view)
        discoverLayout.addView(garlandLayout)
    }

}