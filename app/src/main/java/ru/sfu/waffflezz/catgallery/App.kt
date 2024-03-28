package ru.sfu.waffflezz.catgallery

import android.app.Application
import ru.sfu.waffflezz.catgallery.data.CatGalleryDatabase

class App : Application() {
    val database by lazy { CatGalleryDatabase.createDataBase(this) }
}