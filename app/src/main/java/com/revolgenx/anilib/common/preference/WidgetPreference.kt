package com.revolgenx.anilib.common.preference

import android.content.Context


object AiringWidgetPreference {

    private const val AIRING_WIDGET_PAGE_PREF_KEY = "AIRING_WIDGET_PAGE_PREF_KEY"

    fun storePage(widgetId: Int, page: Int){
        val widgetKey = prepareWidgetKey(widgetId, AIRING_WIDGET_PAGE_PREF_KEY)
        save(widgetKey, page)
    }

    fun getPage(widgetId: Int): Int {
        val widgetKey = prepareWidgetKey(widgetId, AIRING_WIDGET_PAGE_PREF_KEY)
        return load(widgetKey, 1)
    }

    fun isAiringWeekly(weekly: Boolean? = null): Boolean {
        return if(weekly == null){
            load(WIDGET_IS_AIRING_WEEKLY_KEY, false)
        }else{
            save(WIDGET_IS_AIRING_WEEKLY_KEY, weekly)
            weekly
        }
    }


    fun isAlreadyAired(isAired: Boolean? = null): Boolean {
        return if(isAired == null){
            load(WIDGET_AIRING_NOT_AIRED_KEY, false)
        }else{
            save(WIDGET_AIRING_NOT_AIRED_KEY, isAired)
            isAired
        }
    }


    fun isAiringPlanningOnly(planning: Boolean? = null): Boolean {
        return if(planning == null){
            load(WIDGET_AIRING_PLANNING_KEY, false)
        }else{
            save(WIDGET_AIRING_PLANNING_KEY, planning)
            planning
        }
    }


    fun isAiringWatchingOnly(watching: Boolean? = null): Boolean {
        return if(watching == null){
            load(WIDGET_AIRING_WATCHING_KEY, false)
        }else{
            save(WIDGET_AIRING_WATCHING_KEY, watching)
            watching
        }
    }


    fun clickOpenListEditor(opensListEditor: Boolean? = null): Boolean {
        return if(opensListEditor == null){
            load(WIDGET_AIRING_CLICK_OPEN_LIST_EDITOR, false)
        }else{
            save(WIDGET_AIRING_CLICK_OPEN_LIST_EDITOR, opensListEditor)
            opensListEditor
        }
    }

    fun showEta(showEta: Boolean? = null): Boolean {
        return if(showEta == null){
            load(WIDGET_AIRING_SHOW_ETA_KEY, true)
        }else{
            save(WIDGET_AIRING_SHOW_ETA_KEY, showEta)
            showEta
        }
    }



}
const val WIDGET_ID_PREF = "WIDGET_PREF_ID"
private fun prepareWidgetKey(widgetId: Int, extra:String): String {
    return WIDGET_ID_PREF + "_$widgetId" + "_$extra"
}