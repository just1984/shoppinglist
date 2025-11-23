package com.gifttrack.core.database.di

import android.content.Context
import androidx.room.Room
import com.gifttrack.core.database.GiftTrackDatabase
import com.gifttrack.core.database.dao.OrderDao
import com.gifttrack.core.database.dao.RecipientDao
import com.gifttrack.core.database.dao.ShopDao
import com.gifttrack.core.database.dao.TrackingEventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGiftTrackDatabase(
        @ApplicationContext context: Context
    ): GiftTrackDatabase {
        return Room.databaseBuilder(
            context,
            GiftTrackDatabase::class.java,
            "gifttrack.db"
        )
            .fallbackToDestructiveMigration() // For development only - TODO: Add proper migrations for production
            .build()
    }

    @Provides
    @Singleton
    fun provideOrderDao(database: GiftTrackDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    @Singleton
    fun provideRecipientDao(database: GiftTrackDatabase): RecipientDao {
        return database.recipientDao()
    }

    @Provides
    @Singleton
    fun provideShopDao(database: GiftTrackDatabase): ShopDao {
        return database.shopDao()
    }

    @Provides
    @Singleton
    fun provideTrackingEventDao(database: GiftTrackDatabase): TrackingEventDao {
        return database.trackingEventDao()
    }
}
