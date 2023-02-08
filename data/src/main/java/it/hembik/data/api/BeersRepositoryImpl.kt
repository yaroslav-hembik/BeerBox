package it.hembik.data.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import it.hembik.data.datasource.BeersDataSource
import it.hembik.data.datasource.BeersPagingDataSource
import it.hembik.domain.model.Beer
import it.hembik.domain.repository.BeersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BeersRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val beersDataSource: BeersDataSource
) : BeersRepository {

    override suspend fun getBeers(beerName: String?): Flow<PagingData<Beer>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BeersPagingDataSource(
                    httpClient = httpClient,
                    beersDataSource = beersDataSource,
                    beerName = beerName
                )
            },
            initialKey = 1
        ).flow
    }

    override fun getBeerDetails(id: Int): Beer? {
        return beersDataSource.getBeerById(id)
    }
}