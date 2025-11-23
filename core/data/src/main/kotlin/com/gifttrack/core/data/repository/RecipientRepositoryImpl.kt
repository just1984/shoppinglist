package com.gifttrack.core.data.repository

import com.gifttrack.core.database.dao.RecipientDao
import com.gifttrack.core.data.mapper.toEntity
import com.gifttrack.core.data.mapper.toModel
import com.gifttrack.core.domain.model.Recipient
import com.gifttrack.core.domain.repository.RecipientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of RecipientRepository using Room database.
 *
 * This class bridges the domain layer (repository interface)
 * and the data layer (Room database).
 */
class RecipientRepositoryImpl @Inject constructor(
    private val recipientDao: RecipientDao
) : RecipientRepository {

    override fun getRecipients(): Flow<List<Recipient>> {
        return recipientDao.getRecipients().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getRecipientById(id: String): Recipient? {
        return recipientDao.getRecipientById(id)?.toModel()
    }

    override suspend fun insertRecipient(recipient: Recipient) {
        recipientDao.insert(recipient.toEntity())
    }

    override suspend fun insertRecipients(recipients: List<Recipient>) {
        recipientDao.insertAll(recipients.map { it.toEntity() })
    }

    override suspend fun updateRecipient(recipient: Recipient) {
        recipientDao.update(recipient.toEntity())
    }

    override suspend fun deleteRecipient(id: String) {
        recipientDao.deleteById(id)
    }

    override suspend fun deleteAllRecipients() {
        recipientDao.deleteAll()
    }
}
