package it.hembik.beerbox.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.hembik.domain.repository.BeersRepository
import it.hembik.data.api.BeersRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindStargazersRepository(impl: BeersRepositoryImpl): BeersRepository
}