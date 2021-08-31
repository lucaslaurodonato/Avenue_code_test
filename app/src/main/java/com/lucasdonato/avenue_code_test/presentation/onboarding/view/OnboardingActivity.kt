package com.lucasdonato.avenue_code_test.presentation.onboarding.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lucasdonato.avenue_code_test.R
import com.lucasdonato.avenue_code_test.mechanism.permission.AppPermissionUtils
import com.lucasdonato.avenue_code_test.mechanism.permission.AppPermissionUtils.Companion.LOCATION_PERMISSION
import com.lucasdonato.avenue_code_test.mechanism.permission.PermissionListener
import com.lucasdonato.avenue_code_test.mechanism.permission.hasPermission
import com.lucasdonato.avenue_code_test.presentation.onboarding.dialog.WelcomeChoiceDialog
import com.lucasdonato.avenue_code_test.presentation.home.view.HomeActivity
import com.lucasdonato.avenue_code_test.presentation.onboarding.presenter.OnboardingPresenter
import kotlinx.android.synthetic.main.activity_onboarding.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class OnboardingActivity : AppCompatActivity(), PermissionListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }

    private var homeDialog: WelcomeChoiceDialog? = null
    private val permissionUtils: AppPermissionUtils by inject { parametersOf(this, this) }
    private val presenter: OnboardingPresenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setupClickListeners()
        setupBackground()
    }

    private fun setupBackground() {
        background.background = presenter.getRandomBackground()
    }

    private fun startHomeScreen() {
        startActivity(HomeActivity.getStartIntent(this))
        finish()
    }

    private fun setupClickListeners() {
        start_home_button.setOnClickListener {
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
        requestCode: Int, permissions: Array<String>,
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