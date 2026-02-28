package com.gymatch.rankingengine.api

import com.gymatch.rankingengine.model.RankingOutcome
import com.gymatch.rankingengine.model.RankingSignal

interface RankingAdvantageEngine {
    fun compute(signal: RankingSignal): RankingOutcome
}
