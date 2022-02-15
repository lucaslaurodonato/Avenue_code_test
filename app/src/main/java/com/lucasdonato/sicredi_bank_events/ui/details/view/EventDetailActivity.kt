package com.lucasdonato.sicredi_bank_events.ui.details.view

import com.lucasdonato.sicredi_bank_events.model.data.model.entities.details.CheckIn
import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.databinding.ActivityDetailsBinding
import com.lucasdonato.sicredi_bank_events.model.data.model.entities.home.Events
import com.lucasdonato.sicredi_bank_events.utils.constants.EXTRA_RESULTS
import com.lucasdonato.sicredi_bank_events.utils.livedata.Status
import com.lucasdonato.sicredi_bank_events.utils.location.MapManager
import com.lucasdonato.sicredi_bank_events.ui.base.view.BaseActivity
import com.lucasdonato.sicredi_bank_events.ui.details.dialog.CheckInDialog
import com.lucasdonato.sicredi_bank_events.ui.details.viewModel.EventDetailViewModel
import com.lucasdonato.sicredi_bank_events.utils.constants.URI_INTENT
import com.lucasdonato.sicredi_bank_events.utils.constants.URI_MAPS
import com.lucasdonato.sicredi_bank_events.utils.extensions.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EventDetailActivity : BaseActivity<ActivityDetailsBinding>(R.layout.activity_details),
    OnMapReadyCallback {

    ///TODO FAZER BOTÃO PARA COMPARTILHAR EVENTO

    companion object {
        fun getStartIntent(context: Context, id: Int?): Intent =
            Intent(context, EventDetailActivity::class.java).apply {
                putExtra(EXTRA_RESULTS, id)
            }
    }

    private val viewModel: EventDetailViewModel by viewModel()
    private val mapManager: MapManager by inject { parametersOf(this) }
    private var checkInDialog: CheckInDialog? = null
    private var id: Int = 0
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveData()
        setupObserver()
        setupMaps()
        checkInButton()
    }

    private fun setupMaps() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fm_details_maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupToolbar(title: String) {
        binding.incToolbar.apply {
            this.ivBack.setOnClickListener { finish() }
            this.tvTextToolbar.text = title
        }
    }

    private fun receiveData() {
        intent?.getIntExtra(EXTRA_RESULTS, 0)?.let { id = it }
        callEventById()
    }

    private fun callEventById() {
        viewModel.getEventById(id)
    }

    private fun setupObserver() {
        viewModel.getEvent.observe(this, Observer { eventById ->
            when (eventById.status) {
                Status.LOADING -> setupLoading(false)
                Status.SUCCESS -> eventById.data?.let { setupView(it) }
                Status.ERROR -> setupError(false)
            }
        })
        viewModel.checkIn.observe(this, Observer { checkIn ->
            when (checkIn.status) {
                Status.LOADING -> setupLoading(true)
                Status.SUCCESS -> setupCheckInSuccess()
                Status.ERROR -> setupError(true)
            }
        })
    }

    private fun checkInButton() {
        binding.incCardDetails.tvDescriptionEventOpenCheckIn.setOnClickListener {
            showCheckInDialog()
        }
    }

    private fun successCaseCheckIn() {
        binding.incLoaderCheckIn.gone()
        binding.incSuccessCheckIn.incLoadingBackground.visible()
    }

    private fun setupCheckInSuccess() {
        successCaseCheckIn()
        binding.incSuccessCheckIn.ltvCheckInAnimation.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                binding.incSuccessCheckIn.incLoadingBackground.gone()
                checkInDialog?.dismiss()
            }
        })
    }

    private fun setupLoading(isCheckIn: Boolean) {
        if (isCheckIn) {
            binding.incLoaderCheckIn.visible()
            checkInDialog?.dismiss()
        } else {
            binding.incCardDetails.incCardDetails.gone()
            binding.incLoaderDetails.visible()
        }
    }

    private fun setupError(isCheckIn: Boolean) {
        if (isCheckIn) {
            binding.incLoaderCheckIn.gone()
            toast(getString(R.string.error_in_check_in))
        } else {
            binding.incLoaderDetails.gone()
            binding.incCardDetails.incCardDetails.gone()
            binding.incEmptyState.emptyState.visible()
            binding.incEmptyState.btTryAgain.setOnClickListener {
                viewModel.getEventById(id)
            }
        }
    }

    private fun successCase() {
        binding.incLoaderDetails.gone()
        binding.incEmptyState.emptyState.gone()
        binding.incCardDetails.incCardDetails.visible()
    }

    private fun setupView(events: Events?) {
        successCase()
        events?.also {
            setupToolbar(it.title)
            lat = it.latitude
            lng = it.longitude
            binding.incCardDetails.tvPriceEvent.text = convertToPrice(it.price)
            binding.incCardDetails.tvDescriptionEvent.text = it.description
            binding.incCardDetails.tvDateEvent.text = convertLongToTime(it.date)
            binding.incCardDetails.tvAddressEvent.text = mapManager.getAddress(lat, lng)
            mapManager.showLocationOnMap(lat, lng)
        }
        createMapIntent()
    }

    private fun createMapIntent() {
        binding.incCardDetails.tvDescriptionEventOpenMaps.setOnClickListener {
            createIntentToGoogleMaps()
        }
    }

    private fun createIntentToGoogleMaps() {
        val uri = Uri.parse("$URI_INTENT$lat,$lng")
        Intent(Intent.ACTION_VIEW, uri).apply {
            this.setPackage(URI_MAPS)
            startActivity(this)
        }
    }

    private fun showCheckInDialog() {
        checkInDialog = CheckInDialog(context = this@EventDetailActivity,
            listener = object : CheckInDialog.DialogListener {
                override fun onSubmitButtonClick(name: String, email: String) {
                    viewModel.postCheckIn(CheckIn(eventId = id, name = name, email = email))
                }
            }).apply {
            setCancelable(true)
            show()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mapManager.onMapReady(map)
    }

}