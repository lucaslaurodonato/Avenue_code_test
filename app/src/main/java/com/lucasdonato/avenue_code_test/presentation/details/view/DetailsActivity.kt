package com.lucasdonato.avenue_code_test.presentation.details.view

import Events
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.lucasdonato.avenue_code_test.R
import com.lucasdonato.avenue_code_test.mechanism.constants.EXTRA_RESULTS
import com.lucasdonato.avenue_code_test.mechanism.extensions.convertLongToTime
import com.lucasdonato.avenue_code_test.mechanism.extensions.convertToPrice
import com.lucasdonato.avenue_code_test.mechanism.extensions.gone
import com.lucasdonato.avenue_code_test.mechanism.extensions.visible
import com.lucasdonato.avenue_code_test.mechanism.livedata.Status
import com.lucasdonato.avenue_code_test.mechanism.location.MapManager
import com.lucasdonato.avenue_code_test.presentation.details.presenter.DetailsPresenter
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.empty_state_mode.*
import kotlinx.android.synthetic.main.include_card_details.*
import kotlinx.android.synthetic.main.include_toolbar.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        fun getStartIntent(context: Context, id: Int?): Intent =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_RESULTS, id)
            }
    }

    private val presenter: DetailsPresenter by inject()
    private val mapManager: MapManager by inject { parametersOf(this) }
    private var id: Int = 0
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        receiveData()
        setupObserver()
        setupMaps()
    }

    private fun setupMaps() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.details_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun receiveData() {
        intent?.getIntExtra(EXTRA_RESULTS, 0)?.let {
            id = it
            presenter.getEventsList(id)
        }
    }

    private fun setupObserver() {
        presenter.getEventLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> setupLoading()
                Status.SUCCESS -> it.data?.let { setupView(it) }
                Status.ERROR -> setupError()
                else -> setupError()
            }
        })
    }

    override fun onMapReady(map: GoogleMap?) {
        mapManager.onMapReady(map)
    }

    private fun clickListeners(title: String) {
        include_toolbar.apply {
            this.back.setOnClickListener { finish() }
            this.text_toolbar.text = title
        }
    }

    private fun setupLoading() {
        loader_details.visible()
        include_card_details.gone()
    }

    private fun setupError() {
        loader_details.gone()
        include_card_details.gone()
        empty_state.visible()
        try_again_button.setOnClickListener {
            presenter.getEventsList(id)
        }
    }

    private fun createIntentToGoogleMaps() {
        val uri = Uri.parse("google.navigation:q=$lat,$lng")
        Intent(Intent.ACTION_VIEW, uri).apply {
            this.setPackage("com.google.android.apps.maps")
            startActivity(this)
        }
    }

    private fun setupView(events: Events?) {
        loader_details.gone()
        empty_state.gone()
        include_card_details.visible()
        events?.also {
            lat = it.latitude
            lng = it.longitude
            clickListeners(it.title)
            price_event.text = convertToPrice(it.price)
            description_event.text = it.description
            date_event.text = convertLongToTime(it.date)
            address_event.text = mapManager.getAddress(lat, lng)
            mapManager.showLocationOnMap(lat, lng)
        }
        description_event_open_maps.setOnClickListener {
            createIntentToGoogleMaps()
        }
    }

}