package com.revolgenx.anilib.common.ext

import cafe.adriel.voyager.navigator.Navigator
import com.revolgenx.anilib.airing.ui.screen.AiringScheduleScreen
import com.revolgenx.anilib.character.ui.screen.CharacterScreen
import com.revolgenx.anilib.common.ui.screen.image.ImageViewerScreen
import com.revolgenx.anilib.entry.ui.screen.MediaListEntryEditorScreen
import com.revolgenx.anilib.list.ui.screen.UserMediaListScreen
import com.revolgenx.anilib.media.ui.screen.MediaScreen
import com.revolgenx.anilib.social.ui.screen.ActivityComposerScreen
import com.revolgenx.anilib.social.ui.screen.ActivityScreen
import com.revolgenx.anilib.staff.ui.screen.StaffScreen
import com.revolgenx.anilib.studio.ui.screen.StudioScreen
import com.revolgenx.anilib.user.ui.screen.UserScreen

fun Navigator.airingScheduleScreen() {
    push(AiringScheduleScreen())
}

fun Navigator.mediaScreen(mediaId: Int) {
    push(MediaScreen(mediaId))
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

fun Navigator.userScreen(userId: Int) {
    push(UserScreen(userId))
}

fun Navigator.mediaListEntryEditorScreen(mediaId: Int, userId: Int) {
    push(MediaListEntryEditorScreen(mediaId, userId))
}

fun Navigator.userMediaListScreen(userId: Int) {
    push(UserMediaListScreen(userId))
}


fun Navigator.activityScreen(activityId: Int) {
    push(ActivityScreen(activityId))
}

fun Navigator.activityComposerScreen(activityId: Int? = null){
    push(ActivityComposerScreen(activityId))
}

fun Navigator.imageViewerScreen(url: String) {
    push(ImageViewerScreen(url))
}
