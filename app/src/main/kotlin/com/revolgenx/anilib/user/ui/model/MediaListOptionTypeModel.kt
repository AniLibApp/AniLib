package com.revolgenx.anilib.user.ui.model

import com.revolgenx.anilib.fragment.MediaListCollectionTypeOptions

data class MediaListOptionTypeModel(
    val advancedScoringEnabled: Boolean = false,
    val advancedScoring: List<String>? = null,
    val customLists: List<String>? = null,
    val sectionOrder: List<String>? = null,
    val splitCompletedSectionByFormat: Boolean = false
)

private val completedOrders = listOf(
    "Completed",
    "Completed TV",
    "Completed Movie",
    "Completed OVA",
    "Completed ONA",
    "Completed TV Short",
    "Completed Special",
    "Completed Music",
    "Completed Manga",
    "Completed Novel",
    "Completed One Shot"
)


fun MediaListCollectionTypeOptions.toModel(): MediaListOptionTypeModel {
    fun getSectionOrder(sectionOrder: List<String>?): List<String> {
        sectionOrder ?: return emptyList()
        return sectionOrder.map {
            if (completedOrders.contains(it)) "Completed" else it
        }.distinct()
    }

    val customLists = customLists?.filterNotNull()
    return MediaListOptionTypeModel(
        customLists = customLists,
        sectionOrder = getSectionOrder(sectionOrder?.filterNotNull())
            .toMutableList()
            .also { sectionOrder ->
                customLists?.forEach { customList ->
                    if (sectionOrder.contains(customList).not()) {
                        sectionOrder.add(customList)
                    }
                }
            }
    )
}