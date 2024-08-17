package org.hyperskill.phrases

import android.app.Application
import androidx.room.Room

class PhraseApplication : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "phrases.db")
            .allowMainThreadQueries()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: PhraseApplication
            private set
    }
}