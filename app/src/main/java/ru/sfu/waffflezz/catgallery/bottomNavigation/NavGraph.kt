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
import ru.sfu.waffflezz.catgallery.data.CardViewModel
import ru.sfu.waffflezz.catgallery.viewmodels.FilterScreenViewModel
import ru.sfu.waffflezz.catgallery.viewmodels.MainScreenViewModel

@Composable
fun NavGraph(
    navHostController: NavHostController,
    innerPadding: PaddingValues,
    mainScreenViewModel: MainScreenViewModel,
    filterScreenViewModel: FilterScreenViewModel,
    cardViewModel: CardViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.HomeScreenRoute,
        Modifier.padding(innerPadding)
    ) {
        composable(Routes.HomeScreenRoute) {
            HomeScreen(
                mainScreenViewModel,
                navController = navHostController,
                cardViewModel
            )
        }
        composable(Routes.CatsScreenRoute) {
            CatsScreen(
                navHostController,
                filterScreenViewModel,
                cardViewModel
            )
        }
        composable(Routes.FavoriteScreenRoute) {
            FavoriteScreen(
                cardViewModel,
                navHostController
            )
        }
        composable(
            Routes.CardScreenRouteGraph,
            arguments = listOf(navArgument(Routes.CardScreenRouteArgument) { type = NavType.StringType })
        ) { backStackEntry ->
            FullScreenCard(
                backStackEntry.arguments?.getString(Routes.CardScreenRouteArgument),
                mainScreenViewModel,
                filterScreenViewModel,
                cardViewModel
            )
        }
        composable(Routes.SettingsScreenRoute) {
            SettingsScreen()
        }
    }
}