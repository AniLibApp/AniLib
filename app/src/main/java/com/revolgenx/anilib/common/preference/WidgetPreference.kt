package com.revolgenx.anilib.common.preference

import android.content.Context

object WidgetPreference {

    private const val WIDGET_ID_PREF = "WIDGET_PREF_ID"

    fun storeNewWidget(context: Context, widgetId: Int) {
        context.putInt(prepareWidgetKey(widgetId), widgetId)
    }

    fun isWidgetPresent(context: Context, widgetId: Int): Boolean {
        return context.getInt(prepareWidgetKey(widgetId), -1) == widgetId
    }

    fun removeWidget(context: Context, widgetId: Int){
        context.sharedPreference().edit().remove(prepareWidgetKey(widgetId)).apply()
    }

    private fun prepareWidgetKey(widgetId: Int): String {
        return WIDGET_ID_PREF + "_$widgetId"
    }

}