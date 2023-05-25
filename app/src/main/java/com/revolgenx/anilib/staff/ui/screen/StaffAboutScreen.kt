package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.component.image.AsyncImage
import com.revolgenx.anilib.common.ui.screen.ErrorScreen
import com.revolgenx.anilib.common.ui.screen.LoadingScreen
import com.revolgenx.anilib.staff.ui.model.StaffModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel
import com.skydoves.landscapist.ImageOptions
import org.koin.androidx.compose.koinViewModel

@Composable
fun StaffAboutScreen(
    staffId: Int,
    viewModel: StaffAboutViewModel = koinViewModel()
) {
    LaunchedEffect(staffId) {
        viewModel.field.staffId = staffId
        viewModel.getResource()
    }

    when (val resource = viewModel.resource.value) {
        is ResourceState.Error -> ErrorScreen(error = resource.message) {
            viewModel.refresh()
        }

        is ResourceState.Loading -> LoadingScreen()

        is ResourceState.Success -> {
            val staff = resource.data ?: return
            StaffAbout(staff)
        }

        else -> {}
    }
}


@Composable
private fun StaffAbout(
    staff: StaffModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(height = 210.dp, width = 140.dp)
                    .clip(RoundedCornerShape(12.dp)),
                imageUrl =  staff.image?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
            
            Text(
                modifier= Modifier.padding(top = 8.dp),
                text = staff.name?.full.naText(),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
            staff.name?.alternative?.joinToString(", ")?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}