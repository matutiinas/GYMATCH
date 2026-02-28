package com.gymatch.domain.repository

import com.gymatch.domain.activity.ExternalConnection
import com.gymatch.domain.activity.ExternalProvider
import com.gymatch.domain.activity.VerifiedWorkout
import com.gymatch.domain.ads.SponsoredProfile
import com.gymatch.domain.model.AthleteProfile
import com.gymatch.domain.model.DiscoverCandidate
import com.gymatch.domain.model.GeoPoint
import com.gymatch.domain.model.MatchResult
import com.gymatch.domain.model.SwipeAction
import com.gymatch.domain.ranking.RankingEntry
import com.gymatch.domain.ranking.RankingPreference
import com.gymatch.domain.ranking.RankingScope
import com.gymatch.trainingengine.model.TrainingContext
import com.gymatch.trainingengine.model.TrainingPlan

interface AuthRepository {
    suspend fun registerWithEmail(email: String, password: String): String
    suspend fun registerWithGoogle(idToken: String): String
}

interface ProfileRepository {
    suspend fun getProfile(userId: String): AthleteProfile
    suspend fun updateProfile(profile: AthleteProfile)
    suspend fun updateVisibility(userId: String, isPublic: Boolean)
    suspend fun deactivateAccount(userId: String)
    suspend fun deleteAccount(userId: String)
    suspend fun uploadPhoto(userId: String, bytes: ByteArray, position: Int): String
    suspend fun updateLocation(userId: String, point: GeoPoint)
}

interface DiscoverRepository {
    suspend fun getDiscoverCandidates(userId: String, limit: Int): List<DiscoverCandidate>
    suspend fun getSponsoredCandidates(userId: String, limit: Int): List<SponsoredProfile>
    suspend fun submitSwipe(action: SwipeAction): MatchResult
    suspend fun getDailyMatchCount(userId: String): Int
    suspend fun getDailySwipeCount(userId: String): Int
    suspend fun getWhoLikedMe(userId: String): List<String>
}

interface ActivityRepository {
    suspend fun linkProvider(userId: String, provider: ExternalProvider, authCode: String): ExternalConnection
    suspend fun syncWorkouts(userId: String, provider: ExternalProvider): List<VerifiedWorkout>
}

interface RankingRepository {
    suspend fun getRanking(scope: RankingScope, limit: Int): List<RankingEntry>
    suspend fun updatePreference(preference: RankingPreference)
}

interface ChatRepository {
    suspend fun sendMessage(matchId: String, senderId: String, text: String)
    fun observeMessages(matchId: String): kotlinx.coroutines.flow.Flow<List<String>>
}

interface TrainingRepository {
    suspend fun generatePlan(userA: AthleteProfile, userB: AthleteProfile, context: TrainingContext): TrainingPlan
}

interface ActivityProvider {
    suspend fun getLatestActivities(userId: String): List<String>
}
