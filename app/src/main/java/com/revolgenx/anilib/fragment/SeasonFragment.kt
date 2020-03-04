package com.revolgenx.anilib.fragment

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MainActivity
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.fragment.base.BaseViewPagerPresenterFragment
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.field.SeasonField
import com.revolgenx.anilib.presenter.MediaPresenter
import com.revolgenx.anilib.util.makeToast
import com.revolgenx.anilib.viewmodel.SeasonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonFragment : BasePresenterFragment<CommonMediaModel>() {

    private val viewModel by viewModel<SeasonViewModel>()

    override val basePresenter: Presenter<CommonMediaModel> by lazy {
        MediaPresenter(context!!)
    }

    override val baseSource: Source<CommonMediaModel>
        get() {
            return viewModel.seasonSource
        }

    override fun createSource(): Source<CommonMediaModel> {
        return viewModel.createSource(SeasonField.create(context!!))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        invalidateAdapter()

        isVisible

        (activity as MainActivity).addOptionItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navSetting -> {
                    makeToast("yeasss")
                }
            }
        }
    }

}
