package ru.sfu.waffflezz.catgallery.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_table")
data class CardEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val width: Int,
    val height: Int,
    val imageUrl: String,
    val title: String?,
    val origin: String?,
    val description: String?,
    val temperament: String?,
    val lifeSpan: String?,
)
