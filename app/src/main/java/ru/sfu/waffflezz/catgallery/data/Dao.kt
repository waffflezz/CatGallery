package ru.sfu.waffflezz.catgallery.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM card_table WHERE id = :cardId")
    fun getCardById(cardId: String): Flow<CardEntity>

    @Insert
    suspend fun insertCard(cardEntity: CardEntity)

    @Delete
    suspend fun deleteCard(cardEntity: CardEntity)

    @Update
    suspend fun updateCard(cardEntity: CardEntity)

    @Query("SELECT * FROM card_table")
    fun getAllCards(): Flow<List<CardEntity>>

    @Query("SELECT COUNT(*) FROM card_table WHERE id = :cardId")
    suspend fun hasCardById(cardId: String): Boolean
}