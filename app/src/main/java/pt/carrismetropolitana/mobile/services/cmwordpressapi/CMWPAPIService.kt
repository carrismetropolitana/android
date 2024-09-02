package pt.carrismetropolitana.mobile.services.cmwordpressapi

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CMWPAPIService {
    @GET("noticia")
    suspend fun getNews(@Query("per_page") perPage: Int): List<News>

    @GET("media/{mediaId}")
    suspend fun getMediaURL(@Path("mediaId") mediaId: Int): String
}

object CMWPAPINetworkService {
    private const val BASE_URL = "https://carrismetropolitana.pt/wp-json/wp/v2/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json; charset=utf-8".toMediaType())
        )
        .build()

    val apiService: CMWPAPIService = retrofit.create(CMWPAPIService::class.java)
}