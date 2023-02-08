package it.hembik.beers.beer_details

import it.hembik.domain.model.Beer

sealed class BeerDetailsState {
    object Idle : BeerDetailsState()
    data class BeerDetails(val beer: Beer) : BeerDetailsState()
    object Error: BeerDetailsState()
}