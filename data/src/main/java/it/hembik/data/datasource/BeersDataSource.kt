package it.hembik.data.datasource

import it.hembik.domain.model.Beer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BeersDataSource @Inject constructor() {
    private var beerList: MutableList<Beer> = mutableListOf()

    fun setBeerList(beers: List<Beer>) {
        beerList.addAll(beers)
    }

    fun clear() {
        beerList.clear()
    }

    fun getBeerById(id: Int): Beer? {
        return beerList.find { it.id == id }
    }
}