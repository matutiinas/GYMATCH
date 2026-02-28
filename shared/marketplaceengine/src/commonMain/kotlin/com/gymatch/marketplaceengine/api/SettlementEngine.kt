package com.gymatch.marketplaceengine.api

import com.gymatch.marketplaceengine.model.SettlementInput
import com.gymatch.marketplaceengine.model.SettlementResult

interface SettlementEngine {
    fun settle(input: SettlementInput): SettlementResult
}
