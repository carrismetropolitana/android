package pt.carrismetropolitana.mobile.services.cmapi

import retrofit2.Response
import retrofit2.http.GET

interface CMAPI {

    @GET("/stops")
    suspend fun getStops(): Response<List<Stop>>

}