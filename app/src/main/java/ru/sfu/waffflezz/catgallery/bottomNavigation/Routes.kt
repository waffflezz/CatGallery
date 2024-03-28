package ru.sfu.waffflezz.catgallery.bottomNavigation

class Routes {
    companion object {
        const val HomeScreenRoute = "home_screen"
        const val CatsScreenRoute = "cats_screen"
        const val FavoriteScreenRoute = "favorite_screen"

        const val SettingsScreenRoute = "settings"

        const val CardScreenRouteArgument = "cardId"
        const val CardScreenRoute = "card_screen"
        const val CardScreenRouteGraph = "${CardScreenRoute}/{${CardScreenRouteArgument}}"
    }
}