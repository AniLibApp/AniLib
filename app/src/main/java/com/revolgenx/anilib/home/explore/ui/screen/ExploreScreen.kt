package com.revolgenx.anilib.home.explore.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.airing.ui.screen.AiringScheduleScreen
import com.revolgenx.anilib.common.ext.localNavigator
import com.revolgenx.anilib.common.ui.view.MarkdownTextView
import com.revolgenx.anilib.social.markwon.anilify
import com.revolgenx.anilib.studio.ui.screen.StudioScreen

@Composable
fun ExploreScreen() {
    Column {
        Text("Explore")
//        MarkdownTextView(
//            modifier = Modifier.fillMaxWidth(),
//                    text =
//            "<p>hello world</p> <video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video></div>\" +\n" +
//                    "                \"<span class='markdown_spoiler'><video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video></div>\" +\n" +
//                    "                \"<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div>"
//        )


        val navigator = localNavigator()
        Text("Studio", fontSize = 25.sp, modifier= Modifier.clickable {
            navigator.push(StudioScreen(569))
        })
        Text("Airing Schedule", fontSize = 25.sp, modifier= Modifier.clickable {
            navigator.push(AiringScheduleScreen())
        })
    }
}