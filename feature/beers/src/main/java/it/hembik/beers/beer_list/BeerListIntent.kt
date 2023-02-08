package it.hembik.beers.beer_list

sealed class BeerListIntent {
    object FetchBeerList : BeerListIntent()
    data class FetchBeersWithFilters(val beerName: String) : BeerListIntent()
}