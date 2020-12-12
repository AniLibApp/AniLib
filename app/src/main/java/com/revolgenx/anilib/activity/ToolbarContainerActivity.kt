package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment
import com.revolgenx.anilib.databinding.ToolbarContainerActivityLayoutBinding
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import kotlinx.android.synthetic.main.toolbar_layout.*

class ToolbarContainerActivity :BaseDynamicActivity<ToolbarContainerActivityLayoutBinding>(){

    companion object {
        const val toolbarFragmentContainerKey = "toolbar_fragment_container_key"

        fun <T : BaseFragment> openActivity(
            context: Context,
            parcelableFragment: ParcelableFragment<T>,
            option: ActivityOptionsCompat? = null
        ) {
            context.startActivity(Intent(context, ToolbarContainerActivity::class.java).also {
                it.putExtra(toolbarFragmentContainerKey, parcelableFragment)
            }, option?.toBundle())
        }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ToolbarContainerActivityLayoutBinding {
        return ToolbarContainerActivityLayoutBinding.inflate(inflater)
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dynamicToolbar)

        val parcel =
            intent.getParcelableExtra<ParcelableFragment<BaseFragment>>(toolbarFragmentContainerKey)
                ?: return

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containerFrameLayout, parcel.clzz.newInstance().apply {
                this.arguments = parcel.bundle
            }).commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    override fun finishAfterTransition() {
        finish()
    }

    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }


}