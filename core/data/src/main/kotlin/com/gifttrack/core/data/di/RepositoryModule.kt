package com.gifttrack.core.data.di

import com.gifttrack.core.data.repository.OrderRepositoryImpl
import com.gifttrack.core.data.repository.RecipientRepositoryImpl
import com.gifttrack.core.domain.repository.OrderRepository
import com.gifttrack.core.domain.repository.RecipientRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for binding repository interfaces to implementations.
 *
 * This is where we tell Hilt which implementation to use when
 * a repository interface is requested.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindRecipientRepository(
        recipientRepositoryImpl: RecipientRepositoryImpl
    ): RecipientRepository
}
