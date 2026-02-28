package com.gymatch.rankingengine.core

import com.gymatch.rankingengine.api.RankingAdvantageEngine
import com.gymatch.rankingengine.model.RankingOutcome
import com.gymatch.rankingengine.model.RankingSignal
import kotlin.math.ln

class VerifiedWorkoutRankingEngine : RankingAdvantageEngine {
    override fun compute(signal: RankingSignal): RankingOutcome {
        val verificationBonus = ln((signal.verifiedWorkoutCount + 1).toDouble()) * 0.12
        val premiumVisibilityBoost = if (signal.premiumBoostEligible) 0.08 else 0.0
        val finalScore = (signal.baseScore + verificationBonus + premiumVisibilityBoost).coerceIn(0.0, 1.5)
        return RankingOutcome(
            finalScore = finalScore,
            visibilityBoost = premiumVisibilityBoost + verificationBonus,
        )
    }
}
