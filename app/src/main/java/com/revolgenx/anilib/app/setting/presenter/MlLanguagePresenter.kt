package com.revolgenx.anilib.app.setting.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.setting.data.model.MlLanguageModel
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.databinding.MlLanguagePresenterLayoutBinding

class MlLanguagePresenter(
    context: Context,
    private val onDownloadDeleteClicked: (model: MlLanguageModel) -> Unit,
    private val onSetMlDefault: (model: MlLanguageModel) -> Unit
) : BasePresenter<MlLanguagePresenterLayoutBinding, MlLanguageModel>(context) {

    override val elementTypes: Collection<Int> = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): MlLanguagePresenterLayoutBinding {
        return MlLanguagePresenterLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onBind(page: Page, holder: Holder, element: Element<MlLanguageModel>) {
        super.onBind(page, holder, element)
        val item = element.data!!
        with(holder.getBinding()!!) {
            mlLanguageName.text = item.locale
            usingDownloadedMlModel.visibility = if (item.isInUse) View.VISIBLE else View.GONE
            downloadDeleteMlModel.setImageResource(if (item.downloaded) R.drawable.ic_delete else R.drawable.ads_ic_download)

            downloadDeleteMlModel.setOnClickListener {
                onDownloadDeleteClicked.invoke(item)
            }

            root.setOnClickListener {
                onSetMlDefault.invoke(item)
            }

        }
    }
}