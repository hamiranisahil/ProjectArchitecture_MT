package com.example.myarchitecture

import android.app.Application

class AppClass: Application() {

    override fun onCreate() {
        super.onCreate()
        //        FirebaseApp.initializeApp(this);
        AppConfig().projectSetUp(applicationContext)
    }
}