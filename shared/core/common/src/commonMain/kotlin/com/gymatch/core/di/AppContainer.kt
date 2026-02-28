package com.gymatch.core.di

import com.gymatch.data.repository.DefaultTrainingRepository
import com.gymatch.data.seed.ProfileSeedDataSource
import com.gymatch.data.seed.SeedResult
import com.gymatch.data.seed.SeedService
import com.gymatch.domain.activity.VerifiedWorkoutRules
import com.gymatch.domain.activity.WorkoutVerificationService
import com.gymatch.domain.policy.RestrictionPolicyService
import com.gymatch.domain.premium.FeatureFlagProvider
import com.gymatch.domain.premium.PremiumFeatureFlag
import com.gymatch.domain.premium.RestrictionSnapshot
import com.gymatch.domain.premium.SubscriptionManager
import com.gymatch.domain.recommendation.RecommendationFeatureProvider
import com.gymatch.domain.recommendation.RecommendationService
import com.gymatch.domain.recommendation.RecommendationVector
import com.gymatch.domain.repository.TrainingRepository
import com.gymatch.rankingengine.api.RankingAdvantageEngine
import com.gymatch.rankingengine.core.VerifiedWorkoutRankingEngine
import com.gymatch.recommendationengine.core.HybridRecommendationService
import com.gymatch.trainingengine.api.TrainingPlanGenerator
import com.gymatch.trainingengine.core.RuleBasedTrainingPlanGenerator

interface AppContainer {
    val trainingRepository: TrainingRepository
    val restrictionPolicyService: RestrictionPolicyService
    val workoutVerificationService: WorkoutVerificationService
    val rankingAdvantageEngine: RankingAdvantageEngine
    val recommendationService: RecommendationService
    val seedService: SeedService

    suspend fun initializeData(isDebug: Boolean): SeedResult
}

class DefaultAppContainer(
    private val trainingPlanGenerator: TrainingPlanGenerator = RuleBasedTrainingPlanGenerator(),
    private val subscriptionManager: SubscriptionManager = InMemorySubscriptionManager(),
    private val featureFlags: FeatureFlagProvider = StaticFeatureFlagProvider(),
    private val recommendationFeatureProvider: RecommendationFeatureProvider = InMemoryRecommendationFeatureProvider(),
    private val profileSeedDataSource: ProfileSeedDataSource = InMemoryProfileSeedDataSource(),
) : AppContainer {
    override val trainingRepository: TrainingRepository by lazy {
        DefaultTrainingRepository(trainingPlanGenerator)
    }

    override val restrictionPolicyService: RestrictionPolicyService by lazy {
        RestrictionPolicyService(subscriptionManager, featureFlags)
    }

    override val workoutVerificationService: WorkoutVerificationService by lazy {
        VerifiedWorkoutRules()
    }

    override val rankingAdvantageEngine: RankingAdvantageEngine by lazy {
        VerifiedWorkoutRankingEngine()
    }

    override val recommendationService: RecommendationService by lazy {
        HybridRecommendationService(recommendationFeatureProvider)
    }

    override val seedService: SeedService by lazy {
        SeedService(profileSeedDataSource)
    }

    override suspend fun initializeData(isDebug: Boolean): SeedResult {
        return seedService.seedProfilesIfEmpty(isDebug = isDebug, targetCount = 50)
    }
}

private class StaticFeatureFlagProvider : FeatureFlagProvider {
    override fun isEnabled(flag: PremiumFeatureFlag): Boolean = true
}

private class InMemorySubscriptionManager : SubscriptionManager {
    override suspend fun getRestrictionSnapshot(userId: String): RestrictionSnapshot {
        return RestrictionSnapshot(
            maxDailySwipes = 120,
            maxDailyMatches = 30,
            canSeeWhoLikedYou = true,
            canUseBoost = true,
        )
    }
}

private class InMemoryRecommendationFeatureProvider : RecommendationFeatureProvider {
    override suspend fun getUserVector(userId: String): RecommendationVector = RecommendationVector(
        sports = setOf("GYM", "RUNNING"),
        objectives = setOf("MUSCLE", "CONSISTENCY"),
        level = 3,
        activityRecencyDays = 2,
        sportsElo = 1240.0,
    )

    override suspend fun getCandidateVector(candidateId: String): RecommendationVector = RecommendationVector(
        sports = setOf("RUNNING"),
        objectives = setOf("CONSISTENCY"),
        level = 3,
        activityRecencyDays = 1,
        sportsElo = 1310.0,
    )

    override suspend fun getCollaborativeAffinity(userId: String, candidateId: String): Double = 0.62
}

private class InMemoryProfileSeedDataSource : ProfileSeedDataSource {
    private var count: Long = 0

    override suspend fun countProfiles(): Long = count

    override suspend fun saveProfiles(profiles: List<com.gymatch.data.seed.SeedProfileRecord>) {
        count += profiles.size
    }
}
