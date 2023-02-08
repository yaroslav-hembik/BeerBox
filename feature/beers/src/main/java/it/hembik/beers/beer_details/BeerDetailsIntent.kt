package it.hembik.beers.beer_details

sealed class BeerDetailsIntent {
    data class GetBeerDetails(val id: Int) : BeerDetailsIntent()
}