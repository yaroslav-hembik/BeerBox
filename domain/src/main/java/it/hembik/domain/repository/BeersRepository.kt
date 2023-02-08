package it.hembik.domain.repository

import androidx.paging.PagingData
import it.hembik.domain.model.Beer
import kotlinx.coroutines.flow.Flow

interface BeersRepository {
    suspend fun getBeers(beerName: String?): Flow<PagingData<Beer>>

    fun getBeerDetails(id: Int): Beer?
}