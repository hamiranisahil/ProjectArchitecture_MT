package com.example.library.util

class UnitConverterUtility {

    fun getMiles(meters: Int): Double {
        return meters * 0.000621371192
    }

    fun getMeters(miles: Int): Double {
        return miles * 1609.344
    }

    fun milesToKm(miles: Int) : Double{
        return miles * 1.609
    }

}