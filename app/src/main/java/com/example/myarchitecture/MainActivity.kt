package com.example.myarchitecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.library.api_call.ApiCall
import com.example.library.modals.CommonRes
import com.example.library.rx_api_call.RXApiCall
import com.example.library.util.printLog
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val coinListReq = CoinListReq()
        val coinList= RXApiCall(this, AppConfig().getRequestparams(coinListReq), coinListReq, ApiCall.WebServiceType.WS_SIMPLE).call() as Observable<CoinList>
        coinList?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe(this::handleResults, this::handleError)
    }

    fun handleResults(coinList: CoinList){
        printLog(Gson().toJson(coinList))
    }

    fun handleError(t: Throwable){
        t.message?.let {
            printLog(it)
        }
    }
}
