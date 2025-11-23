package com.gifttrack.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gifttrack.core.database.entity.RecipientEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Recipient entities.
 */
@Dao
interface RecipientDao {
    /**
     * Observes all recipients.
     */
    @Query("SELECT * FROM recipients ORDER BY name ASC")
    fun getRecipients(): Flow<List<RecipientEntity>>

    /**
     * Gets a single recipient by ID.
     */
    @Query("SELECT * FROM recipients WHERE id = :id")
    suspend fun getRecipientById(id: String): RecipientEntity?

    /**
     * Inserts or replaces a recipient.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipient: RecipientEntity)

    /**
     * Inserts multiple recipients.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipients: List<RecipientEntity>)

    /**
     * Deletes a recipient by ID.
     */
    @Query("DELETE FROM recipients WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Deletes all recipients.
     */
    @Query("DELETE FROM recipients")
    suspend fun deleteAll()
}
