package com.revolgenx.anilib.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.fragment.base.BasePagerFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import kotlinx.android.synthetic.main.setting_fragment_layout.*

class SettingFragment : BasePagerFragment() {

    override fun title(context: Context): String {
        return context.getString(R.string.profile)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.setting_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        themeItemView.setOnClickListener {
            ContainerActivity.openActivity(this.context!!, ParcelableFragment(ThemeControllerFragment::class.java, bundleOf()))
        }
    }
}
