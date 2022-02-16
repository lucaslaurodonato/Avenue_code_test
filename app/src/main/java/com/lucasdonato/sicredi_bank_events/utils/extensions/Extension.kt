package com.lucasdonato.sicredi_bank_events.utils.extensions

import android.app.Activity
import android.util.Patterns
import android.view.View
import android.view.View.*
import android.widget.EditText
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

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

fun EditText.validate() = text.toString().trim().isEmpty()

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

fun confirmIfEmailIsValid(email: String): Boolean {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches().not()
}





