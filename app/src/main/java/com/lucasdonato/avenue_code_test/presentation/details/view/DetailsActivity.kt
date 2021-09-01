package com.lucasdonato.avenue_code_test.presentation.details.view

import CheckIn
import Events
import android.animation.Animator
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
import com.lucasdonato.avenue_code_test.mechanism.extensions.*
import com.lucasdonato.avenue_code_test.mechanism.livedata.Status
import com.lucasdonato.avenue_code_test.mechanism.location.MapManager
import com.lucasdonato.avenue_code_test.mechanism.permission.AppPermissionUtils
import com.lucasdonato.avenue_code_test.mechanism.permission.hasPermission
import com.lucasdonato.avenue_code_test.presentation.details.dialog.CheckInDialog
import com.lucasdonato.avenue_code_test.presentation.details.presenter.DetailsPresenter
import com.lucasdonato.avenue_code_test.presentation.onboarding.dialog.WelcomeChoiceDialog
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.success_check_in
import kotlinx.android.synthetic.main.empty_state_mode.*
import kotlinx.android.synthetic.main.include_card_details.*
import kotlinx.android.synthetic.main.include_toolbar.view.*
import kotlinx.android.synthetic.main.success_check_in_layout.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import kotlin.io.print

class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        fun getStartIntent(context: Context, id: Int?): Intent =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_RESULTS, id)
            }
    }

    private val presenter: DetailsPresenter by inject()
    private val mapManager: MapManager by inject { parametersOf(this) }
    private var checkInDialog: CheckInDialog? = null
    private var id: Int = 0
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        receiveData()
        setupObserver()
        setupMaps()
        checkInButton()
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
                Status.LOADING -> setupLoading(false)
                Status.SUCCESS -> it.data?.let { setupView(it) }
                Status.ERROR -> setupError(false)
                else -> setupError(false)
            }
        })
        presenter.postCheckInLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> setupLoading(true)
                Status.SUCCESS -> setupCheckInSuccess()
                Status.ERROR -> setupError(true)
                else -> setupError(true)
            }
        })
    }

    private fun showCheckInDialog() {
        checkInDialog = CheckInDialog(context = this@DetailsActivity,
            listener = object : CheckInDialog.DialogListener {
                override fun onSubmitButtonClick(name: String, email: String) {
                    presenter.postCheckIn(CheckIn(eventId = id, name = name, email = email))
                }
            }).apply {
            setCancelable(true)
            show()
        }
    }

    private fun checkInButton() {
        description_event_open_check_in.setOnClickListener {
            showCheckInDialog()
        }
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

    private fun setupCheckInSuccess(){
        success_check_in.visible()
        check_in_animation.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                loader_check_in.gone()
                success_check_in.gone()
                checkInDialog?.dismiss()
            }
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
    }

    private fun setupLoading(isCheckin: Boolean) {
        if(isCheckin){
            loader_check_in.visible()
            checkInDialog?.dismiss()
        } else {
            loader_details.visible()
            include_card_details.gone()
        }
    }

    private fun setupError(isCheckin: Boolean) {
        if(isCheckin){
            loader_check_in.gone()
            toast(getString(R.string.error_in_check_in))
        } else {
            loader_details.gone()
            include_card_details.gone()
            empty_state.visible()
            try_again_button.setOnClickListener {
                presenter.getEventsList(id)
            }
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