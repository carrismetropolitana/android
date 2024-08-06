package pt.carrismetropolitana.mobile.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import java.net.URL
import coil.compose.AsyncImage
import coil.request.ImageRequest

private const val SCROLL_ANIMATION_DURATION = 5_000L

data class CarouselItem (
    val imageURL: String
)

val dummyItems = listOf(
    CarouselItem("https://www.carrismetropolitana.pt/wp-content/uploads/2024/06/AF-Inquerito-Noticia-_-Banner.png"),
    CarouselItem("https://www.carrismetropolitana.pt/wp-content/uploads/2024/06/Linhas-Mar_Banner.png"),
    CarouselItem("https://www.carrismetropolitana.pt/wp-content/uploads/2024/05/AF-_-Santo-Antonio_Banner-1.png"),
    CarouselItem("https://www.carrismetropolitana.pt/wp-content/uploads/2024/05/Banner-Mini-Passageiros.png")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WrappingCarousel(items: List<CarouselItem>, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val pagerState = rememberPagerState(pageCount = { items.size })
            val isDraggedState = pagerState.interactionSource.collectIsDraggedAsState()

            HorizontalPager(
                state = pagerState,
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
//                    .background(MaterialTheme.colors.background),
            ) {
                // max value is trophies.size
                CarouselItem(it, item = items[it])
            }


//            Surface(
//                modifier = Modifier
//                    .padding(bottom = 8.dp)
//                    .align(Alignment.BottomCenter),
//                shape = CircleShape,
//                color = Color.Black.copy(alpha = 0.5f)
//            ) {
//                HorizontalPagerIndicator(
//                    pagerState = pagerState,
//                    pageCount = realSize,
//                    pageIndexMapping = { it % realSize },
//                    activeColor = Color.White,
//                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
//                )
//            }

            // Start auto-scroll effect
            LaunchedEffect(isDraggedState) {
                // convert compose state into flow
                snapshotFlow { isDraggedState.value }
                    .collectLatest { isDragged ->
                        // if not isDragged start slide animation
                        if (!isDragged) {
                            // infinity loop
                            while (true) {
                                // duration before each scroll animation
                                delay(SCROLL_ANIMATION_DURATION)
                                runCatching {
                                    pagerState.animateScrollToPage(pagerState.currentPage.inc() % pagerState.pageCount)
                                }
                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun CarouselItem(
    page: Int,
    item: CarouselItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(shape = RoundedCornerShape(size = 12.dp))
            .background(Color.White)
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.imageURL)
                .crossfade(true)
                .build(),
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(size = 12.dp)),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(color = Color.Black.copy(alpha = 0.5f))
//                .padding(10.dp)
//                .align(Alignment.BottomStart)
//        ) {
//            Text(
//                text = trophy.location,
//                color = Color.White,
//                style = Typography.h6,
//                textAlign = TextAlign.Center
//            )
//
//            Text(
//                text = trophy.year,
//                color = Color.White,
//                style = Typography.h4,
//                textAlign = TextAlign.Center
//            )
//        }
//
//        Text(
//            text = "$page",
//            style = Typography.body1,
//            color = Color.Black,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .padding(10.dp)
//                .clip(shape = RoundedCornerShape(size = 4.dp))
//                .background(Color.White)
//                .padding(10.dp)
//                .align(Alignment.BottomEnd)
//
//        )
    }
}