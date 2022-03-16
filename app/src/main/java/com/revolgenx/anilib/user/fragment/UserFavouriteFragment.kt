package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.search.data.field.SearchTypes
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.viewmodel.getViewModelOwner
import com.revolgenx.anilib.user.presenter.UserFavouritePresenter
import com.revolgenx.anilib.user.viewmodel.UserFavouriteContainerSharedVM
import com.revolgenx.anilib.user.viewmodel.UserFavouriteViewModel
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class UserFavouriteFragment() : BasePresenterFragment<BaseModel>() {
    override val applyInset: Boolean = false
    override val basePresenter: Presenter<BaseModel>
        get() = UserFavouritePresenter(requireContext(), viewLifecycleOwner)

    override val baseSource: Source<BaseModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseModel> {
        return viewModel.createSource()
    }

    abstract val favouriteType: SearchTypes

    private val viewModel by viewModel<UserFavouriteViewModel>()
    protected val sharedViewModel by viewModel<UserFavouriteContainerSharedVM>(owner = getViewModelOwner())

    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3

    override fun getBaseLayoutManager(): RecyclerView.LayoutManager {
        return if (favouriteType == SearchTypes.STUDIO) {
            LinearLayoutManager(requireContext())
        } else {
            super.getBaseLayoutManager()
        }
    }

    override fun getItemSpanSize(position: Int): Int {
        if (favouriteType != SearchTypes.STUDIO) {
            return if (adapter?.getItemViewType(position)?.let {
                    it == SearchTypes.MANGA.ordinal || it == SearchTypes.ANIME.ordinal || it == SearchTypes.CHARACTER.ordinal || it == SearchTypes.STAFF.ordinal
                } == true) {
                1
            } else {
                span
            }
        }
        return super.getItemSpanSize(position)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.hasUserData ?: return
        with(viewModel.field) {
            userName = sharedViewModel.userName
            userId = sharedViewModel.userId
            favType = favouriteType
        }
    }
}