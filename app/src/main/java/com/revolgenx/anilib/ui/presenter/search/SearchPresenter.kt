package com.revolgenx.anilib.ui.presenter.search

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
import com.revolgenx.anilib.data.model.search.*
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.databinding.*
import com.revolgenx.anilib.ui.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.util.naText

class SearchPresenter(context: Context, private val lifecycleOwner: LifecycleOwner) :
    BasePresenter<ViewBinding, BaseModel>(context) {
    override val elementTypes: Collection<Int>
        get() = SearchTypes.values().map { it.ordinal }

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color).map { Color.parseColor(it) }
    }


    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }

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
            SearchTypes.USER.ordinal -> {
                SearchUserLayoutBinding.inflate(getLayoutInflater(), parent, false)
            }
            else -> {
                SearchMediaLayoutBinding.inflate(getLayoutInflater(), parent, false)
            }
        }
    }


    override fun onBind(page: Page, holder: Holder, element: Element<BaseModel>) {
        super.onBind(page, holder, element)

        val item = element.data ?: return
        val binding = holder.getBinding() ?: return
        when (holder.elementType) {
            SearchTypes.ANIME.ordinal, SearchTypes.MANGA.ordinal -> {
                (binding as SearchMediaLayoutBinding).updateMedia(item as MediaSearchModel)
            }
            SearchTypes.CHARACTER.ordinal -> {
                (binding as SearchCharacterLayoutBinding).updateCharacter(item as CharacterSearchModel)
            }
            SearchTypes.STAFF.ordinal -> {
                (binding as SearchStaffLayoutBinding).updateStaff(item as StaffSearchModel)
            }
            SearchTypes.STUDIO.ordinal -> {
                (binding as SearchStudioLayoutBinding).updateStudio(item as StudioSearchModel)
            }
            SearchTypes.USER.ordinal -> {
                (binding as SearchUserLayoutBinding).updateUser(item as UserSearchModel)
            }
        }
    }


    private fun SearchMediaLayoutBinding.updateMedia(item: MediaSearchModel) {
        searchMediaImageView.setImageURI(item.coverImage?.image(context))
        searchMediaTitleTv.text = item.title?.title(context)
        searchMediaRatingTv.text = item.averageScore
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

    private fun SearchCharacterLayoutBinding.updateCharacter(item: CharacterSearchModel) {
        searchCharacterImageView.setImageURI(item.characterImageModel?.image)
        searchCharacterNameTv.text = item.name?.full
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

    private fun SearchStaffLayoutBinding.updateStaff(item: StaffSearchModel) {
        searchStaffImageView.setImageURI(item.staffImage?.image)
        searchStaffNameTv.text = item.staffName?.full
        root.setOnClickListener {
            BrowseStaffEvent(StaffMeta(item.staffId ?: -1, item.staffImage?.image)).postEvent
        }
    }

    private fun SearchStudioLayoutBinding.updateStudio(item: StudioSearchModel) {
        searchStudioHeader.text = item.studioName + " >>"
        searchStudioMedia.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        searchStudioHeader.setOnClickListener {
            BrowseStudioEvent(StudioMeta(item.studioId)).postEvent
        }

        Adapter.builder(lifecycleOwner)
            .addSource(Source.fromList(item.studioMedia!!))
            .addPresenter(BrowseStudioMediaPresenter())
            .into(searchStudioMedia)
    }


    private fun SearchUserLayoutBinding.updateUser(item: UserSearchModel) {
        searchCharacterImageView.setImageURI(item.avatar?.image)
        searchCharacterNameTv.text = item.userName
        root.setOnClickListener {
            UserBrowseEvent(item.userId).postEvent
        }
    }


    inner class BrowseStudioMediaPresenter : BasePresenter<BrowseStudioMediaPresenterBinding,MediaSearchModel>(context) {
        override val elementTypes: Collection<Int>
            get() = listOf(0)


        override fun bindView(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            elementType: Int
        ): BrowseStudioMediaPresenterBinding {
            return BrowseStudioMediaPresenterBinding.inflate(inflater, parent, false)
        }


        override fun onBind(page: Page, holder: Holder, element: Element<MediaSearchModel>) {
            super.onBind(page, holder, element)
            val item = element.data ?: return

            val binding: BrowseStudioMediaPresenterBinding = holder.getBinding() ?: return


            binding.apply {

                searchMediaImageView.setImageURI(item.coverImage?.image(context))
                searchMediaTitleTv.text = item.title?.title(context)
                searchMediaRatingTv.text = item.averageScore
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

