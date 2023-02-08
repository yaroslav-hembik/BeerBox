package it.hembik.domain.usecases

import it.hembik.domain.model.Beer
import it.hembik.domain.repository.BeersRepository
import javax.inject.Inject

class GetBeerDetailsUseCase @Inject constructor(
    private val repository: BeersRepository
) {
    operator fun invoke(id: Int): Beer? = repository.getBeerDetails(id)
}