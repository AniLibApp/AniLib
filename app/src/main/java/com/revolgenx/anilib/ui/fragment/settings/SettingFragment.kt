package com.revolgenx.anilib.ui.fragment.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.AboutActivity
import com.revolgenx.anilib.activity.ContainerActivity
import com.revolgenx.anilib.activity.ToolbarContainerActivity
import com.revolgenx.anilib.common.preference.*
import com.revolgenx.anilib.ui.dialog.ReleaseInfoDialog
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.databinding.SettingFragmentLayoutBinding
import com.revolgenx.anilib.ui.fragment.notification.NotificationSettingFragment
import com.revolgenx.anilib.ui.view.makeToast

class SettingFragment : BaseToolbarFragment<SettingFragmentLayoutBinding>() {

    override val title: Int = R.string.settings

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): SettingFragmentLayoutBinding {
        return SettingFragmentLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).also {
            it.supportActionBar?.setDisplayShowHomeEnabled(true)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.applicationSettingIv.setOnClickListener {
            ContainerActivity.openActivity(
                requireContext(),
                ParcelableFragment(
                    ApplicationSettingFragment::class.java,
                    null
                )
            )
        }

        binding.notificationSetting.setOnClickListener {
            ToolbarContainerActivity.openActivity(
                requireContext(),
                ParcelableFragment(
                    NotificationSettingFragment::class.java,
                    null
                )
            )
        }

        binding.adultContentPrefCardView.visibility = if(userEnabledAdultContent(requireContext())) View.VISIBLE else View.GONE

        binding.aboutItemView.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }
        binding.whatsNew.setOnClickListener {
            ReleaseInfoDialog().show(childFragmentManager, ReleaseInfoDialog.tag)
        }
    }
}
