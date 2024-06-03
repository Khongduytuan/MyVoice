package com.eagletech.myvoice.appEvr

import android.app.Application
import android.content.Context

class App : Application() {
    var app: Context? = null
    override fun onCreate() {
        super.onCreate()
        app = this
    }
}