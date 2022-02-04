package com.revolgenx.anilib.list.fragment

import android.os.Bundle
import android.view.View

abstract class MediaListCollectionStoreFragment : BaseMediaListCollectionFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLoggedInUser) {
            listCollectionStoreVM.dataSetChangeLiveData.observe(viewLifecycleOwner) {
                if(it == viewModel.currentGroupNameHistory){
                    //uncomment later
//                    viewModel.reevaluateGroupNameWithCount()
//                    notifyDataSetChanged()
                }
            }
        }
    }
}