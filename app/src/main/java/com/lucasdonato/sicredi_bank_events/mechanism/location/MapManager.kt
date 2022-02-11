package com.lucasdonato.sicredi_bank_events.mechanism.location

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.data.model.Address
import com.lucasdonato.sicredi_bank_events.data.model.toAddressString
import com.lucasdonato.sicredi_bank_events.mechanism.constants.ERROR_TO_GET_ADDRESS

class MapManager(private val context: Context, private val locationUtils: LocationUtils) {

    private val TAG = MapManager::class.java.simpleName
    private var googleMap: GoogleMap? = null
    private var pin: Int = R.drawable.ic_google_maps

    fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            it.uiSettings?.apply {
                setAllGesturesEnabled(false)
                isCompassEnabled = false
                isMyLocationButtonEnabled = true
                isZoomControlsEnabled = false
                isZoomGesturesEnabled = true
                isScrollGesturesEnabled = true
                isRotateGesturesEnabled = true
            }
            it.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_json))
        }
    }

    fun showLocationOnMap(lat: Double, lng: Double) {
        try {
            googleMap?.let {
                it.clear()
                val currentLocation = LatLng(lat, lng)
                googleMap?.addMarker(
                    MarkerOptions().position(currentLocation).flat(false)
                        .icon(locationUtils.bitmapDescriptorFromVector(context, pin))
                        .draggable(true)
                )
                googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15.0f))
            }
        } catch (e: Exception) {
///TODO : DEFINIR UM ERRO
        }
    }

    fun showAllLocation(locationArrayList: ArrayList<LatLng?>) {
        try {
            googleMap.let { maps ->
                for (i in locationArrayList.indices) {
                    locationArrayList.get(i)?.let { latLng ->
                        maps?.addMarker(
                            MarkerOptions().position(latLng).flat(false)
                                .icon(locationUtils.bitmapDescriptorFromVector(context, pin))
                                .draggable(true)
                        )
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f))
                    }
                }
            }

        } catch (e: Exception) {
///TODO : DEFINIR UM ERRO
        }
    }

    fun getAddress(lat: Double, lng: Double): String {
        try {
            locationUtils.getAddress(lat, lng)?.let {
                Address(
                    neighborhood = it.subLocality,
                    city = it.subAdminArea,
                    state = it.adminArea,
                    country = it.countryName,
                    postalCode = it.postalCode,
                    latitude = it.latitude,
                    longitude = it.longitude
                ).apply {
                    fullAddress = toAddressString()
                    return fullAddress
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return ERROR_TO_GET_ADDRESS
    }

}