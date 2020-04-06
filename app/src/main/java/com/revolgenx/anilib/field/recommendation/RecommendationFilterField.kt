package com.revolgenx.anilib.field.recommendation

import android.content.Context
import com.revolgenx.anilib.preference.getRecommendationField

class RecommendationFilterField(var onList:Boolean, var sorting:Int){
    companion object{
        fun field(context: Context) = getRecommendationField(context)
    }
}
