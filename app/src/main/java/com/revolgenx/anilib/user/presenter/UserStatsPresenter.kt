package com.revolgenx.anilib.user.presenter

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
import com.revolgenx.anilib.search.data.model.filter.MediaSearchFilterModel
import com.revolgenx.anilib.databinding.ImageStatsPresenterLayoutBinding
import com.revolgenx.anilib.databinding.TextStatsPresenterLayoutBinding
import com.revolgenx.anilib.common.presenter.Constant.PRESENTER_BINDING_KEY
import com.revolgenx.anilib.user.data.model.stats.*

class UserStatsPresenter(context: Context) : Presenter<BaseStatisticModel>(context) {
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


    override fun onBind(page: Page, holder: Holder, element: Element<BaseStatisticModel>) {
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
                is UserGenreStatisticModel -> {
                    item.genre?.let { genre ->
                        statsTitleTv.text = genre
                        root.setOnClickListener { _ ->
                            OpenSearchEvent(MediaSearchFilterModel().also {
                                it.genre = listOf(genre.trim())
                            }).postEvent
                        }
                    }
                }
                is UserTagStatisticModel -> {
                    item.tag?.let { tag ->
                        statsTitleTv.text = tag.name
                        statsMediaLayout.setOnClickListener {
                            OpenMediaListingEvent(item.mediaIds ?: emptyList()).postEvent;
                            return@setOnClickListener;
                        }
                        root.setOnClickListener { _ ->
                            OpenSearchEvent(MediaSearchFilterModel().also {
                                it.tags = listOf(tag.name.trim())
                            }).postEvent
                        }
                    }
                }
                is UserStudioStatisticModel -> {
                    item.studio?.let { studio ->
                        statsTitleTv.text = studio.studioName
                        root.setOnClickListener {
                            OpenStudioEvent(item.id).postEvent
                        }
                    }
                }
                is UserVoiceActorStatisticModel -> {
                    item.voiceActor?.let { staff ->
                        statsTitleTv.text = staff.name?.full
                        root.setOnClickListener { _ ->
                            OpenStaffEvent(
                                staff.id,
                            ).postEvent
                        }
                        statsImageDraweeView?.setImageURI(staff.image?.image)
                    }
                }
                is UserStaffStatisticModel -> {

                    item.staff?.let { staff ->
                        statsTitleTv.text = staff.name?.full
                        root.setOnClickListener { _ ->
                            OpenStaffEvent(
                                staff.id,
                            ).postEvent
                        }
                        statsImageDraweeView?.setImageURI(staff.image?.image)
                    }
                }
            }

            statsMediaTv.text = (item.mediaIds?.size ?: 0).toString()
            statsMediaLayout.setOnClickListener {
                OpenMediaListingEvent(item.mediaIds ?: emptyList()).postEvent;
                return@setOnClickListener;
            }

        }
    }

    private fun TextStatsPresenterLayoutBinding.updateCommonItem(item: BaseStatisticModel) {
        statsCount.title = item.count.toString()
        statsMeanScoreTv.title = "${item.meanScore}%"
        statsTimeWatched.title =
            context.getString(R.string.s_day_s_hour).format(item.day, item.hour)
        item.chaptersRead?.let {
            statsTimeWatched.subtitle = context.getString(R.string.chapters_read)
            statsTimeWatched.title = it.toString()
        }
    }

    private fun ImageStatsPresenterLayoutBinding.updateCommonItem(item: BaseStatisticModel) {
        statsCount.subtitle = item.count.toString()
        statsMeanScoreTv.subtitle = "${item.meanScore}%"
        statsTimeWatched.subtitle =
            context.getString(R.string.s_day_s_hour).format(item.day, item.hour)

        item.chaptersRead?.let {
            statsTimeWatched.title = context.getString(R.string.chapters_read)
            statsTimeWatched.subtitle = it.toString()
        }
    }
}

