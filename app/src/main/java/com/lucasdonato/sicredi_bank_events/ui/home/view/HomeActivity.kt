package com.lucasdonato.sicredi_bank_events.ui.home.view

import Events
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.data.remote.WebService
import com.lucasdonato.sicredi_bank_events.data.remote.dataSource.EventsDataSource
import com.lucasdonato.sicredi_bank_events.data.repository.events.EventsRepository
import com.lucasdonato.sicredi_bank_events.data.useCase.EventsUseCase
import com.lucasdonato.sicredi_bank_events.databinding.ActivityHomeBinding
import com.lucasdonato.sicredi_bank_events.mechanism.extensions.gone
import com.lucasdonato.sicredi_bank_events.mechanism.extensions.visible
import com.lucasdonato.sicredi_bank_events.mechanism.livedata.Status
import com.lucasdonato.sicredi_bank_events.mechanism.location.MapManager
import com.lucasdonato.sicredi_bank_events.ui.base.view.BaseActivity
import com.lucasdonato.sicredi_bank_events.ui.details.view.EventDetailActivity
import com.lucasdonato.sicredi_bank_events.ui.home.adapter.EventsRecyclerAdapter
import com.lucasdonato.sicredi_bank_events.ui.home.viewmodel.HomeViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home), OnMapReadyCallback {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    private val viewModel: HomeViewModel by viewModel()

    private val eventsRecyclerAdapter: EventsRecyclerAdapter by lazy { EventsRecyclerAdapter() }

    private val mapManager: MapManager by inject { parametersOf(this) }

    private val locationArrayList: ArrayList<LatLng?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getEventsList()
        setupObserver()
        setupMapsFragment()
        closeApp()
    }

    private fun setupMapsFragment() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.home_map) as SupportMapFragment
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

    private fun setupSuccess(results: List<Events>) {
        binding.loaderHome.gone()
        binding.groupEmptyState.gone()

        binding.groupHome.visible()
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

    private fun setupRecyclerView() {
        binding.homeRecyclerEvents.eventsRecycler.apply {
            adapter = eventsRecyclerAdapter
            eventsRecyclerAdapter.onItemClickListener = {
                startActivity(EventDetailActivity.getStartIntent(context, it.id))
            }
        }
    }

    private fun closeApp() {
        binding.toolbar.closeHome.setOnClickListener { finish() }
    }

    private fun setupLoading() {
        binding.groupHome.gone()
        binding.loaderHome.visible()
    }

    private fun setupError() {
        binding.loaderHome.gone()
        binding.groupHome.gone()
        binding.groupEmptyState.visible()
        binding.emptyState.btTryAgain.setOnClickListener {
            viewModel.getEventsList()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mapManager.onMapReady(map)
    }

}