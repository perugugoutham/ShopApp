package com.goutham.perugu.shopapp

import android.app.Application
import com.facebook.stetho.Stetho

class ShoppingApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}