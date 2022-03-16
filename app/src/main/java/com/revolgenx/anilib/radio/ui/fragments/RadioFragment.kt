package com.revolgenx.anilib.radio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.otaliastudios.elements.Adapter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.BuildConfig
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.disableAds
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.RadioFragmentBinding
import com.revolgenx.anilib.radio.data.events.FavouriteEvent
import com.revolgenx.anilib.radio.repository.api.ResourceWrapper
import com.revolgenx.anilib.radio.repository.room.RadioStation
import com.revolgenx.anilib.radio.ui.source.RadioStationListSource
import com.revolgenx.anilib.radio.source.RadioStationSource
import com.revolgenx.anilib.radio.ui.adapter.RadioListAdapter
import com.revolgenx.anilib.radio.ui.presenter.AllRadioStationPresenter
import com.revolgenx.anilib.radio.ui.presenter.FavouriteRadioStationPresenter
import com.revolgenx.anilib.radio.ui.presenter.RecentRadioStationPresenter
import com.revolgenx.anilib.common.ui.view.KaoMojiStateView
import com.revolgenx.anilib.util.EventBusListener
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe
import org.koin.android.ext.android.inject


class RadioFragment : BaseLayoutFragment<RadioFragmentBinding>(), EventBusListener {

    private val dynamicTheme get() = DynamicTheme.getInstance().get()

    private val radioStoreSource by inject<RadioStationSource>()

    private val favouriteRadioSource by lazy {
        RadioStationListSource()
    }

    private val recentRadioStationSource by lazy {
        RadioStationListSource()
    }

    private val recentRadioStationPresenter by lazy {
        RecentRadioStationPresenter(requireContext())
    }

    private val allRadioStationPresenter by lazy {
        AllRadioStationPresenter(requireContext())
    }

    private val favouriteStationPresenter by lazy {
        FavouriteRadioStationPresenter(requireContext())
    }

    private lateinit var favouriteStationAdapter: Adapter
    private lateinit var allStationAdapter: RadioListAdapter
    private lateinit var recentStationAdapter: Adapter


    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun bindView(inflater: LayoutInflater, parent: ViewGroup?): RadioFragmentBinding {
        val v = RadioFragmentBinding.inflate(inflater, parent, false)

        if (!disableAds()) {
            val adView = AdView(requireContext())
            adView.id = R.id.adView
            adView.adSize = AdSize.SMART_BANNER
            adView.adUnitId = if (BuildConfig.DEBUG) {
                getString(R.string.ads_unit_id)
            } else {
                BuildConfig.adUnitId
            }

            v.radioFragmentScrollContainer.addView(adView, 0)

        }

        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(!disableAds()){
            with(binding.root.findViewById<AdView>(R.id.adView)) {
                loadAd(AdRequest.Builder().build());
            }
        }

        binding.favouriteStationRecyclerView.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        binding.radioStationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recentStationRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        recentStationAdapter =
            Adapter.builder(viewLifecycleOwner).addSource(recentRadioStationSource)
                .addPresenter(recentRadioStationPresenter).build()
        favouriteStationAdapter =
            Adapter.builder(viewLifecycleOwner).addSource(favouriteRadioSource)
                .addPresenter(favouriteStationPresenter).build()
        allStationAdapter =
            RadioListAdapter(requireContext())

        binding.radioStationRecyclerView.adapter = allStationAdapter
        binding.favouriteStationRecyclerView.adapter = favouriteStationAdapter
        binding.recentStationRecyclerView.adapter = recentStationAdapter


        radioStoreSource.recentRadioStations.observe(viewLifecycleOwner) {
            binding.recentStationLayout.visibility =
                if (it.isEmpty()) View.GONE else View.VISIBLE

            recentRadioStationSource.submitList(it)
        }


        radioStoreSource.favouriteRadioStations.observe(viewLifecycleOwner) {
            binding.favouriteStationLayout.visibility =
                if (it.isEmpty()) View.GONE else View.VISIBLE


            (binding.favouriteStationRecyclerView.layoutManager as GridLayoutManager).spanCount =
                if (it.size > 1) 2 else 1

            favouriteRadioSource.submitList(it)

        }

        radioStoreSource.radioStations.observe(viewLifecycleOwner) {
            allStationAdapter.submitList(it)
        }

        radioStoreSource.storeStateCallback.observe(viewLifecycleOwner, onResourceLoaded)


        binding.swipeToRefresh.setOnRefreshListener {
            radioStoreSource.loadAllStation(true)
        }

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.player_controller_container, RadioControllerFragment()).commit()
            if (radioStoreSource.storeStateCallback.value == null) {
                radioStoreSource.loadAllStation()
            }
        }
    }


    private val onResourceLoaded = Observer<ResourceWrapper<List<RadioStation>>> { it ->
        binding.swipeToRefresh.isRefreshing = false
        when (it) {
            is ResourceWrapper.Success -> {
                binding.kaoMojiStateView.visibility = View.GONE
            }
            is ResourceWrapper.GenericError -> {
                binding.kaoMojiStateView.visibility = View.VISIBLE
                binding.kaoMojiStateView.kaoMojiState = KaoMojiStateView.KoeMojiState.ERROR
                binding.kaoMojiStateView.kaoMojiMsgText =
                    it.code?.toString() + " " + it.error?.errorMsg
            }
            is ResourceWrapper.UnknownError -> {
                binding.kaoMojiStateView.visibility = View.VISIBLE
                binding.kaoMojiStateView.kaoMojiState = KaoMojiStateView.KoeMojiState.ERROR
                it.error?.errorMsg?.let {
                    binding.kaoMojiStateView.kaoMojiMsgText = it
                }
            }
            ResourceWrapper.Loading -> {
                binding.kaoMojiStateView.visibility = View.VISIBLE
                binding.kaoMojiStateView.kaoMojiState = KaoMojiStateView.KoeMojiState.LOADING
            }
            ResourceWrapper.NetworkError -> {
                binding.kaoMojiStateView.visibility = View.VISIBLE
                binding.kaoMojiStateView.kaoMojiState = KaoMojiStateView.KoeMojiState.ERROR
                binding.kaoMojiStateView.kaoMojiMsgText = getString(R.string.network_error)
            }
        }
    }


    @Subscribe
    fun onFavouriteEvent(event: FavouriteEvent) {
        radioStoreSource.setFavourite(event.id, event.isFavourite)
        allStationAdapter.onRadioItemChanged(event.id)
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

}