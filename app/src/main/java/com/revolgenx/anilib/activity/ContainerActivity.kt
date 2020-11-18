package com.revolgenx.anilib.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseFragment
import com.revolgenx.anilib.common.ui.fragment.ParcelableFragment

class ContainerActivity : BaseDynamicActivity() {

    companion object {
        const val fragmentContainerKey = "fragment_container_key"

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


    override val layoutRes: Int = R.layout.container_activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parcel =
            intent.getParcelableExtra<ParcelableFragment<BaseFragment>>(fragmentContainerKey)
                ?: return

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, parcel.clzz.newInstance().apply {
                this.arguments = parcel.bundle
            }).commitNow()
        }
    }

    override fun finishAfterTransition() {
        finish()
    }


}