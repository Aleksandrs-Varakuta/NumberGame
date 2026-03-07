package com.rtu.number.game.data.di

import com.rtu.number.game.data.GameGraphRepositoryImpl
import com.rtu.number.game.domain.GameGraphRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideGameGraphRepository(): GameGraphRepository = GameGraphRepositoryImpl()

}