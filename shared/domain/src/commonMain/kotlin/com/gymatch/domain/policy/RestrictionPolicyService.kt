package com.gymatch.domain.policy

import com.gymatch.domain.premium.FeatureFlagProvider
import com.gymatch.domain.premium.PremiumFeatureFlag
import com.gymatch.domain.premium.RestrictionSnapshot
import com.gymatch.domain.premium.SubscriptionManager

class RestrictionPolicyService(
    private val subscriptionManager: SubscriptionManager,
    private val featureFlagProvider: FeatureFlagProvider,
) {
    suspend fun getUserRestrictions(userId: String): RestrictionSnapshot {
        if (!featureFlagProvider.isEnabled(PremiumFeatureFlag.PREMIUM_ENABLED)) {
            return RestrictionSnapshot(
                maxDailySwipes = 30,
                maxDailyMatches = 10,
                canSeeWhoLikedYou = false,
                canUseBoost = false,
            )
        }
        return subscriptionManager.getRestrictionSnapshot(userId)
    }
}
