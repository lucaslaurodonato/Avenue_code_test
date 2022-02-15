package com.lucasdonato.sicredi_bank_events.ui.details.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.utils.extensions.get
import com.lucasdonato.sicredi_bank_events.utils.extensions.validate

class CheckInDialog(context: Context, private val listener: DialogListener) : Dialog(context) {

    private lateinit var btSubmit: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_in_dialog)

        btSubmit = findViewById(R.id.submit_button)
        etName = findViewById(R.id.check_in_name_edit_text)
        etEmail = findViewById(R.id.check_in_email_edit_text)

        btSubmit.apply {
            setOnClickListener {
                val name = etName.validate()
                val email = etEmail.validate()

                etName.error = null
                etEmail.error = null

                when {
                    name -> etName.error = context.getString(R.string.fields_error_check_in_name)
                    email -> etEmail.error = context.getString(R.string.fields_error_check_in_email)
                    else -> listener.onSubmitButtonClick(
                        etName.get(),
                        etEmail.get()
                    )
                }
            }
        }

        val lp = WindowManager.LayoutParams()

        lp.apply {

            this@CheckInDialog.window?.let {
                copyFrom(it.attributes)
            }
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        this.window?.let {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.attributes = lp
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }

    interface DialogListener {
        fun onSubmitButtonClick(name: String, email: String)
    }
}