package com.revolgenx.anilib.fragment.user

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.constant.BrowseTypes

class StudioFavouriteFragment :UserFavouriteFragment(){
    override val favouriteType: BrowseTypes = BrowseTypes.STUDIO


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(requireContext())
    }
}