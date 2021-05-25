package com.revolgenx.anilib.social.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.databinding.ActivityUnionFragmentLayoutBinding
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.ui.presenter.ActivityUnionPresenter
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityUnionFragment:BasePresenterFragment<ActivityUnionModel>() {
    override val basePresenter: Presenter<ActivityUnionModel>
        get() = ActivityUnionPresenter(requireContext())
    override val baseSource: Source<ActivityUnionModel>
        get() = viewModel.source?: createSource()

    override var selfAddLayoutManager: Boolean = false

    private val viewModel:ActivityUnionFragmentViewModel by viewModel()

    private var _uBinding:ActivityUnionFragmentLayoutBinding? = null
    private val uBinding get() =_uBinding!!

    override fun createSource(): Source<ActivityUnionModel> {
        return viewModel.createSource()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v=  super.onCreateView(inflater, container, savedInstanceState)

        _uBinding = ActivityUnionFragmentLayoutBinding.inflate(inflater, container, false)
        uBinding.activityUnionContainer.addView(v)

        return uBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _uBinding = null
    }
}