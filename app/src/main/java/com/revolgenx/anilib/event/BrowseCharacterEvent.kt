package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.event.meta.CharacterMeta

data class BrowseCharacterEvent(var meta: CharacterMeta, var sharedElement: View) : BaseEvent()