package com.gymatch.data.seed

/**
 * Adapter de Data para persistir seeds en Firestore o cualquier backend equivalente.
 *
 * Implementación esperada:
 * - `countProfiles`: contar documentos de la colección `profiles`.
 * - `saveProfiles`: escritura por lotes (batch) para eficiencia.
 */
interface FirestoreClient {
    suspend fun count(collection: String): Long
    suspend fun upsertBatch(collection: String, documents: List<Map<String, Any?>>)
}

class FirestoreProfileSeedDataSource(
    private val firestoreClient: FirestoreClient,
    private val collectionName: String = "profiles",
) : ProfileSeedDataSource {

    override suspend fun countProfiles(): Long = firestoreClient.count(collectionName)

    override suspend fun saveProfiles(profiles: List<SeedProfileRecord>) {
        val docs = profiles.map { it.toDocument() }
        firestoreClient.upsertBatch(collectionName, docs)
    }

    private fun SeedProfileRecord.toDocument(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "name" to name,
        "age" to age,
        "photos" to photos,
        "sports" to sports,
        "level" to level,
        "objective" to objective,
        "personalBests" to personalBests,
        "location" to mapOf("lat" to latitude, "lng" to longitude),
        "isOnline" to isOnline,
        "lastActiveEpochSec" to lastActiveEpochSec,
    )
}
