package com.revolgenx.anilib.ui.view.about

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pranavpandey.android.dynamic.support.model.DynamicInfo
import com.pranavpandey.android.dynamic.support.recyclerview.DynamicRecyclerViewFrame
import com.pranavpandey.android.dynamic.support.recyclerview.adapter.DynamicSimpleBinderAdapter
import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder
import com.pranavpandey.android.dynamic.support.recyclerview.binder.factory.InfoBigBinder
import com.pranavpandey.android.dynamic.support.utils.DynamicLayoutUtils
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils
import com.revolgenx.anilib.R

class LicenseView : DynamicRecyclerViewFrame {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        setAdapter()
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager? {
        return DynamicLayoutUtils.getStaggeredGridLayoutManager(
            DynamicLayoutUtils.getGridCount(context),
            StaggeredGridLayoutManager.VERTICAL
        )
    }

    private fun setAdapter() {
        val licenses = ArrayList<DynamicInfo>()

        licenses.add(
            DynamicInfo()
                .setTitle(
                    context
                        .getString(R.string.ads_license_ads_modules)
                )
                .setDescription(context.getString(R.string.ads_license_copy_me_17))
                .setLinks(
                    resources.getStringArray(
                        R.array.ads_license_links_ads_modules
                    )
                )
                .setLinksSubtitles(
                    resources.getStringArray(
                        R.array.ads_license_links_subtitles_ads_modules
                    )
                )
                .setLinksUrls(
                    resources.getStringArray(
                        R.array.ads_license_links_urls_ads_modules
                    )
                )
                .setLinksIconsResId(R.array.ads_license_links_icons_ads_modules)
                .setLinksColorsResId(R.array.ads_license_links_colors_ads_modules)
                .setIconBig(
                    DynamicResourceUtils.getDrawable(
                        context, R.drawable.ads_ic_extension
                    )
                )
        )
        licenses.add(
            DynamicInfo()
                .setTitle(
                    context
                        .getString(R.string.ads_license_ads)
                )
                .setDescription(context.getString(R.string.ads_license_copy_me_18))
                .setLinks(
                    resources.getStringArray(
                        R.array.ads_license_links_ads
                    )
                )
                .setLinksSubtitles(
                    resources.getStringArray(
                        R.array.ads_license_links_subtitles_ads
                    )
                )
                .setLinksUrls(
                    resources.getStringArray(
                        R.array.ads_license_links_urls_ads
                    )
                )
                .setLinksIconsResId(R.array.ads_license_links_icons_ads)
                .setLinksColorsResId(R.array.ads_license_links_colors_ads)
                .setIconBig(
                    DynamicResourceUtils.getDrawable(
                        context, R.drawable.ads_ic_extension
                    )
                )
        )

        licenses.add(
            DynamicInfo()
                .setTitle(context.getString(R.string.license_apollo_android))
                .setDescription(context.getString(R.string.license_copy_apollo_android))
                .setLinks(resources.getStringArray(R.array.license_links_apollo_android))
                .setLinksSubtitles(
                    resources.getStringArray(
                        R.array.license_links_subtitles_apollo_android
                    )
                )
                .setLinksUrls(
                    resources.getStringArray(
                        R.array.license_links_url_apollo_android
                    )
                )
                .setLinksIconsResId(R.array.license_links_icons_license)
                .setLinksColorsResId(R.array.license_links_colors_license)
                .setIconBig(
                    DynamicResourceUtils.getDrawable(
                        context, R.drawable.ic_graphql_logo
                    )
                )
        )

        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_rx_android))
            .setDescription(context.getString(R.string.license_copy_rx_android))
            .setLinks(resources.getStringArray(
                R.array.license_links_rx_android))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_rx_android))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_rx_android))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))

        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_eventbus))
            .setDescription(context.getString(R.string.license_copy_eventbus))
            .setLinks(resources.getStringArray(
                R.array.license_links_eventbus))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_eventbus))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_eventbus))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))

        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_koin))
            .setLinks(resources.getStringArray(
                R.array.license_links_koin))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_koin))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_koin))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))


        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_coroutines))
            .setLinks(resources.getStringArray(
                R.array.license_links_coroutines))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_coroutines))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_coroutines))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))



        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_elements))
            .setLinks(resources.getStringArray(
                R.array.license_links_elements))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_elements))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_elements))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))


        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_fresco))
            .setDescription(context.getString(R.string.license_copy_fresco))
            .setLinks(resources.getStringArray(
                R.array.license_links_fresco))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_fresco))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_fresco))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))



        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_timber))
            .setDescription(context.getString(R.string.license_copy_timber))
            .setLinks(resources.getStringArray(
                R.array.license_links_timber))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_timber))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_timber))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))


        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_app_auth))
            .setLinks(resources.getStringArray(
                R.array.license_links_app_auth))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_app_auth))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_app_auth))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))


        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_jwt_auth))
            .setDescription(context.getString(R.string.license_copy_jwt_auth))
            .setLinks(resources.getStringArray(
                R.array.license_links_jwt_auth))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_jwt_auth))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_jwt_auth))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))


        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_smarttab))
            .setDescription(context.getString(R.string.license_copy_smarttab))
            .setLinks(resources.getStringArray(
                R.array.license_links_smarttab))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_smarttab))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_smarttab))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))


        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_mpandroidchart))
            .setDescription(context.getString(R.string.license_copy_mpandroidchart))
            .setLinks(resources.getStringArray(
                R.array.license_links_mpandroidchart))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_mpandroidchart))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_mpandroidchart))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ic_chart)))



        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_markwon))
            .setDescription(context.getString(R.string.license_copy_markwon))
            .setLinks(resources.getStringArray(
                R.array.license_links_markwon))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_markwon))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_markwon))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ic_gesture)))

        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_jsoup))
            .setDescription(context.getString(R.string.license_copy_jsoup))
            .setLinks(resources.getStringArray(
                R.array.license_links_jsoup))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_jsoup))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_jsoup))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))


        licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_exoplayer))
            .setLinks(resources.getStringArray(
                R.array.license_links_exoplayer))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_exoplayer))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_exoplayer))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))

     licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_android_toggle_switch))
            .setDescription(context.getString(R.string.license_copy_android_toggle_switch))
            .setLinks(resources.getStringArray(
                R.array.license_links_android_toggle_switch))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_android_toggle_switch))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_android_toggle_switch))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))

     licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_hauler))
            .setDescription(context.getString(R.string.license_copy_hauler))
            .setLinks(resources.getStringArray(
                R.array.license_links_hauler))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_hauler))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_hauler))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ic_hauler)))

     licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_bigimageviewer))
            .setDescription(context.getString(R.string.license_copy_bigimageviewer))
            .setLinks(resources.getStringArray(
                R.array.license_links_bigimageviewer))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_bigimageviewer))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_bigimageviewer))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))

     licenses.add(DynamicInfo()
            .setTitle(context
                .getString(R.string.license_prettytime))
            .setLinks(resources.getStringArray(
                R.array.license_links_prettytime))
            .setLinksSubtitles(resources.getStringArray(
                R.array.license_links_subtitles_prettytime))
            .setLinksUrls(resources.getStringArray(
                R.array.license_links_url_prettytime))
            .setLinksIconsResId(R.array.ads_license_links_icons)
            .setLinksColorsResId(R.array.ads_license_links_colors)
            .setIconBig(DynamicResourceUtils.getDrawable(
                context, R.drawable.ads_ic_android)))

        licenses.add(
            DynamicInfo()
                .setTitle(context.getString(R.string.ads_license_android))
                .setDescription(context.getString(R.string.ads_license_copy_android_google))
                .setLinks(resources.getStringArray(R.array.ads_license_links_apache_only))
                .setLinksSubtitles(
                    resources.getStringArray(
                        R.array.ads_license_links_subtitles_license
                    )
                )
                .setLinksUrls(
                    resources.getStringArray(
                        R.array.ads_license_links_urls_apache_only
                    )
                )
                .setLinksIconsResId(R.array.ads_license_links_icons_license)
                .setLinksColorsResId(R.array.ads_license_links_colors_license)
                .setIconBig(
                    DynamicResourceUtils.getDrawable(
                        context, R.drawable.ads_ic_android
                    )
                )
        )


        // Set adapter for the recycler view.
        adapter =
            LicensesAdapter(licenses)
    }

    internal class LicensesAdapter(

        /**
         * Data set used by this adapter.
         */
        private val dataSet: ArrayList<DynamicInfo>?
    ) : DynamicSimpleBinderAdapter<DynamicRecyclerViewBinder<*>>() {

        init {
            // Add license binder for this adapter.
            addDataBinder(LicenseBinder(this))
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            (getDataBinder(getItemViewType(position))
                    as InfoBigBinder).data = getItem(position)

            super.onBindViewHolder(viewHolder, position)
        }

        override fun getItemCount(): Int {
            return dataSet!!.size
        }

        /**
         * Returns the item according the position.
         *
         * @param position The position to retrieve the item.
         *
         * @return The item according the position.
         */
        private fun getItem(position: Int): DynamicInfo {
            return dataSet!![position]
        }

        inner class LicenseBinder(binderAdapter: LicensesAdapter) : InfoBigBinder(binderAdapter) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_license_card, parent, false
                    )
                )
            }
        }

    }


}