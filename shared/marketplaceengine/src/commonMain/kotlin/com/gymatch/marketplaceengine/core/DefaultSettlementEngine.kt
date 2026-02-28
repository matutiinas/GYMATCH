package com.gymatch.marketplaceengine.core

import com.gymatch.marketplaceengine.api.SettlementEngine
import com.gymatch.marketplaceengine.model.SettlementInput
import com.gymatch.marketplaceengine.model.SettlementResult

class DefaultSettlementEngine : SettlementEngine {
    override fun settle(input: SettlementInput): SettlementResult {
        val commission = (input.grossAmount * input.commissionBps) / 10_000
        return SettlementResult(
            commissionAmount = commission,
            coachNetAmount = input.grossAmount - commission,
        )
    }
}
