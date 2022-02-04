package com.revolgenx.anilib.airing.presenter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
import com.revolgenx.anilib.airing.presenter.AiringPresenterBindingHelper.bindPresenter
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.common.preference.getAiringDisplayMode
import com.revolgenx.anilib.constant.AiringListDisplayMode
import com.revolgenx.anilib.databinding.AiringCompactPresenterLayoutBinding
import com.revolgenx.anilib.databinding.AiringMinimalListPresenterLayoutBinding
import com.revolgenx.anilib.databinding.AiringPresenterLayoutBinding
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.ui.view.drawable.DynamicBackgroundGradientDrawable

class AiringPresenter(context: Context) :
    BasePresenter<ViewBinding, AiringScheduleModel>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): ViewBinding {
        return when (displayMode) {
            AiringListDisplayMode.NORMAL -> {
                AiringPresenterLayoutBinding.inflate(inflater, parent, false)
            }
            AiringListDisplayMode.MINIMAL_LIST ->{
                AiringMinimalListPresenterLayoutBinding.inflate(inflater, parent, false)
            }
            else -> {
                AiringCompactPresenterLayoutBinding.inflate(inflater, parent, false)
            }
        }
    }

    private val statusColors by lazy {
        context.resources.getStringArray(R.array.status_color)
    }

    private val mediaFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    private val mediaStatus by lazy {
        context.resources.getStringArray(R.array.media_status)
    }

    private val mediaListStatus by lazy {
        context.resources.getStringArray(R.array.media_list_status)
    }

    private val blurColor by lazy {
        DynamicColorUtils.setAlpha(dynamicBackgroundColor, 140)
    }

    private val gradientBlur by lazy {
        DynamicBackgroundGradientDrawable(
            orientation = GradientDrawable.Orientation.BOTTOM_TOP,
            alpha = 255
        )
    }

    private val mediaListStatusColor by lazy {
        context.resources.getStringArray(R.array.media_list_status_color)
    }


    private val displayMode = getAiringDisplayMode()


    override fun onBind(page: Page, holder: Holder, element: Element<AiringScheduleModel>) {
        super.onBind(page, holder, element)
        val item = element.data ?: return

        when (val binding = holder.getBinding()) {
            is AiringPresenterLayoutBinding -> {
                binding.bindPresenter(context, item, mediaFormats, mediaStatus, statusColors)
            }
            is AiringCompactPresenterLayoutBinding -> {
                binding.bindPresenter(context, item, mediaFormats, mediaStatus, statusColors)
            }
            is AiringMinimalListPresenterLayoutBinding ->{
                binding.bindPresenter(context, item,gradientBlur , mediaFormats , mediaListStatus, mediaListStatusColor)
            }
        }

    }
}