package com.lucasdonato.sicredi_bank_events.ui.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.databinding.ActivityHomeBinding
import com.lucasdonato.sicredi_bank_events.model.data.model.entities.home.Events
import com.lucasdonato.sicredi_bank_events.utils.extensions.gone
import com.lucasdonato.sicredi_bank_events.utils.extensions.visible
import com.lucasdonato.sicredi_bank_events.utils.livedata.Status
import com.lucasdonato.sicredi_bank_events.utils.location.MapManager
import com.lucasdonato.sicredi_bank_events.ui.base.view.BaseActivity
import com.lucasdonato.sicredi_bank_events.ui.details.view.EventDetailActivity
import com.lucasdonato.sicredi_bank_events.ui.home.adapter.EventsRecyclerAdapter
import com.lucasdonato.sicredi_bank_events.ui.home.viewmodel.HomeViewModel
import com.lucasdonato.sicredi_bank_events.utils.extensions.toast
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home), OnMapReadyCallback {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    private val viewModel: HomeViewModel by viewModel()
    private val mapManager: MapManager by inject { parametersOf(this) }
    private val eventsRecyclerAdapter: EventsRecyclerAdapter by lazy { EventsRecyclerAdapter() }
    private val locationArrayList: ArrayList<LatLng?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getEventsList()
        setupObserver()
        setupMapsFragment()
        closeApp()
        easterEgg()
    }

    private fun setupMapsFragment() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_home_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupObserver() {
        viewModel.eventList.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> setupLoading()
                Status.SUCCESS -> it.data?.let { setupSuccess(it.toMutableList()) }
                Status.ERROR -> setupError()
            }
        })
    }

    private fun setupRecyclerView() {
        binding.incHomeRecyclerEvents.eventsRecycler.apply {
            adapter = eventsRecyclerAdapter
            eventsRecyclerAdapter.onItemClickListener = {
                startActivity(EventDetailActivity.getStartIntent(context, it.id))
            }
        }
    }

    private fun closeApp() {
        binding.incToolbar.ivCloseHome.setOnClickListener { finish() }
    }

    private fun successCase() {
        binding.incLoaderHome.gone()
        binding.incEmptyState.emptyState.gone()
        binding.groupHome.visible()
    }

    private fun setupSuccess(results: List<Events>) {
        successCase()
        results.let {
            it.forEach {
                val latLng = LatLng(it.latitude, it.longitude)
                locationArrayList.addAll(listOf(latLng))
            }
            mapManager.showAllLocation(locationArrayList)
            eventsRecyclerAdapter.data = it.toMutableList()
            setupRecyclerView()
        }
    }

    private fun setupLoading() {
        binding.groupHome.gone()
        binding.incLoaderHome.visible()
    }

    private fun errorCase() {
        binding.incLoaderHome.gone()
        binding.groupHome.gone()
        binding.incEmptyState.emptyState.visible()
    }

    private fun setupError() {
        errorCase()
        binding.incEmptyState.btTryAgain.setOnClickListener {
            viewModel.getEventsList()
        }
    }

    private fun easterEgg() {
        binding.incToolbar.ivIconSicredi.setOnClickListener {
            toast(getString(R.string.easter_egg))
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mapManager.onMapReady(map)
    }

}