package com.revolgenx.anilib.search.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.preference.loggedIn
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.databinding.*
import com.revolgenx.anilib.entry.data.meta.EntryEditorMeta
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.data.model.StaffModel
import com.revolgenx.anilib.studio.data.model.StudioModel
import com.revolgenx.anilib.ui.view.makeToast
import com.revolgenx.anilib.user.data.model.UserModel
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
                (binding as SearchMediaLayoutBinding).updateMedia(item as MediaModel)
            }
            SearchTypes.CHARACTER.ordinal -> {
                (binding as SearchCharacterLayoutBinding).updateCharacter(item as CharacterModel)
            }
            SearchTypes.STAFF.ordinal -> {
                (binding as SearchStaffLayoutBinding).updateStaff(item as StaffModel)
            }
            SearchTypes.STUDIO.ordinal -> {
                (binding as SearchStudioLayoutBinding).updateStudio(item as StudioModel)
            }
            SearchTypes.USER.ordinal -> {
                (binding as SearchUserLayoutBinding).updateUser(item as UserModel)
            }
        }
    }


    private fun SearchMediaLayoutBinding.updateMedia(item: MediaModel) {
        searchMediaImageView.setImageURI(item.coverImage?.image(context))
        searchMediaTitleTv.text = item.title?.title(context)
        searchMediaRatingTv.text = item.averageScore
        searchMediaFormatTv.text =
            context.getString(R.string.media_format_year_s).format(item.format?.let {
                mediaFormats[it]
            }.naText(), item.seasonYear?.toString().naText())

        searchMediaFormatTv.status = item.mediaListEntry?.status

        searchMediaStatusTv.text = item.status?.let {
            searchMediaStatusTv.color = statusColors[it]
            mediaStatus[it]
        }

        root.setOnClickListener {
            OpenMediaInfoEvent(
                MediaInfoMeta(
                    item.id,
                    item.type!!,
                    item.title!!.romaji!!,
                    item.coverImage!!.image(context),
                    item.coverImage!!.largeImage,
                    item.bannerImage
                )
            ).postEvent
        }

        root.setOnLongClickListener {
            if (context.loggedIn()) {
                OpenMediaListEditorEvent(
                    EntryEditorMeta(
                        item.id,
                        item.type!!,
                        item.title!!.title(context)!!,
                        item.coverImage!!.image(context),
                        item.bannerImage
                    )
                ).postEvent
            } else {
                context.makeToast(R.string.please_log_in, null, R.drawable.ic_person)
            }
            true
        }

    }

    private fun SearchCharacterLayoutBinding.updateCharacter(item: CharacterModel) {
        searchCharacterImageView.setImageURI(item.image?.image)
        searchCharacterNameTv.text = item.name?.full
        root.setOnClickListener {
            OpenCharacterEvent(
                item.id
            ).postEvent
        }
    }

    private fun SearchStaffLayoutBinding.updateStaff(item: StaffModel) {
        searchStaffImageView.setImageURI(item.image?.image)
        searchStaffNameTv.text = item.name?.full
        root.setOnClickListener {
            OpenStaffEvent(
                item.id,
            ).postEvent
        }
    }

    private fun SearchStudioLayoutBinding.updateStudio(item: StudioModel) {
        searchStudioHeader.text = item.studioName + " >>"
        searchStudioMedia.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        searchStudioHeader.setOnClickListener {
            OpenStudioEvent(item.id).postEvent
        }

        Adapter.builder(lifecycleOwner)
            .addSource(Source.fromList(item.media?.nodes!!))
            .addPresenter(BrowseStudioMediaPresenter())
            .into(searchStudioMedia)
    }


    private fun SearchUserLayoutBinding.updateUser(item: UserModel) {
        searchCharacterImageView.setImageURI(item.avatar?.image)
        searchCharacterNameTv.text = item.name
        root.setOnClickListener {
            OpenUserProfileEvent(item.id).postEvent
        }
    }


    inner class BrowseStudioMediaPresenter : BasePresenter<BrowseStudioMediaPresenterBinding, MediaModel>(context) {
        override val elementTypes: Collection<Int>
            get() = listOf(0)


        override fun bindView(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            elementType: Int
        ): BrowseStudioMediaPresenterBinding {
            return BrowseStudioMediaPresenterBinding.inflate(inflater, parent, false)
        }


        override fun onBind(page: Page, holder: Holder, element: Element<MediaModel>) {
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

                searchMediaFormatTv.status = item.mediaListEntry?.status

                searchMediaStatusTv.text = item.status?.let {
                    searchMediaStatusTv.color = statusColors[it]
                    mediaStatus[it]
                }

                root.setOnClickListener {
                    OpenMediaInfoEvent(
                        MediaInfoMeta(
                            item.id,
                            item.type!!,
                            item.title!!.romaji!!,
                            item.coverImage!!.image(context),
                            item.coverImage!!.largeImage,
                            item.bannerImage
                        )
                    ).postEvent
                }

                root.setOnLongClickListener {
                    if (context.loggedIn()) {
                        OpenMediaListEditorEvent(
                            EntryEditorMeta(
                                item.id,
                                item.type!!,
                                item.title!!.title(context)!!,
                                item.coverImage!!.image(context),
                                item.bannerImage
                            )
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