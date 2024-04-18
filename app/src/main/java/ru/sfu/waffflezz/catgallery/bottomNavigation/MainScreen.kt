package ru.sfu.waffflezz.catgallery.bottomNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import ru.sfu.waffflezz.catgallery.R
import ru.sfu.waffflezz.catgallery.data.CardViewModel
import ru.sfu.waffflezz.catgallery.viewmodels.FilterScreenViewModel
import ru.sfu.waffflezz.catgallery.viewmodels.MainScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.factory),
    filterScreenViewModel: FilterScreenViewModel = viewModel<FilterScreenViewModel>(),
    cardViewModel: CardViewModel = viewModel(factory = CardViewModel.factory)
) {
    val navController = rememberNavController()
    Scaffold (
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .scale(0.6f),
                            painter = painterResource(id = R.drawable.cat_logo),
                            contentDescription = "cat logo"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.SettingsScreenRoute) {
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(navController = navController) 
        }
    ) {innerPadding ->
        NavGraph(
            navHostController = navController,
            innerPadding,
            mainScreenViewModel,
            filterScreenViewModel,
            cardViewModel
        )
    }
}