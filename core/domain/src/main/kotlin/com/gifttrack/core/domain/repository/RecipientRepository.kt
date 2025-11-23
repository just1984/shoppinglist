package com.gifttrack.core.domain.repository

import com.gifttrack.core.domain.model.Recipient
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing gift recipients.
 */
interface RecipientRepository {
    /**
     * Observes all recipients as a Flow.
     */
    fun getRecipients(): Flow<List<Recipient>>

    /**
     * Retrieves a single recipient by ID.
     */
    suspend fun getRecipientById(id: String): Recipient?

    /**
     * Inserts or updates a recipient.
     */
    suspend fun insertRecipient(recipient: Recipient)

    /**
     * Deletes a recipient by ID.
     */
    suspend fun deleteRecipient(id: String)
}
