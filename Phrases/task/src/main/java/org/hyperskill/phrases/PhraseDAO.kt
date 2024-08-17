package org.hyperskill.phrases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhraseDAO {
    @Insert
    fun insert(phrase: Phrase)

    @Delete
    fun delete(phrase: Phrase)

    @Query("SELECT * FROM phrases WHERE id = :id")
    fun get(id: Long) : Phrase

    @Query("SELECT * FROM phrases")
    fun getAll() : List<Phrase>
}