package org.hyperskill.phrases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phrases")
data class Phrase(
    var phrase: String,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)