package com.gymatch.domain.ranking

import com.gymatch.domain.model.SportType

enum class RankingDimension { CITY, SPORT, CATEGORY }

data class RankingScope(
    val cityCode: String?,
    val sportType: SportType?,
    val categoryCode: String?,
)

data class RankingEntry(
    val userId: String,
    val displayName: String,
    val score: Double,
    val verifiedWorkoutCount: Int,
    val visibilityBoost: Double,
)

data class RankingPreference(
    val userId: String,
    val showInRanking: Boolean,
)
