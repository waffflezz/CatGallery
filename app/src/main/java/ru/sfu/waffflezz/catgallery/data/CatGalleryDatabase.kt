package ru.sfu.waffflezz.catgallery.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [
        CardEntity::class
    ],
    version = 1
)
abstract class CatGalleryDatabase : RoomDatabase() {
    abstract val dao: Dao

    companion object {
        fun createDataBase(context: Context): CatGalleryDatabase {
            return Room.databaseBuilder(
                context,
                CatGalleryDatabase::class.java,
                "main.db"
            ).build()
        }
    }
}