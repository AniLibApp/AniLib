package com.revolgenx.anilib.explore.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.revolgenx.anilib.common.ui.view.MarkdownTextView
import com.revolgenx.anilib.social.markwon.anilify

@Composable
fun ExploreScreen() {
    Column {
        Text("Explore")
        MarkdownTextView(
            modifier = Modifier.fillMaxWidth(),
                    text =
            "<p>hello world</p> <video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video></div>\" +\n" +
                    "                \"<span class='markdown_spoiler'><video muted loop autoplay controls><source src='https://files.catbox.moe/0zofnv.mp4' type='video/webm'>Your browser does not support the video tag.</video></div>\" +\n" +
                    "                \"<div class='youtube' id='https://www.youtube.com/watch?v=XoyLbuX8EXU'><p>youtube###https://www.youtube.com/watch?v=XoyLbuX8EXU</p></div>".anilify()
        )
    }
}