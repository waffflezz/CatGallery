package ru.sfu.waffflezz.catgallery.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sfu.waffflezz.catgallery.data.api.BreedRequest

@Entity(tableName = "card_table")
data class CardEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val description: String,
    val breed_id: String,
    val life_span: String,
    val name: String,
    val origin: String,
    val temperament: String,
    val wikipedia_url: String
)
