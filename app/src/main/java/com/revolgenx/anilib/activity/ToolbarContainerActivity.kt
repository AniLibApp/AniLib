package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.ParcelableFragment
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import kotlinx.android.synthetic.main.toolbar_layout.*

class ToolbarContainerActivity :BaseDynamicActivity(){
    override val layoutRes: Int = R.layout.toolbar_container_activity_layout

    companion object {
        private const val fragmentContainerKey = "fragment_container_key"

        fun <T : BaseFragment> openActivity(
            context: Context,
            parcelableFragment: ParcelableFragment<T>,
            option: ActivityOptionsCompat? = null
        ) {
            context.startActivity(Intent(context, ContainerActivity::class.java).also {
                it.putExtra(fragmentContainerKey, parcelableFragment)
            }, option?.toBundle())
        }
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container_activity)
        setSupportActionBar(dynamicToolbar)

        val parcel =
            intent.getParcelableExtra<ParcelableFragment<BaseFragment>>(fragmentContainerKey)
                ?: return

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containerFrameLayout, parcel.clzz.newInstance().apply {
                this.arguments = parcel.bundle
            }).commitNow()
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