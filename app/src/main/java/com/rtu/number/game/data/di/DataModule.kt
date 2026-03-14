package com.rtu.number.game.data.di

import com.rtu.number.game.data.repository.InMemoryGameSessionRepository
import com.rtu.number.game.domain.repository.GameSessionRepository
import com.rtu.number.game.domain.rules.DefaultGameStatusResolver
import com.rtu.number.game.domain.rules.DefaultMoveApplier
import com.rtu.number.game.domain.rules.DefaultMoveGenerator
import com.rtu.number.game.domain.rules.DefaultMoveResolver
import com.rtu.number.game.domain.rules.DefaultMoveValidator
import com.rtu.number.game.domain.rules.GameStatusResolver
import com.rtu.number.game.domain.rules.InitialGameStateFactory
import com.rtu.number.game.domain.rules.MoveApplier
import com.rtu.number.game.domain.rules.MoveGenerator
import com.rtu.number.game.domain.rules.MoveResolver
import com.rtu.number.game.domain.rules.MoveValidator
import com.rtu.number.game.domain.rules.RandomInitialGameStateFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGameSessionRepository(
        repository: InMemoryGameSessionRepository,
    ): GameSessionRepository = repository

    @Provides
    @Singleton
    fun provideInitialGameStateFactory(): InitialGameStateFactory =
        RandomInitialGameStateFactory()

    @Provides
    @Singleton
    fun provideMoveGenerator(): MoveGenerator = DefaultMoveGenerator()

    @Provides
    @Singleton
    fun provideMoveValidator(): MoveValidator = DefaultMoveValidator()

    @Provides
    @Singleton
    fun provideMoveResolver(): MoveResolver = DefaultMoveResolver()

    @Provides
    @Singleton
    fun provideMoveApplier(
        moveValidator: MoveValidator,
        moveResolver: MoveResolver,
    ): MoveApplier = DefaultMoveApplier(
        moveValidator = moveValidator,
        moveResolver = moveResolver,
    )

    @Provides
    @Singleton
    fun provideGameStatusResolver(): GameStatusResolver = DefaultGameStatusResolver()
}
