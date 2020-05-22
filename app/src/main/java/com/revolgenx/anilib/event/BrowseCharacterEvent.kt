package com.revolgenx.anilib.event

import android.view.View
import com.revolgenx.anilib.meta.CharacterMeta

data class BrowseCharacterEvent(var meta: CharacterMeta, var sharedElement: View) : CommonEvent()
