package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.revolgenx.anilib.search.data.field.SearchTypes

class StudioFavouriteFragment : UserFavouriteFragment(){
    override val favouriteType: SearchTypes = SearchTypes.STUDIO

}