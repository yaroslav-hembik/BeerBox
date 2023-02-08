package it.hembik.beers.beer_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import it.hembik.domain.usecases.GetBeerListUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(
    private val getBeerListUseCase: GetBeerListUseCase
) : ViewModel() {

    val userIntent = Channel<BeerListIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<BeerListState>(BeerListState.Idle)
    val state: StateFlow<BeerListState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is BeerListIntent.FetchBeerList -> fetchBeer()
                    is BeerListIntent.FetchBeersWithFilters -> fetchBeer(it.beerName)
                }
            }
        }
    }

    private fun fetchBeer(beerName: String? = null) {
        viewModelScope.launch {
            getBeerListUseCase.invoke(beerName).cachedIn(viewModelScope).collect {
                _state.value = BeerListState.Loading
                _state.value = try {
                    BeerListState.Beers(it)
                } catch (e: Exception) {
                    BeerListState.Error(e.localizedMessage)
                }
            }
        }
    }
}