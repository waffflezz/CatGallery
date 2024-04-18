package ru.sfu.waffflezz.catgallery.bottomNavigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ru.sfu.waffflezz.catgallery.Utils
import ru.sfu.waffflezz.catgallery.components.CatCard
import ru.sfu.waffflezz.catgallery.components.ExpandableContainer
import ru.sfu.waffflezz.catgallery.data.CardViewModel
import ru.sfu.waffflezz.catgallery.data.api.CardRequest
import ru.sfu.waffflezz.catgallery.viewmodels.FilterScreenViewModel
import ru.sfu.waffflezz.catgallery.viewmodels.MainScreenViewModel

@Composable
fun HomeScreen(
    mainScreenViewModel: MainScreenViewModel,
    navController: NavController,
    cardViewModel: CardViewModel
) {
    val catCards by remember { mainScreenViewModel.rememberCatCards }

    val refreshing = mainScreenViewModel.refreshing

    LaunchedEffect(refreshing) {
        if (refreshing.value) {
            mainScreenViewModel.refreshCatCards()
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing.value),
        onRefresh = { mainScreenViewModel.refreshCatCards() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                catCards,
                key = {
                    it.id
                }
            ) { card ->
                CatCard(
                    cardRequest = card,
                    navController,
                    cardViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatsScreen(
    navController: NavController,
    filterScreenViewModel: FilterScreenViewModel,
    cardViewModel: CardViewModel
) {
    val catCards by remember { filterScreenViewModel.rememberFilteredCatCards }

    val refreshing = remember { filterScreenViewModel.refreshing }

    Column {
        ExpandableContainer(viewModel = filterScreenViewModel)
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing.value),
            onRefresh = { filterScreenViewModel.refreshFilteredCatCards() }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    catCards,
                    key = {
                        it.id
                    }
                ) { card ->
                    CatCard(
                        cardRequest = card,
                        navController,
                        cardViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteScreen(
    cardViewModel: CardViewModel,
    navController: NavController
) {
    val catCards = cardViewModel.cardsList.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            catCards.value,
            key = {
                it.id
            }
        ) { card ->
            CatCard(
                cardRequest = Utils.fromEntityToRequest(card),
                navController,
                cardViewModel
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullScreenCard(
    userId: String?,
    mainScreenViewModel: MainScreenViewModel,
    filterScreenViewModel: FilterScreenViewModel,
    cardViewModel: CardViewModel
) {
    val cardRequest = if (mainScreenViewModel.getCatCardById(userId!!) == null) {
        filterScreenViewModel.getCatCardById(userId)
    } else {
        mainScreenViewModel.getCatCardById(userId)
    }

    val isFavorite = remember { mutableStateOf(false) }

    cardViewModel.hasCardById(userId) {
        isFavorite.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Название породы
        Text(
            text = cardRequest!!.breeds[0].name,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val catHabit = Utils.mapOfOrigins[cardRequest.breeds[0].origin] ?: LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(catHabit, 3f)
        }

        val pagerState = rememberPagerState(pageCount = {
            2
        })
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) { page ->
            when (page) {
                0 -> Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    painter = rememberAsyncImagePainter(cardRequest.url),
                    contentDescription = "Cat",
                )
                1 -> GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = catHabit),
                        title = cardRequest.breeds[0].origin
                    )
                }
            }
        }

        // Место обитания
        Text(
            text = "Origin: ${cardRequest.breeds[0].origin}",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Описание породы
        Text(
            text = cardRequest.breeds[0].description,
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Характер
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(cardRequest.breeds[0].temperament.split(", ")) { temperamentText ->
                SuggestionChip(onClick = { /*TODO*/ }, label = { Text(text = temperamentText) })
            }
        }

        // Иконка сердечка
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = if (!isFavorite.value) Icons.Rounded.FavoriteBorder else Icons.Rounded.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { /* TODO: */ } // Делаем иконку кликабельной
            )
        }
    }
}

@Composable
fun SettingsScreen() {
    var checker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dark Mode",
                fontSize = 24.sp
            )
            Switch(
                checked = checker,
                onCheckedChange = { checker = it }
            )
        }
    }
}