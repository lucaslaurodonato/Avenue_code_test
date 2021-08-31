package com.lucasdonato.avenue_code_test.mechanism.location

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.util.*

class LocationUtils(private val context: Context) {

    private val geocoder = Geocoder(context, Locale("pt", "BR"))
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    fun getAddress(lat: Double, lng: Double): android.location.Address? {
        return try {
            geocoder.getFromLocation(lat, lng, 1)?.first()
        } catch (e: Exception) {
            null
        }
    }

    fun getLatLngFromAddress(address: String): android.location.Address? {
        return try {
            geocoder.getFromLocationName(address, 1)?.first()
        } catch (e: Exception) {
            null
        }
    }
}

