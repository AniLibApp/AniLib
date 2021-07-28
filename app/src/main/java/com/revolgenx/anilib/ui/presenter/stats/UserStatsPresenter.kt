package com.revolgenx.anilib.ui.presenter.stats

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.facebook.drawee.view.SimpleDraweeView
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.infrastructure.event.*
import com.revolgenx.anilib.data.model.search.filter.MediaSearchFilterModel
import com.revolgenx.anilib.data.model.user.stats.*
import com.revolgenx.anilib.databinding.ImageStatsPresenterLayoutBinding
import com.revolgenx.anilib.databinding.TextStatsPresenterLayoutBinding
import com.revolgenx.anilib.ui.presenter.Constant.PRESENTER_BINDING_KEY

class UserStatsPresenter(context: Context) : Presenter<BaseStatsModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0, 1)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return if (elementType == 0) {
            val binding =
                TextStatsPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false)
            binding.statsMediaTv.compoundDrawablesRelative[0].setTint(
                DynamicTheme.getInstance().get().tintBackgroundColor
            )
            Holder(binding.root).also {
                it[PRESENTER_BINDING_KEY] = binding
            }
        } else {
            val binding =
                ImageStatsPresenterLayoutBinding.inflate(getLayoutInflater(), parent, false)
            binding.statsMediaTv.compoundDrawablesRelative[0].setTint(
                DynamicTheme.getInstance().get().tintBackgroundColor
            )
            Holder(binding.root).also {
                it[PRESENTER_BINDING_KEY] = binding
            }
        }
    }


    override fun onBind(page: Page, holder: Holder, element: Element<BaseStatsModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding: ViewBinding = holder[PRESENTER_BINDING_KEY] ?: return
        val statsTitleTv: TextView
        val statsMediaTv: TextView
        val statsMediaLayout: View
        var statsImageDraweeView: SimpleDraweeView? = null
        if (element.type == 0 && binding is TextStatsPresenterLayoutBinding) {
            statsTitleTv = binding.statsTitleTv
            statsMediaTv = binding.statsMediaTv
            statsMediaLayout = binding.statsMediaLayout
            binding.updateCommonItem(item)

        } else {
            if (binding is ImageStatsPresenterLayoutBinding) {
                statsTitleTv = binding.statsTitleTv
                statsMediaTv = binding.statsMediaTv
                statsMediaLayout = binding.statsMediaLayout
                statsImageDraweeView = binding.imageSimpleDrawee
                binding.updateCommonItem(item)
            } else {
                return
            }
        }

        binding.apply {
            when (item) {
                is StatsGenreModel -> {
                    item.genre?.let { genre ->
                        statsTitleTv.text = genre
                        root.setOnClickListener { _ ->
                            OpenSearchEvent(MediaSearchFilterModel().also {
                                it.genre = listOf(genre.trim())
                            }).postEvent
                        }
                    }
                }
                is StatsTagModel -> {
                    item.tag?.let { tag ->
                        statsTitleTv.text = tag
                        statsMediaLayout.setOnClickListener {
                            OpenMediaListingEvent(item.mediaIds ?: emptyList()).postEvent;
                            return@setOnClickListener;
                        }
                        root.setOnClickListener { _ ->
                            OpenSearchEvent(MediaSearchFilterModel().also {
                                it.tags = listOf(tag.trim())
                            }).postEvent
                        }
                    }
                }
                is StatsStudioModel -> {
                    item.studio?.let {
                        statsTitleTv.text = it
                        root.setOnClickListener {
                            OpenStudioEvent(item.id!!).postEvent
                        }
                    }
                }
                is StatsVoiceActorModel -> {
                    item.name?.let {
                        statsTitleTv.text = it
                    }
                    item.id?.let {
                        root.setOnClickListener { _ ->
                            OpenStaffEvent(
                                it,
                            ).postEvent
                        }
                    }
                    statsImageDraweeView?.setImageURI(item.image)
                }
                is StatsStaffModel -> {
                    item.name?.let {
                        statsTitleTv.text = it
                    }

                    item.id?.let {
                        root.setOnClickListener { _ ->
                            OpenStaffEvent(
                                it,
                            ).postEvent
                        }
                    }
                    statsImageDraweeView?.setImageURI(item.image)
                }
            }

            statsMediaTv.text = (item.mediaIds?.size ?: 0).toString()
            statsMediaLayout.setOnClickListener {
                OpenMediaListingEvent(item.mediaIds ?: emptyList()).postEvent;
                return@setOnClickListener;
            }

        }
    }

    private fun TextStatsPresenterLayoutBinding.updateCommonItem(item: BaseStatsModel) {
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

    private fun ImageStatsPresenterLayoutBinding.updateCommonItem(item: BaseStatsModel) {
        item.count?.let {
            statsCount.subtitle = it.toString()
        }

        item.meanScore?.let {
            statsMeanScoreTv.subtitle = "$it%"
        }

        statsTimeWatched.subtitle =
            context.getString(R.string.s_day_s_hour).format(item.day, item.hour)

        item.chaptersRead?.let {
            statsTimeWatched.title = context.getString(R.string.chapters_read)
            statsTimeWatched.subtitle = it.toString()
        }
    }
}

