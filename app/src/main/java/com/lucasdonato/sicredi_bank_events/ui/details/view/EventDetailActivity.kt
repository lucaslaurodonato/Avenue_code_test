package com.lucasdonato.sicredi_bank_events.ui.details.view

import CheckIn
import Events
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
import com.lucasdonato.sicredi_bank_events.mechanism.constants.EXTRA_RESULTS
import com.lucasdonato.sicredi_bank_events.mechanism.extensions.*
import com.lucasdonato.sicredi_bank_events.mechanism.livedata.Status
import com.lucasdonato.sicredi_bank_events.mechanism.location.MapManager
import com.lucasdonato.sicredi_bank_events.ui.base.view.BaseActivity
import com.lucasdonato.sicredi_bank_events.ui.details.dialog.CheckInDialog
import com.lucasdonato.sicredi_bank_events.ui.details.viewModel.EventDetailViewModel
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.empty_state_mode.*
import kotlinx.android.synthetic.main.include_card_details.*
import kotlinx.android.synthetic.main.include_card_details.view.*
import kotlinx.android.synthetic.main.include_toolbar.view.*
import kotlinx.android.synthetic.main.success_check_in_layout.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class EventDetailActivity : BaseActivity<ActivityDetailsBinding>(R.layout.activity_details),
    OnMapReadyCallback {

    companion object {
        fun getStartIntent(context: Context, id: Int?): Intent =
            Intent(context, EventDetailActivity::class.java).apply {
                putExtra(EXTRA_RESULTS, id)
            }
    }

    private val viewModel: EventDetailViewModel by inject()
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

    private fun receiveData() {
        intent?.getIntExtra(EXTRA_RESULTS, 0)?.let {
            id = it
            viewModel.getEventById(id)
        }
    }

    private fun setupObserver() {
        viewModel.getEvent.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> setupLoading(false)
                Status.SUCCESS -> it.data?.let { setupView(it) }
                Status.ERROR -> setupError(false)
            }
        })
        viewModel.checkIn.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> setupLoading(true)
                Status.SUCCESS -> setupCheckInSuccess()
                Status.ERROR -> setupError(true)
            }
        })
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

    private fun checkInButton() {
        binding.incCardDetails.tvDescriptionEventOpenCheckIn.setOnClickListener {
            showCheckInDialog()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        mapManager.onMapReady(map)
    }

    private fun setupToolbar(title: String) {
        binding.incToolbar.apply {
            this.ivBack.setOnClickListener { finish() }
            this.tvTextToolbar.text = title
        }
    }

    private fun setupCheckInSuccess() {
        binding.incSuccessCheckIn.incLoadingBackground.visible()
        binding.incSuccessCheckIn.ltvCheckInAnimation.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                binding.incLoaderDetails.gone()
                binding.incSuccessCheckIn.incLoadingBackground.gone()
                checkInDialog?.dismiss()
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
    }

    private fun setupLoading(isCheckin: Boolean) {
        if (isCheckin) {
            binding.incLoaderCheckIn.visible()
            checkInDialog?.dismiss()
        } else {
            binding.incLoaderDetails.visible()
            binding.incCardDetails.incCardDetails.gone()
        }
    }

    private fun setupError(isCheckin: Boolean) {
        if (isCheckin) {
            binding.incLoaderCheckIn.gone()
            toast(getString(R.string.error_in_check_in))
        } else {
            binding.incLoaderDetails.gone()
            binding.incCardDetails.incCardDetails.gone()
            binding.incEmptyState.incEmptyState.visible()
            binding.incEmptyState.btTryAgain.setOnClickListener {
                viewModel.getEventById(id)
            }
        }
    }

    private fun setupView(events: Events?) {
        binding.incLoaderDetails.gone()
        binding.incCardDetails.incCardDetails.visible()
        binding.incEmptyState.incEmptyState.gone()

        events?.also {
            lat = it.latitude
            lng = it.longitude
            setupToolbar(it.title)
            binding.incCardDetails.tvPriceEvent.text = convertToPrice(it.price)
            binding.incCardDetails.tvDescriptionEvent.text = it.description
            binding.incCardDetails.tvDateEvent.text = convertLongToTime(it.date)
            binding.incCardDetails.tvAddressEvent.text = mapManager.getAddress(lat, lng)
            mapManager.showLocationOnMap(lat, lng)
        }
        binding.incCardDetails.tvDescriptionEventOpenMaps.setOnClickListener {
            createIntentToGoogleMaps()
        }
    }

    ///TODO: REVIEW THIS METHOD
    private fun createIntentToGoogleMaps() {
        val uri = Uri.parse("google.navigation:q=$lat,$lng")
        Intent(Intent.ACTION_VIEW, uri).apply {
            this.setPackage("com.google.android.apps.maps")
            startActivity(this)
        }
    }

}