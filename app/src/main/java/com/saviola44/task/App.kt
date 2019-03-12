package com.saviola44.task

import android.app.Application

class App: Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App
        fun appCtx() = instance.applicationContext
    }
}