package com.revolgenx.anilib.common.ext

import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.airing.ui.screen.AiringScheduleScreen
import com.revolgenx.anilib.browse.data.store.BrowseFilterData
import com.revolgenx.anilib.browse.ui.screen.BrowseScreen
import com.revolgenx.anilib.character.ui.screen.CharacterScreen
import com.revolgenx.anilib.common.ui.screen.image.ImageViewerScreen
import com.revolgenx.anilib.entry.ui.screen.MediaListEntryEditorScreen
import com.revolgenx.anilib.list.ui.screen.UserMediaListScreen
import com.revolgenx.anilib.media.ui.screen.MediaScreen
import com.revolgenx.anilib.relation.ui.screen.UserSocialRelationScreen
import com.revolgenx.anilib.review.ui.screen.ReviewScreen
import com.revolgenx.anilib.social.ui.screen.ActivityScreen
import com.revolgenx.anilib.staff.ui.screen.StaffScreen
import com.revolgenx.anilib.studio.ui.screen.StudioScreen
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.screen.UserScreen

fun Navigator.airingScheduleScreen() {
    push(AiringScheduleScreen())
}

fun Navigator.mediaScreen(mediaId: Int, type: MediaType?) {
    push(MediaScreen(mediaId, type))
}

fun Navigator.staffScreen(staffId: Int) {
    push(StaffScreen(staffId))
}

fun Navigator.characterScreen(characterId: Int) {
    push(CharacterScreen(characterId))
}

fun Navigator.studioScreen(studioId: Int) {
    push(StudioScreen(studioId))
}

fun Navigator.userScreen(userId: Int? = null, username: String? = null) {
    push(UserScreen(id = userId, userName = username))
}

fun Navigator.mediaListEntryEditorScreen(mediaId: Int, userId: Int) {
    push(MediaListEntryEditorScreen(mediaId, userId))
}

fun Navigator.userMediaListScreen(userId: Int, mangaTab: Boolean = false) {
    push(UserMediaListScreen(userId, mangaTab))
}

fun Navigator.userRelationScreen(userId: Int, isFollower: Boolean? = null) {
    push(UserSocialRelationScreen(userId, isFollower))
}

fun Navigator.activityScreen(activityId: Int) {
    push(ActivityScreen(activityId))
}

fun Navigator.imageViewerScreen(url: String) {
    push(ImageViewerScreen(url))
}


fun Navigator.reviewScreen(reviewId: Int) {
    push(ReviewScreen(reviewId))
}

fun Navigator.openBrowseScreen(browseFilterData: BrowseFilterData? = null){
    push(BrowseScreen(browseFilterData))
}

fun Navigator.openGenre(genre: String){
    openBrowseScreen(BrowseFilterData(genreIn = listOf(genre)))
}

fun Navigator.openTag(tag: String){
    openBrowseScreen(BrowseFilterData(tagsIn = listOf(tag)))
}