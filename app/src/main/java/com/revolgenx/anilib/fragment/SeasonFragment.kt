package com.revolgenx.anilib.fragment

import android.content.Context
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.CommonMediaModel
import com.revolgenx.anilib.model.field.SeasonField
import com.revolgenx.anilib.presenter.MediaPresenter
import com.revolgenx.anilib.viewmodel.SeasonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeasonFragment : BasePresenterFragment<CommonMediaModel>() {

    override fun title(context: Context): String {
        return context.getString(R.string.season)
    }

    private val viewModel by viewModel<SeasonViewModel>()

    override var basePresenter: Presenter<CommonMediaModel>? = null
        get() = if (field == null) MediaPresenter(context!!) else field

    override var baseSource: Source<CommonMediaModel>? = null
        get() = viewModel.seasonSource


    override fun createSource(): Source<CommonMediaModel> {
        return viewModel.createSource(SeasonField.create(context!!))
    }

}
