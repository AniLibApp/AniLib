package com.revolgenx.anilib.fragment.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.dialog.RecommendationFilterDialog
import com.revolgenx.anilib.event.BaseEvent
import com.revolgenx.anilib.event.BrowseEvent
import com.revolgenx.anilib.event.RecommendationFilterEvent
import com.revolgenx.anilib.field.recommendation.RecommendationField
import com.revolgenx.anilib.field.recommendation.RecommendationFilterField
import com.revolgenx.anilib.fragment.base.BaseFragment
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.recommendation.RecommendationModel
import com.revolgenx.anilib.preference.loggedIn
import com.revolgenx.anilib.preference.setRecommendationField
import com.revolgenx.anilib.presenter.recommendation.RecommendationPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.viewmodel.RecommendationViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

//todo gridlayout
class RecommendationFragment : BasePresenterFragment<RecommendationModel>() {
    override val basePresenter: Presenter<RecommendationModel>
        get() = RecommendationPresenter(requireContext(), viewLifecycleOwner, viewModel)
    override val baseSource: Source<RecommendationModel>
        get() = viewModel.source ?: createSource()

    private val viewModel by viewModel<RecommendationViewModel>()

    private val field: RecommendationField by lazy {
        RecommendationField().apply {
            RecommendationFilterField.field(requireContext()).let {
                sort = it.sorting
                if (requireContext().loggedIn())
                    onList = it.onList
            }
        }
    }

    override fun createSource(): Source<RecommendationModel> {
        return viewModel.createSource(field)
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.elementAt(position)?.element?.type == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionMenu()
        setHasOptionsMenu(true)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        inflater.inflate(R.menu.recommendation_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.searchMenu -> {
                BrowseEvent().postEvent
                true
            }

            R.id.recommendationFilter -> {
                RecommendationFilterDialog.newInstance().show(childFragmentManager, "filter_tag")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(event: BaseEvent) {
        when (event) {
            is RecommendationFilterEvent -> {
                if (requireContext().loggedIn()) {
                    field.onList = event.field.onList
                } else {
                    field.onList = null
                }
                field.sort = event.field.sorting
                setRecommendationField(requireContext(), event.field)
                createSource()
                invalidateAdapter()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }
}
