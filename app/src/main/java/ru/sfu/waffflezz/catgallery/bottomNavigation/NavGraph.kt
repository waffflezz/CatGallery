package ru.sfu.waffflezz.catgallery.bottomNavigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.sfu.waffflezz.catgallery.viewmodels.MainScreenViewModel

@Composable
fun NavGraph(
    navHostController: NavHostController,
    innerPadding: PaddingValues,
    mainScreenViewModel: MainScreenViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.HomeScreenRoute,
        Modifier.padding(innerPadding)
    ) {
        composable(Routes.HomeScreenRoute) {
            HomeScreen(mainScreenViewModel, navController = navHostController)
        }
        composable(Routes.CatsScreenRoute) {
            CatsScreen()
        }
        composable(Routes.FavoriteScreenRoute) {
            FavoriteScreen()
        }
        composable(
            Routes.CardScreenRouteGraph,
            arguments = listOf(navArgument(Routes.CardScreenRouteArgument) { type = NavType.StringType })
        ) { backStackEntry ->
            FullScreenCard(
                backStackEntry.arguments?.getString(Routes.CardScreenRouteArgument),
                mainScreenViewModel
            )
        }
        composable(Routes.SettingsScreenRoute) {
            SettingsScreen()
        }
    }
}