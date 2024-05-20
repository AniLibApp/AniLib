package com.revolgenx.anilib.setting.ui.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.common.Grid
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcGithub
import com.revolgenx.anilib.common.ui.icons.appicon.IcList

data class InfoData(
    val title: String,
    val subTitle: String,
    val imageVector: ImageVector?,
    val painterResource: Painter? = null
)

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = localContext()
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.ic_main),
                    contentDescription = null
                )

                Column {
                    MediumText(
                        text = stringResource(id = anilib.i18n.R.string.app_name),
                        fontSize = 16.sp
                    )
                    MediumText(
                        text = stringResource(id = anilib.i18n.R.string.app_anilist_client_desc),
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(
                        text = stringResource(id = anilib.i18n.R.string.app_desc),
                        fontSize = 13.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            val mainIcon = painterResource(id = R.drawable.ic_main)
            val appInfo = remember {
                listOf(
                    InfoData(
                        title = context.getString(anilib.i18n.R.string.app_anilib_site),
                        subTitle = context.getString(anilib.i18n.R.string.app_anilib_site_desc),
                        painterResource = mainIcon,
                        imageVector = null
                    ),
                    InfoData(
                        title = context.getString(anilib.i18n.R.string.github),
                        subTitle = context.getString(anilib.i18n.R.string.github_source_desc),
                        imageVector = AppIcons.IcGithub
                    ),
                    InfoData(
                        title = context.getString(anilib.i18n.R.string.license_apache_2),
                        subTitle = context.getString(anilib.i18n.R.string.license_apache_desc),
                        imageVector = AppIcons.IcList
                    )
                )
            }
            Grid(items = appInfo, columns = 2, columnSpacing = 16.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (it.imageVector != null) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = it.imageVector,
                            contentDescription = null
                        )
                    }
                    if (it.painterResource != null) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = it.painterResource,
                            contentDescription = null
                        )
                    }

                    Column {
                        MediumText(text = it.title)
                        Text(
                            text = it.subTitle,
                            fontSize = 13.sp,
                            lineHeight = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}