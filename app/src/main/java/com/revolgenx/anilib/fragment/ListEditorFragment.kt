package com.revolgenx.anilib.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.model.field.BaseField
import kotlinx.android.synthetic.main.list_editor_fragment_layout.*

class ListEditorFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_editor_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mediaId = arguments?.getInt(BaseField.MEDIA_ID_KEY) ?: return
        val mediaUrl = arguments?.getString(BaseField.MEDIA_COVER_URL_KEY) ?: return
        val mediaBannerUrl = arguments?.getString(BaseField.MEDIA_BANNER_URL_KEY)
        listEditorCoverImage.setImageURI(mediaUrl)
        listEditorBannerImage.setImageURI(mediaBannerUrl)
    }
}