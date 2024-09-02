package pt.carrismetropolitana.mobile.composables.components.news

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import pt.carrismetropolitana.mobile.composables.WrappingCarousel
import pt.carrismetropolitana.mobile.services.cmwordpressapi.CMWPAPI
import pt.carrismetropolitana.mobile.services.cmwordpressapi.News

@Composable
fun NewsCarousel(
    news: List<News>,
    onNewsClick: (news: News) -> Unit
) {

    LaunchedEffect(news) {
        val newsImageUrls = news.map {
            val imageUrl = it.featuredMedia.let { mediaId ->
                val media = CMWPAPI.shared.getMediaURL(mediaId)
            }
        }
    }

//    WrappingCarousel(items = news.map {}, onItemClick = { onNewsClick(it) })
}