package com.revolgenx.anilib.common.ui.screen.about

import android.text.Spanned
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.colorScheme
import com.revolgenx.anilib.common.ext.prettyNumberFormat
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithValue
import com.skydoves.landscapist.ImageOptions

@Composable
fun AboutScreen(
    name: String,
    alternative: String?,
    imageUrl: String?,
    favourites: Int,
    isFavourite: Boolean,
    onFavouriteClick: OnClick,
    description: String?,
    spannedDescription: Spanned?,
    onImageClick: OnClickWithValue<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(height = 210.dp, width = 140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = {
                        imageUrl?.let {
                            onImageClick(it)
                        }
                    }),
                imageUrl = imageUrl,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center
            )
            alternative?.let {
                MediumText(
                    text = it,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.size(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.description),
                    style = MaterialTheme.typography.titleLarge
                )

                FilledTonalButton(
                    onClick = onFavouriteClick
                ) {
                    Text(
                        text = favourites.prettyNumberFormat(),
                        color = colorScheme().onSurfaceVariant
                    )
                    Icon(
                        painter = painterResource(id = if (isFavourite) R.drawable.ic_heart else R.drawable.ic_heart_outline),
                        contentDescription = null
                    )
                }

            }

            Spacer(modifier = Modifier.size(8.dp))
            if (description != null) {
                MarkdownText(spanned = spannedDescription)
            } else {
                Text(text = stringResource(id = R.string.no_description))
            }
        }
    }
}