package com.revolgenx.anilib.activity

import android.text.Spanned
import androidx.activity.ComponentActivity
import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.common.data.event.CommonEvent
import com.revolgenx.anilib.common.data.event.EventBusListener
import com.revolgenx.anilib.common.data.event.OpenCharacterScreenEvent
import com.revolgenx.anilib.common.data.event.OpenImageEvent
import com.revolgenx.anilib.common.data.event.OpenMediaScreenEvent
import com.revolgenx.anilib.common.data.event.OpenSpoilerEvent
import com.revolgenx.anilib.common.data.event.OpenUserScreenEvent
import com.revolgenx.anilib.common.data.event.registerForEvent
import com.revolgenx.anilib.common.data.event.unRegisterForEvent
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.imageViewerScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.userScreen
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity : ComponentActivity(), EventBusListener {
    protected val viewModel by viewModel<MainActivityViewModel>()
    protected var navigator: Navigator? = null


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }


    override fun onStop() {
        unRegisterForEvent()
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CommonEvent) {
        event.apply {
            when (this) {
                is OpenImageEvent -> {
                    imageUrl ?: return
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.imageViewerScreen(imageUrl)
                }

                is OpenSpoilerEvent -> {
                    viewModel.spoilerSpanned = spanned
                    viewModel.openSpoilerBottomSheet.value = true
                }

                is OpenMediaScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.mediaScreen(mediaId, type)
                }

                is OpenCharacterScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.characterScreen(characterId)
                }

                is OpenUserScreenEvent -> {
                    viewModel.openSpoilerBottomSheet.value = false
                    navigator?.userScreen(userId, username)
                }
            }
        }
    }

}