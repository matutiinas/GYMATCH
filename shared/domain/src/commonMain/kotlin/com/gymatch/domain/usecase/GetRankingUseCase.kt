package com.gymatch.domain.usecase

import com.gymatch.domain.ranking.RankingEntry
import com.gymatch.domain.ranking.RankingRepository
import com.gymatch.domain.ranking.RankingScope

class GetRankingUseCase(
    private val rankingRepository: RankingRepository,
) {
    suspend operator fun invoke(userId: String, scope: RankingScope, limit: Int): List<RankingEntry> {
        val preference = rankingRepository.getPreference(userId)
        if (!preference.showInRanking) return emptyList()
        return rankingRepository.getRanking(scope, limit)
    }
}
