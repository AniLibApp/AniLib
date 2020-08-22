package com.revolgenx.anilib.fragment.user

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.constant.SearchTypes

class StudioFavouriteFragment :UserFavouriteFragment(){
    override val favouriteType: SearchTypes = SearchTypes.STUDIO


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(requireContext())
    }
}