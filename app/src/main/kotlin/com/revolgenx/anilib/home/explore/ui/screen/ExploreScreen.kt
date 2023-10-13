package com.revolgenx.anilib.home.explore.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.airingScheduleScreen
import com.revolgenx.anilib.common.ext.characterScreen
import com.revolgenx.anilib.common.ext.mediaListEntryEditorScreen
import com.revolgenx.anilib.common.ext.mediaScreen
import com.revolgenx.anilib.common.ext.reviewScreen
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ext.userMediaListScreen
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.button.SegmentedButton
import com.revolgenx.anilib.common.ui.component.common.HeaderBox
import com.revolgenx.anilib.common.ui.component.common.HeaderText
import com.revolgenx.anilib.common.ui.component.common.ShowIfLoggedIn
import com.revolgenx.anilib.common.ui.component.menu.SelectMenu
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcFilter
import com.revolgenx.anilib.common.ui.icons.appicon.IcMoreHoriz
import com.revolgenx.anilib.common.ui.screen.image.ImageViewerScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.social.markdown.anilify
import com.revolgenx.anilib.type.MediaType

@Composable
fun ExploreScreen() {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {

        ExploreAiringScheduleSection()
        ExploreMediaSection(ExploreMediaSectionType.TRENDING)
        ExploreMediaSection(ExploreMediaSectionType.POPULAR)
        ExploreMediaSection(ExploreMediaSectionType.NEWLY_ADDED)
        /*
                val navigator = localNavigator()
                Spacer(modifier = Modifier.size(20.dp))
                SegmentedButton(items = arrayOf("Day", "Week", "Month"), selectedPosition = 0) {

                }

                Text("Airing Schedule", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.airingScheduleScreen()
                })
                Text("Media", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.mediaScreen(21, MediaType.ANIME)
                })
                Text("Staff", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.staffScreen(97139)
                })
                Text("Character", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.characterScreen(183885)
                })
                Text("Studio", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.studioScreen(569)
                })
                Text("User", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.userScreen(260015)
                })
                Text("Review", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.reviewScreen(21990)
                })
                SelectMenu(
                    label = stringResource(id = R.string.status),
                    entries = stringArrayResource(id = com.revolgenx.anilib.R.array.media_status).toList(),
                    showNoneItem = true
                ) { selectedItem ->

                }
                ShowIfLoggedIn { userId ->
                    Text("MediaListEditor", fontSize = 25.sp, modifier = Modifier.clickable {
                        navigator.mediaListEntryEditorScreen(128893, userId)
                    })
                }

                Text("User Media List", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.userMediaListScreen(6188)
                })

                Text("Image Viewer", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.push(ImageViewerScreen("https://i.imgur.com/N6MhhkR.png"))
                })
                Text("Image Viewer 2", fontSize = 25.sp, modifier = Modifier.clickable {
                    navigator.push(ImageViewerScreen("https://s4.anilist.co/file/anilistcdn/media/manga/cover/large/bx105393-oiHumQoBGKG5.jpg"))
                })

                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify(
                        "~~~Img500(https://i.imgur.com/I4JYvuE.gif)~~~\n# __~~~<a>Happy Birthday for Seiyuu Takuya Eguchi</a>~~~__\n~~~Img500(https://i.imgur.com/I4JYvuE.gif)~~~\n~~~Img500(https://64.media.tumblr.com/7bda006b1d1d40d3a7fe37e6dbf5f636/53901828960b04c4-e4/s540x810/ea86b2f1ebe4dca309636edca066858a422eb7df.gif)~~~\n~~~Img500(https://64.media.tumblr.com/08597006864e321111c8a621b43a4651/7bf56e20ae41922d-2e/s500x750/b27deefe224f162c661dd38c11145e620c3bbfb0.gifv)~~~\n~~~Img500(https://64.media.tumblr.com/6628e87a2134c0cfcf379b68fc1bc2ea/54e7e0bc34ae1e71-ec/s540x810/1c0242c0cae7cf19e025b9632a4153f0de9c7f57.gifv)~~~\n~~~Img500(https://64.media.tumblr.com/211d57fd32b715ce9fcf0502a5c32ae3/tumblr_odd3ufLh5a1v1oed6o3_500.gifv)~~~\n~~~Img500(https://media.tenor.com/VAK7Pp29y3MAAAAC/hanma-hanma-tokyo-revengers.gif)~~~\n~~~Img500(https://64.media.tumblr.com/d23b85696afc8f243e49b708980c3a20/3b6a4be4738fd0d3-0f/s500x750/614358895aacebe18f32590ac88254a170aad267.gif)~~~\n~~~Img500(https://64.media.tumblr.com/86e1b82243ac05d05099feae0fdf0a06/tumblr_mzs15rIDSO1s3nakco9_250.gif)~~~\n~~~Img500(https://media.tenor.com/mRJZtPJeOWoAAAAC/terushima-yuji.gif)~~~\n~~~Img500(https://media.discordapp.net/attachments/843441176794759218/1100827143879012463/Ousama-Ranking-02-26.jpg)~~~\n~~~Img500(https://64.media.tumblr.com/c121f081cd5ab5739a083ad4ce7b9010/68bdaec45063e128-78/s1280x1920/201953bc74bc13d7fc42ca54c1bf2d878a1a8c99.jpg)~~~\n~~~Img500(https://i.pinimg.com/474x/8b/cc/55/8bcc5544735dbc5591764bfb6b0f5bdb.jpg)~~~\n~~~Img500(https://i.imgur.com/I4JYvuE.gif)~~~"
                    )
                )

                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("~~~__Good Morning!!!__\nimg470(https://c.tenor.com/LxEySGBwEzUAAAAC/mieruko-chan-anime.gif)~~~")
                )
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("webm(https://files.catbox.moe/91t99m.mp4)")
                )
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("Reset day of the week\n img220(https://i.pinimg.com/564x/13/03/01/13030198bddf456b92dcdd019e336e82.jpg)")
                )
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("<center>[*i found the cure to growing older.*](https://m.youtube.com/watch?v=_p0uqYVCLII)\n[img500(https://files.catbox.moe/y830w1.jpeg) ](https://www.pixiv.net/en/artworks/100222781)")
                )

                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("TDLR: Mech go brrr kkakak booom\n(Bandai can take my money) \nyoutube(https://youtu.be/wkmnaMLCfAQ)")
                )
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("~~~__Song Of The Day __~~~\n~~~youtube(https://www.youtube.com/watch?v=FWdNd4Pv-6o)~~~\n~~~__Breakin My Heart Mint Condition __~~~")
                )
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("Still working on it\n~~~\n##Random song/AMV while after-effect working\n##Kyle - Don't Wanna Fall in Love [Goofy movie[1995]]\nyoutube(https://youtu.be/16JAsfzHl9k)\n\n\n\n\n~~~")
                )
                MarkdownText(
                    modifier = Modifier.fillMaxWidth(),
                    text = anilify("~~~img440(https://imgur.com/VSEaD0c.jpg)~~~\n~~~https://anilist.co/anime/263/Hajime-no-Ippo-The-Fighting/~~~")
                )*/
    }
}

@Composable
internal fun ExploreScreenHeader(
    text: String,
    icon: ImageVector? = null,
    onFilter: OnClick? = null,
    onMore: OnClick? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExploreHeaderText(text, icon)
        ExploreHeaderFilterButton(onFilter, onMore)
    }
}


@Composable
private fun ExploreHeaderText(text: String, icon: ImageVector?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
//        Icon(modifier = Modifier.size(28.dp),imageVector = icon, contentDescription = null)
        HeaderText(
            text = text,
        )
    }
}

@Composable
private fun ExploreHeaderFilterButton(filter: OnClick?, more: OnClick?) {
    Row {
        filter?.let {
            ActionMenu(icon = AppIcons.IcFilter, onClick = filter)
        }
        more?.let {
            ActionMenu(icon = AppIcons.IcMoreHoriz, onClick = more)
        }
    }
}


val ExploreMediaCardWidth = 160.dp
val ExploreMediaCardHeight = 248.dp
