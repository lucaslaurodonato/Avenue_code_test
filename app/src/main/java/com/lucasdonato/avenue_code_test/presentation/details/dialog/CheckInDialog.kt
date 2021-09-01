package com.lucasdonato.avenue_code_test.presentation.details.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.lucasdonato.avenue_code_test.R
import com.lucasdonato.avenue_code_test.mechanism.extensions.get
import kotlinx.android.synthetic.main.check_in_dialog.*

class CheckInDialog(
    context: Context,
    private val listener: DialogListener
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_in_dialog)

        submit_button.apply {
            setOnClickListener {
                val name = check_in_name_edit_text.text.isNullOrEmpty()
                val email = check_in_email_edit_text.text.isNullOrEmpty()

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