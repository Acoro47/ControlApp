package com.example.controlhorasmobile

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class ControlHorasApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}