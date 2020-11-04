package com.revolgenx.anilib.infrastructure.event

import android.view.View
import com.revolgenx.anilib.data.meta.CharacterMeta

data class BrowseCharacterEvent(var meta: CharacterMeta, var sharedElement: View) : CommonEvent()
