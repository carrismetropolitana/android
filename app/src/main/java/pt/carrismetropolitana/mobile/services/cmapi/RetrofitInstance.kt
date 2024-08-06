package pt.carrismetropolitana.mobile.services.cmapi

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.carrismetropolitana.pt"
    const val STOPS = "$BASE_URL/stops"

    private fun getInstance(): Retrofit {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CASE_WITH_UNDERSCORES)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()
    }

    val cmApi = getInstance().create(CMAPI::class.java)
}