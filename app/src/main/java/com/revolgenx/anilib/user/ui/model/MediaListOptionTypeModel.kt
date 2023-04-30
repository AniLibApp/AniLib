package com.revolgenx.anilib.user.ui.model

data class MediaListOptionTypeModel(
    val advancedScoringEnabled: Boolean = false,
    val advancedScoring: MutableList<String>? = null,
    val customLists: List<String>? = null,
    val sectionOrder: List<String>? = null,
    val splitCompletedSectionByFormat: Boolean? = null
)