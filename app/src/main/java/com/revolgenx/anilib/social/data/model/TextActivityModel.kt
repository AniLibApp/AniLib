package com.revolgenx.anilib.social.data.model

import android.text.Spanned
import androidx.core.text.toSpanned

class TextActivityModel : ActivityUnionModel() {
    var text: String = ""
    var anilifiedText:String = ""
    var textSpanned:Spanned = "".toSpanned()
}