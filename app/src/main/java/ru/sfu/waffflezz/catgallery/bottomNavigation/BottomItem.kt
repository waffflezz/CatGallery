package ru.sfu.waffflezz.catgallery.bottomNavigation

import ru.sfu.waffflezz.catgallery.R

sealed class BottomItem(val title: String, val iconId: Int, val route: String) {
    object HomeScreenItem: BottomItem("Home", R.drawable.home, Routes.HomeScreenRoute)
    object CatsScreenItem: BottomItem("Cats", R.drawable.cat, Routes.CatsScreenRoute)
    object FavoriteScreenItem: BottomItem("Favorite", R.drawable.favorite, Routes.FavoriteScreenRoute)
}