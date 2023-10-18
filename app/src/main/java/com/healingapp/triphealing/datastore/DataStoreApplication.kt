package com.healingapp.triphealing.datastore

import android.app.Application

class DataStoreApplication : Application() {

    private lateinit var dataStore : DataStoreModule

    companion object {
        private lateinit var sampleApplication: DataStoreApplication
        fun getInstance() : DataStoreApplication = sampleApplication
    }

    override fun onCreate() {
        super.onCreate()
        sampleApplication = this
        dataStore = DataStoreModule(this)
    }

    fun getDataStore() : DataStoreModule = dataStore
}