package com.revolgenx.anilib.event

import android.view.View

data class ListEditorEvent(var id:Int, var coverImage:String,var bannerImage:String?, var sharedElement: View):BaseEvent()