package it.hembik.data.api

import it.hembik.data.dto.BeerDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("beers")
    suspend fun getBeers(
        @Query("page") page: Int
    ): List<BeerDTO>

    @GET("beers")
    suspend fun getBeersFiltered(
        @Query("page") page: Int,
        @Query("beer_name") beerName: String
    ): List<BeerDTO>
}