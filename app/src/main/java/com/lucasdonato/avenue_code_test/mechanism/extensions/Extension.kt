package com.lucasdonato.avenue_code_test.mechanism.extensions

import android.app.Activity
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY
import android.view.View
import android.view.View.*
import android.widget.EditText
import android.widget.Toast
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun EditText.setDrawableEnd(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd.MM.yyyy - HH:mm")
    return format.format(date)
}

fun convertToPrice(price: Double): String {
    val str: String = NumberFormat.getCurrencyInstance().format(price)
    return str
}


fun EditText.get() = text.toString().trim()

fun EditText.validate() = text.toString().isNotEmpty()

fun View.gone() {
    visibility = GONE
}

fun View.visible() {
    visibility = VISIBLE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun Activity.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun View.toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()






