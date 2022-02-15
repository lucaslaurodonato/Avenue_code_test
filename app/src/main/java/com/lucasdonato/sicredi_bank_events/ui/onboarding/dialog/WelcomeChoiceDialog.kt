package com.lucasdonato.sicredi_bank_events.ui.onboarding.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.lucasdonato.sicredi_bank_events.R
import kotlinx.android.synthetic.main.card_home_recycler_view.*
import kotlinx.android.synthetic.main.welcome_choice_dialog.*

class WelcomeChoiceDialog(
    context: Context,
    private val listener: DialogListener
) : Dialog(context) {

    private lateinit var positiveButton: Button
    private lateinit var negativeButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_choice_dialog)

        positiveButton = findViewById(R.id.bt_positive)
        negativeButton = findViewById(R.id.bt_negative)

        positiveButton.setOnClickListener {
            listener.onPositiveClickListener()
        }

        negativeButton.setOnClickListener {
            listener.onNegativeClickListener()
        }

        val lp = WindowManager.LayoutParams()

        lp.apply {
            this@WelcomeChoiceDialog.window?.let {
                copyFrom(it.attributes)
            }
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        this.window?.let {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.attributes = lp
        }
    }

    interface DialogListener {
        fun onNegativeClickListener()
        fun onPositiveClickListener()
    }
}