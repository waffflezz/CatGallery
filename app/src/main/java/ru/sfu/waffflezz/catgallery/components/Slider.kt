package ru.sfu.waffflezz.catgallery.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import ru.sfu.waffflezz.catgallery.data.api.CardRequest


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Slider(cardRequest: CardRequest) {
    val pages = listOf(
        Page(
            title = "Cat",
            image = cardRequest.url
        ),
        Page(
            title = "Map",
            image = null
        )
    )

    val currentPage by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = {
        2
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = pagerState
        ) {page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (page == 0) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        painter = rememberAsyncImagePainter(cardRequest.url),
                        contentDescription = pages[page].title
                    )
                } else {
//                    AndroidView(
////                        factory = { context ->
////                            MapViewModel(context)
////                        }
//                    )
                }
            }
        }
    }
}

class Page(val title: String, val image: String?)