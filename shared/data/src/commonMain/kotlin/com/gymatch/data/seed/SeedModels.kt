package com.gymatch.data.seed

data class SeedProfileRecord(
    val userId: String,
    val name: String,
    val age: Int,
    val photos: List<String>,
    val sports: List<String>,
    val level: String,
    val objective: String,
    val personalBests: Map<String, String>,
    val latitude: Double,
    val longitude: Double,
    val isOnline: Boolean,
    val lastActiveEpochSec: Long,
)

interface ProfileSeedDataSource {
    suspend fun countProfiles(): Long
    suspend fun saveProfiles(profiles: List<SeedProfileRecord>)
}
