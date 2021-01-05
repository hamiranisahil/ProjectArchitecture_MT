package com.example.library.util

import android.content.Context
import com.example.library.modals.GooglePlaceAutoComplete.GooglePlaceAutoCompleteRes
import com.example.library.modals.GooglePlaceAutoComplete.Prediction
import com.example.library.modals.GooglePlaceNearBy.GooglePlaceNearByRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class GooglePlaceUtil(val context: Context, val placeFetchListener: PlaceFetchListener) {

    companion object {
        var GOOGLE_MAP_KEY = ""
        val LOCATION_BASE_URL = "https://maps.googleapis.com/maps/api/place/"
    }

    init {
        initRetrofitClient()
    }

    private lateinit var retrofit: Retrofit

    fun getResults(input: String){
        val apiService = retrofit.create(ApiInterface::class.java)

        val call = apiService.searchLocation(
            input, GOOGLE_MAP_KEY
        )

        call.enqueue(object : Callback<GooglePlaceAutoCompleteRes> {

            override fun onResponse(
                call: Call<GooglePlaceAutoCompleteRes>,
                response: Response<GooglePlaceAutoCompleteRes>
            ) {
                try {
                    placeFetchListener.onPlaceFetchComplete(response.body()!!.predictions)
                } catch (e: Exception) {
                    placeFetchListener.onPlaceFetchError(e)
                }
            }

            override fun onFailure(call: Call<GooglePlaceAutoCompleteRes>, t: Throwable) {
                try {
                    context.showToast(t.message!!)
                } catch (e: java.lang.IllegalStateException) {
                    placeFetchListener.onPlaceFetchError(e)
                }
            }
        })
    }

    private fun initRetrofitClient() {
        retrofit = Retrofit.Builder()
            .baseUrl(LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    interface PlaceFetchListener {
        fun onPlaceFetchComplete(predictions: MutableList<Prediction>)
        fun onPlaceFetchError(e: Exception)
    }

    interface ApiInterface {

        @GET("autocomplete/json")
        fun searchLocation(
            @Query("input") input: String, @Query("key") key: String
        ): Call<GooglePlaceAutoCompleteRes>

        @GET("nearbysearch/json")
        fun nearbyLocation(
            @Query("location") location: String, @Query("radius") radius: String, @Query("key") key: String
        ): Call<GooglePlaceNearByRes>

    }


}