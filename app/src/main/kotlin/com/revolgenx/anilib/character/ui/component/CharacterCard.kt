package com.revolgenx.anilib.character.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.model.CharacterModel
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.skydoves.landscapist.ImageOptions

@Composable
fun CharacterCard(staff: CharacterModel, onClick: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .height(224.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick.invoke(staff.id)
            }
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(165.dp)
                    .fillMaxWidth(),
                imageUrl = staff.image?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                text = staff.name?.full.naText(),
                maxLines = 2,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}