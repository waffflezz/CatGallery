package ru.sfu.waffflezz.catgallery.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ru.sfu.waffflezz.catgallery.R
import ru.sfu.waffflezz.catgallery.bottomNavigation.Routes
import ru.sfu.waffflezz.catgallery.data.CardViewModel
import ru.sfu.waffflezz.catgallery.data.api.CardRequest
import ru.sfu.waffflezz.catgallery.viewmodels.MainScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatCard(
    cardRequest: CardRequest,
    navController: NavController,
    cardViewModel: CardViewModel
) {
    /* TODO: написать метод проверки реквеста в базе данных. Если он там есть, то менять favorite */
    val isFavorite = remember { mutableStateOf(false) }

    cardViewModel.hasCardById(cardRequest.id) {
        isFavorite.value = true
    }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        onClick = {
            navController.navigate("${Routes.CardScreenRoute}/${cardRequest.id}")
        }
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = cardRequest.breeds[0].name,
                fontSize = 24.sp
            )
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cardRequest.url)
                    .crossfade(true)
                    .build(),
                contentDescription = "Meow",
                placeholder = painterResource(id = R.drawable.punpun)
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cardRequest.breeds[0].temperament.split(", ").take(2).forEach { temperamentText ->
                        SuggestionChip(onClick = { /*TODO*/ }, label = { Text(text = temperamentText) })
                    }
                }
                Button(onClick = {
                    if (!isFavorite.value) {
                        cardViewModel.insertItem(cardRequest)
                        isFavorite.value = !isFavorite.value
                    } else {
                        cardViewModel.deleteItem(cardRequest)
                        isFavorite.value = !isFavorite.value
                    }
                }) {
                    Icon(
                        if (!isFavorite.value) Icons.Rounded.FavoriteBorder else Icons.Rounded.Favorite,
                        contentDescription = null)
                }
            }
        }
    }
}