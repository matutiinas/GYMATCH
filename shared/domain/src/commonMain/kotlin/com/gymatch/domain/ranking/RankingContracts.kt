package com.gymatch.domain.ranking

interface RankingRepository {
    suspend fun getRanking(scope: RankingScope, limit: Int): List<RankingEntry>
    suspend fun upsertPreference(preference: RankingPreference)
    suspend fun getPreference(userId: String): RankingPreference
}
