package com.lucasdonato.avenue_code_test.presentation.home.view

import Events
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.lucasdonato.avenue_code_test.R
import com.lucasdonato.avenue_code_test.databinding.ActivityHomeBinding
import com.lucasdonato.avenue_code_test.mechanism.extensions.gone
import com.lucasdonato.avenue_code_test.mechanism.extensions.visible
import com.lucasdonato.avenue_code_test.mechanism.livedata.Status
import com.lucasdonato.avenue_code_test.mechanism.location.MapManager
import com.lucasdonato.avenue_code_test.presentation.base.view.BaseActivity
import com.lucasdonato.avenue_code_test.presentation.details.view.DetailsActivity
import com.lucasdonato.avenue_code_test.presentation.home.adapter.EventsRecyclerAdapter
import com.lucasdonato.avenue_code_test.presentation.home.presenter.HomePresenter
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.empty_state_mode.*
import kotlinx.android.synthetic.main.home_content.*
import kotlinx.android.synthetic.main.home_toolbar.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home), OnMapReadyCallback {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    private val presenter: HomePresenter by inject()
    private val eventsRecyclerAdapter: EventsRecyclerAdapter by lazy { EventsRecyclerAdapter() }
    private val mapManager: MapManager by inject { parametersOf(this) }
    private val locationArrayList: ArrayList<LatLng?> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.getEventsList()
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
        presenter.getEventsLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> setupLoading()
                Status.SUCCESS -> it.data?.let { setupSuccess(it.toMutableList()) }
                Status.ERROR -> setupError()
                else -> setupError()
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
                startActivity(DetailsActivity.getStartIntent(context, it.id))
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
        binding.emptyState.tryAgainButton.setOnClickListener {
            presenter.getEventsList()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mapManager.onMapReady(map)
    }

}