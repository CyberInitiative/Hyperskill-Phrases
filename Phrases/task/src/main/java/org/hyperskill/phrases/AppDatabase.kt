package org.hyperskill.phrases

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Phrase::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun phrases(): PhraseDAO
}