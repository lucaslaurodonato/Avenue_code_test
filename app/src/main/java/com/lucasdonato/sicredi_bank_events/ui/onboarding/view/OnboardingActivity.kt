package com.lucasdonato.sicredi_bank_events.ui.onboarding.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.databinding.ActivityOnboardingBinding
import com.lucasdonato.sicredi_bank_events.utils.permission.AppPermissionUtils
import com.lucasdonato.sicredi_bank_events.utils.permission.AppPermissionUtils.Companion.LOCATION_PERMISSION
import com.lucasdonato.sicredi_bank_events.utils.permission.PermissionListener
import com.lucasdonato.sicredi_bank_events.utils.permission.hasPermission
import com.lucasdonato.sicredi_bank_events.ui.base.view.BaseActivity
import com.lucasdonato.sicredi_bank_events.ui.onboarding.dialog.WelcomeChoiceDialog
import com.lucasdonato.sicredi_bank_events.ui.home.view.HomeActivity
import com.lucasdonato.sicredi_bank_events.ui.onboarding.viewModel.OnboardingViewModel
import kotlinx.android.synthetic.main.activity_onboarding.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding),
    PermissionListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }

    private var homeDialog: WelcomeChoiceDialog? = null
    private val permissionUtils: AppPermissionUtils by inject { parametersOf(this, this) }
    private val viewModel: OnboardingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListeners()
        setupBackground()
    }

    private fun setupBackground() {
        binding.ivBackground.background = viewModel.getRandomBackground()
    }

    private fun startHomeScreen() {
        startActivity(HomeActivity.getStartIntent(this))
        finish()
    }

    private fun setupClickListeners() {
        binding.startHomeButton.setOnClickListener {
            if (hasPermission(this@OnboardingActivity, LOCATION_PERMISSION)) {
                startHomeScreen()
            } else {
                showPermissionDialog()
            }
        }
    }

    private fun showPermissionDialog() {
        homeDialog = WelcomeChoiceDialog(context = this,
            listener = object : WelcomeChoiceDialog.DialogListener {
                override fun onNegativeClickListener() {
                    homeDialog?.dismiss()
                    if (!hasPermission(this@OnboardingActivity, LOCATION_PERMISSION)) {
                        permissionUtils.requestPermissions(LOCATION_PERMISSION)
                    }
                }

                override fun onPositiveClickListener() {
                    permissionUtils.requestPermissions(LOCATION_PERMISSION)
                    homeDialog?.dismiss()
                }
            }).apply {
            setCancelable(true)
            show()
        }
    }

    override fun onPermissionGranted(permissions: List<String>) {
        when (permissions) {
            LOCATION_PERMISSION -> {
                startHomeScreen()
            }
        }
    }

    override fun onPermissionDenied(permissions: List<String>) {
        when (permissions) {
            LOCATION_PERMISSION -> {
                permissionUtils.openPermissions()
            }
        }
    }

    override fun onPermissionNeverAskAgain(permissions: List<String>) {
        when (permissions) {
            LOCATION_PERMISSION -> {
                permissionUtils.openPermissions()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppPermissionUtils.PERMISSION_REQUEST_CODE -> {
                if (hasPermission(this, LOCATION_PERMISSION)) {
                    startHomeScreen()
                }
            }
        }
    }

}