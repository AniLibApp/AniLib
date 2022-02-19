package com.revolgenx.anilib.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.AlActivityType
import com.revolgenx.anilib.databinding.UserActivityUnionFragmentLayoutBinding
import com.revolgenx.anilib.infrastructure.event.OpenActivityMessageComposer
import com.revolgenx.anilib.infrastructure.event.OpenActivityTextComposer
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.ui.presenter.ActivityUnionPresenter
import com.revolgenx.anilib.social.ui.viewmodel.ActivityInfoViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityMessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import com.revolgenx.anilib.type.ActivityType
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.user.viewmodel.UserContainerSharedVM
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserActivityUnionFragment : BasePresenterFragment<ActivityUnionModel>() {
    private val sharedViewModel by viewModel<UserContainerSharedVM>(owner = {
        ViewModelOwner.from(
            this.parentFragment ?: this,
            this.parentFragment
        )
    })
    private val userId get() = UserPreference.userId

    override val basePresenter: Presenter<ActivityUnionModel>
        get() = ActivityUnionPresenter(
            requireContext(),
            viewModel,
            activityInfoViewModel,
            textComposerViewModel,
            messageComposerViewModel
        )
    override val baseSource: Source<ActivityUnionModel>
        get() = viewModel.source ?: createSource()

    override val autoAddLayoutManager: Boolean = false
    private val viewModel by viewModel<ActivityUnionViewModel>()
    private val textComposerViewModel by sharedViewModel<ActivityTextComposerViewModel>()
    private val messageComposerViewModel by sharedViewModel<ActivityMessageComposerViewModel>()
    private val activityInfoViewModel by sharedViewModel<ActivityInfoViewModel>()


    private var _uBinding: UserActivityUnionFragmentLayoutBinding? = null
    private val uBinding get() = _uBinding!!

    private val activityAdapterItems by lazy {
        resources.getStringArray(R.array.user_activity_type_entries).map {
            DynamicMenu(
                null, it
            )
        }
    }

    override fun createSource(): Source<ActivityUnionModel> {
        return viewModel.createSource()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _uBinding = UserActivityUnionFragmentLayoutBinding.inflate(inflater, container, false)
        uBinding.userActivityContainer.addView(v)
        baseRecyclerView.recycledViewPool.setMaxRecycledViews(ActivityType.TEXT.ordinal, 10)
        return uBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.hasUserData ?: return

        if (savedInstanceState == null) {
            viewModel.field = ActivityUnionField().apply {
                userId = sharedViewModel.userId
                userName = sharedViewModel.userName
            }
        }

        layoutManager = LinearLayoutManager(requireContext())
        val adapter = makeSpinnerAdapter(requireContext(), activityAdapterItems)
        uBinding.userActivitySpinner.adapter = adapter
        uBinding.userActivitySpinner.onItemSelectedListener = null

        uBinding.userActivitySpinner.onItemSelected {
            if (!visibleToUser) return@onItemSelected
            viewModel.field.type = AlActivityType.values()[it]
            createSource()
            invalidateAdapter()
        }

        uBinding.userActivityCreateIv.setOnClickListener {
            if (viewModel.field.userId == userId) {
                OpenActivityTextComposer().postEvent
            } else {
                OpenActivityMessageComposer(viewModel.field.userId!!).postEvent
            }
        }
    }
}