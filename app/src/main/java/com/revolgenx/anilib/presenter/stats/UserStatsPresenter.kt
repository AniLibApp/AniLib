package com.revolgenx.anilib.presenter.stats

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.BrowseGenreEvent
import com.revolgenx.anilib.event.BrowseStaffEvent
import com.revolgenx.anilib.event.BrowseStudioEvent
import com.revolgenx.anilib.event.BrowseTagEvent
import com.revolgenx.anilib.meta.StaffMeta
import com.revolgenx.anilib.meta.StudioMeta
import com.revolgenx.anilib.model.search.filter.MediaBrowseFilterModel
import com.revolgenx.anilib.model.user.stats.*
import kotlinx.android.synthetic.main.image_stats_presenter_layout.view.*
import kotlinx.android.synthetic.main.text_stats_general_layout.view.*
import kotlinx.android.synthetic.main.user_activity_layout.view.*

class UserStatsPresenter(context: Context) : Presenter<BaseStatsModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0, 1)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return if (elementType == 0)
            Holder(
                getLayoutInflater().inflate(
                    R.layout.text_stats_presenter_layout,
                    parent,
                    false
                )
            )
        else
            Holder(
                getLayoutInflater().inflate(
                    R.layout.image_stats_presenter_layout,
                    parent,
                    false
                )
            )
    }


    override fun onBind(page: Page, holder: Holder, element: Element<BaseStatsModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        holder.itemView.apply {
            this.updateCommonItem(item)
            when (item) {
                is StatsGenreModel -> {
                    item.genre?.let { genre ->
                        statsTitleTv.text = genre
                        setOnClickListener { _ ->
                            BrowseGenreEvent(MediaBrowseFilterModel().also {
                                it.genre = listOf(genre.trim())
                            }).postEvent
                        }
                    }
                }
                is StatsTagModel -> {
                    item.tag?.let { tag ->
                        statsTitleTv.text = tag
                        setOnClickListener { _ ->
                            BrowseTagEvent(MediaBrowseFilterModel().also {
                                it.tags = listOf(tag.trim())
                            }).postEvent
                        }
                    }
                }
                is StatsStudioModel -> {
                    item.studio?.let {
                        statsTitleTv.text = it
                        setOnClickListener {
                            BrowseStudioEvent(StudioMeta(item.baseId)).postEvent
                        }
                    }
                }
                is StatsVoiceActorModel -> {
                    item.name?.let {
                        statsTitleTv.text = it
                    }
                    item.baseId?.let {
                        setOnClickListener { _ ->
                            BrowseStaffEvent(
                                StaffMeta(
                                    it,
                                    item.image
                                )
                            ).postEvent
                        }
                    }
                    imageSimpleDrawee.setImageURI(item.image)
                }
                is StatsStaffModel -> {
                    item.name?.let {
                        statsTitleTv.text = it
                    }

                    item.baseId?.let {
                        setOnClickListener { _ ->
                            BrowseStaffEvent(
                                StaffMeta(
                                    it,
                                    item.image
                                )
                            ).postEvent
                        }
                    }
                    imageSimpleDrawee.setImageURI(item.image)
                }
            }
        }
    }

    private fun View.updateCommonItem(item: BaseStatsModel) {
        item.count?.let {
            statsCount.title = it.toString()
        }

        item.meanScore?.let {
            statsMeanScoreTv.title = "$it%"
        }

        statsTimeWatched.title =
            context.getString(R.string.s_day_s_hour).format(item.day, item.hour)

        item.chaptersRead?.let {
            statsTimeWatched.subtitle = context.getString(R.string.chapters_read)
            statsTimeWatched.title = it.toString()
        }
    }
}

