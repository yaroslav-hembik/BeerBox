package it.hembik.beers.beer_list

import androidx.paging.PagingData
import it.hembik.domain.model.Beer

sealed class BeerListState {
    object Idle : BeerListState()
    object Loading : BeerListState()
    data class Beers(val beers: PagingData<Beer>) : BeerListState()
    data class Error(val error: String?) : BeerListState()
}