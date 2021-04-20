package com.revolgenx.anilib.ui.presenter.home.discover

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.data.model.list.MediaListModel
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.common.preference.userName
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.data.meta.ListEditorMeta
import com.revolgenx.anilib.data.meta.MediaBrowserMeta
import com.revolgenx.anilib.databinding.*
import com.revolgenx.anilib.infrastructure.event.BrowseMediaEvent
import com.revolgenx.anilib.infrastructure.event.ListEditorEvent
import com.revolgenx.anilib.ui.presenter.Constant.PRESENTER_BINDING_KEY
import com.revolgenx.anilib.ui.presenter.list.binding.*
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.ui.viewmodel.list.MediaListCollectionViewModel

class MediaListCollectionPresenter(
    context: Context,
    mediaListMeta: MediaListMeta,
    private val viewModel: MediaListCollectionViewModel
) :
    Presenter<MediaListModel>(context) {

    companion object {
        val textPrimaryColor by lazy {
            DynamicTheme.getInstance().get().textPrimaryColor
        }
    }

    override val elementTypes: Collection<Int> = listOf(0)
    private val mediaFormats =
        context.resources.getStringArray(R.array.media_format)
    private val mediaStatus =
        context.resources.getStringArray(R.array.media_status)
    private val statusColors =
        context.resources.getStringArray(R.array.status_color)
    private val isLoggedInUser =
        mediaListMeta.userId == context.userId() || mediaListMeta.userName == context.userName()
    private val displayMode = getMediaListGridPresenter()


    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return when (displayMode) {
            MediaListDisplayMode.COMPACT -> {
                MediaListCollectionCompactPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false).let {
                    Holder(it.root).also { h->h[PRESENTER_BINDING_KEY] = it }
                }
            }
            MediaListDisplayMode.NORMAL -> {
                MediaListCollectionNormalPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false).let {
                    Holder(it.root).also { h->h[PRESENTER_BINDING_KEY] = it }
                }
            }
            MediaListDisplayMode.CARD -> {
                MediaListCollectionCardPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false).let {
                    it.mediaMetaBackground.setBackgroundColor(
                        ColorUtils.setAlphaComponent(
                            DynamicTheme.getInstance().get().backgroundColor,
                            200
                        )
                    )
                    Holder(it.root).also { h->h[PRESENTER_BINDING_KEY] = it }

                }
            }
            MediaListDisplayMode.MINIMAL->{
                MediaListCollectionMinimalPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false).let {
                    Holder(it.root).also { h->h[PRESENTER_BINDING_KEY] = it }
                }
            }
            MediaListDisplayMode.MINIMAL_LIST->{
                MediaListPresenterListMinimalPresenterBinding.inflate(getLayoutInflater(), parent, false).let {
                    Holder(it.root).also { h->h[PRESENTER_BINDING_KEY] = it }
                }
            }
            else ->{
                MediaListCollectionClassicCardPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false).let {
                    Holder(it.root).also { h->h[PRESENTER_BINDING_KEY] = it }
                }
            }
        }

    }

    @Suppress("UNUSED_VARIABLE")
    override fun onBind(page: Page, holder: Holder, element: Element<MediaListModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding: ViewBinding = holder[PRESENTER_BINDING_KEY] ?: return

        when (displayMode) {
            MediaListDisplayMode.COMPACT -> CompactHolderBinding.bind(
                holder[PRESENTER_BINDING_KEY],
                context,
                item,
                mediaFormats,
                mediaStatus,
                statusColors,
                isLoggedInUser,
                viewModel
            )
            MediaListDisplayMode.NORMAL -> NormalHolderBinding.bind(
                holder[PRESENTER_BINDING_KEY],
                context,
                item,
                mediaFormats,
                mediaStatus,
                statusColors,
                isLoggedInUser,
                viewModel
            )
            MediaListDisplayMode.CARD -> {
                CardHolderBinding.bind(
                    holder[PRESENTER_BINDING_KEY],
                    context,
                    item,
                    mediaFormats,
                    statusColors,
                    isLoggedInUser,
                    viewModel
                )
            }
            MediaListDisplayMode.CLASSIC -> {
                ClassicCardHolderBinding.bind(
                    holder[PRESENTER_BINDING_KEY],
                    context,
                    item,
                    mediaFormats,
                    mediaStatus,
                    isLoggedInUser,
                    viewModel
                )
            }
            MediaListDisplayMode.MINIMAL->{
                MinimalHolderBinding.bindView(
                    holder[PRESENTER_BINDING_KEY],
                    context,
                    item,
                    isLoggedInUser,
                    viewModel
                )
            }
            MediaListDisplayMode.MINIMAL_LIST->{
                MinimalListHolderBinding.bindView(
                    holder[PRESENTER_BINDING_KEY],
                    context,
                    item,
                    isLoggedInUser,
                    viewModel
                )
            }
        }
    }


}
