package com.revolgenx.anilib.relation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.userScreen
import com.revolgenx.anilib.common.ui.component.card.Card
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.viewmodel.collectAsLazyPagingItems
import com.revolgenx.anilib.relation.ui.viewmodel.UserRelationType
import com.revolgenx.anilib.relation.ui.viewmodel.UserRelationViewModel
import com.revolgenx.anilib.user.ui.model.UserModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.qualifier.named
import anilib.i18n.R as I18nR


@Composable
fun UserRelationScreen(userId: Int, isFollower: Boolean) {
    val viewModel: UserRelationViewModel =
        koinViewModel(named(if (isFollower) UserRelationType.USER_RELATION_FOLLOWER else UserRelationType.USER_RELATION_FOLLOWING))
    viewModel.field.userId = userId
    val pagingItems = viewModel.collectAsLazyPagingItems()
    LazyPagingList(
        pagingItems = pagingItems,
        onRefresh = {
            viewModel.refresh()
        }
    ) { userModel ->
        userModel ?: return@LazyPagingList
        UserRelationItem(userModel)
    }
}


@Composable
fun UserRelationItem(user: UserModel) {
    val navigator = localNavigator()
    Row(
        modifier = Modifier
            .clickable {
                navigator.userScreen(user.id)
            }
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageAsync(
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(10)),
                imageUrl = user.avatar?.image,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.bleach
            )
            Text(
                text = user.name.orEmpty(),
                fontSize = 15.sp
            )
        }

        if (user.isMutual) {
            Card {
                MediumText(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    text = stringResource(id = I18nR.string.mutual),
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                )
            }
        }
    }
}
