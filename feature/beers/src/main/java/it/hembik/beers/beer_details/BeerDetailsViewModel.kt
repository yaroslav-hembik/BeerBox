package it.hembik.beers.beer_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.hembik.domain.usecases.GetBeerDetailsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerDetailsViewModel @Inject constructor(
    private val getBeerDetailsUseCase: GetBeerDetailsUseCase
) : ViewModel() {

    val userIntent = Channel<BeerDetailsIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<BeerDetailsState>(BeerDetailsState.Idle)
    val state: StateFlow<BeerDetailsState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is BeerDetailsIntent.GetBeerDetails -> getBeerDetail(it.id)
                }
            }
        }
    }

    private fun getBeerDetail(id: Int) {
        viewModelScope.launch {
            getBeerDetailsUseCase.invoke(id)?.let {
                _state.value = BeerDetailsState.BeerDetails(it)
            } ?: run {
                _state.value = BeerDetailsState.Error
            }
        }
    }
}