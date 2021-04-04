package com.revolgenx.anilib.data.model.setting

data class MlLanguageModel(val localeCode:String, val locale:String, var downloaded:Boolean = false, var isInUse:Boolean = false)
