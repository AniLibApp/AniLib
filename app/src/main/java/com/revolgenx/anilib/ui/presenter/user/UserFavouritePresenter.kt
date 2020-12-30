package com.revolgenx.anilib.ui.presenter.user

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.*
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.data.meta.*
import com.revolgenx.anilib.data.model.BaseModel
import com.revolgenx.anilib.data.model.user.CharacterFavouriteModel
import com.revolgenx.anilib.data.model.user.MediaFavouriteModel
import com.revolgenx.anilib.data.model.user.StaffFavouriteModel
import com.revolgenx.anilib.data.model.user.StudioFavouriteModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.*
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.presenter.Constant.PRESENTER_BINDING_KEY
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText

//todo://studio rotation
class UserFavouritePresenter(requireContext: Context, private val lifecycleOwner: LifecycleOwner) :
    BasePresenter<ViewBinding, BaseModel>(requireContext) {
    override val elementTypes: Collection<Int>
        get() = SearchTypes.values().map { it.ordinal }


    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding {
        return when (elementType) {
            SearchTypes.ANIME.ordinal, SearchTypes.MANGA.ordinal -> {
                SearchMediaLayoutBinding.inflate(getLayoutInflater(), parent, false)
            }
            SearchTypes.CHARACTER.ordinal -> {
                SearchCharacterLayoutBinding.inflate(getLayoutInflater(), parent, false)
            }
            SearchTypes.STAFF.ordinal -> {
                SearchStaffLayoutBinding.inflate(getLayoutInflater(), parent, false)
            }
            SearchTypes.STUDIO.ordinal -> {
                SearchStudioLayoutBinding.inflate(getLayoutInflater(), parent, false)
            }
            else -> {
                EmptyLayoutBinding.inflate(getLayoutInflater(), parent, false)
            }
        }
    }


    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color).map { Color.parseColor(it) }
    }


    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }


    override fun onBind(page: Page, holder: Holder, element: Element<BaseModel>) {
        super.onBind(page, holder, element)

        val item = element.data ?: return
        val binding: ViewBinding = holder.getBinding() ?: return

        when (holder.elementType) {
            SearchTypes.ANIME.ordinal, SearchTypes.MANGA.ordinal -> {
                (binding as SearchMediaLayoutBinding).updateMedia(item)
            }
            SearchTypes.CHARACTER.ordinal -> {
                (binding as SearchCharacterLayoutBinding).updateCharacter(item)
            }
            SearchTypes.STAFF.ordinal -> {
                (binding as SearchStaffLayoutBinding).updateStaff(item)
            }
            SearchTypes.STUDIO.ordinal -> {
                (binding as SearchStudioLayoutBinding).updateStudio(item)
            }
        }
    }


    private fun SearchMediaLayoutBinding.updateMedia(item: BaseModel) {
        val data = item as MediaFavouriteModel
        searchMediaImageView.setImageURI(data.coverImage?.image(context))
        searchMediaTitleTv.text = data.title?.title(context)
        searchMediaRatingTv.text = data.averageScore?.toString().naText()
        searchMediaFormatTv.text =
            context.getString(R.string.media_format_year_s).format(data.format?.let {
                mediaFormats[it]
            }.naText(), data.seasonYear?.toString().naText())

        searchMediaFormatTv.status = data.mediaEntryListModel?.status

        searchMediaStatusTv.text = data.status?.let {
            searchMediaStatusTv.color = statusColors[it]
            mediaStatus[it]
        }

        root.setOnClickListener {
            BrowseMediaEvent(
                MediaBrowserMeta(
                    data.mediaId,
                    data.type!!,
                    data.title!!.romaji!!,
                    data.coverImage!!.image(context),
                    data.coverImage!!.largeImage,
                    data.bannerImage
                ), searchMediaImageView
            ).postEvent
        }

        root.setOnLongClickListener {
            if (context.loggedIn()) {
                ListEditorEvent(
                    ListEditorMeta(
                        data.mediaId,
                        data.type!!,
                        data.title!!.title(context)!!,
                        data.coverImage!!.image(context),
                        data.bannerImage
                    ), searchMediaImageView
                ).postEvent
            } else {
                context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
            true
        }

    }

    private fun SearchCharacterLayoutBinding.updateCharacter(item: BaseModel) {
        val data = item as CharacterFavouriteModel
        searchCharacterImageView.setImageURI(data.characterImageModel?.image)
        searchCharacterNameTv.text = data.name?.full
        root.setOnClickListener {
            BrowseCharacterEvent(
                CharacterMeta(
                    item.characterId,
                    item.characterImageModel?.image
                ),
                searchCharacterImageView
            ).postEvent
        }
    }

    private fun SearchStaffLayoutBinding.updateStaff(item: BaseModel) {
        val data = item as StaffFavouriteModel
        searchStaffImageView.setImageURI(data.staffImage?.image)
        searchStaffNameTv.text = data.staffName?.full
        root.setOnClickListener {
            BrowseStaffEvent(StaffMeta(item.staffId ?: -1, item.staffImage?.image)).postEvent
        }
    }

    private fun SearchStudioLayoutBinding.updateStudio(item: BaseModel) {
        val data = item as StudioFavouriteModel
        searchStudioHeader.text = data.studioName + " >>"
        searchStudioMedia.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        searchStudioHeader.setOnClickListener {
            BrowseStudioEvent(StudioMeta(data.studioId)).postEvent
        }

        Adapter.builder(lifecycleOwner)
            .addSource(Source.fromList(data.studioMedia!!))
            .addPresenter(BrowseStudioMediaPresenter())
            .into(searchStudioMedia)
    }


    inner class BrowseStudioMediaPresenter : BasePresenter<BrowseStudioMediaPresenterBinding, MediaFavouriteModel>(context) {
        override val elementTypes: Collection<Int>
            get() = listOf(0)

        override fun bindView(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            elementType: Int
        ): BrowseStudioMediaPresenterBinding {
            return BrowseStudioMediaPresenterBinding.inflate(inflater, parent, false)
        }

        override fun onBind(page: Page, holder: Holder, element: Element<MediaFavouriteModel>) {
            super.onBind(page, holder, element)
            val item = element.data ?: return
            val binding: BrowseStudioMediaPresenterBinding = holder.getBinding() ?: return


            binding.apply {

                searchMediaImageView.setImageURI(item.coverImage?.image(context))
                searchMediaTitleTv.text = item.title?.title(context)
                searchMediaRatingTv.text = item.averageScore?.toString().naText()
                searchMediaFormatTv.text =
                    context.getString(R.string.media_format_year_s).format(item.format?.let {
                        mediaFormats[it]
                    }.naText(), item.seasonYear?.toString().naText())

                searchMediaFormatTv.status = item.mediaEntryListModel?.status

                searchMediaStatusTv.text = item.status?.let {
                    searchMediaStatusTv.color = statusColors[it]
                    mediaStatus[it]
                }

                root.setOnClickListener {
                    BrowseMediaEvent(
                        MediaBrowserMeta(
                            item.mediaId,
                            item.type!!,
                            item.title!!.romaji!!,
                            item.coverImage!!.image(context),
                            item.coverImage!!.largeImage,
                            item.bannerImage
                        ), searchMediaImageView
                    ).postEvent
                }

                root.setOnLongClickListener {
                    if (context.loggedIn()) {
                        ListEditorEvent(
                            ListEditorMeta(
                                item.mediaId,
                                item.type!!,
                                item.title!!.title(context)!!,
                                item.coverImage!!.image(context),
                                item.bannerImage
                            ), searchMediaImageView
                        ).postEvent
                    } else {
                        context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
                    }
                    true
                }
            }
        }
    }

}
