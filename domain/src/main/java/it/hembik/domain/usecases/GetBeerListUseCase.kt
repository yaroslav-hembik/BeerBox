package it.hembik.domain.usecases

import androidx.paging.PagingData
import it.hembik.domain.model.Beer
import it.hembik.domain.repository.BeersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBeerListUseCase @Inject constructor(
    private val repository: BeersRepository
) {
    operator fun invoke(beerName: String?): Flow<PagingData<Beer>> = flow {
        repository.getBeers(beerName).collect {
            emit(it)
        }
    }
}