package com.lucasdonato.sicredi_bank_events.ui.details.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.mechanism.extensions.get
import com.lucasdonato.sicredi_bank_events.mechanism.extensions.validate
import kotlinx.android.synthetic.main.check_in_dialog.*

class CheckInDialog(context: Context, private val listener: DialogListener) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_in_dialog)

        submit_button.apply {
            setOnClickListener {
                val name = check_in_name_edit_text.validate()
                val email = check_in_email_edit_text.validate()

                input_text_name.error = null
                input_text_email.error = null

                when {
                    name -> input_text_name.error = context.getString(R.string.fields_error_check_in_name)
                    email -> input_text_email.error = context.getString(R.string.fields_error_check_in_email)
                    else -> listener.onSubmitButtonClick(
                        check_in_name_edit_text.get(),
                        check_in_email_edit_text.get()
                    )
                }
            }
        }

        val lp = WindowManager.LayoutParams()

        lp.apply{

            this@CheckInDialog.window?.let {
                copyFrom(it.attributes)
            }
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        this.window?.let{
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.attributes = lp
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }

    }

    interface DialogListener {
        fun onSubmitButtonClick(name: String, email: String)
    }
}