package com.android.intdv

import android.app.Application
import android.content.Context
import com.android.intdv.core.di.applicationModule
import com.android.intdv.core.di.movieModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Represents the application class
 */
class MoviesApp : Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MoviesApp

        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
    }

    private fun injectMembers() =
        startKoin {
            androidLogger()

            // use the Android context given there
            androidContext(this@MoviesApp.applicationContext)

            // module list
            modules(listOf(applicationModule, movieModule))
        }
}