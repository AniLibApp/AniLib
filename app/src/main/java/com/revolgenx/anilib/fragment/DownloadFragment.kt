package com.revolgenx.anilib.fragment

import android.content.Context
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BasePagerFragment

class DownloadFragment :BasePagerFragment(){

    override fun title(context: Context): String {
        return context.getString(R.string.download)
    }

}
