package it.hembik.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import it.hembik.data.api.HttpClient
import it.hembik.data.dto.toBeer
import it.hembik.domain.model.Beer

private const val STARTING_PAGE_INDEX = 1

class BeersPagingDataSource (
    private val httpClient: HttpClient,
    private val beersDataSource: BeersDataSource,
    private val beerName: String?
) : PagingSource<Int, Beer>() {

    private var currentBeerName: String? = ""

    override fun getRefreshKey(state: PagingState<Int, Beer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Beer> {
        val position = params.key ?: 1
        return try {
            val response = beerName?.let { safeBeerName ->
                httpClient.api.getBeersFiltered(page = position, beerName = safeBeerName).map { beerDTO ->
                    beerDTO.toBeer()
                }
            } ?: run {
                httpClient.api.getBeers(page = position).map { beerDTO ->
                    beerDTO.toBeer()
                }
            }
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + 1
            }

            if (currentBeerName != beerName) {
                beersDataSource.clear()
            }
            currentBeerName = beerName
            beersDataSource.setBeerList(response)
            LoadResult.Page(
                data = response,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}

