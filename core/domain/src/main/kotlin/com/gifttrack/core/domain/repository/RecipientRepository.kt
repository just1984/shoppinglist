package com.gifttrack.core.domain.repository

import com.gifttrack.core.domain.model.Recipient
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing gift recipients.
 *
 * This follows the repository pattern where the domain layer
 * defines the interface and the data layer provides the implementation.
 */
interface RecipientRepository {
    /**
     * Observes all recipients as a Flow.
     * The Flow will emit new values whenever the data changes.
     */
    fun getRecipients(): Flow<List<Recipient>>

    /**
     * Retrieves a single recipient by ID.
     *
     * @return The recipient if found, null otherwise.
     */
    suspend fun getRecipientById(id: String): Recipient?

    /**
     * Inserts or updates a recipient.
     */
    suspend fun insertRecipient(recipient: Recipient)

    /**
     * Inserts or updates multiple recipients.
     */
    suspend fun insertRecipients(recipients: List<Recipient>)

    /**
     * Updates a recipient.
     */
    suspend fun updateRecipient(recipient: Recipient)

    /**
     * Deletes a recipient by ID.
     */
    suspend fun deleteRecipient(id: String)

    /**
     * Deletes all recipients (for testing/development).
     */
    suspend fun deleteAllRecipients()
}
