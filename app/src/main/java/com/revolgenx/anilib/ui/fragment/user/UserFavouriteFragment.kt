package com.revolgenx.anilib.ui.fragment.user

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.UserMeta
import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.ui.presenter.user.UserFavouritePresenter
import com.revolgenx.anilib.ui.viewmodel.user.UserFavouriteViewModel
import com.revolgenx.anilib.ui.viewmodel.user.UserProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class UserFavouriteFragment() : BasePresenterFragment<BaseModel>() {
    override val basePresenter: Presenter<BaseModel>
        get() = UserFavouritePresenter(requireContext(), viewLifecycleOwner)

    override val baseSource: Source<BaseModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<BaseModel> {
        return viewModel.createSource()
    }

    lateinit var userProfileViewModel: UserProfileViewModel

    companion object {
        const val USER_FAVOURITE_PARCELABLE_KEY = "user_favourite_parcelable_key"
    }

    abstract val favouriteType: SearchTypes

    private val viewModel by viewModel<UserFavouriteViewModel>()

    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.classLoader = UserMeta::class.java.classLoader
        val userMeta = arguments?.getParcelable<UserMeta>(USER_FAVOURITE_PARCELABLE_KEY)
        if(userMeta != null){
            viewModel.field.also {
                it.userName = userMeta.userName
                it.userId = userMeta.userId
                it.favType = favouriteType
            }
        }

        if(::userProfileViewModel.isInitialized){
            viewModel.field.also {
                it.userName = userProfileViewModel.userField.userName
                it.userId = userProfileViewModel.userField.userId
                it.favType = favouriteType
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter?.getItemViewType(position)?.let {
                        it == SearchTypes.MANGA.ordinal || it == SearchTypes.ANIME.ordinal || it == SearchTypes.CHARACTER.ordinal || it == SearchTypes.STAFF.ordinal
                    } == true) {
                    1
                } else {
                    span
                }
            }
        }
    }
}