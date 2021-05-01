package com.revolgenx.anilib.common.preference

import android.content.Context


object AiringWidgetPreference {

    private const val AIRING_WIDGET_PAGE_PREF_KEY = "AIRING_WIDGET_PAGE_PREF_KEY"

    fun storePage(context:Context, widgetId: Int, page:Int){
        val widgetKey = prepareWidgetKey(widgetId, AIRING_WIDGET_PAGE_PREF_KEY)
        context.putInt(widgetKey, page)
    }

    fun getPage(context: Context, widgetId: Int): Int {
        val widgetKey = prepareWidgetKey(widgetId, AIRING_WIDGET_PAGE_PREF_KEY)
        return context.getInt(widgetKey, 1)
    }

    fun isAiringWeekly(context: Context, weekly:Boolean? = null): Boolean {
        return if(weekly == null){
            context.getBoolean(WIDGET_IS_AIRING_WEEKLY_KEY, false)
        }else{
            context.putBoolean(WIDGET_IS_AIRING_WEEKLY_KEY, weekly)
            weekly
        }
    }


    fun isAlreadyAired(context: Context, isAired:Boolean? = null): Boolean {
        return if(isAired == null){
            context.getBoolean(WIDGET_AIRING_NOT_AIRED_KEY, false)
        }else{
            context.putBoolean(WIDGET_AIRING_NOT_AIRED_KEY, isAired)
            isAired
        }
    }


    fun isAiringPlanningOnly(context: Context, planning:Boolean? = null): Boolean {
        return if(planning == null){
            context.getBoolean(WIDGET_AIRING_PLANNING_KEY, false)
        }else{
            context.putBoolean(WIDGET_AIRING_PLANNING_KEY, planning)
            planning
        }
    }


    fun isAiringWatchingOnly(context: Context, watching:Boolean? = null): Boolean {
        return if(watching == null){
            context.getBoolean(WIDGET_AIRING_WATCHING_KEY, false)
        }else{
            context.putBoolean(WIDGET_AIRING_WATCHING_KEY, watching)
            watching
        }
    }


    fun clickOpenListEditor(context: Context, opensListEditor:Boolean? = null): Boolean {
        return if(opensListEditor == null){
            context.getBoolean(WIDGET_AIRING_CLICK_OPEN_LIST_EDITOR, false)
        }else{
            context.putBoolean(WIDGET_AIRING_CLICK_OPEN_LIST_EDITOR, opensListEditor)
            opensListEditor
        }
    }

    fun showEta(context: Context, showEta:Boolean? = null): Boolean {
        return if(showEta == null){
            context.getBoolean(WIDGET_AIRING_SHOW_ETA_KEY, true)
        }else{
            context.putBoolean(WIDGET_AIRING_SHOW_ETA_KEY, showEta)
            showEta
        }
    }



}
const val WIDGET_ID_PREF = "WIDGET_PREF_ID"
private fun prepareWidgetKey(widgetId: Int, extra:String): String {
    return WIDGET_ID_PREF + "_$widgetId" + "_$extra"
}